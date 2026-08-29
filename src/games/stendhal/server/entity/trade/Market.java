/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2010 - Stendhal                    *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.server.entity.trade;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import games.stendhal.server.core.engine.GameEvent;
import games.stendhal.server.core.engine.ItemLogger;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.dbcommand.LogMarketTradeCommand;
import games.stendhal.server.core.engine.dbcommand.LogSimpleItemEventCommand;
import games.stendhal.server.core.engine.dbcommand.SyncMarketDataCommand;
import games.stendhal.server.entity.PassiveEntity;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.item.Money;
import games.stendhal.server.entity.item.StackableItem;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.semos.tavern.market.TradingUtility;
import marauroa.common.game.Definition;
import marauroa.common.game.RPClass;
import marauroa.common.game.RPObject;
import marauroa.common.game.RPSlot;
import marauroa.server.db.command.DBCommandQueue;

/**
 * A Market handles sales offers of players. Players can place offers or accept
 * offers to buy a desired item. Offers last only for a certain amount of time
 * before they expire and can be prolonged. Expired Offers get removed
 * permanently from the market after another period of time. When an offer has
 * been accepted, the offering Player can come and fetch his earnings for that
 * sale.
 *
 * @author madmetzger, kiheru
 */
public class Market extends PassiveEntity {
	private static Logger logger = Logger.getLogger(Market.class);

	/**
	 * the RPClass name for the market
	 */
	public static final String MARKET_RPCLASS_NAME = "market";
	/**
	 * the name of the slot where the earnings are stored
	 */
	public static final String EARNINGS_SLOT_NAME = "earnings";
	/**
	 * the name of the slot where the offers are stored
	 */
	public static final String OFFERS_SLOT_NAME = "offers";
	/**
	 * the name of the slot where the expired offers are stored
	 */
	public static final String EXPIRED_OFFERS_SLOT_NAME = "expired_offers";

	/**
	 * the name of the slot where the active buy requests are stored
	 */
	public static final String BUY_REQUESTS_SLOT_NAME = "buy_requests";
	/**
	 * the name of the slot where the expired buy requests are stored
	 */
	public static final String EXPIRED_BUY_REQUESTS_SLOT_NAME = "expired_buy_requests";
	/**
	 * the name of the slot where fulfilled item deliveries are stored for claiming
	 */
	public static final String ITEM_COLLECTIONS_SLOT_NAME = "item_collections";

	/**
	 * Generate the RPClass for the Market
	 */
	public static void generateRPClass() {
		final RPClass shop = new RPClass(MARKET_RPCLASS_NAME);
		shop.isA("entity");
		shop.addRPSlot(OFFERS_SLOT_NAME, -1, Definition.HIDDEN);
		shop.addRPSlot(EARNINGS_SLOT_NAME, -1, Definition.HIDDEN);
		shop.addRPSlot(EXPIRED_OFFERS_SLOT_NAME, -1, Definition.HIDDEN);
		shop.addRPSlot(BUY_REQUESTS_SLOT_NAME, -1, Definition.HIDDEN);
		shop.addRPSlot(EXPIRED_BUY_REQUESTS_SLOT_NAME, -1, Definition.HIDDEN);
		shop.addRPSlot(ITEM_COLLECTIONS_SLOT_NAME, -1, Definition.HIDDEN);
	}

