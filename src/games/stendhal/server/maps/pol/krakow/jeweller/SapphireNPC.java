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
import games.stendhal.server.entity.npc.behaviour.adder.ProducerAdder;
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
		final SpeakerNPC krzesim = new SpeakerNPC("Journeyman Krzesim") {

			@Override
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting("Good day.");
				addReply("master",
						"The master already spoke to me. I can cut your #sapphire crystal.");
				addReply("sapphire",
						"I can do that without any trouble. Just tell me #'grind sapphire'.");
				addGoodbye("Farewell.");
				final Map<String, Integer> requiredResources = new TreeMap<String, Integer>();
				requiredResources.put("money", 380);
				requiredResources.put("sapphire crystal", 1);

				final ProducerBehaviour behaviour = new ProducerBehaviour(
					"krzesim_cast_sapphire", "grind", "sapphire",
					requiredResources, 4 * 60);

				new ProducerAdder().addProducer(this, behaviour,
						"Good day.");
				addReply("money",
						"Please direct any questions regarding prices to Master Drogosz.");
				addReply("sapphire crystal",
						"You should ask the master. He knows where all the best gemstones are mined.");
			}
		};

		krzesim.setEntityClass("man_001_npc");
		krzesim.setPosition(27, 17);
		krzesim.setDirection(Direction.LEFT);
		krzesim.initHP(100);
		zone.add(krzesim);
	}
}
