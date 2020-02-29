/* $Id: JacerNPC.java,v 1.12 2010/09/19 02:35:40 nhnb Exp $ */
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
package games.stendhal.server.maps.semos.crazyroom;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.SpeakerNPC;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Builds the NPC who deals in rainbow beans.
 * Other behaviour defined in maps/quests/RainbowBeans.java
 *
 * @author kymara
 */
public class JacerNPC implements ZoneConfigurator {
	//
	// ZoneConfigurator
	//

	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildNPC(zone, attributes);
	}

	//
	// IL0_GreeterNPC
	//

	private void buildNPC(final StendhalRPZone zone, final Map<String, String> attributes) {
		final SpeakerNPC JacerNPC = new SpeakerNPC("Jacer") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(37, 4));
				nodes.add(new Node(39, 4));
				nodes.add(new Node(37, 7));
				nodes.add(new Node(39, 7));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				addGreeting("Witaj wojowniku!");
				addJob("Mam dla ciebie coś specjalnego.");
				addHelp("Nic od ciebie nie potrzebuje.");
				addQuest("Nie zadania dla ciebie.");
				addOffer("Nic nie sprzedaje. Nic nie kupuje !");
				addGoodbye("Dowidzenia.");
			}
		};

		JacerNPC.setEntityClass("JacerNPC");
		JacerNPC.setPosition(37, 4);
		JacerNPC.initHP(100);
		JacerNPC.setDescription("Widzisz Jacera. Jest bardzo dziwnym człowiekiem. Wygląda jakby lubiał mieszać w kotle.");
		zone.add(JacerNPC);
	}
}