	/**
	 * Creates a new Market from an RPObject
	 *
	 * @param object
	 */
	public Market(final RPObject object) {
		super(object);
		this.setRPClass(MARKET_RPCLASS_NAME);
		hide();

		// delete the slots whose contents get wrong types
		// when loaded from the db
		if (hasSlot(OFFERS_SLOT_NAME)) {
			removeSlot(OFFERS_SLOT_NAME);
		}
		addSlot(OFFERS_SLOT_NAME);

		if (hasSlot(EARNINGS_SLOT_NAME)) {
			removeSlot(EARNINGS_SLOT_NAME);
		}
		addSlot(EARNINGS_SLOT_NAME);

		if (hasSlot(EXPIRED_OFFERS_SLOT_NAME)) {
			removeSlot(EXPIRED_OFFERS_SLOT_NAME);
		}
		addSlot(EXPIRED_OFFERS_SLOT_NAME);

		if (hasSlot(BUY_REQUESTS_SLOT_NAME)) {
			removeSlot(BUY_REQUESTS_SLOT_NAME);
		}
		addSlot(BUY_REQUESTS_SLOT_NAME);

		if (hasSlot(EXPIRED_BUY_REQUESTS_SLOT_NAME)) {
			removeSlot(EXPIRED_BUY_REQUESTS_SLOT_NAME);
		}
		addSlot(EXPIRED_BUY_REQUESTS_SLOT_NAME);

		if (hasSlot(ITEM_COLLECTIONS_SLOT_NAME)) {
			removeSlot(ITEM_COLLECTIONS_SLOT_NAME);
		}
		addSlot(ITEM_COLLECTIONS_SLOT_NAME);

		// copy the contents from the old slots
		if (object.hasSlot(OFFERS_SLOT_NAME)) {
			for (final RPObject rpo : object.getSlot(OFFERS_SLOT_NAME)) {
				Offer offer = new Offer(rpo);

				// an offer might have become obsolete, when items are removed
				if (offer.getItem() == null) {
					logger.warn("Cannot restore an offer by "
							+ offer.getOfferer() + " because this item"
							+ " was removed from items.xml");
					continue;
				}

				this.getSlot(OFFERS_SLOT_NAME).add(offer);
			}
		}
		if (object.hasSlot(EARNINGS_SLOT_NAME)) {
			for (final RPObject rpo : object.getSlot(EARNINGS_SLOT_NAME)) {
				final Earning earning = new Earning(rpo);
				this.getSlot(EARNINGS_SLOT_NAME).add(earning);
			}
		}
		if (object.hasSlot(EXPIRED_OFFERS_SLOT_NAME)) {
			for (final RPObject rpo : object.getSlot(EXPIRED_OFFERS_SLOT_NAME)) {
				Offer offer = new Offer(rpo);

				// an offer might have become obsolete, when items are removed
				if (offer.getItem() == null) {
					logger.warn("Cannot restore an offer by "
							+ offer.getOfferer() + " because this item"
							+ " was removed from items.xml");
					continue;
				}

				this.getSlot(EXPIRED_OFFERS_SLOT_NAME).add(offer);
			}
		}
		if (object.hasSlot(BUY_REQUESTS_SLOT_NAME)) {
			for (final RPObject rpo : object.getSlot(BUY_REQUESTS_SLOT_NAME)) {
				final BuyRequest request = new BuyRequest(rpo);
				this.getSlot(BUY_REQUESTS_SLOT_NAME).add(request);
			}
		}
		if (object.hasSlot(EXPIRED_BUY_REQUESTS_SLOT_NAME)) {
			for (final RPObject rpo : object.getSlot(EXPIRED_BUY_REQUESTS_SLOT_NAME)) {
				final BuyRequest request = new BuyRequest(rpo);
				this.getSlot(EXPIRED_BUY_REQUESTS_SLOT_NAME).add(request);
			}
		}
		if (object.hasSlot(ITEM_COLLECTIONS_SLOT_NAME)) {
			for (final RPObject rpo : object.getSlot(ITEM_COLLECTIONS_SLOT_NAME)) {
				final ItemCollection coll = new ItemCollection(rpo);
				if (coll.hasItem()) {
					this.getSlot(ITEM_COLLECTIONS_SLOT_NAME).add(coll);
				}
			}
		}
		store();
	}

	/**
	 * Factory method for the market
	 *
	 * @return a new Market
	 */
	public static Market createShop() {
		Market shop = new Market();
		return shop;
	}

	private Market() {
		super();
		setRPClass(MARKET_RPCLASS_NAME);
		hide();
		if (!this.hasSlot(OFFERS_SLOT_NAME)) {
			addSlot(OFFERS_SLOT_NAME);
		}
		if (!this.hasSlot(EARNINGS_SLOT_NAME)) {
			addSlot(EARNINGS_SLOT_NAME);
		}
		if (!this.hasSlot(EXPIRED_OFFERS_SLOT_NAME)) {
			addSlot(EXPIRED_OFFERS_SLOT_NAME);
		}
		if (!this.hasSlot(BUY_REQUESTS_SLOT_NAME)) {
			addSlot(BUY_REQUESTS_SLOT_NAME);
		}
		if (!this.hasSlot(EXPIRED_BUY_REQUESTS_SLOT_NAME)) {
			addSlot(EXPIRED_BUY_REQUESTS_SLOT_NAME);
		}
		if (!this.hasSlot(ITEM_COLLECTIONS_SLOT_NAME)) {
			addSlot(ITEM_COLLECTIONS_SLOT_NAME);
		}
		store();
	}

