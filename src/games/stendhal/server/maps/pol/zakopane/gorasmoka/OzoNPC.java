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
package games.stendhal.server.maps.pol.zakopane.gorasmoka;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.SpeakerNPC;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Builds the NPC who deals in magiczny scroll.
 * Other behaviour defined in maps/quests/Labirynt.java
 *
 * @author kymara
 */
public class OzoNPC implements ZoneConfigurator {
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

	//
	// IL0_GreeterNPC
	//

	private void buildNPC(final StendhalRPZone zone) {
		final SpeakerNPC ozoNPC = new SpeakerNPC("Ozo") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(33, 49));
				nodes.add(new Node(33, 54));
				nodes.add(new Node(36, 54));
				nodes.add(new Node(36, 49));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				addJob("I think you know what I do.");
				addHelp("If I have to be honest with you, dude, know that I will not help you with it too much, you better go to town looking for answers.");
				addQuest("Task? at my place? .. ha ha ... .");
				addOffer("Hm, I have something you might be interested in, #'magic ticket'.");
				addGoodbye("Dowidzenia.");
			}
		};

		ozoNPC.setEntityClass("scarletarmynpc");
		ozoNPC.setPosition(33, 49);
		ozoNPC.initHP(1000);
		zone.add(ozoNPC);
	}
}
