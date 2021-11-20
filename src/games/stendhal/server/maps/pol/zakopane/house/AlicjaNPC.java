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
// Based on ../games/stendhal/server/maps/ados/abandonedkeep/OrcKillGiantDwarfNPC.java
package games.stendhal.server.maps.pol.zakopane.house;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.ConversationPhrases;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Builds the orc kill diant dwarf NPC.
 *
 * @author Teiv
 */
public class AlicjaNPC implements ZoneConfigurator {
	//
	// ZoneConfigurator
	//

	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */

	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildNPC(zone);
	}


	private void buildNPC(final StendhalRPZone zone) {
		final SpeakerNPC dziewczynkaNPC = new SpeakerNPC("Alicja") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(3, 15));
				nodes.add(new Node(8, 15));
				nodes.add(new Node(8, 19));
				nodes.add(new Node(3, 19));
				nodes.add(new Node(3, 15));
				nodes.add(new Node(5, 15));
				nodes.add(new Node(5, 19));
				nodes.add(new Node(7, 19));
				nodes.add(new Node(7, 16));
				nodes.add(new Node(5, 16));
				nodes.add(new Node(5, 17));
				nodes.add(new Node(3, 17));
				nodes.add(new Node(3, 15));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				addGreeting("Oh, HI!.");
				addJob("I go to kindergarten. And in a year I will be fit for #school!");
				addReply("school", "Is school not something wonderful? Mom will buy me a colorful backpack, pencil case and crayons."
						+ " I will also have my own notebooks and books, such as my brother. I can not wait!");
				addOffer("The best I can offer you is a game of tag, hihi ^^");
				addHelp("I am only a child, my help will be of no use to you.");
				addGoodbye("Bye Bye! Come back as soon as possible, we'll play together!");
			}
		};

		dziewczynkaNPC.setEntityClass("npcdziewczynka");
		dziewczynkaNPC.setPosition(3, 15);
		dziewczynkaNPC.initHP(1000);
		zone.add(dziewczynkaNPC);
	}
}