	/**
	 * creates a new offer at the market
	 *
	 * @param offerer
	 *            offering player
	 * @param item
	 *            item to sell
	 * @param money
	 *            price for the item
	 * @param number
	 *            number of items to sell
	 * @return the new created offer
	 */
	public Offer createOffer(final Player offerer, Item item,
			final Integer money, final Integer number) {
		if (item == null || item.isBound()) {
			return null;
		}

		if (item instanceof StackableItem) {
			if (!offerer.equals(item.getBaseContainer())) {
				return null;
			}
			item = ((StackableItem) item).splitOff(number);
			if (item == null) {
				return null;
			}
		} else if (!offerer.drop(item)) {
			return null;
		}

		Offer offer = new Offer(item, money, offerer);
		RPSlot slot = this.getSlot(OFFERS_SLOT_NAME);
		slot.add(offer);
		getZone().storeToDatabase();

		new ItemLogger().addLogItemEventCommand(new LogSimpleItemEventCommand(
				item, offerer, "slot-to-market", item.get("name"), Integer
						.toString(getQuantity(item)), "new offer",
				OFFERS_SLOT_NAME));
		DBCommandQueue.get().enqueue(new SyncMarketDataCommand(this));

		return offer;
	}

	/**
	 * Completes a trade of an offer by transferring item to accepting player
	 * and taking the money from him
	 *
	 * @param offer
	 * @param acceptingPlayer
	 * @return <code>true</code> if the trade was done, <code>false</code> on
	 * 	failure
	 */
	public boolean acceptOffer(final Offer offer, final Player acceptingPlayer) {
		if (getSlot(OFFERS_SLOT_NAME).has(offer.getID()) && offer.hasItem()) {
			int price = offer.getPrice().intValue();
			// Take the money; free items should always succeed
			if ((price == 0) || acceptingPlayer.drop("money", price)) {
				Item item = offer.getItem();
				offer.getSlot(Offer.OFFER_ITEM_SLOT_NAME).remove(item.getID());
				acceptingPlayer.equipOrPutOnGround(item);
				// Do not give trading bonus for accepting free items
				boolean reward = offer.shouldReward(acceptingPlayer)
						&& price > 0;
				final Earning earning = new Earning(offer.getPrice(),
						offer.getOfferer(), reward);
				this.getSlot(EARNINGS_SLOT_NAME).add(earning);
				this.getSlot(OFFERS_SLOT_NAME).remove(offer.getID());
				if (reward) {
					applyTradingBonus(acceptingPlayer);
				}

				// log the item movement
				String slotName = null;
				String target = "ground";
				if (item.getContainerSlot() != null) {
					slotName = item.getContainerSlot().getName();
					target = "slot";
				}
				int quantity = getQuantity(item);
				new ItemLogger()
						.addLogItemEventCommand(new LogSimpleItemEventCommand(
								item, acceptingPlayer, "market-to-" + target,
								item.get("name"), Integer
										.toString(quantity),
								"accept offer", slotName));

				int pricePerPiece = quantity > 0 ? (price / quantity) : price;
				DBCommandQueue.get().enqueue(new LogMarketTradeCommand("SELL_OFFER_BUY",
						acceptingPlayer.getName(), offer.getOfferer(), item.getName(),
						quantity, pricePerPiece, price, 0));
				DBCommandQueue.get().enqueue(new SyncMarketDataCommand(this));

				this.getZone().storeToDatabase();
				return true;
			}
		}

		return false;
	}

	/**
	 * rewards player for a successfull trade
	 *
	 * @param player
	 *            the player to reward
	 */
	private void applyTradingBonus(Player player) {
		player.incrementTradescore();
	}

