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
package games.stendhal.server.maps.athor.island;

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
public class WoytykNPC implements ZoneConfigurator {
	
	public void configureZone(final StendhalRPZone zone,
			final Map<String, String> attributes) {
		buildNPC(zone);
	}

	private void buildNPC(final StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Woy'tyk") {
			@Override
			public void createDialog() {
				addGreeting("Witaj wędrowcze. Znajdujesz się na egzotycznej wyspię Athor, gdzie magia jest wszechobecna. Jeśli szukasz potężnych artefaktów, wypowiedz słowo #różdżka, a może będę mógł Ci pomóc.");
				addJob("Jestem czarodziejem, który zamieszkuje tę wyspę. Znam jej zakątki i tajemnice, które skrywa. Moim celem jest poszukiwanie i ochrona starożytnych artefaktów.");
				addHelp("Mogę Ci pomóc, ale najpierw musisz udowodnić swoją wartość. Mieszkańcy wyspy potrzebują jedzenia. Powiedz słowo #jedzenie, a powiem Ci, co należy zrobić.");
				addGoodbye("Powodzenia na Twojej drodze, wędrowcze. Uważaj na siebie na tej wyspie.");
			}

		};
		npc.setPosition(79, 107);
		npc.setDescription("Przed Tobą stoi Woy'tyk, czarodziej zamieszkujący egzotyczną wyspę Athor. Jego oczy zdradzają mądrość i doświadczenie, a jego otoczenie emanuje aurą magii i tajemniczości. Woy'tyk zna sekrety tej wyspy i jest gotów podzielić się nimi z tymi, którzy są tego godni.");
		npc.setEntityClass("wotyknpc");
		zone.add(npc);
	}
}
