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
 //Zrobione na podstawie GoldsmithNPC z Ados/goldsmith
 
package games.stendhal.server.maps.pol.zakopane.city;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.behaviour.adder.ProducerAdder;
import games.stendhal.server.entity.npc.behaviour.impl.ProducerBehaviour;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Zakopane Nosiwoda Jakub (Outside / Level 0).
 *
 * @author edi18028
 */
public class ButelkaNPC implements ZoneConfigurator {
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
		final SpeakerNPC npc = new SpeakerNPC("Nosiwoda Jakub") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(19, 95));
				nodes.add(new Node(23, 95));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				addGreeting("Welcome!");
				addJob("I fill the bottle with water, just say #fill.");
				addHelp("We help Gerwazy in filling the bottles with water.");
				addGoodbye("Goodbye, and good luck.");

				final Map<String, Integer> requiredResources = new TreeMap<String, Integer>();
				requiredResources.put("bottle", 1);
				requiredResources.put("money", 100);

				final ProducerBehaviour behaviour = new ProducerBehaviour("nosiwoda_fill_butelke",
						"fill", "water", requiredResources, 1 * 10);

				new ProducerAdder().addProducer(this, behaviour,
						"Regards! If you bring me a bottle, I will pour you water from the spring. Just say #fill.");
				addReply("bottle",
						"You can buy it at Boguś or in the Semos tavern .");
			}
		};

		npc.setEntityClass("oldcowboynpc");
		npc.setPosition(19, 95);
		npc.initHP(1000);
		npc.setDescription("Here is Nosiwoda Jakub.");
		zone.add(npc);
	}
}