	/**
	 * The earnings for complete trades are paid to the player.
	 *
	 * @param earner
	 *            the player fetching his earnings
	 * @return the fetched earnings
	 */
	public Set<Earning> fetchEarnings(final Player earner) {
		Set<Earning> earningsToRemove = new HashSet<Earning>();
		for (RPObject earningRPObject : this.getSlot(EARNINGS_SLOT_NAME)) {
			Earning earning = (Earning) earningRPObject;
			if (earning.getSeller().equals(earner.getName())) {
				earningsToRemove.add(earning);
			}
		}

		if(!earningsToRemove.isEmpty()) {
			int summedUpEarnings = 0;
			//sum up
			for (Earning earningToSumUp : earningsToRemove) {
				summedUpEarnings = summedUpEarnings + earningToSumUp.getValue();
			}
			Item moneyObj = SingletonRepository.getEntityManager().getItem("money");
			final StackableItem item = (moneyObj instanceof StackableItem) ? (StackableItem) moneyObj : new Money(summedUpEarnings);
			item.setQuantity(summedUpEarnings);
			if(earner.equipToInventoryOnly(item)) {
				//reward only if equipping was done
				for(Earning earningToReward : earningsToRemove) {
					if (earningToReward.shouldReward()) {
						applyTradingBonus(earner);
					}
				}
				removeEarnings(earningsToRemove);
			} else {
				//signal that no earnings were collected at all
				//a caller can only distinguish if anything was collected
				earningsToRemove.clear();
			}
		}

		return earningsToRemove;
	}

	/**
	 * Remove a set of earnings.
	 *
	 * @param earningsToRemove
	 *            The earnings to be removed
	 */
	public void removeEarnings(Iterable<Earning> earningsToRemove) {
		for (Earning earning : earningsToRemove) {
			this.getSlot(EARNINGS_SLOT_NAME).remove(earning.getID());
		}
		this.getZone().storeToDatabase();
	}

	/**
	 * counts the number of offers, a player has placed
	 *
	 * @param offerer
	 * @return the number of offers
	 */
	public int countOffersOfPlayer(Player offerer) {
		int count = 0;
		for (RPObject object : this.getSlot(OFFERS_SLOT_NAME)) {
			Offer offer = (Offer) object;
			if (offer.getOfferer().equals(offerer.getName())) {
				count = count + 1;
			}
		}
		return count;
	}

	/**
	 * removes an offer from the market and returns the item to the user
	 *
	 * @param o
	 *            the offer to remove
	 * @param p
	 *            the removing player
	 */
	public void removeOffer(Offer o, Player p) {
		Item item = o.getItem();
		String itemName = item.getName();

		o.getSlot(Offer.OFFER_ITEM_SLOT_NAME).remove(item.getID());
		p.equipOrPutOnGround(item);

		getSlot(OFFERS_SLOT_NAME).remove(o.getID());

		getExpiredOffers().remove(o);
		getSlot(EXPIRED_OFFERS_SLOT_NAME).remove(o.getID());

		getZone().storeToDatabase();

		// log the item movement
		String slotName = null;
		String target = "ground";
		if (item.getContainerSlot() != null) {
			slotName = item.getContainerSlot().getName();
			target = "slot";
		}
		new ItemLogger().addLogItemEventCommand(new LogSimpleItemEventCommand(item, p,
						"market-to-" + target, itemName, Integer
								.toString(getQuantity(item)), "remove offer",
						slotName));
	}

	/**
	 * expires an offer and removes it from the available offers
	 *
	 * @param o
	 *            the offer to expire
	 */
	public void expireOffer(Offer o) {
		this.getSlot(OFFERS_SLOT_NAME).remove(o.getID());
		this.getSlot(EXPIRED_OFFERS_SLOT_NAME).add(o);
		this.getZone().storeToDatabase();
		String itemname = "null";
		if (o.hasItem()) {
			itemname = o.getItem().getName();
		}
		new GameEvent("market", "expire-offer", o.getOfferer(), itemname, o.getPrice().toString()).raise();
	}

	/**
	 * @return all currently expired offers in the market
	 */
	public List<Offer> getExpiredOffers() {
		List<Offer> expiredOffers = new LinkedList<Offer>();
		for(RPObject o : getSlot(EXPIRED_OFFERS_SLOT_NAME)) {
			expiredOffers.add((Offer) o);
		}
		return expiredOffers;
	}

