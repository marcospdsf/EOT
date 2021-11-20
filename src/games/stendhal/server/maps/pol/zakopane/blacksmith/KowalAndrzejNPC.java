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
		final SpeakerNPC kuznia = new SpeakerNPC("Kowal Andrzej") {

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
				addGreeting("Welcome!");
				addJob("Hello. I am a local blacksmith. As a special #task, I can make a #gold #ciupaga for you or cast iron.");
				addHelp("I cast iron and, as part of a special task, craft a #golden #ciupaga .");
				addOffer("I can cast iron if you bring me # iron # ore and #wood. Just say #cast it. I can also, as part of a special #task, make a #gold #ciupaga, provided you bring me #golden #ingot, #wood and some #money, and if you can prove your courage! To do this, go to Gazda Jędrzej and ask for a #quest #for #real #knight! Only when you do them, and he lets me know, I'll make a #golden #ciupaga for you .");
				addGoodbye("Dowidzenia.");
				addReply(Arrays.asList("złota ciupaga", "gold ciupaga"),"I will undertake # the task of making the golden ciupaga, as long as you have a #ciupaga, #wood, #gold #bar and #money. I'm a bit overworked and when you come, remind me by saying 'remind' .");
				addReply(Arrays.asList("ciupaga", "ciupagę"),"Ciupaga is the favorite weapon of thieves in Zakopane.");
				addReply("wood","wood can be found on the edge of forests. I need them to keep the fire going or to make the handle.");
				addReply("gold ingot","Joshua in Ados City blacksmith casts the gold bars. I need them to make the blade and the details.");
				addReply("money","Everyone needs them. Apart from that, they are needed to buy special ingredients, without which the gold hunk won't be worth much. I will not tell you what it is about. It is a family secret passed down from generation to generation.");

				// Joshua makes gold if you bring him gold nugget and wood
				final Map<String, Integer> requiredResources = new TreeMap<String, Integer>();	
				requiredResources.put("wood", 1);
				requiredResources.put("iron ore", 1);

				final ProducerBehaviour behaviour = new ProducerBehaviour("andrzej_cast_iron",
						"cast", "iron", requiredResources, 3 * 60);

				new ProducerAdder().addProducer(this, behaviour,
				        "Hi! I am a local blacksmith. If you want me to cast #iron for you, let me know!");
				addReply(Arrays.asList("ruda", "iron", "ruda żelaza"),
								"You will find iron ore in the mountains of Zakopane and Kościelisko. Take care there!");
			}
		};

		kuznia.setDescription("Here is the busy blacksmith Andrzej. Sometimes he needs to be reminded of what to do!");
		kuznia.setEntityClass("goldsmithnpc");
		kuznia.setDirection(Direction.DOWN);
		kuznia.setPosition(23, 12);
		kuznia.initHP(100);
		zone.add(kuznia);
	}
}
