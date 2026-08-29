/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2026 - Stendhal                    *
 ***************************************************************************/
package games.stendhal.server.entity.trade;

import games.stendhal.server.core.engine.transformer.ItemTransformer;
import games.stendhal.server.entity.Entity;
import games.stendhal.server.entity.item.Item;
import marauroa.common.game.Definition.Type;
import marauroa.common.game.RPClass;
import marauroa.common.game.RPObject;

/**
 * Represents an item waiting in Harold's delivery/collection box for a player
 * who had a Buy Request fulfilled.
 */
public class ItemCollection extends Entity implements Dateable {

	public static final String ITEM_COLLECTION_RPCLASS_NAME = "item_collection";
	public static final String ITEM_SLOT_NAME = "item";
	private static final String PLAYER_ATTRIBUTE_NAME = "player";
	private static final String TIMESTAMP_ATTRIBUTE = "timestamp";

	public static void generateRPClass() {
		final RPClass clazz = new RPClass(ITEM_COLLECTION_RPCLASS_NAME);
		clazz.isA("entity");
		clazz.addAttribute(PLAYER_ATTRIBUTE_NAME, Type.STRING);
		clazz.addAttribute(TIMESTAMP_ATTRIBUTE, Type.STRING);
		clazz.addRPSlot(ITEM_SLOT_NAME, 1);
	}

	public ItemCollection(final String playerName, final Item item) {
		super();
		setRPClass(ITEM_COLLECTION_RPCLASS_NAME);
		hide();

		put(PLAYER_ATTRIBUTE_NAME, playerName);
		if (!hasSlot(ITEM_SLOT_NAME)) {
			addSlot(ITEM_SLOT_NAME);
		}
		if (item != null) {
			getSlot(ITEM_SLOT_NAME).add(item);
		}
		updateTimestamp();
	}

	public ItemCollection(final RPObject object) {
		super(object);
		setRPClass(ITEM_COLLECTION_RPCLASS_NAME);
		hide();

		if (object.hasSlot(ITEM_SLOT_NAME) && !object.getSlot(ITEM_SLOT_NAME).isEmpty()) {
			getSlot(ITEM_SLOT_NAME).clear();
			final RPObject itemObject = object.getSlot(ITEM_SLOT_NAME).getFirst();
			final Item entity = new ItemTransformer().transform(itemObject);
			if (entity != null) {
				getSlot(ITEM_SLOT_NAME).add(entity);
			}
		}
	}

	public String getPlayer() {
		return get(PLAYER_ATTRIBUTE_NAME);
	}

	public Item getItem() {
		if (hasSlot(ITEM_SLOT_NAME) && !getSlot(ITEM_SLOT_NAME).isEmpty()) {
			return (Item) getSlot(ITEM_SLOT_NAME).getFirst();
		}
		return null;
	}

	public boolean hasItem() {
		return hasSlot(ITEM_SLOT_NAME) && !getSlot(ITEM_SLOT_NAME).isEmpty();
	}

	public void updateTimestamp() {
		put(TIMESTAMP_ATTRIBUTE, Long.toString(System.currentTimeMillis()));
	}

	@Override
	public long getTimestamp() {
		if (has(TIMESTAMP_ATTRIBUTE)) {
			try {
				return Long.parseLong(get(TIMESTAMP_ATTRIBUTE));
			} catch (NumberFormatException e) {
				return 0L;
			}
		}
		return 0L;
	}
}