	/**
	 * removes an expired offer permanently from the market
	 *
	 * @param offerToRemove
	 */
	public void removeExpiredOffer(Offer offerToRemove) {
		this.getSlot(EXPIRED_OFFERS_SLOT_NAME).remove(offerToRemove.getID());

		Item item = offerToRemove.getItem();
		if (item != null) {
			new ItemLogger().destroy(null, this.getSlot(EXPIRED_OFFERS_SLOT_NAME),
					item, "timeout");
		}

		this.getZone().storeToDatabase();
	}

	/**
	 * prolongs an offer in the market to make it available again
	 *
	 * @param offer
	 *            the offer to prolong
	 * @return the prolonged offer
	 */
	public Offer prolongOffer(Offer offer) {
		offer.updateTimestamp();
		if (getSlot(EXPIRED_OFFERS_SLOT_NAME).has(offer.getID())) {
			// It had expired. Move to active offers slot.
			this.getSlot(EXPIRED_OFFERS_SLOT_NAME).remove(offer.getID());
			RPSlot slot = this.getSlot(OFFERS_SLOT_NAME);
			slot.add(offer);
		} else if (!getSlot(OFFERS_SLOT_NAME).has(offer.getID())) {
			// Such an offer does not exist anymore
			return null;
		}

		this.getZone().storeToDatabase();
		return offer;
	}

	/**
	 * Get a list of offers whose timestamp is older than specified.
	 *
	 * @param seconds
	 *            age of offers in seconds
	 * @return list of offers that are older than the specified time
	 */
	public List<Offer> getOffersOlderThan(int seconds) {
		List<Offer> offers = new LinkedList<Offer>();
		for (RPObject o : getSlot(OFFERS_SLOT_NAME)) {
			offers.add((Offer) o);
		}
		return getOlderThan(offers, seconds);
	}

	/**
	 * Get a list of expired offers whose timestamp is older than specified.
	 *
	 * @param seconds
	 *            age of offers in seconds
	 * @return list of expired offers that are older than the specified time
	 */
	public List<Offer> getExpiredOffersOlderThan(int seconds) {
		return getOlderThan(getExpiredOffers(), seconds);
	}

	/**
	 * Get a list of earnings whose timestamp is older than specified.
	 *
	 * @param seconds
	 *            age of offers in seconds
	 * @return list of earnings that are older than the specified time
	 */
	public List<Earning> getEarningsOlderThan(int seconds) {
		RPSlot slot = this.getSlot(EARNINGS_SLOT_NAME);
		List<Earning> earnings = new LinkedList<Earning>();
		for (RPObject o : slot) {
			earnings.add((Earning) o);
		}
		return getOlderThan(earnings, seconds);
	}

	/**
	 * retrieves Dateable objects older than seconds from a given Iterable
	 *
	 * @param <T>
	 * @param set
	 *            the set to search in
	 * @param seconds
	 *            the maximum age
	 * @return the filtered list
	 */
	private <T extends Dateable> List<T> getOlderThan(Iterable<T> set,
			int seconds) {
		List<T> old = new LinkedList<T>();
		for (T obj : set) {
			if (System.currentTimeMillis() >= obj.getTimestamp() + 1000L
					* seconds) {
				old.add(obj);
			}
		}

		return old;
	}

	/**
	 * gets the quantity of an item
	 *
	 * @param item
	 * @return the quantity
	 */
	private int getQuantity(Item item) {
		int quantity = 1;
		if (item instanceof StackableItem) {
			quantity = ((StackableItem) item).getQuantity();
		}

		return quantity;
	}

	/**
	 * @param o
	 * @return true iff the Offer o is in this market's offers
	 */
	public boolean contains(Offer o) {
		return getSlot(OFFERS_SLOT_NAME).has(o.getID());
	}

