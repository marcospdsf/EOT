/* $Id: SmithNPC.java,v 1.19 2010/09/19 09:45:27 kymara Exp $ */
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
package games.stendhal.server.maps.ados.caves;

import games.stendhal.common.Direction;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;

import java.util.Arrays;
import java.util.Map;

public class GoldAppleNPC implements ZoneConfigurator {
	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildGoldAppleArea(zone);
	}

	private void buildGoldAppleArea(final StendhalRPZone zone) {
		final SpeakerNPC GoldApple = new SpeakerNPC("Tajemniczy Wędrowiec") {

			@Override
			// he doesn't move.
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting("Witaj, jestem wędrowcem z odległych krain.");
				addGoodbye("Na razie");
				addHelp("Mógłbym dla ciebie zrobić złote jabłko.");
				addJob("Pochodzę z dalekich krain...");
				
				add(
				        ConversationStates.ATTENDING,
				        Arrays.asList("special", "specjalnego"),
				        null,
				        ConversationStates.ATTENDING,
				        "Kto Ci to powiedział!?! *kaszlnięcie* W każdym razie tak. Mogę Ci zrobić złote jabłko, ale będziesz musiał wykonać #zadanie",
				        null);
			}
		};

		GoldApple.setDescription("Oto Tajemniczy Wędrowiec. Otacza go dziwna aura.");
		GoldApple.setEntityClass("koreknpc3");
		GoldApple.setPosition(118, 70);
		GoldApple.setDirection(Direction.DOWN);
		GoldApple.initHP(100);
		zone.add(GoldApple);
	}
}
