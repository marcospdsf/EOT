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
 //Zrobiony na podstawie blacksmithNPC
package games.stendhal.server.maps.pol.zakopane.blacksmith;

import games.stendhal.common.Direction;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.behaviour.adder.ProducerAdder;
import games.stendhal.server.entity.npc.behaviour.impl.ProducerBehaviour;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class KowalAndrzejNPC implements ZoneConfigurator {
	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildKuznia(zone);
	}

	private void buildKuznia(final StendhalRPZone zone) {
		final SpeakerNPC kuznia = new SpeakerNPC("Blacksmith Andrzej") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(23, 12));
				nodes.add(new Node(29,12));
				nodes.add(new Node(29,5));
				nodes.add(new Node(17,5));
				nodes.add(new Node(17,9));
				nodes.add(new Node(28,9));
				nodes.add(new Node(28,12));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				addGreeting("Greetings!");
				addJob("Welcome! I am the local blacksmith. As a special #quest, I can forge a #golden highlander axe for you, or #cast iron bars.");
				addHelp("I cast iron and, as part of a special quest, craft the legendary #golden highlander axe.");
				addOffer("I can cast iron bars if you bring me #iron ore and #wood. Just say #cast. As part of a special #quest, I can also forge a #golden highlander axe if you bring me a #highlander axe, #gold bars, #wood, and some #money—provided you prove your courage! Speak to Gazda Jędrzej and ask for a #mission for a true knight! Only when you complete it and he sends word will I forge the #golden highlander axe for you.");
				addGoodbye("Farewell.");
				addReply(Arrays.asList("golden highlander axe", "złota ciupaga", "złotą ciupagę"),"I will undertake the #quest to forge the golden highlander axe if you bring me a #highlander axe, #wood, #gold bars, and #money. I am quite busy, so when you return, remind me by saying 'remind'.");
				addReply(Arrays.asList("highlander axe", "highlander walking axe", "ciupaga", "ciupagę"),"The highlander axe is the favored weapon of mountain brigands around Zakopane.");
				addReply("wood","You can find wood at the forest edges. I need it to fuel my forge fires and fashion handles.");
				addReply(Arrays.asList("gold bar", "gold bars"),"Joshua in Ados smelts gold bars. I require them for the gilded blade and ornate detailing.");
				addReply("money","Everyone needs coin! Moreover, it covers special rare flux ingredients without which the golden axe would be brittle. I will not reveal the exact formula—it is a family secret passed down through generations.");

				// Cast iron from iron ore and wood
				final Map<String, Integer> requiredResources = new TreeMap<String, Integer>();	
				requiredResources.put("wood", 1);
				requiredResources.put("iron ore", 1);

				final ProducerBehaviour behaviour = new ProducerBehaviour("andrzej_cast_iron",
						"cast", "iron", requiredResources, 3 * 60);

				new ProducerAdder().addProducer(this, behaviour,
				        "Hello! I am the local blacksmith. If you want me to cast #iron bars for you, let me know!");
				addReply(Arrays.asList("ore", "iron", "iron ore", "ruda", "ruda żelaza"),
								"Iron ore can be found in the mountains of Zakopane and Kościelisko. Watch yourself up there!");
			}
		};

		kuznia.setDescription("You see the busy Blacksmith Andrzej hammering away at his anvil.");
		kuznia.setEntityClass("goldsmithnpc");
		kuznia.setDirection(Direction.DOWN);
		kuznia.setPosition(23, 12);
		kuznia.initHP(100);
		zone.add(kuznia);
	}
}