	/**
	 * @param player
	 * @return true iff there are earnings for this player in the market
	 */
	public boolean hasEarningsFor(Player player) {
		for(RPObject o : this.getSlot(EARNINGS_SLOT_NAME)) {
			Earning e = (Earning) o;
			if(e.getSeller().equals(player.getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Creates a buy request / order in the market.
	 *
	 * @param player buyer placing the order
	 * @param itemName name of requested item
	 * @param itemClass class path of requested item
	 * @param quantity total quantity wanted
	 * @param pricePerPiece price offered per piece
	 * @return created BuyRequest or null on failure
	 */
	public BuyRequest createBuyRequest(final Player player, final String itemName, final String itemClass,
			final int quantity, final int pricePerPiece) {
		final int totalEscrow = quantity * pricePerPiece;
		final int fee = TradingUtility.calculateFee(player, totalEscrow).intValue();
		final int totalCost = totalEscrow + fee;

		if (player.drop("money", totalCost)) {
			final BuyRequest request = new BuyRequest(player, itemName, itemClass, quantity, pricePerPiece);
			this.getSlot(BUY_REQUESTS_SLOT_NAME).add(request);
			this.getZone().storeToDatabase();
			DBCommandQueue.get().enqueue(new SyncMarketDataCommand(this));
			return request;
		}
		return null;
	}

	/**
	 * Fulfills part or all of an active buy request.
	 *
	 * @param seller player selling items to fulfill the request
	 * @param request the buy request being fulfilled
	 * @param fulfillQuantity quantity of items to fulfill
	 * @return actual quantity fulfilled, or 0 on failure
	 */
	public int fulfillBuyRequest(final Player seller, final BuyRequest request, final int fulfillQuantity) {
		if (!getSlot(BUY_REQUESTS_SLOT_NAME).has(request.getID())) {
			return 0;
		}
		final int remaining = request.getRemainingQuantity();
		final int toFulfill = Math.min(fulfillQuantity, remaining);
		if (toFulfill <= 0) {
			return 0;
		}

		final String itemName = request.getItemName();
		final Item item = seller.getFirstEquipped(itemName);
		if (item == null) {
			return 0;
		}

		// Remove items from seller
		if (item instanceof StackableItem) {
			final StackableItem stack = (StackableItem) item;
			if (stack.getQuantity() < toFulfill) {
				return 0;
			}
			seller.drop(itemName, toFulfill);
		} else {
			if (toFulfill > 1) {
				return 0; // non-stackables handled 1 by 1
			}
			seller.drop(item);
		}

		// Pay seller immediately into inventory (or drop on ground if bag is full)
		final int earnedMoney = toFulfill * request.getPricePerPiece();
		final StackableItem moneyItem = new Money(earnedMoney);
		seller.equipOrPutOnGround(moneyItem);
		applyTradingBonus(seller);

		// Place delivered item into buyer's ItemCollection slot
		Item deliveredItem = (Item) item.clone();
		if (deliveredItem instanceof StackableItem) {
			((StackableItem) deliveredItem).setQuantity(toFulfill);
		}
		final ItemCollection collection = new ItemCollection(request.getBuyer(), deliveredItem);
		this.getSlot(ITEM_COLLECTIONS_SLOT_NAME).add(collection);

		// Update buy request fulfillment and escrow balance
		request.fulfill(toFulfill);
		if (request.isFulfilled()) {
			this.getSlot(BUY_REQUESTS_SLOT_NAME).remove(request.getID());
		}

		DBCommandQueue.get().enqueue(new LogMarketTradeCommand("BUY_ORDER_FULFILL",
				request.getBuyer(), seller.getName(), itemName,
				toFulfill, request.getPricePerPiece(), earnedMoney, 0));
		DBCommandQueue.get().enqueue(new SyncMarketDataCommand(this));

		this.getZone().storeToDatabase();
		return toFulfill;
	}

	/**
	 * Cancels a buy request and refunds the remaining escrow money to the buyer.
	 *
	 * @param buyer player who placed the buy request
	 * @param request the buy request to cancel
	 * @return amount of escrow money refunded, or -1 on error
	 */
	public int cancelBuyRequest(final Player buyer, final BuyRequest request) {
		if (!request.getBuyer().equals(buyer.getName())) {
			return -1;
		}
		final int refund = request.getEscrowMoney();
		if (refund > 0) {
			final Earning refundEarning = new Earning(refund, buyer.getName(), false);
			this.getSlot(EARNINGS_SLOT_NAME).add(refundEarning);
		}

		if (getSlot(BUY_REQUESTS_SLOT_NAME).has(request.getID())) {
			getSlot(BUY_REQUESTS_SLOT_NAME).remove(request.getID());
		} else if (getSlot(EXPIRED_BUY_REQUESTS_SLOT_NAME).has(request.getID())) {
			getSlot(EXPIRED_BUY_REQUESTS_SLOT_NAME).remove(request.getID());
		}

		DBCommandQueue.get().enqueue(new SyncMarketDataCommand(this));
		this.getZone().storeToDatabase();
		return refund;
	}

	/**
	 * Claims all fulfilled items waiting for the buyer in the delivery collection box.
	 *
	 * @param player player claiming items
	 * @return list of claimed items
	 */
	public List<Item> claimItems(final Player player) {
		return claimItems(player, null);
	}

	/**
	 * Claims fulfilled items waiting for the buyer in the delivery collection box,
	 * optionally filtered by a specific item name.
	 *
	 * @param player player claiming items
	 * @param specificItemName item name to filter by (or null for all items)
	 * @return list of claimed items
	 */
	public List<Item> claimItems(final Player player, final String specificItemName) {
		final List<ItemCollection> toRemove = new LinkedList<ItemCollection>();
		final List<Item> claimed = new LinkedList<Item>();

		for (final RPObject obj : this.getSlot(ITEM_COLLECTIONS_SLOT_NAME)) {
			final ItemCollection coll = (ItemCollection) obj;
			if (coll.getPlayer().equals(player.getName()) && coll.hasItem()) {
				final Item item = coll.getItem();
				if (specificItemName != null && !specificItemName.isEmpty() && !item.getName().equalsIgnoreCase(specificItemName)) {
					continue;
				}
				toRemove.add(coll);
				coll.getSlot(ItemCollection.ITEM_SLOT_NAME).remove(item.getID());
				player.equipOrPutOnGround(item);
				claimed.add(item);
			}
		}

		for (final ItemCollection coll : toRemove) {
			this.getSlot(ITEM_COLLECTIONS_SLOT_NAME).remove(coll.getID());
		}

		if (!toRemove.isEmpty()) {
			this.getZone().storeToDatabase();
		}
		return claimed;
	}

	/**
	 * Prolongs an expired buy request.
	 *
	 * @param request the request to prolong
	 * @return the prolonged BuyRequest or null on failure
	 */
	public BuyRequest prolongBuyRequest(final BuyRequest request) {
		request.updateTimestamp();
		if (getSlot(EXPIRED_BUY_REQUESTS_SLOT_NAME).has(request.getID())) {
			getSlot(EXPIRED_BUY_REQUESTS_SLOT_NAME).remove(request.getID());
			getSlot(BUY_REQUESTS_SLOT_NAME).add(request);
		} else if (!getSlot(BUY_REQUESTS_SLOT_NAME).has(request.getID())) {
			return null;
		}

		this.getZone().storeToDatabase();
		return request;
	}

	/**
	 * Returns active buy requests older than specified seconds.
	 */
	public List<BuyRequest> getBuyRequestsOlderThan(final int seconds) {
		final List<BuyRequest> requests = new LinkedList<BuyRequest>();
		for (final RPObject o : getSlot(BUY_REQUESTS_SLOT_NAME)) {
			requests.add((BuyRequest) o);
		}
		return getOlderThan(requests, seconds);
	}

	/**
	 * Returns expired buy requests.
	 */
	public List<BuyRequest> getExpiredBuyRequests() {
		final List<BuyRequest> requests = new LinkedList<BuyRequest>();
		for (final RPObject o : getSlot(EXPIRED_BUY_REQUESTS_SLOT_NAME)) {
			requests.add((BuyRequest) o);
		}
		return requests;
	}

	/**
	 * Counts active buy requests placed by a player.
	 */
	public int countBuyRequestsOfPlayer(final Player player) {
		int count = 0;
		for (final RPObject o : getSlot(BUY_REQUESTS_SLOT_NAME)) {
			final BuyRequest req = (BuyRequest) o;
			if (req.getBuyer().equals(player.getName())) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Returns all item collection objects belonging to a player.
	 */
	public List<ItemCollection> getItemCollectionsOfPlayer(final Player player) {
		final List<ItemCollection> list = new LinkedList<ItemCollection>();
		for (final RPObject o : getSlot(ITEM_COLLECTIONS_SLOT_NAME)) {
			final ItemCollection coll = (ItemCollection) o;
			if (coll.getPlayer().equals(player.getName())) {
				list.add(coll);
			}
		}
		return list;
	}

	/**
	 * Checks if a buy request is in this market's active requests.
	 */
	public boolean contains(final BuyRequest r) {
		return getSlot(BUY_REQUESTS_SLOT_NAME).has(r.getID());
	}
}
