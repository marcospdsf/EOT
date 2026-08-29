/* $Id: SmithNPC.java,v 1.19 2020/11/01 12:38:32 szyg edited by davvids Exp $ */
/***************************************************************************
 *                   (C) Copyright 2003-2020 - Stendhal                    *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.server.maps.ados.caves;

import games.stendhal.common.Direction;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;

import java.util.Arrays;
import java.util.Map;

public class PodroznikNPC implements ZoneConfigurator {
	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildPodroznikArea(zone);
	}

	private void buildPodroznikArea(final StendhalRPZone zone) { 
		final SpeakerNPC Podroznik = new SpeakerNPC("Tajemniczy Wędrowiec") {

			@Override
			// he doesn't move.
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting("Witaj, jestem podróżnikiem z odległych krain.");
				addGoodbye("Na razie");
				addHelp("Potrzebuje paru rzeczy lecz nie mam możliwości zdobyc ich samemu. Jeżeli mi pomożesz to dam ci w zamian magiczny #naszyjnik.");
				addJob("Pochodzę z dalekich krain. Wiem o rzeczach o których nie maja pojecia ludzie z waszej krainy.");
				
				add(
				        ConversationStates.ATTENDING,
				        Arrays.asList("naszyjnik", "magiczny"),
				        null,
				        ConversationStates.ATTENDING,
				        "Posiadam magiczny naszyjnik ktory odpedza od wszelkich klatw. Mogę ci go podarować, ale będziesz musiał wykonać dla mnie jedno #zadanie",
				        null);
			}
		};

		Podroznik.setDescription("You see the Mysterious Traveler, surrounded by a subtle aura.");
		Podroznik.setEntityClass("koreknpc3");
		Podroznik.setPosition(118, 70);
		Podroznik.setDirection(Direction.DOWN);
		Podroznik.initHP(100);
		zone.add(Podroznik);
	}
}
