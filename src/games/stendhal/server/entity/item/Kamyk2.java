/* $Id: Kamyk2.java,v 1.19 2011/04/02 15:44:18 kymara Exp $ */
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
 * A Kamyk2 which can be unwrapped.
 * 
 * @author kymara
 */
public class Kamyk2 extends Box {

	private static final String[] ITEMS = { "niebieski odłamek1", "niebieski odłamek2", "niebieski odłamek3", "niebieski odłamek4", "niebieski odłamek5", "niebieski odłamek6", "niebieski odłamek7", "niebieski odłamek8", "niebieski odłamek9", "niebieski odłamek10", "niebieski odłamek11",	"niebieski odłamek12", };

	/**
	 * Creates a new Kamyk2.
	 * 
	 * @param name
	 * @param clazz
	 * @param subclass
	 * @param attributes
	 */
	public Kamyk2(final String name, final String clazz, final String subclass,
			final Map<String, String> attributes) {
		super(name, clazz, subclass, attributes);

		setContent(ITEMS[Rand.rand(ITEMS.length)]);
	}

	/**
	 * Sets content.
	 * @param type of item to be produced.
	 */
	public void setContent(final String type) {
		setInfoString(type);
	}

	/**
	 * Copy constructor.
	 * 
	 * @param item
	 *            item to copy
	 */
	public Kamyk2(final Kamyk2 item) {
		super(item);
	}

	@Override
	protected boolean useMe(final Player player) {
		this.removeOne();

		final String itemName = getInfoString();
		final Item item = SingletonRepository.getEntityManager().getItem(itemName);
		player.sendPrivateText("Oderwałeś najpiękniejszy kawałek");
		player.equipOrPutOnGround(item);
		player.notifyWorldAboutChanges();

		return true;
	}

}
