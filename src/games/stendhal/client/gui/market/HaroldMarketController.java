/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2026 - Stendhal                    *
 ***************************************************************************/
package games.stendhal.client.gui.market;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import games.stendhal.client.ClientSingletonRepository;
import games.stendhal.client.gui.j2DClient;
import marauroa.common.game.RPAction;

/**
 * Controller for managing Harold's market GUI operations and state.
 */
public class HaroldMarketController {

	private static HaroldMarketController instance;
	private HaroldMarketWindow window;

	private final List<MarketOfferItem> activeOffers = new ArrayList<>();
	private final List<MarketOfferItem> myOffers = new ArrayList<>();
	private final List<MarketOfferItem> myExpiredOffers = new ArrayList<>();
	private final List<MarketRequestItem> otherRequests = new ArrayList<>();
	private final List<MarketRequestItem> myRequests = new ArrayList<>();
	private final List<DeliveryCollectionItem> deliveryItems = new ArrayList<>();

	public static class MarketOfferItem {
		private final String id;
		private final String itemName;
		private final int quantity;
		private final int price;
		private final String description;
		private final String offerer;
		private final boolean isExpired;
		private final boolean isStackable;
		private final boolean isPerPiece;
		private final String classPath;

		public MarketOfferItem(String id, String itemName, int quantity, int price,
				String description, String offerer, boolean isExpired,
				boolean isStackable, boolean isPerPiece, String classPath) {
			this.id = id;
			this.itemName = itemName;
			this.quantity = quantity;
			this.price = price;
			this.description = description;
			this.offerer = offerer;
			this.isExpired = isExpired;
			this.isStackable = isStackable;
			this.isPerPiece = isPerPiece;
			this.classPath = classPath;
		}

		public String getId() {
			return id;
		}

		public String getItemName() {
			return itemName;
		}

		public int getQuantity() {
			return quantity;
		}

		public int getPrice() {
			return price;
		}

		public String getDescription() {
			return description;
		}

		public String getOfferer() {
			return offerer;
		}

		public boolean isExpired() {
			return isExpired;
		}

		public boolean isStackable() {
			return isStackable;
		}

		public boolean isPerPiece() {
			return isPerPiece;
		}

		public String getClassPath() {
			return classPath;
		}
	}

	public static class MarketRequestItem {
		private final String id;
		private final String itemName;
		private final int totalQuantity;
		private final int fulfilledQuantity;
		private final int remainingQuantity;
		private final int pricePerPiece;
		private final String buyer;
		private final String classPath;

		public MarketRequestItem(String id, String itemName, int totalQuantity,
				int fulfilledQuantity, int remainingQuantity, int pricePerPiece,
				String buyer, String classPath) {
			this.id = id;
			this.itemName = itemName;
			this.totalQuantity = totalQuantity;
			this.fulfilledQuantity = fulfilledQuantity;
			this.remainingQuantity = remainingQuantity;
			this.pricePerPiece = pricePerPiece;
			this.buyer = buyer;
			this.classPath = classPath;
		}

		public String getId() {
			return id;
		}

		public String getItemName() {
			return itemName;
		}

		public int getTotalQuantity() {
			return totalQuantity;
		}

		public int getFulfilledQuantity() {
			return fulfilledQuantity;
		}

		public int getRemainingQuantity() {
			return remainingQuantity;
		}

		public int getPricePerPiece() {
			return pricePerPiece;
		}

		public int getTotalCost() {
			return remainingQuantity * pricePerPiece;
		}

		public String getBuyer() {
			return buyer;
		}

		public String getClassPath() {
			return classPath;
		}
	}

	public static class DeliveryCollectionItem {
		private final String id;
		private final String itemName;
		private final int quantity;
		private final String classPath;

		public DeliveryCollectionItem(String id, String itemName, int quantity, String classPath) {
			this.id = id;
			this.itemName = itemName;
			this.quantity = quantity;
			this.classPath = classPath;
		}

		public String getId() {
			return id;
		}

		public String getItemName() {
			return itemName;
		}

		public int getQuantity() {
			return quantity;
		}

		public String getClassPath() {
			return classPath;
		}
	}

	private HaroldMarketController() {
		// Window will be lazily initialized
	}

	public static synchronized HaroldMarketController get() {
		if (instance == null) {
			instance = new HaroldMarketController();
		}
		return instance;
	}

