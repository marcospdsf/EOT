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
		final SpeakerNPC drogosz = new SpeakerNPC("Master Drogosz") {

			@Override
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog(){
				addGreeting("Greetings!");
				addJob("We are a renowned goldsmith workshop. My journeymen and I refine many precious gems and metals.\n"
					+ "I smelt raw silver ore into bars—just say #cast.\n"
					+ "Ziemirad cuts ruby crystals.\n"
					+ "Sobek is an expert at cutting obsidian crystals.\n"
					+ "Mieszek—ah, that Mieszek, what a prankster, but skilled nonetheless—he will cut your emerald crystals.\n"
					+ "And the most talented of the four, Krzesim, dedicates his craft to sapphire crystals.\n"
					+ "If you wish to have any of these gems cut, approach them and say #master so they know I sent you.");
				addHelp("What kind of assistance do you need? If you wish to learn what we do here, ask about my #job and I shall gladly explain.");
				addGoodbye("Farewell.");

				final Map<String, Integer> requiredResources = new TreeMap<String, Integer>();
				requiredResources.put("wood", 2);
				requiredResources.put("silver ore", 1);
				requiredResources.put("money", 100);

				final ProducerBehaviour behaviour = new ProducerBehaviour(
					"drogosz_cast_silver", "cast", "silver bar",
					requiredResources, 10 * 60);

				new ProducerAdder().addProducer(this, behaviour,
						"Greetings. I presume you are interested in refined silver. If so, ask me for #'help'.");
				addReply("wood",
						"You must ask the Woodcutter. He knows how to gather quality timber.");
				addReply("silver ore",
						"Precious ores and gemstones of all kinds can be found deep in the mines. My old friend #Bercik can tell you more about them.");
				addReply("Bercik",
						"You can find Bercik in Kościelisko near Zakopane. Beware, I recall white tigers prowl around those parts.");
			}
		};

		drogosz.setEntityClass("richardstallmannpc");
		drogosz.setPosition(15, 3);
		drogosz.setDirection(Direction.DOWN);
		drogosz.initHP(100);
		zone.add(drogosz);
	}
}
