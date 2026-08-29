/* $Id: IvarNPC.java,v 1.6 2024/05/18 12:27:58 nhnb Exp $ */
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
package games.stendhal.server.maps.ados.townhall;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.SpeakerNPC;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Builds an NPC to keep track of all the traders in Faiumoni
 * This means players can come find prices of all items. 
 * The shop signs now have to be coded in XML not java because the implementation got moved over :(
 * So if you want to read them see data/conf/zones/ados.xml
 * @author kymara
 */
public class IvarNPC implements ZoneConfigurator {

	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildNPC(zone);
	}

	private void buildNPC(final StendhalRPZone zone) {
		// Please change the NPCOwned Chest name if you change this NPC name.
		final SpeakerNPC npc = new SpeakerNPC("Ivar") {

			@Override
			protected void createPath() {
				// NPC nie rusza się
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting("Witam. Jak mogę pomóc?");
				addJob("Mam tu ogromne zamieszanie, więc nie przeszkadzaj mi za bardzo! Te przeklęte potwory w tunelach chaosu napadają na naszych kupców i powodują chaos. Muszę się tym zająć.");
				addHelp("To prawdziwa tragedia! Potwory w tunelach chaosu sieją spustoszenie, atakują naszych kupców i niszczą nasze korytarze. Przez te ataki nasze finanse są w opłakanym stanie. Potrzebujemy pomocy, by sobie z tym poradzić.");
				addOffer("Nie zajmuję się sprzedażą. Jestem urzędnikiem, a nie handlarzem. Moim zadaniem jest dbanie o finanse miasta.");
				addQuest("Musimy znaleźć sposób na rozwiązanie problemów z potworami w tunelach chaosu. Te ataki nie mogą trwać wiecznie");
				addGoodbye("Do widzenia. Proszę, nie zawracaj mi głowy, muszę zająć się bałaganem, który te potwory wywołały.");
			}
		};
		npc.setDescription("You see an Ados city hall official deeply engrossed in civic matters, managing city affairs and trade security amidst chaos tunnel incursions.");
		npc.setEntityClass("meatandfishsmokernpc");
		npc.setPosition(13, 13);
		npc.initHP(100);
		zone.add(npc);


	}
}
