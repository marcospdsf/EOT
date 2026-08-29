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
				addGreeting("Oh, hi there!");
				addJob("I go to kindergarten. And next year I'll be enrolled in school as a proper #student! ☺");
				addReply(Arrays.asList("student", "school", "ucznia"), "Isn't school wonderful? Mother is getting me a colorful satchel, a pencil case, and crayons."
						+ " I'll have my very own notebooks and storybooks just like my brother. I can't wait!");
				addOffer("The best I can offer you is a game of tag, hehe ^^");
				addHelp("I'm just a kid, my help wouldn't be much use to a grown adventurer.");
				addGoodbye("Bye-bye! Come back soon so we can play together!");
			}
		};

		dziewczynkaNPC.setEntityClass("npcdziewczynka");
		dziewczynkaNPC.setPosition(3, 15);
		dziewczynkaNPC.initHP(1000);
		zone.add(dziewczynkaNPC);
	}
}
