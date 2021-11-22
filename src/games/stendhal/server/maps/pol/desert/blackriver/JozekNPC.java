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
package games.stendhal.server.maps.pol.desert.blackriver;

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
public class JozekNPC implements ZoneConfigurator {

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
		final SpeakerNPC jozekNPC = new SpeakerNPC("Józek") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(6, 2));
				nodes.add(new Node(6, 5));
				nodes.add(new Node(5, 5));
				nodes.add(new Node(5, 2));
				setPath(new FixedPath(nodes, true));

			}

			@Override
			protected void createDialog() {
				addGreeting("Welcome.");
				addJob("Maybe you want to #upgrade #'golden ciupaga'? I can do it for you .");
				addReply(Arrays.asList("upgrade", "ulepszyć", "golden ciupaga"), "I need a few things so you have to do the #task.");
				addHelp("I deal with the improvement of the golden ciupaga.");
				addGoodbye("Good luck.");
			}
		};

		jozekNPC.setEntityClass("weaponsellernpc");
		jozekNPC.setPosition(6, 2);
		jozekNPC.initHP(1000);
		jozekNPC.setDescription("Here is Jozek that can upgrade your golden ciupaga.");
		zone.add(jozekNPC);
	}
}
