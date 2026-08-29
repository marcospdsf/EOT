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
package games.stendhal.server.maps.fado.city;

import games.stendhal.common.Direction;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;

import java.util.Arrays;
import java.util.Map;

public class ElementalNPC implements ZoneConfigurator {
	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildElementalArea(zone);
	}

	private void buildElementalArea(final StendhalRPZone zone) {
		final SpeakerNPC elemental = new SpeakerNPC("Ognistiak") {

			@Override
			// he doesn't move.
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting("Witaj, jestem ognistym żywiołem.");
				addGoodbye("Na razie");
				addHelp("Mógłbym Ci pomóc w zdobyciu specjalnego sztyletu.");
				addJob("Kiedyś byłem szanowanym kowalem na całą Faumonie.");
				
				add(
				        ConversationStates.ATTENDING,
				        Arrays.asList("special", "specjalnego"),
				        null,
				        ConversationStates.ATTENDING,
				        "Kto Ci to powiedział!?! *kaszlnięcie* W każdym razie tak. Mogę wykuć dla Ciebie specjalny przedmiot, ale będziesz musiał wykonać #zadanie",
				        null);
			}
		};

		elemental.setDescription("Oto Ognistiak. Czujesz się dziwnie stojąc w jego pobliżu.");
		elemental.setEntityClass("koreknpc1");
		elemental.setPosition(103, 75);
		elemental.setDirection(Direction.DOWN);
		elemental.initHP(100);
		zone.add(elemental);
	}
}
