/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2026 - Stendhal                    *
 ***************************************************************************/
package games.stendhal.server.actions;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import games.stendhal.common.constants.SoundID;
import games.stendhal.common.constants.SoundLayer;
import games.stendhal.common.grammar.Grammar;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.engine.dbcommand.LogTradeEventCommand;
import games.stendhal.server.core.engine.dbcommand.SyncMarketDataCommand;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.entity.trade.BuyRequest;
import games.stendhal.server.entity.trade.Earning;
import games.stendhal.server.entity.trade.Market;
import games.stendhal.server.entity.trade.Offer;
import games.stendhal.server.events.ShowMarketOffersEvent;
import games.stendhal.server.events.SoundEvent;
import games.stendhal.server.maps.semos.tavern.market.MarketManagerNPC;
import games.stendhal.server.maps.semos.tavern.market.TradeCenterZoneConfigurator;
import games.stendhal.server.maps.semos.tavern.market.TradingUtility;
import games.stendhal.server.util.AsynchronousProgramExecutor;
import marauroa.common.game.RPAction;
import marauroa.common.game.RPObject;
import marauroa.server.db.command.DBCommandQueue;

/**
 * Direct RPC Action handler for Trading Center operations (Buy Orders, Sell Offers,
 * Partial Fulfillments, Deposit, and Deliveries).
 */
public class MarketAction implements ActionListener {

	private static final Logger logger = Logger.getLogger(MarketAction.class);

	public static void register() {
		CommandCenter.register("market_action", new MarketAction());
	}

	@Override
	public void onAction(Player player, RPAction action) {
		if (player == null || action == null) {
			return;
		}

		String subAction = action.get("action");
		if (subAction == null || subAction.isEmpty()) {
			logger.warn("[MARKET] Player " + player.getName() + " sent market_action without sub-action: " + action);
			return;
		}

		logger.info("[MARKET RPC] >> Player '" + player.getName() + "' dispatched action: '" + subAction + "' (data: " + action + ")");

		Market market = getMarket(player);
		if (market == null) {
			logger.error("[MARKET] Trading Center zone Market object not found for player: " + player.getName());
			player.sendPrivateText("The Trading Center is currently unavailable.");
			return;
		}

		MarketManagerNPC manager = findMarketManager(player);

		switch (subAction) {
		case "place_offer":
			handlePlaceOffer(player, market, action);
			break;
		case "buy_offer":
			handleBuyOffer(player, market, manager, action);
			break;
		case "prolong_offer":
			handleProlongOffer(player, market, manager, action);
			break;
		case "remove_offer":
			handleRemoveOffer(player, market, manager, action);
			break;
		case "place_request":
			handlePlaceRequest(player, market, action);
			break;
		case "fulfill_request":
			handleFulfillRequest(player, market, manager, action);
			break;
		case "cancel_request":
			handleCancelRequest(player, market, manager, action);
			break;
		case "claim_items":
			handleClaimItems(player, market, action);
			break;
		case "fetch_earnings":
			handleFetchEarnings(player, market);
			break;
		case "refresh":
			logger.info("[MARKET] Player " + player.getName() + " requested offer refresh.");
			break;
		default:
			logger.warn("[MARKET] Unknown market sub-action from " + player.getName() + ": " + subAction);
			break;
		}

		// Ensure persistent snapshot synchronization to MySQL
		DBCommandQueue.get().enqueue(new SyncMarketDataCommand(market));

		// Push refreshed market data to client
		player.addEvent(new ShowMarketOffersEvent(market, player));
	}

