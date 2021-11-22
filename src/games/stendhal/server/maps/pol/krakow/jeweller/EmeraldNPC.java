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
public class EmeraldNPC implements ZoneConfigurator {

	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildMieszek(zone);
	}

	private void buildMieszek(final StendhalRPZone zone) {
		final SpeakerNPC mieszek = new SpeakerNPC("czeladnik Mieszek") {

			@Override
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting("Hello there.");
				addReply("Master",
						"Master?? Yes, yes I know the master. What do you need?");
				addReply("emerald",
						"Aaaa ... I have to process an emerald crystal. How about if you would like you could ask #'grind emerald'.");    
				addGoodbye("Goodbye.");
				final Map<String, Integer> requiredResources = new TreeMap<String, Integer>();
				requiredResources.put("money", 180);
				requiredResources.put("emerald crystal", 1);

				final ProducerBehaviour behaviour = new ProducerBehaviour(
					"mieszek_cast_emerald", "grind", "emerald",
					requiredResources, 4 * 60);

				new ProducerAdder().addProducer(this, behaviour,
						"Hello there.");
				addReply("money",
						"Expensive? Not expensive, Master Drogosz is going to raise the price anyway. So there's no need to wonder.");
			}
		};

		mieszek.setEntityClass("recruiter1npc");
		mieszek.setPosition(27, 29);
		mieszek.setDirection(Direction.LEFT);
		mieszek.initHP(100);
		zone.add(mieszek);
	}
}
