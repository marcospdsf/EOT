/* $Id: ThorgalNPC.java,v 1.9 2024/05/18 12:38:15 davvids Exp $ */
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
package games.stendhal.server.maps.semos.mines;


import games.stendhal.server.entity.npc.behaviour.adder.SellerAdder;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.behaviour.impl.QuestCompletedSellerBehaviour;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class ThorgalNPC implements ZoneConfigurator {

	public static final int BUYING_PRICE = 10000;
 	private static final String QUEST_SLOT = "chaos_tunnels";

	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildThorgal(zone);
	}

	private void buildThorgal(final StendhalRPZone zone) {
		final SpeakerNPC Thorgal = new SpeakerNPC("Thorgal") {

			@Override
			protected void createPath() {
				// NPC doesn't move
				setPath(null);
			}

			@Override
			protected void createDialog() {

				final Map<String, Integer> items = new HashMap<String, Integer>();
				items.put("chaos scroll", BUYING_PRICE);

				new SellerAdder().addSeller(this, new QuestCompletedSellerBehaviour(QUEST_SLOT, "I only sell chaos tunnel scrolls to trusted comrades who have proven their mettle in those perilous depths.", items));
				// for quest see games.stendhal.server.maps.quest.HelpMrsYeti
				addGreeting("Greetings, adventurer! I am Thorgal, an explorer of the chaos tunnels. If you have the bravery to face the abyss, bring me the full chaos set: chaos helmet, chaos armor, chaos legs, chaos boots, chaos cloak, chaos shield, and chaos sword.");
				addJob("I explore the vast chaos tunnels hidden deep beneath Semos. These tunnels conceal danger and ancient relics. I currently require no assistance.");
				addHelp("The chaos tunnels are perilous and teeming with fiends. If you venture into those dark corridors, be prepared for deadly threats and ancient treasures.");				
				addGoodbye("Good luck, warrior. May your courage guide you through the depths of chaos. Return safely.");
			}
		};

		Thorgal.setEntityClass("minernpc");
		Thorgal.setDescription("You see Thorgal, a veteran subterranean explorer who has braved the chaos tunnels beneath Semos for years.");
		Thorgal.setPosition(117, 85);
		Thorgal.initHP(100);
		zone.add(Thorgal);
	}
}
