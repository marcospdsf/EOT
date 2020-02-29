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
import games.stendhal.server.entity.creature.TrainingCreature;
import games.stendhal.server.entity.creature.Creature;
import games.stendhal.server.entity.player.Player;
import games.stendhal.common.Rand;
import games.stendhal.common.NotificationType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Represents a creature Training scroll.
 * yamaka 2008/12/07
 */
public class TrainingScroll extends InfoStringScroll {

	private static final int MAX_ZONE_NPCS = 600; // 50;

	private static final Logger logger = Logger.getLogger(TrainingScroll.class);

	/**
	 * Creates a new Training scroll.
	 * 
	 * @param name
	 * @param clazz
	 * @param subclass
	 * @param attributes
	 */
	public TrainingScroll(final String name, final String clazz, final String subclass,
			final Map<String, String> attributes) {
		super(name, clazz, subclass, attributes);
	}

	/**
	 * Copy constructor.
	 * 
	 * @param item
	 *            item to copy
	 */
	public TrainingScroll(final TrainingScroll item) {
		super(item);
	}

	/**
	 * Is invoked when a Training scroll is used.
	 * 
	 * @param player
	 *            The player who used the scroll
	 * @return true iff summoning was successful
	 */
	@Override
	protected boolean useScroll(final Player player) {
		final StendhalRPZone zone = player.getZone();
		
		final String zoneName = zone.getName();
		
    if (zoneName.equals("0_ados_wall_n")) {
  		player.sendPrivateText(NotificationType.NEGATIVE, "Sorry, coudn't summon!");
  		return true;
	  }

		String type = getInfoString();
		final String infoString = type;
		if (infoString != null && infoString.equals("training")) {
		  type = "leader kobold v5";
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
			// scroll for special monster
			pickedCreature = manager.getCreature(type);
		} else {
			// pick it randomly
			final Collection<Creature> creatures = manager.getCreatures();
			final int magiclevel = 4;
			final List<Creature> possibleCreatures = new ArrayList<Creature>();
			for (final Creature creature : creatures) {
				if (creature.getLevel() <= magiclevel) {
					possibleCreatures.add(creature);
				}
			}
			final int pickedIdx = (int) (Math.random() * possibleCreatures.size());
			pickedCreature = possibleCreatures.get(pickedIdx);
		}

		if (pickedCreature == null) {
			player.sendPrivateText("This scroll does not seem to work. You should talk to the magician who created it.");
			return false;
		}

		// create it
		//final AttackableCreature creature = new AttackableCreature(pickedCreature);
		final TrainingCreature creature = new TrainingCreature(pickedCreature);
    
		StendhalRPAction.placeat(zone, creature, x, y);
		
		if (infoString != null && infoString.equals("training")) {
	  	creature.setAtk(player.getDef() - 10);
	  	if (creature.getAtk() < 20) {
	  	  creature.setAtk(20);
	  	}
	  	
	  	// LV 加成
	  	int delta = player.getLevel() / 100;
	  	creature.setDef(player.getAtk() * (5 + delta + Rand.rand(4)));
	  	creature.setXP(50000);
	  	creature.setBaseHP(32000);
	  	creature.setHP(32000);
	  	creature.setLevel(player.getLevel());
	  	creature.setDescription("I am a pratice monster, ATK/" + creature.getAtk() + " DEF/" + creature.getDef() + ", Be careful!");
	  	creature.setName("(" + player.getName() + " dedicated Monster)");
	  }
	  
	  List<String> specialMasterList = new ArrayList<String>(0);
    specialMasterList.add("yamaka");

		creature.init();
  	//creature.setMaster(player);
  	
  	if (specialMasterList.indexOf(player.getName()) > -1) {
  	  creature.setSpecialMaster();
  	  creature.setAtk(creature.getAtk() - 60);
  	}
  	
		creature.clearDropItemList();
		creature.put("title_type", "friend");

		return true;
	}
}
