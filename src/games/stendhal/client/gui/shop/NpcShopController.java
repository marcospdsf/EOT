/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2026 - Stendhal                    *
 ***************************************************************************/
package games.stendhal.client.gui.shop;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import games.stendhal.client.StendhalClient;
import marauroa.common.game.RPAction;

/**
 * Controller for NPC merchant shop GUI windows.
 */
public class NpcShopController {

	private static NpcShopController instance;

	private NpcShopWindow shopWindow;

	public static synchronized NpcShopController get() {
		if (instance == null) {
			instance = new NpcShopController();
		}
		return instance;
	}

	public static class ShopItem {
		private final String name;
		private final int price;
		private final String classPath;

		public ShopItem(final String name, final int price) {
			this(name, price, null);
		}

		public ShopItem(final String name, final int price, final String classPath) {
			this.name = name;
			this.price = price;
			this.classPath = classPath;
		}

		public String getName() {
			return name;
		}

		public int getPrice() {
			return price;
		}

		public String getClassPath() {
			return classPath;
		}
	}

	public void openShop(final String npcName, final String buyData, final String sellData) {
		final List<ShopItem> buyList = parseOffers(buyData);
		final List<ShopItem> sellList = parseOffers(sellData);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (shopWindow == null) {
					shopWindow = new NpcShopWindow();
				}
				shopWindow.setData(npcName, buyList, sellList);
				shopWindow.setVisible(true);
				shopWindow.toFront();
			}
		});
	}

	public void sendTrade(final String npcName, final String mode, final String itemName, final int quantity) {
		final RPAction action = new RPAction();
		action.put("type", "npc_shop_action");
		action.put("action", "trade");
		action.put("npc", npcName);
		action.put("mode", mode);
		action.put("item", itemName);
		action.put("quantity", quantity);

		StendhalClient.get().send(action);
	}

	public void sendClose(final String npcName) {
		if (npcName == null || npcName.trim().isEmpty()) {
			return;
		}
		final RPAction action = new RPAction();
		action.put("type", "npc_shop_action");
		action.put("action", "close");
		action.put("npc", npcName);

		StendhalClient.get().send(action);
	}

	private List<ShopItem> parseOffers(final String data) {
		final List<ShopItem> list = new ArrayList<>();
		if (data == null || data.trim().isEmpty()) {
			return list;
		}
		final String[] tokens = data.split(";");
		for (final String token : tokens) {
			final String[] parts = token.split(":");
			if (parts.length >= 2) {
				try {
					final String name = parts[0].trim();
					final int price = Integer.parseInt(parts[1].trim());
					final String classPath = (parts.length >= 3) ? parts[2].trim() : null;
					if (!name.isEmpty() && price > 0) {
						list.add(new ShopItem(name, price, classPath));
					}
				} catch (final NumberFormatException e) {
					// skip malformed
				}
			}
		}
		return list;
	}
}
