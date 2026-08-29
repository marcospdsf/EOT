/* $Id: FlorianNPC.java,v 1.21 2025/06/15 21:47:38 davvids Exp $ */
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
package games.stendhal.server.maps.bandit.temple;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.behaviour.adder.HealerAdder;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FlorianNPC implements ZoneConfigurator {
	

	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildSemosTempleArea(zone, attributes);
	}

	private void buildSemosTempleArea(final StendhalRPZone zone, final Map<String, String> attributes) {
		final SpeakerNPC npc = new SpeakerNPC("Florian") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(8, 9));
				nodes.add(new Node(15, 9));
				nodes.add(new Node(15, 15));
				nodes.add(new Node(8, 15));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				addGreeting("Witaj, podróżniku! Florian to jo, ziółka znam i leczę.");
				addJob("Chcesz zdrowia? Wpisz #ulecz, a ulżę ci w bólu.");
				addHelp("Za dutki cię uleczę – ino napisz #ulecz, a zaraz poczujesz ulgę.");
				new HealerAdder().addHealer(this, -2);
				addGoodbye("Zdrów bądź i wracaj, gdy znów cię coś boli.");
			}

		};

		npc.setEntityClass("npcksiezniczka");
		npc.setDescription("Oto Florian. Młody kapłan, co aura jego leczy jak górski halny – każdą ranę zagoi i pociechę przyniesie.");
		npc.setPosition(11, 9);
		npc.initHP(100);
		zone.add(npc);
	}
}
