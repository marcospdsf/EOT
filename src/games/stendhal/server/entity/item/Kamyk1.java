/* $Id: Kamyk1.java,v 1.19 2011/04/02 15:44:18 kymara Exp $ */
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
package games.stendhal.server.entity.item;

import java.util.Map;

import games.stendhal.common.Rand;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.entity.player.Player;

/**
 * A Kamyk1 which can be unwrapped.
 * 
 * @author kymara
 */
public class Kamyk1 extends Box {

	private static final String[] ITEMS = { "red shard", "red shard 1",
			"red shard 2", "red shard 3", "red shard 4", "red shard 5", "red shard 6" };

	/**
	 * Creates a new Kamyk1.
	 * 
	 * @param name
	 * @param clazz
	 * @param subclass
	 * @param attributes
	 */
	public Kamyk1(final String name, final String clazz, final String subclass,
			final Map<String, String> attributes) {
		super(name, clazz, subclass, attributes);

		setContent(ITEMS[Rand.rand(ITEMS.length)]);
	}

	/**
	 * Sets content.
	 * @param type of item to be produced.
	 */
	public void setContent(final String type) {
		setItemData(type);
	}

	/**
	 * Copy constructor.
	 * 
	 * @param item
	 *            item to copy
	 */
	public Kamyk1(final Kamyk1 item) {
		super(item);
	}

	@Override
	protected boolean useMe(final Player player) {
		this.removeOne();

		final String itemName = getItemData();
		final Item item = SingletonRepository.getEntityManager().getItem(itemName);
		player.sendPrivateText("You chipped off the finest shard!");
		player.equipOrPutOnGround(item);
		player.notifyWorldAboutChanges();

		return true;
	}

}
