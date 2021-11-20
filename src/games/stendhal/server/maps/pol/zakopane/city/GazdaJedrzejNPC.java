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
// based on ../games/stendhal/server/maps/ados/abandonedkeep/OrcKillGiantDwarfNPC.java
package games.stendhal.server.maps.pol.zakopane.city;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.SpeakerNPC;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Builds the orc kill diant dwarf NPC.
 *
 * @author Teiv
 */
public class GazdaJedrzejNPC implements ZoneConfigurator {

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
		final SpeakerNPC gazdajedrzejNPC = new SpeakerNPC("Gazda Jędrzej") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(31, 125));
				nodes.add(new Node(35, 125));
				setPath(new FixedPath(nodes, true));

			}

			@Override
			protected void createDialog() {
				addGreeting("Welcome to a place plagued by hordes of robbers.");
				addJob("I am waiting for strong warriors to defeat the #robbers marauding in our area.");
				addReply(Arrays.asList("task for a real knight", "misyjka dla prawdziwego rycerza"), "Ah, that Andrzej with his slogans. He he he. All you have to do is say #task.");
				addReply(Arrays.asList("robbers", "zbójników"), "They came out of nowhere and spread fear and chaos. Can you help us with this problem and do this #task?");
				addHelp("Before that, we had a quiet and happy life, until the nasty robbers appeared in our neighborhood. They keep attacking us and robbing us of our food. Now we are looking for a #real #knight who would cope with this difficult task.");
				addGoodbye("I wish you good luck and happiness on your expeditions.");
			}
		};

		gazdajedrzejNPC.setEntityClass("npcgazdajedrzej");
		gazdajedrzejNPC.setPosition(31, 125);
		gazdajedrzejNPC.initHP(1000);
		zone.add(gazdajedrzejNPC);
	}
}
