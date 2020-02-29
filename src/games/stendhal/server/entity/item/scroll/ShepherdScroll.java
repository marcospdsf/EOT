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

import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.rp.StendhalRPAction;
import games.stendhal.server.core.rule.EntityManager;
import games.stendhal.server.entity.creature.AttackableCreature;
import games.stendhal.server.entity.creature.Creature;
import games.stendhal.server.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Represents a creature Shepherd scroll.
 * yamaka 2008/11/18
 */
public class ShepherdScroll extends InfoStringScroll {

	private static final int MAX_ZONE_NPCS = 600; // 50;
	private List<String> zoneList = new ArrayList<String>(0);

	private static final Logger logger = Logger.getLogger(ShepherdScroll.class);

	/**
	 * Creates a new Shepherd scroll.
	 * 牧羊人召喚軸
	 * @param name
	 * @param clazz
	 * @param subclass
	 * @param attributes
	 */
	public ShepherdScroll(final String name, final String clazz, final String subclass,
			final Map<String, String> attributes) {
		super(name, clazz, subclass, attributes);
	}

	/**
	 * Copy constructor.
	 * 
	 * @param item
	 *            item to copy
	 */
	public ShepherdScroll(final ShepherdScroll item) {
		super(item);
	}

	/**
	 * Is invoked when a Shepherd scroll is used.
	 * 
	 * @param player
	 *            The player who used the scroll
	 * @return true iff summoning was successful
	 */
	@Override
	protected boolean useScroll(final Player player) {
		zoneList.add("0_semos_city");
		zoneList.add("1_semos_sky_pasture");
		zoneList.add("1_semos_sky_farm_n");
		
		final StendhalRPZone zone = player.getZone();
		
		String type = getInfoString();
//		final String infoString = type;
		final String zoneName = zone.getName();
		
    if (zoneList.indexOf(zoneName) < 0 && !player.getName().equals("yama") && !player.getName().equals("yamaka")) {
  		player.sendPrivateText("很抱歉!! 此區無法使用牧羊人召喚捲軸!");
  		return false;
	  }

		if (zone.getNPCList().size() >= MAX_ZONE_NPCS) {
			player.sendPrivateText("Mysteriously, the scroll does not function! Perhaps this area is too crowded...");
			logger.error("too many npcs");
			return false;
		}

		final int x = player.getInt("x");
		final int y = player.getInt("y");

		final EntityManager manager = SingletonRepository.getEntityManager();

		Creature pickedCreature = null;
    
		if (type != null) {
			pickedCreature = manager.getCreature(type);
		}

		if (pickedCreature == null) {
			player.sendPrivateText("This scroll does not seem to work. You should talk to the magician who created it.");
			return false;
		}

		// create it
		final AttackableCreature creature = new AttackableCreature(pickedCreature);

		StendhalRPAction.placeat(zone, creature, x, y);
		
//		creature.init();
//		if (player.getName().equals("yama") || player.getName().equals("yamaka")) {
//  		creature.setMaster(player);
//  	}
//		creature.clearDropItemList();
//		creature.put("title_type", "friend");

		return true;
	}
}
