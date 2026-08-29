/* $Id: HealerNPC.java,v 1.19 2010/09/19 02:35:36 nhnb Exp $ */
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
package games.stendhal.server.maps.kirdneh.city;


import games.stendhal.server.entity.npc.shop.ShopsList;
import games.stendhal.server.entity.npc.shop.ShopType;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.behaviour.adder.HealerAdder;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A young lady (original name: Carmen) who heals players without charge. 
 */
public class YlrikNPC implements ZoneConfigurator {
	
	public void configureZone(final StendhalRPZone zone,
			final Map<String, String> attributes) {
		buildNPC(zone);
	}

	private void buildNPC(final StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Ylrik") {
			@Override
			public void createDialog() {
				addGreeting("Witaj, podróżniku! Widzę, że masz w sobie ducha przygody. Co Cię sprowadza do naszego miasta?");
				addJob("Jestem czarodziejem zamieszkującym to miejsce. Mimo wyglądu wojownika, moją pasją jest gotowanie. Lubię łączyć magię z kulinariami.");
				addHelp("Na tę chwilę nie mamy nic do omówienia, ale może następnym razem będę mógł Ci pomóc. Jeśli lubisz wyzwania, możesz spróbować swoich sił na ptakach na zachodzie. Są dość uciążliwe.");
				addGoodbye("Żegnaj, wędrowcze. Niech Twoja podróż będzie bezpieczna i pełna przygód.");
			}

		};
		ShopsList.get().configureNPC(npc, "healing", ShopType.ITEM_SELL, true);
		new HealerAdder().addHealer(npc, 0);
		npc.setPosition(120, 92);
		npc.setDescription("Przed Tobą stoi Ylrik, czarodziej o wyglądzie wojownika, siedzący obok słynnego muzeum w południowym mieście Kirdneh. Jego muskularna sylwetka kontrastuje z jego magicznymi zdolnościami. Powietrze wokół niego wypełnia zapach świeżo przygotowanego jedzenia, zdradzając jego zamiłowanie do gotowania.");
		npc.setEntityClass("ylriknpc");
		zone.add(npc);
	}
}
