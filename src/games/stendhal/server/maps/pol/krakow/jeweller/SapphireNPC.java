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
package games.stendhal.server.maps.pol.krakow.jeweller;

import games.stendhal.common.Direction;
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
/*
 * Twilight zone is a copy of sewing room in dirty colours with a delirious sick lda (like Ida) in it
 */
public class SapphireNPC implements ZoneConfigurator {

	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildKrzesim(zone);
	}

	private void buildKrzesim(final StendhalRPZone zone) {
		final SpeakerNPC krzesim = new SpeakerNPC("czeladnik Krzesim") {

			@Override
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting("Hello there.");
				addReply("master",
						"The master has already told me. I'm going to work the #sapphire crystal.");
				addReply("sapphire",
						"I'll do it without a problem. Just say #'grind sapphire'.");
				addGoodbye("GoodBye.");
				final Map<String, Integer> requiredResources = new TreeMap<String, Integer>();
				requiredResources.put("money", 380);
				requiredResources.put("sapphire crystal", 1);

				final ProducerBehaviour behaviour = new ProducerBehaviour(
					"krzesim_cast_sapphire", "grind", "sapphire",
					requiredResources, 4 * 60);

				new ProducerAdder().addProducer(this, behaviour,
						"Hello there.");
				addReply("money",
						"Please ask Master Drogosz with this question.");
				addReply("sapphire crystal",
						"You have to ask the master. He knows what, where and how.");
			}
		};

		krzesim.setEntityClass("man_001_npc");
		krzesim.setPosition(27, 17);
		krzesim.setDirection(Direction.LEFT);
		krzesim.initHP(100);
		zone.add(krzesim);
	}
}
