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
 
//Zrobiony na podstawie ChefNPC z Semos. 

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


public class ChefNPC implements ZoneConfigurator {

	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildNPC(zone);
	}


	private void buildNPC(final StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Jaś") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(15, 3));
				nodes.add(new Node(15, 8));
				nodes.add(new Node(13, 8));
				nodes.add(new Node(13, 10));
				nodes.add(new Node(10, 10));
				nodes.add(new Node(10, 12));
				nodes.add(new Node(7, 12));
				nodes.add(new Node(7, 10));
				nodes.add(new Node(3, 10));
				nodes.add(new Node(3, 4));
				nodes.add(new Node(5, 4));
				nodes.add(new Node(5, 3));
				nodes.add(new Node(5, 4));
				nodes.add(new Node(15, 4));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				addJob("I am the local baker. One of the services I provide in this area is the production of sandwiches for our dear customers who appreciate their taste! Just say #make.");
				addHelp("I only do sandwiches. Say #make if you decide to have a #sandwich.");
				addReply("bread", "This is what Erna deals with in our company. Go speak to her.");
				addReply("cheese",
						"We have a lot of difficulties with cheese, because we recently had a plague of rats. I wonder how these nasty pests took everything with them? That is why we add goat cheese from the milk of our sheep to our sandwiches .");
				addReply("ham",
						"Well, you look like a brave hunter to me. Maybe just go to the forest and hunt. Just don't bring me those little pieces of meat and dry steaks. Only top-class ham is suitable for sandwiches!");
				addReply(Arrays.asList("sandwich", "sandwiches", "sandwich", "kanapki", "kanapkę"),
						"My sandwiches are tasty and nutritious. If you want me to make one for you say #'make'.");
				addGoodbye();
			}
		};

		final Map<String, Integer> requiredResources = new TreeMap<String, Integer>();
		requiredResources.put("bread", 1);
		requiredResources.put("cheese", 2);
		requiredResources.put("ham", 1);

		final ProducerBehaviour behaviour = new ProducerBehaviour(
				"jas_make_sandwiches","make", "sandwich",
				requiredResources, 3 * 60);

		new ProducerAdder().addProducer(npc, behaviour,
				"Welcome! How nice that you came to my bakery, where I make #sandwiches.");

		npc.setEntityClass("chefnpc");
		npc.setPosition(15, 3);
		npc.initHP(1000);
		zone.add(npc);
	}
}
