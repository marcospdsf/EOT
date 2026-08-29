/* $Id: HealerNPC.java,v 1.19 2020/09/19 02:35:36 Szygolek Exp $ */
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
package games.stendhal.server.maps.ados.mountain;

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


public class SamaronNPC implements ZoneConfigurator {
	
	public void configureZone(final StendhalRPZone zone,
			final Map<String, String> attributes) {
		buildNPC(zone);
	}

	private void buildNPC(final StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Samaron") {
			@Override
			public void createDialog() {
				addGreeting("Witaj, podróżniku. Nazywam się Samaron. Krążą legendy o potężnej różdżce, której fragmenty są rozsiane po całym świecie. Różdżka ta posiada moc, o której zwykli śmiertelnicy mogą tylko marzyć. Jeśli chcesz dowiedzieć się więcej, wypowiedz słowo #różdżka");
				addJob("Jestem mistrzem magii i alchemii. Od lat poszukuję składników i wiedzy potrzebnych do stworzenia najpotężniejszych artefaktów. Moim największym celem jest odnalezienie trzech części legendarnej różdżki Mokoszy i złożenie jej w jedną całość. Wielu próbowało, ale nieliczni przetrwali.");
				addHelp("Mogę pomóc Ci w zdobyciu tej niezwykle silnej różdżki Mokoszy, która może przeważyć szalę na Twoją korzyść w każdej bitwie. Aby to osiągnąć, musisz znaleźć trzy magiczne części ukryte w różnych zakątkach świata. Przyjdź do mnie, gdy będziesz gotów na to wyzwanie i powiedz #różdżka");
				addGoodbye("Powodzenia, niech magia będzie z Tobą. Wracaj, gdy będziesz gotów na kolejne wyzwania.");
			}


		};
		npc.setPosition(112, 45);
		npc.setDescription("Before you stands Samaron, a formidable mage with a piercing gaze and an aura of arcane power. Ancient tomes and relics surround him, radiating profound mystical energy.");
		npc.setEntityClass("samaronnpc");
		zone.add(npc);
	}
}
