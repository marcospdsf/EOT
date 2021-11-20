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
package games.stendhal.server.maps.pol.zakopane.blacksmith;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.behaviour.adder.ProducerAdder;
import games.stendhal.server.entity.npc.behaviour.adder.SellerAdder;
import games.stendhal.server.entity.npc.behaviour.impl.ProducerBehaviour;
import games.stendhal.server.entity.npc.behaviour.impl.SellerBehaviour;
import games.stendhal.server.core.engine.SingletonRepository;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Builds a Healer NPC for the magic city.
 *
 * @author kymara
 */
public class KrasnoludNPC implements ZoneConfigurator {

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
		final SpeakerNPC npc = new SpeakerNPC("Krasnolud") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				// walks along the aqueduct path, roughly
				nodes.add(new Node(8, 3));
				nodes.add(new Node(7, 3));
				nodes.add(new Node(7, 7));
				nodes.add(new Node(8, 7));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
			new SellerAdder().addSeller(this, new SellerBehaviour(SingletonRepository.getShopList().get("sellkopalnia")));

			// Xoderos casts iron if you bring him wood and iron ore.
			final Map<String, Integer> requiredResources = new TreeMap<String, Integer>();	
			requiredResources.put("wood", 1);
			requiredResources.put("iron ore", 1);
			requiredResources.put("feather", 1);
			requiredResources.put("money", 1);

			final ProducerBehaviour behaviour = new ProducerBehaviour("krasnolud_cast_arrow",
				"make", "wooden arrow", requiredResources, 1 * 60);

			new ProducerAdder().addProducer(this, behaviour,
				"Welcome! I can make arrows for you or maybe you are interested in my #special offer? Just say #make.");
				addGreeting();
				addJob("I make arrows.");

				addReply("wood",
						"I need wood for the beam of the arrow. Talk to the woodcutter and he will tell you where to get wood.");
				addReply(Arrays.asList("ore", "iron", "iron ore","ruda żelaza"),
						"The iron ore can be found near the springs to the east of the cottage below the thieves' cave. I need her for the arrow tips.");
				addReply("feather",
						"I need them for darts. Kill some pigeons.");
				addReply("pick",
						"Useful for sulfur and coal mining.");

				addReply("shovel", "Well, you have to dig with something.");
				addReply("rope", "Useful when you want to go down to the lower levels .");
				addHelp("If you bring me #wood, #'iron ore' and #feather, I can make some arrows for you. Just say #make.");
				addGoodbye();
			}
		};

		npc.setEntityClass("dwarfnpc");
		npc.setPosition(8, 3);
		npc.initHP(100);
		zone.add(npc);
	}
}
