/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2026 - Stendhal                    *
 ***************************************************************************/
package games.stendhal.server.events;

import java.util.List;
import java.util.Map;

import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.item.StackableItem;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.entity.trade.BuyRequest;
import games.stendhal.server.entity.trade.ItemCollection;
import games.stendhal.server.entity.trade.Market;
import games.stendhal.server.entity.trade.Offer;
import games.stendhal.server.maps.semos.tavern.market.MarketManagerNPC;
import marauroa.common.game.RPClass;
import marauroa.common.game.RPEvent;
import marauroa.common.game.RPObject;
import marauroa.common.game.SyntaxException;

/**
 * Event sent by server to notify the client about all current market offers,
 * active buy orders, and collection box deliveries.
 */
public class ShowMarketOffersEvent extends RPEvent {

	public static final String RPCLASS_NAME = "show_market_offers";
	public static final String OFFERS_DATA = "offers_data";

	/**
	 * Creates the rpclass.
	 */
	public static void generateRPClass() {
		final RPClass rpclass = new RPClass(RPCLASS_NAME);
		rpclass.add(marauroa.common.game.Definition.DefinitionClass.ATTRIBUTE, OFFERS_DATA, marauroa.common.game.Definition.Type.LONG_STRING);
	}

	public ShowMarketOffersEvent() {
		super(RPCLASS_NAME);
	}

	public ShowMarketOffersEvent(Market market, Player player) {
		super(RPCLASS_NAME);

		StringBuilder sb = new StringBuilder();

		if (market != null && player != null) {
			MarketManagerNPC manager = findMarketManager(player);
			Map<String, Offer> offerMap = null;
			Map<String, BuyRequest> reqMap = null;
			if (manager != null) {
				offerMap = manager.getOfferMap();
				reqMap = manager.getRequestMap();
			}

			// Section 0: Active market sell offers ("Market" tab)
			List<Offer> activeOffers = market.getOffersOlderThan(0);
			for (Offer offer : activeOffers) {
				String key = getOfferKey(offer);
				if (offerMap != null) {
					offerMap.put(key, offer);
				}
				serializeOffer(sb, key, offer, false);
			}

			sb.append("---SECTION---\n");

			// Section 1: Player's active sell offers ("My Offers" tab)
			for (Offer offer : activeOffers) {
				if (offer.getOfferer().equals(player.getName())) {
					String key = getOfferKey(offer);
					serializeOffer(sb, key, offer, false);
				}
			}

			sb.append("---SECTION---\n");

			// Section 2: Player's expired sell offers ("My Offers" tab expired)
			List<Offer> expiredOffers = market.getExpiredOffers();
			for (Offer offer : expiredOffers) {
				if (offer.getOfferer().equals(player.getName())) {
					String key = getOfferKey(offer);
					if (offerMap != null) {
						offerMap.put(key, offer);
					}
					serializeOffer(sb, key, offer, true);
				}
			}

			sb.append("---SECTION---\n");

			// Section 3: All active buy requests ("All Requests" tab)
			List<BuyRequest> activeRequests = market.getBuyRequestsOlderThan(0);
			for (BuyRequest req : activeRequests) {
				String reqKey = getRequestKey(req);
				if (reqMap != null) {
					reqMap.put(reqKey, req);
				}
				serializeRequest(sb, reqKey, req);
			}

			sb.append("---SECTION---\n");

			// Section 4: Player's own active buy requests ("My Requests" tab)
			for (BuyRequest req : activeRequests) {
				if (req.getBuyer().equals(player.getName())) {
					String reqKey = getRequestKey(req);
					serializeRequest(sb, reqKey, req);
				}
			}

			sb.append("---SECTION---\n");

			// Section 5: Player's delivered items in collection box
			List<ItemCollection> collections = market.getItemCollectionsOfPlayer(player);
			for (ItemCollection coll : collections) {
				serializeCollection(sb, coll);
			}
		}

		put(OFFERS_DATA, sb.toString());
	}

	private String getOfferKey(Offer offer) {
		if (offer.getID() != null) {
			return String.valueOf(offer.getID().getObjectID());
		}
		return String.valueOf(offer.hashCode());
	}

	private String getRequestKey(BuyRequest req) {
		if (req.getID() != null) {
			return String.valueOf(req.getID().getObjectID());
		}
		return String.valueOf(req.hashCode());
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

	private void serializeOffer(StringBuilder sb, String key, Offer offer, boolean isExpired) {
		if (offer == null || !offer.hasItem()) {
			return;
		}
		Item item = offer.getItem();
		int quantity = 1;
		boolean isStackable = false;
		if (item instanceof StackableItem) {
			quantity = ((StackableItem) item).getQuantity();
			isStackable = true;
		}

		String classPath = "";
		if (item.has("class")) {
			classPath = item.get("class");
			if (item.has("subclass")) {
				classPath += "/" + item.get("subclass");
			}
		} else {
			classPath = offer.getItemName().toLowerCase().replace(" ", "_");
		}

		sb.append(key).append("|~|")
		  .append(offer.getItemName()).append("|~|")
		  .append(quantity).append("|~|")
		  .append(offer.getPrice()).append("|~|")
		  .append(quantity + " " + offer.getItemName()).append("|~|")
		  .append(offer.getOfferer()).append("|~|")
		  .append(isExpired ? "1" : "0").append("|~|")
		  .append(isStackable ? "1" : "0").append("|~|")
		  .append(isStackable ? "1" : "0").append("|~|")
		  .append(classPath).append("\n");
	}

	private void serializeRequest(StringBuilder sb, String key, BuyRequest req) {
		String classPath = req.getItemClass();
		if (classPath == null || classPath.isEmpty() || !classPath.contains("/")) {
			final Item proto = SingletonRepository.getEntityManager().getItem(req.getItemName());
			if (proto != null && proto.has("class")) {
				classPath = proto.get("class");
				if (proto.has("subclass")) {
					classPath += "/" + proto.get("subclass");
				}
			}
		}
		if (classPath == null || classPath.isEmpty()) {
			classPath = req.getItemName().toLowerCase().replace(" ", "_");
		}

		sb.append(key).append("|~|")
		  .append(req.getItemName()).append("|~|")
		  .append(req.getTotalQuantity()).append("|~|")
		  .append(req.getFulfilledQuantity()).append("|~|")
		  .append(req.getRemainingQuantity()).append("|~|")
		  .append(req.getPricePerPiece()).append("|~|")
		  .append(req.getBuyer()).append("|~|")
		  .append(classPath).append("\n");
	}

	private void serializeCollection(StringBuilder sb, ItemCollection coll) {
		if (coll == null || !coll.hasItem()) {
			return;
		}
		Item item = coll.getItem();
		int quantity = 1;
		if (item instanceof StackableItem) {
			quantity = ((StackableItem) item).getQuantity();
		}

		String classPath = "";
		if (item.has("class")) {
			classPath = item.get("class");
			if (item.has("subclass")) {
				classPath += "/" + item.get("subclass");
			}
		} else {
			classPath = item.getName().toLowerCase().replace(" ", "_");
		}

		sb.append(coll.getID() != null ? String.valueOf(coll.getID().getObjectID()) : "0").append("|~|")
		  .append(item.getName()).append("|~|")
		  .append(quantity).append("|~|")
		  .append(classPath).append("\n");
	}
}