	private void handlePlaceOffer(Player player, Market market, RPAction action) {
		String itemName = action.get("item");
		int price = parseIntSafely(action.get("price"), 0);
		int qty = parseIntSafely(action.get("qty"), 1);

		logger.info("[MARKET] Place Offer: player=" + player.getName() + ", item=" + itemName + ", qty=" + qty + ", price=" + price);

		if (itemName == null || itemName.trim().isEmpty() || price <= 0 || qty <= 0) {
			logger.warn("[MARKET] Place Offer rejected: invalid parameters (item=" + itemName + ", price=" + price + ", qty=" + qty + ")");
			player.sendPrivateText("Invalid offer details.");
			return;
		}

		if (player.getLevel() < 6) {
			logger.warn("[MARKET] Place Offer rejected: player level " + player.getLevel() + " < 6");
			player.sendPrivateText("You need to be at least level 6 to place market offers.");
			return;
		}

		int fee = TradingUtility.calculateFee(player, price).intValue();
		if (!TradingUtility.canPlayerAffordTradingFee(player, price)) {
			logger.warn("[MARKET] Place Offer rejected: cannot afford fee " + fee);
			player.sendPrivateText("You cannot afford the trading fee of " + fee + " money.");
			return;
		}

		Item item = player.getFirstEquipped(itemName);
		if (item == null) {
			logger.warn("[MARKET] Place Offer rejected: item '" + itemName + "' not found in player inventory.");
			player.sendPrivateText("You don't have " + Grammar.quantityplnoun(qty, itemName, "a") + ".");
			return;
		}

		Offer offer = market.createOffer(player, item, price, qty);
		if (offer != null) {
			TradingUtility.substractTradingFee(player, price);
			String msg = "Sold: " + qty + " " + itemName + " for " + price + " money.";
			new AsynchronousProgramExecutor("trade", msg).start();
			DBCommandQueue.get().enqueue(new LogTradeEventCommand(player, item, qty, price));
			logger.info("[MARKET] SUCCESS: Offer created -> ID=" + offer.getID() + ", player=" + player.getName() + ", item=" + itemName + ", qty=" + qty + ", price=" + price + ", fee=" + fee);
			player.addEvent(new SoundEvent(SoundID.COMMERCE2, SoundLayer.CREATURE_NOISE));
			player.sendPrivateText("I added your offer of " + qty + " " + itemName + " to the market and took the fee of " + fee + " money.");
		} else {
			logger.warn("[MARKET] Place Offer failed in market.createOffer (insufficient stack quantity or slot full)");
			player.sendPrivateText("Could not place offer. Please check that you have enough items and open slots.");
		}
	}

	private void handleBuyOffer(Player player, Market market, MarketManagerNPC manager, RPAction action) {
		String offerId = action.get("offer_id");
		logger.info("[MARKET] Buy Offer: buyer=" + player.getName() + ", offerId=" + offerId);

		Offer offer = findOffer(market, manager, offerId);
		if (offer == null) {
			logger.warn("[MARKET] Buy Offer rejected: offerId '" + offerId + "' not found.");
			player.sendPrivateText("That offer is no longer available.");
			return;
		}

		if (market.acceptOffer(offer, player)) {
			logger.info("[MARKET] SUCCESS: " + player.getName() + " purchased offer " + offerId + " (" + offer.getItemName() + ") from " + offer.getOfferer() + " for " + offer.getPrice() + " money.");
			player.addEvent(new SoundEvent(SoundID.COMMERCE2, SoundLayer.CREATURE_NOISE));
			player.sendPrivateText("You purchased " + offer.getItemName() + " from " + offer.getOfferer() + ".");
		} else {
			logger.warn("[MARKET] Buy Offer failed: " + player.getName() + " could not afford " + offer.getPrice() + " money or bag space full.");
			player.sendPrivateText("Could not complete purchase. Check that you have enough money and bag space.");
		}
	}

	private void handleProlongOffer(Player player, Market market, MarketManagerNPC manager, RPAction action) {
		String offerId = action.get("offer_id");
		logger.info("[MARKET] Prolong Offer: player=" + player.getName() + ", offerId=" + offerId);

		Offer offer = findOffer(market, manager, offerId);
		if (offer == null) {
			logger.warn("[MARKET] Prolong Offer rejected: offerId '" + offerId + "' not found.");
			player.sendPrivateText("Offer not found.");
			return;
		}

		if (!offer.getOfferer().equals(player.getName())) {
			logger.warn("[MARKET] Prolong Offer rejected: player " + player.getName() + " is not offer owner (" + offer.getOfferer() + ")");
			player.sendPrivateText("You can only prolong your own offers.");
			return;
		}

		int fee = TradingUtility.calculateFee(player, offer.getPrice()).intValue();
		if (!TradingUtility.canPlayerAffordTradingFee(player, offer.getPrice())) {
			logger.warn("[MARKET] Prolong Offer rejected: cannot afford fee " + fee);
			player.sendPrivateText("You cannot afford the fee of " + fee + " money to prolong this offer.");
			return;
		}

		if (market.prolongOffer(offer) != null) {
			TradingUtility.substractTradingFee(player, offer.getPrice());
			logger.info("[MARKET] SUCCESS: Prolonged offer " + offerId + " for " + player.getName() + " (fee=" + fee + ")");
			player.sendPrivateText("I prolonged your offer of " + offer.getItemName() + " for 7 days.");
		}
	}

	private void handleRemoveOffer(Player player, Market market, MarketManagerNPC manager, RPAction action) {
		String offerId = action.get("offer_id");
		logger.info("[MARKET] Remove Offer: player=" + player.getName() + ", offerId=" + offerId);

		Offer offer = findOffer(market, manager, offerId);
		if (offer == null) {
			logger.warn("[MARKET] Remove Offer rejected: offerId '" + offerId + "' not found.");
			player.sendPrivateText("Offer not found.");
			return;
		}

		if (!offer.getOfferer().equals(player.getName())) {
			logger.warn("[MARKET] Remove Offer rejected: player " + player.getName() + " is not offer owner (" + offer.getOfferer() + ")");
			player.sendPrivateText("You can only remove your own offers.");
			return;
		}

		market.removeOffer(offer, player);
		logger.info("[MARKET] SUCCESS: Removed offer " + offerId + " for " + player.getName() + " and returned items.");
		player.sendPrivateText("Your offer has been removed from the market.");
	}

