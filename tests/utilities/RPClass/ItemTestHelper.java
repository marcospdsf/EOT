/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2010 - Stendhal                    *
 ***************************************************************************/
package utilities.RPClass;

import java.util.Arrays;

import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.item.Money;
import games.stendhal.server.entity.item.StackableItem;
import marauroa.common.game.RPClass;

public class ItemTestHelper {

	public static Item createItem() {
		return createItem("item");
	}

	public static Item createItem(final String name) {
		ItemTestHelper.generateRPClasses();
		final Item item = new Item(name, "itemclass", "subclass", null);
		item.setEquipableSlots(Arrays.asList("bag", "lhand", "rhand"));
		return item;
	}

	public static Item createItem(final String name, final int quantity) {
		ItemTestHelper.generateRPClasses();
		final StackableItem item;
		if ("money".equals(name)) {
			item = new Money(quantity);
		} else {
			item = new StackableItem(name, "itemclass", "subclass", null);
			item.setQuantity(quantity);
		}
		item.setEquipableSlots(Arrays.asList("bag", "pouch", "lhand", "rhand"));
		return item;
	}

	public static void generateRPClasses() {
		EntityTestHelper.generateRPClasses();

		if (!RPClass.hasRPClass("item")) {
			Item.generateRPClass();
		}
	}

}
