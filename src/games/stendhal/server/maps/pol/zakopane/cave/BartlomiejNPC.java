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
package games.stendhal.server.maps.pol.zakopane.cave;

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
public class BartlomiejNPC implements ZoneConfigurator {

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
		final SpeakerNPC bartlomiejNPC = new SpeakerNPC("Bartłomiej") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(11, 59));
				nodes.add(new Node(11, 62));
				nodes.add(new Node(12, 62));
				nodes.add(new Node(12, 59));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				addGreeting("Hello. I am a blacksmith working for highwaymen.");
				addJob("Maybe you need a #'golden horn'. I can talk to my #brother.");
				addReply(Arrays.asList("brother", "bratem"), "He is the only one who knows the secrets of making a golden horn. In return, I expect you to bring me some #feathers just say #task.");
				addReply(Arrays.asList("feathers", "piórko", "piórek"), "After killing each angel, you will find a feather in his corpse");
				addHelp("I know someone who makes golden horns and I want a collection of feathers in exchange.");
				addGoodbye("I wish you good luck and happiness on your expeditions.");
			}
		};

		bartlomiejNPC.setDescription("Here is the blacksmith Bartlomiej. He found a job with a robber leader !");
		bartlomiejNPC.setEntityClass("beardmannpc");
		bartlomiejNPC.setPosition(11, 59);
		bartlomiejNPC.initHP(1000);
		zone.add(bartlomiejNPC);
	}
}
