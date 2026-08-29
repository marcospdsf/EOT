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


import games.stendhal.server.entity.npc.behaviour.adder.BuyerAdder;
import games.stendhal.server.entity.npc.behaviour.impl.BuyerBehaviour;
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
				addJob("I run the kitchen for Blacksmith Andrzej. Ask me to #cook twofold and I will prepare a hot, hearty mountain dish for you!");
				addHelp("You could help by driving away the pesky rats running around outside the house. Our cat seems to have wandered off.");
				addReply(Arrays.asList("potato", "potatoes", "ziemniak", "ziemniaki"), "The local peasants harvest potatoes in the fields. You might gather some from them.");
				addReply(Arrays.asList("sausage", "kiełbasa", "kielbasa"), "Country sausage from our local mountain pigs makes the very best cracklings.");
				addReply(Arrays.asList("onion", "cebula"), "You can recognize fresh onions by their slender green shoots growing in the vegetable patch.");
				addReply(Arrays.asList("butter", "masło", "maslo"), "Good butter is the secret to rich, hearty twofold!");
				addReply(Arrays.asList("milk", "mleko"), "Fresh milk gives the dish its rich flavor. Go milk one of the cows in the pastures.");
				addReply(Arrays.asList("dwojak", "twofold"),
						"Twofold is a traditional clay pot filled to the brim with mashed potatoes, sausage, and savory toppings. If you want one, just tell me #'cook 1 twofold'.");
				addOffer("If you have fresh #grapes, I will gladly buy them from you. Just tell me #'sell grapes'.");
				final Map<String, Integer> offers = new TreeMap<String, Integer>();
				offers.put("grapes", 3);
				new BuyerAdder().addBuyer(this, new BuyerBehaviour(offers), false);

				addGoodbye();

				// Elwirka makes twofold if you bring her potato, sausage, onion, butter and milk.
				final Map<String, Integer> requiredResources = new TreeMap<String, Integer>();
				requiredResources.put("potato", 3);
				requiredResources.put("sausage", 3);
				requiredResources.put("onion", 1);
				requiredResources.put("butter", 1);
				requiredResources.put("milk", 1);

				final ProducerBehaviour behaviour = new ProducerBehaviour(
						"elwirka_make_twofold","cook", "twofold",
						requiredResources, 5 * 60);

				new ProducerAdder().addProducer(this, behaviour,
						"Welcome! What brings you here? Surely you came for my legendary #twofold pot.");

			}};
			npc.setPosition(2, 13);
			npc.setEntityClass("confectionerapplepienpc");
			npc.setDescription("You see Elwirka, who prepares delicious traditional mountain meals and loves fresh grapes.");
			zone.add(npc);
	}
}