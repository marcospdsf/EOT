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
public class SilverNPC implements ZoneConfigurator {

	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildDrogosz(zone);
	}

	private void buildDrogosz(final StendhalRPZone zone) {
		final SpeakerNPC drogosz = new SpeakerNPC("mistrz Drogosz") {

			@Override
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog(){
				addGreeting("Witaj!");
				addJob("We are a famous goldsmith's workshop. My journeymen and I deal with the processing of many precious stones\n"
					+ "I work silver ore anyway, just say #cast.\n"
					+ "Ziemirad is engaged in processing ruby crystals.\n"
					+ "Sobek is very good at working with obsidian crystals.\n"
					+ "Mieszko, oh that Mieszko, a bachelor of him, but to the point. He will process you an emerald crystal.\n"
					+ "And the most talented of the four, Krzesim, devoted his abilities to sapphire crystals.\n"
					+ "If you want to work any of these stones, come over and say #master and the journeymen will know that I sent you.");
				addHelp ("What kind of help do you expect? If you want to know something about what I do, say #cast, and I will be happy to tell you.");
				addGoodbye("Goodbye.");

				final Map<String, Integer> requiredResources = new TreeMap<String, Integer>();
				requiredResources.put("wood", 2);
				requiredResources.put("silver ore", 1);
				requiredResources.put("money", 100);

				final ProducerBehaviour behaviour = new ProducerBehaviour(
					"drogosz_cast_silver", "cast", "silver bar",
					requiredResources, 10 * 60);

				new ProducerAdder().addProducer(this, behaviour,
						"Regards. I guess you are interested in silver. If so, ask me for # 'help'.");
				addReply("wood",
						"You have to ask the Lumberjack. He knows how to get a tree.");
				addReply("silver ore",
						"As far as I know, all kinds of precious stones are found in mines. My old friend #Bercik can tell you more about them.");
				addReply("Bercik",
						"You will find Bercik at the church near Zakopane. I remember there were a lot of white tigers around.");
			}
		};

		drogosz.setEntityClass("richardstallmannpc");
		drogosz.setPosition(15, 3);
		drogosz.setDirection(Direction.DOWN);
		drogosz.initHP(100);
		zone.add(drogosz);
	}
}
