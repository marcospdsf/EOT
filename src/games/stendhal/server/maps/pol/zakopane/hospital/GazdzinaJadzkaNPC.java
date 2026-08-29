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
 //Na podstawie pliku HealerNPC.
 
package games.stendhal.server.maps.pol.zakopane.hospital;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.behaviour.adder.FreeHealerAdder;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Builds a Healer NPC for the magic city.
 *
 * @author kymara
 */
public class GazdzinaJadzkaNPC implements ZoneConfigurator {

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
		final SpeakerNPC npc = new SpeakerNPC("Gaździna Jadźka") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				// walks along the aqueduct path, roughly
				nodes.add(new Node(20, 10));
				nodes.add(new Node(34, 10));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				addGreeting();
				addJob("My gifts and herbal knowledge help me mend wounds and cure afflictions.");
				addHelp("I can #heal your wounds.");
				new FreeHealerAdder().addHealer(this, 0);
				addGoodbye("Farewell.");
			}
		};

		npc.setDescription("You see Gaździna Jadźka, the village matriarch and respected healer.");
		npc.setEntityClass("npcgenowefa");
		npc.setPosition(20, 10);
		npc.initHP(100);
		zone.add(npc);
	}
}