	/**
	 * Opens or brings Harold's market window to front.
	 */
	public void openWindow() {
		SwingUtilities.invokeLater(() -> {
			if (window == null) {
				window = new HaroldMarketWindow(this);
			}
			window.setVisible(true);
			window.toFront();
			refreshOffers();
		});
	}

	/**
	 * Request fresh offer and request data from the server.
	 */
	public void refreshOffers() {
		sendMarketAction("refresh");
	}

	/**
	 * Buys an offer by ID or offer number.
	 */
	public void buyOffer(String offerId, int quantity) {
		sendMarketAction("buy_offer", "offer_id", offerId, "qty", String.valueOf(quantity));
	}

	/**
	 * Places a new sale offer with Harold.
	 */
	public void placeOffer(String itemName, int price, int quantity, boolean perPiece) {
		sendMarketAction("place_offer", "item", itemName, "price", String.valueOf(price), "qty", String.valueOf(quantity), "per_piece", perPiece ? "1" : "0");
	}

	/**
	 * Places a new buy request with Harold.
	 */
	public void placeRequest(String itemName, int quantity, int pricePerPiece) {
		sendMarketAction("place_request", "item", itemName, "qty", String.valueOf(quantity), "price", String.valueOf(pricePerPiece));
	}

	/**
	 * Fulfills part or all of another player's buy request.
	 */
	public void fulfillRequest(String requestId, int quantity) {
		sendMarketAction("fulfill_request", "request_id", requestId, "qty", String.valueOf(quantity));
	}

	/**
	 * Cancels an active buy request.
	 */
	public void cancelRequest(String requestId) {
		sendMarketAction("cancel_request", "request_id", requestId);
	}

	/**
	 * Claims all fulfilled items from Harold's delivery collection box.
	 */
	public void claimItems() {
		sendMarketAction("claim_items");
	}

	/**
	 * Claims delivered items for a specific item from Harold's delivery collection box.
	 */
	public void claimItem(String itemName) {
		sendMarketAction("claim_items", "item", itemName);
	}

	/**
	 * Prolongs an expired offer.
	 */
	public void prolongOffer(String offerId) {
		sendMarketAction("prolong_offer", "offer_id", offerId);
	}

	/**
	 * Removes an active or expired offer.
	 */
	public void removeOffer(String offerId) {
		sendMarketAction("remove_offer", "offer_id", offerId);
	}

	/**
	 * Collects accrued earnings from Harold.
	 */
	public void fetchEarnings() {
		sendMarketAction("fetch_earnings");
	}

	/**
	 * Helper to send direct MarketAction to server.
	 */
	private void sendMarketAction(String subAction, String... keyValues) {
		RPAction action = new RPAction();
		action.put("type", "market_action");
		action.put("action", subAction);
		for (int i = 0; i < keyValues.length; i += 2) {
			if (i + 1 < keyValues.length) {
				action.put(keyValues[i], keyValues[i + 1]);
			}
		}
		ClientSingletonRepository.getClientFramework().send(action);
	}

	public List<MarketOfferItem> getActiveOffers() {
		return activeOffers;
	}

	public List<MarketOfferItem> getMyOffers() {
		return myOffers;
	}

	public List<MarketOfferItem> getMyExpiredOffers() {
		return myExpiredOffers;
	}

	public List<MarketRequestItem> getOtherRequests() {
		return otherRequests;
	}

	public List<MarketRequestItem> getMyRequests() {
		return myRequests;
	}

	public List<DeliveryCollectionItem> getDeliveryItems() {
		return deliveryItems;
	}

	public void updateOffers(List<MarketOfferItem> active, List<MarketOfferItem> mine,
			List<MarketOfferItem> expired, List<MarketRequestItem> others,
			List<MarketRequestItem> myReqs, List<DeliveryCollectionItem> deliveries) {
		activeOffers.clear();
		if (active != null) {
			activeOffers.addAll(active);
		}
		myOffers.clear();
		if (mine != null) {
			myOffers.addAll(mine);
		}
		myExpiredOffers.clear();
		if (expired != null) {
			myExpiredOffers.addAll(expired);
		}
		otherRequests.clear();
		if (others != null) {
			otherRequests.addAll(others);
		}
		myRequests.clear();
		if (myReqs != null) {
			myRequests.addAll(myReqs);
		}
		deliveryItems.clear();
		if (deliveries != null) {
			deliveryItems.addAll(deliveries);
		}

		if (window != null) {
			SwingUtilities.invokeLater(() -> window.updateData());
		}
	}
}
