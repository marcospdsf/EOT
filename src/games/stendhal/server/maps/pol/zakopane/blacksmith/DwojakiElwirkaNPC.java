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
 //Na podstawie pliku piekarza z Semos
 
package games.stendhal.server.maps.pol.zakopane.blacksmith;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.behaviour.adder.BuyerAdder;
import games.stendhal.server.entity.npc.behaviour.adder.ProducerAdder;
import games.stendhal.server.entity.npc.behaviour.impl.BuyerBehaviour;
import games.stendhal.server.entity.npc.behaviour.impl.ProducerBehaviour;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The cooking girl.
 * Makes twofold for players.
 * Buys kisc winogron.
 * 
 * @author daniel
 */
public class DwojakiElwirkaNPC implements ZoneConfigurator  {

	@Override
	public void configureZone(StendhalRPZone zone, Map<String, String> attributes) {
		buildNPC(zone);
	}

	private void buildNPC(StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Elwirka") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(2,13));
				nodes.add(new Node(3,13));
				nodes.add(new Node(2,13));
				nodes.add(new Node(4,13));
				nodes.add(new Node(2,13));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			public void createDialog() {
				addJob("I deal with the kitchen of the blacksmith Andrzej. Ask me to #boil the twofold and I'll make a hot potato dish for you! ");
				addHelp("You can help me and chase away the rats that run around the house. Our cat got lost somewhere .");
				addReply("potatoes", "The villagers have potatoes, but I don't think they will give them to you on their willing will.");
				addReply("sausage", "As far as I know, the sausage from the local pigs is the best for greaves .");
				addReply(Arrays.asList("onion", "cebula"), "You can recognize the onion by the thin green leaves sticking out of the soil.");
				addReply(Arrays.asList("butter", "masło"), "I can't do a good twofold without butter .");
				addReply(Arrays.asList("milk", "mleko"), "I will add a little milk and it will be a delicious dish. Go milk a cow.");
				addReply(Arrays.asList("dwojak", "twofold"),
						"twofold is a clay pot that I will fill to the brim with baked potatoes. If you want to tell me just #'cook 1 twofold'.");
				addOffer("Well. If you can offer me grapes, I will gladly buy them from you. Then tell me #'sell grapes'.");
				final Map<String, Integer> offers = new TreeMap<String, Integer>();
				offers.put("grapes", 3);
				new BuyerAdder().addBuyer(this, new BuyerBehaviour(offers), false);

				addGoodbye();

				// Elwirka makes twofold if you bring her potatoes, sausage, onion, butter and milk.
				final Map<String, Integer> requiredResources = new TreeMap<String, Integer>();
				requiredResources.put("potatoes", 3);
				requiredResources.put("sausage", 3);
				requiredResources.put("onion", 1);
				requiredResources.put("butter", 1);
				requiredResources.put("milk", 1);

				final ProducerBehaviour behaviour = new ProducerBehaviour(
						"elwirka_make_twofold","make", "twofold",
						requiredResources, 5 * 60);

				new ProducerAdder().addProducer(this, behaviour,
						"Hello! What brings you here? Probably my legendary #twofold.");

			}};
			npc.setPosition(2, 13);
			npc.setEntityClass("confectionerapplepienpc");
			npc.setDescription("This is Elwirka, who knows how to prepare delicious potato dishes and likes grapes.");
			zone.add(npc);
	}
}