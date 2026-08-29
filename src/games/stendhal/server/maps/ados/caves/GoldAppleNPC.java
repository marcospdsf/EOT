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
				addGreeting("Greetings, I am a traveler from distant lands.");
				addGoodbye("Farewell.");
				addHelp("I could craft a golden apple for you.");
				addJob("I hail from far away lands...");
				
				add(
				        ConversationStates.ATTENDING,
				        Arrays.asList("special", "specjalnego"),
				        null,
				        ConversationStates.ATTENDING,
				        "Who told you that!?! *cough* In any case, yes. I can make a golden apple for you, but you must complete a #quest.",
				        null);
			}
		};

		GoldApple.setDescription("You see the Mysterious Traveler. A strange aura surrounds him.");
		GoldApple.setEntityClass("koreknpc3");
		GoldApple.setPosition(118, 70);
		GoldApple.setDirection(Direction.DOWN);
		GoldApple.initHP(100);
		zone.add(GoldApple);
	}
}
