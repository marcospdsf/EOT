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
 
 //Zrobiony na podstawie asystent piekarza z Semos.
 
package games.stendhal.server.maps.pol.zakopane.bakery;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.behaviour.adder.ProducerAdder;
import games.stendhal.server.entity.npc.behaviour.impl.ProducerBehaviour;
import games.stendhal.server.entity.player.Player;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class ShopAssistantNPC implements ZoneConfigurator {

	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildNPC(zone);
	}


	private void buildNPC(final StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Małgosia") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(26, 9));
				nodes.add(new Node(26, 6));
				nodes.add(new Node(28, 6));
				nodes.add(new Node(28, 2));
				nodes.add(new Node(28, 5));
				nodes.add(new Node(22, 5));
				nodes.add(new Node(22, 4));
				nodes.add(new Node(22, 7));
				nodes.add(new Node(26, 7));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {

				addJob("I bake bread at this bakery .");
				addReply("flour",
						"We need flour for our work, which was ground in a mill north of here, but the wolves ate the boy who brought it to us! If you bring us flour, we will bake delicious bread for you as a reward. Just say #bake.");
				addHelp("Bread is very good, especially to a daredevil like you who is sick of raw meat. My boss, Hansel, makes the best sandwiches around! ");
				addGoodbye();
			}
		};

		final Map<String, Integer> requiredResources = new TreeMap<String, Integer>();
		requiredResources.put("flour", 2);

		final ProducerBehaviour behaviour = new ProducerBehaviour("malgosia_bake_bread",
				"bake", "bread", requiredResources, 7 * 60);

		new ProducerAdder().addProducer(npc, behaviour,
						"Welcome to the bakery in Zakopane! We can bake delicious bread for anyone who will help us by bringing flour from the mill. Just say #bake.");

		npc.setPosition(26, 9);
		npc.setEntityClass("housewifenpc");
		npc.initHP(1000);
		zone.add(npc);
	}
}