	private void handlePlaceRequest(Player player, Market market, RPAction action) {
		String itemName = action.get("item");
		int qty = parseIntSafely(action.get("qty"), 1);
		int price = parseIntSafely(action.get("price"), 0);

		logger.info("[MARKET] Place Buy Request: player=" + player.getName() + ", item=" + itemName + ", qty=" + qty + ", pricePerPiece=" + price);

		if (itemName == null || itemName.trim().isEmpty() || qty <= 0 || price <= 0) {
			logger.warn("[MARKET] Place Buy Request rejected: invalid parameters (item=" + itemName + ", qty=" + qty + ", price=" + price + ")");
			player.sendPrivateText("Invalid buy request details.");
			return;
		}

		String itemClass = itemName.toLowerCase().replace(" ", "_");
		BuyRequest req = market.createBuyRequest(player, itemName, itemClass, qty, price);
		if (req != null) {
			int totalDeposit = qty * price;
			int fee = TradingUtility.calculateFee(player, totalDeposit).intValue();
			logger.info("[MARKET] SUCCESS: Buy Request created -> ID=" + req.getID() + ", buyer=" + player.getName() + ", item=" + itemName + ", qty=" + qty + ", price=" + price + " (Deposit: " + totalDeposit + ", Fee: " + fee + ")");
			player.addEvent(new SoundEvent(SoundID.COMMERCE2, SoundLayer.CREATURE_NOISE));
			player.sendPrivateText("Buy request placed for " + qty + " " + itemName + " at " + price + " money each (Deposit: " + totalDeposit + ", Fee: " + fee + ").");
		} else {
			int totalDeposit = qty * price;
			int fee = TradingUtility.calculateFee(player, totalDeposit).intValue();
			logger.warn("[MARKET] Place Buy Request failed: " + player.getName() + " has insufficient funds (required: " + (totalDeposit + fee) + " money)");
			player.sendPrivateText("You do not have enough money. Total required: " + (totalDeposit + fee) + " money (Deposit: " + totalDeposit + " + Fee: " + fee + ").");
		}
	}

	private void handleFulfillRequest(Player player, Market market, MarketManagerNPC manager, RPAction action) {
		String reqIdStr = action.get("request_id");
		int qty = parseIntSafely(action.get("qty"), 1);
		logger.info("[MARKET] Fulfill Request: seller=" + player.getName() + ", reqId=" + reqIdStr + ", qty=" + qty);

		BuyRequest req = findBuyRequest(market, manager, reqIdStr);
		if (req == null) {
			logger.warn("[MARKET] Fulfill Request rejected: buy request '" + reqIdStr + "' not found.");
			player.sendPrivateText("That buy request is no longer active.");
			return;
		}

		int fulfilled = market.fulfillBuyRequest(player, req, qty);
		if (fulfilled > 0) {
			int totalEarned = fulfilled * req.getPricePerPiece();
			logger.info("[MARKET] SUCCESS: " + player.getName() + " fulfilled " + fulfilled + "x " + req.getItemName() + " for buyer " + req.getBuyer() + " (earned: " + totalEarned + " money)");
			player.addEvent(new SoundEvent(SoundID.COMMERCE2, SoundLayer.CREATURE_NOISE));
			player.sendPrivateText("You sold " + fulfilled + " " + req.getItemName() + " to " + req.getBuyer() + " and earned " + totalEarned + " money.");
		} else {
			logger.warn("[MARKET] Fulfill Request failed: " + player.getName() + " does not have enough " + req.getItemName() + " in inventory.");
			player.sendPrivateText("Could not fulfill buy request. Make sure you have enough " + req.getItemName() + " in your bag.");
		}
	}

	private void handleCancelRequest(Player player, Market market, MarketManagerNPC manager, RPAction action) {
		String reqIdStr = action.get("request_id");
		logger.info("[MARKET] Cancel Request: player=" + player.getName() + ", reqId=" + reqIdStr);

		BuyRequest target = findBuyRequest(market, manager, reqIdStr);
		if (target == null || !target.getBuyer().equals(player.getName())) {
			logger.warn("[MARKET] Cancel Request rejected: no matching request found for " + player.getName());
			player.sendPrivateText("No active buy request found to cancel.");
			return;
		}

		int refund = market.cancelBuyRequest(player, target);
		if (refund >= 0) {
			logger.info("[MARKET] SUCCESS: Cancelled buy request for " + player.getName() + " (refunded deposit: " + refund + " money to earnings)");
			player.sendPrivateText("Your buy request for " + target.getItemName() + " has been cancelled. Remaining deposit of " + refund + " money was credited to your earnings.");
		}
	}

