/* 
 /***************************************************************************
 *                      (C) Copyright 2003 - Marauroa                      *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.server.entity.item.scroll;

import games.stendhal.common.NotificationType;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.entity.player.Player;

import java.util.Map;

/**
 * power scroll.
 * yamaka 2008/12/01
 */
public class PowerScroll extends InfoStringScroll {
  // base hp increase 20 point
	private final int increaseHP = 25;
	private final int addATKEXP = 10000;
	private final int addDEFEXP = 20000;

	/**
	 * Creates a new power scroll.
	 * 
	 * @param name
	 * @param clazz
	 * @param subclass
	 * @param attributes
	 */
	public PowerScroll(final String name, final String clazz, final String subclass,
			final Map<String, String> attributes) {
		super(name, clazz, subclass, attributes);
	}

	/**
	 * Copy constructor.
	 * 
	 * @param item
	 *            item to copy
	 */
	public PowerScroll(final PowerScroll item) {
		super(item);
	}

	/**
	 * Is invoked when a power scroll is used.
	 *
	 * 增加 base hp & heal & heal poison
	 * 
	 * @param player
	 *            The player who used the scroll
	 *
	 */
	@Override
	protected boolean useScroll(final Player player) {
		
    player.setBaseHP(player.getBaseHP() + increaseHP);
    player.setAtkXP(addATKEXP + player.getAtkXP());
		player.setDefXP(addDEFEXP + player.getDefXP());
		player.incAtkXP();
		player.incDefXP();
    player.heal();
		//player.healPoison();
		player.sendPrivateText(NotificationType.POSITIVE, "You gained " + addATKEXP + " of attack experience points.");
		player.sendPrivateText(NotificationType.POSITIVE, "You gained " + addDEFEXP + " of defence experience points.");
		player.sendPrivateText("After reading the spell, you feel a powerful force into the body(base hp +" + increaseHP + ")!!");
		
		SingletonRepository.getRPWorld().modify(player);
		return true;
	}
}
