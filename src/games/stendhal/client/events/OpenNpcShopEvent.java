/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2026 - Stendhal                    *
 ***************************************************************************/
package games.stendhal.client.events;

import games.stendhal.client.entity.Entity;
import games.stendhal.client.gui.shop.NpcShopController;

/**
 * Client-side event handler for opening the NPC Shop GUI window.
 */
public class OpenNpcShopEvent extends Event<Entity> {

	@Override
	public void execute() {
		final String npcName = event.has("npc_name") ? event.get("npc_name") : "Shop";
		final String buyItems = event.has("buy_items") ? event.get("buy_items") : "";
		final String sellItems = event.has("sell_items") ? event.get("sell_items") : "";

		NpcShopController.get().openShop(npcName, buyItems, sellItems);
	}
}