	private void handleClaimItems(Player player, Market market, RPAction action) {
		String itemName = (action != null && action.has("item")) ? action.get("item") : null;
		logger.info("[MARKET] Claim Items: player=" + player.getName() + (itemName != null ? ", item=" + itemName : ""));
		List<Item> claimed = (itemName != null && !itemName.trim().isEmpty())
				? market.claimItems(player, itemName.trim())
				: market.claimItems(player);
		if (!claimed.isEmpty()) {
			logger.info("[MARKET] SUCCESS: " + player.getName() + " claimed " + claimed.size() + " delivery items.");
			player.addEvent(new SoundEvent(SoundID.COMMERCE2, SoundLayer.CREATURE_NOISE));
			player.sendPrivateText("You collected " + claimed.size() + " delivered item stack(s) from Harold.");
		} else {
			logger.info("[MARKET] Claim Items: no delivered items waiting for " + player.getName());
			player.sendPrivateText("You have no delivered items waiting in the collection box.");
		}
	}

	private void handleFetchEarnings(Player player, Market market) {
		logger.info("[MARKET] Fetch Earnings: player=" + player.getName());
		Set<Earning> earnings = market.fetchEarnings(player);
		if (!earnings.isEmpty()) {
			int total = 0;
			for (Earning e : earnings) {
				total += e.getValue();
			}
			logger.info("[MARKET] SUCCESS: " + player.getName() + " collected " + total + " money in earnings.");
			player.addEvent(new SoundEvent(SoundID.COMMERCE2, SoundLayer.CREATURE_NOISE));
			player.sendPrivateText("You collected " + total + " money in earnings from Harold.");
		} else {
			logger.info("[MARKET] Fetch Earnings: no earnings waiting for " + player.getName());
			player.sendPrivateText("You have no sales earnings to collect.");
		}
	}

	private int parseIntSafely(String val, int def) {
		if (val == null || val.trim().isEmpty()) return def;
		try {
			return Integer.parseInt(val.trim());
		} catch (Exception e) {
			return def;
		}
	}

	private Market getMarket(Player player) {
		if (player.getZone() != null) {
			Market shop = TradeCenterZoneConfigurator.getShopFromZone(player.getZone());
			if (shop != null) return shop;
		}
		StendhalRPZone semosTavern = SingletonRepository.getRPWorld().getZone("0_semos_tavern");
		if (semosTavern != null) {
			return TradeCenterZoneConfigurator.getShopFromZone(semosTavern);
		}
		return null;
	}

	private MarketManagerNPC findMarketManager(Player player) {
		if (player.getZone() != null) {
			for (RPObject obj : player.getZone()) {
				if (obj instanceof MarketManagerNPC) {
					return (MarketManagerNPC) obj;
				}
			}
		}
		return null;
	}

	private Offer findOffer(Market market, MarketManagerNPC manager, String offerId) {
		if (offerId == null || offerId.isEmpty()) return null;
		for (Offer o : market.getOffersOlderThan(0)) {
			String objId = o.getID() != null ? String.valueOf(o.getID().getObjectID()) : null;
			String hash = String.valueOf(o.hashCode());
			if (offerId.equals(objId) || offerId.equals(hash) || (o.getID() != null && offerId.equals(o.getID().toString()))) {
				return o;
			}
		}
		for (Offer o : market.getExpiredOffers()) {
			String objId = o.getID() != null ? String.valueOf(o.getID().getObjectID()) : null;
			String hash = String.valueOf(o.hashCode());
			if (offerId.equals(objId) || offerId.equals(hash) || (o.getID() != null && offerId.equals(o.getID().toString()))) {
				return o;
			}
		}
		if (manager != null && manager.getOfferMap().containsKey(offerId)) {
			return manager.getOfferMap().get(offerId);
		}
		return null;
	}

	private BuyRequest findBuyRequest(Market market, MarketManagerNPC manager, String reqIdStr) {
		if (reqIdStr == null || reqIdStr.isEmpty()) return null;
		for (BuyRequest r : market.getBuyRequestsOlderThan(0)) {
			String objId = r.getID() != null ? String.valueOf(r.getID().getObjectID()) : null;
			String hash = String.valueOf(r.hashCode());
			if (reqIdStr.equals(objId) || reqIdStr.equals(hash) || (r.getID() != null && reqIdStr.equals(r.getID().toString()))) {
				return r;
			}
		}
		if (manager != null && manager.getRequestMap().containsKey(reqIdStr)) {
			return manager.getRequestMap().get(reqIdStr);
		}
		return null;
	}
}
