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
public class CarbuncleNPC implements ZoneConfigurator {

	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildZiemirad(zone);
	}

	private void buildZiemirad(final StendhalRPZone zone) {
		final SpeakerNPC ziemirad = new SpeakerNPC("Journeyman Ziemirad") {

			@Override
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting("Good day.");
				addReply("master",
						"Did the master send you to me regarding a ruby crystal? I can cut and polish it for you, simply say #'grind ruby'.");
				addGoodbye("Farewell.");
				final Map<String, Integer> requiredResources = new TreeMap<String, Integer>();
				requiredResources.put("money", 580);
				requiredResources.put("ruby crystal", 1);
				
				final ProducerBehaviour behaviour = new ProducerBehaviour("ziemirad_cast_crbuncle",
						"grind", "ruby", requiredResources, 4 * 60);
				
				new ProducerAdder().addProducer(this, behaviour,
						"Good day.");
				addReply("money",
						"The master handles all financial matters. Please ask him.");
			}
		};

		ziemirad.setEntityClass("weaponsellernpc");
		ziemirad.setPosition(4, 17);
		ziemirad.setDirection(Direction.RIGHT);
		ziemirad.initHP(100);
		zone.add(ziemirad);
	}
}
