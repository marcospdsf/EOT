/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2026 - Stendhal                    *
 ***************************************************************************/
package games.stendhal.client.events;

import java.util.ArrayList;
import java.util.List;

import games.stendhal.client.entity.Entity;
import games.stendhal.client.gui.market.HaroldMarketController;
import games.stendhal.client.gui.market.HaroldMarketController.DeliveryCollectionItem;
import games.stendhal.client.gui.market.HaroldMarketController.MarketOfferItem;
import games.stendhal.client.gui.market.HaroldMarketController.MarketRequestItem;

/**
 * Client-side handler for receiving market data updates from the server.
 */
public class ShowMarketOffersEvent extends Event<Entity> {

	@Override
	public void execute() {
		String data = event.get("offers_data");
		if (data == null || data.isEmpty()) {
			return;
		}

		String[] sections = data.split("---SECTION---\n");
		List<MarketOfferItem> active = parseOffers(sections.length > 0 ? sections[0] : "");
		List<MarketOfferItem> mine = parseOffers(sections.length > 1 ? sections[1] : "");
		List<MarketOfferItem> expired = parseOffers(sections.length > 2 ? sections[2] : "");
		List<MarketRequestItem> others = parseRequests(sections.length > 3 ? sections[3] : "");
		List<MarketRequestItem> myReqs = parseRequests(sections.length > 4 ? sections[4] : "");
		List<DeliveryCollectionItem> deliveries = parseDeliveries(sections.length > 5 ? sections[5] : "");

		HaroldMarketController.get().updateOffers(active, mine, expired, others, myReqs, deliveries);
	}

	private List<MarketOfferItem> parseOffers(String sectionText) {
		List<MarketOfferItem> list = new ArrayList<>();
		if (sectionText == null || sectionText.trim().isEmpty()) {
			return list;
		}

		String[] lines = sectionText.trim().split("\n");
		for (String line : lines) {
			if (line.trim().isEmpty()) continue;
			String[] parts = line.split("\\|~\\|");
			if (parts.length >= 9) {
				String id = parts[0];
				String itemName = parts[1];
				int quantity = Integer.parseInt(parts[2]);
				int price = Integer.parseInt(parts[3]);
				String description = parts[4];
				String offerer = parts[5];
				boolean isExpired = parts[6].equals("1");
				boolean isStackable = parts[7].equals("1");
				boolean isPerPiece = parts[8].equals("1");

				String classPath = parts.length >= 10 ? parts[9] : itemName.toLowerCase().replace(" ", "_");

				list.add(new MarketOfferItem(id, itemName, quantity, price,
						description, offerer, isExpired, isStackable, isPerPiece, classPath));
			}
		}
		return list;
	}

	private List<MarketRequestItem> parseRequests(String sectionText) {
		List<MarketRequestItem> list = new ArrayList<>();
		if (sectionText == null || sectionText.trim().isEmpty()) {
			return list;
		}

		String[] lines = sectionText.trim().split("\n");
		for (String line : lines) {
			if (line.trim().isEmpty()) continue;
			String[] parts = line.split("\\|~\\|");
			if (parts.length >= 8) {
				String id = parts[0];
				String itemName = parts[1];
				int totalQuantity = Integer.parseInt(parts[2]);
				int fulfilledQuantity = Integer.parseInt(parts[3]);
				int remainingQuantity = Integer.parseInt(parts[4]);
				int pricePerPiece = Integer.parseInt(parts[5]);
				String buyer = parts[6];
				String classPath = parts[7];

				list.add(new MarketRequestItem(id, itemName, totalQuantity,
						fulfilledQuantity, remainingQuantity, pricePerPiece, buyer, classPath));
			}
		}
		return list;
	}

	private List<DeliveryCollectionItem> parseDeliveries(String sectionText) {
		List<DeliveryCollectionItem> list = new ArrayList<>();
		if (sectionText == null || sectionText.trim().isEmpty()) {
			return list;
		}

		String[] lines = sectionText.trim().split("\n");
		for (String line : lines) {
			if (line.trim().isEmpty()) continue;
			String[] parts = line.split("\\|~\\|");
			if (parts.length >= 4) {
				String id = parts[0];
				String itemName = parts[1];
				int quantity = Integer.parseInt(parts[2]);
				String classPath = parts[3];

				list.add(new DeliveryCollectionItem(id, itemName, quantity, classPath));
			}
		}
		return list;
	}
}
