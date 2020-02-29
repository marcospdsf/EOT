/* $Id: elementalNPC.java,v 1.19 2010/09/19 09:45:27 kymara Exp $ */
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
package games.stendhal.server.maps.ados.stronghold;

import games.stendhal.common.Direction;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;

import java.util.Arrays;
import java.util.Map;

public class Archer implements ZoneConfigurator {
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
		final SpeakerNPC elemental = new SpeakerNPC("Trener łuczników") {

			@Override
			// he doesn't move.
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting("Witaj, jestem trenerem łuczników.");
				addGoodbye("Żegnaj!");
				addHelp("Jeśli wykonasz #zadanie, to odznaczę Cię sapphireowym medalem, pod warunkiem że posiadasz emeraldowy.");
				addJob("Jeśli wykonasz #zadanie, to odznaczę Cię sapphireowym medalem,  pod warunkiem że posiadasz emeraldowy.");
				
				add(
				        ConversationStates.ATTENDING,
				        Arrays.asList("odznaka", "medal"),
				        null,
				        ConversationStates.ATTENDING,
				        "Naprawdę chciałbyś wykonać #zadanie?",
				        null);
			}
		};

		elemental.setDescription("Oto dowódca łuczników Ados.");
		elemental.setEntityClass("aboss");
		elemental.setPosition(60, 41);
		elemental.setDirection(Direction.LEFT);
		elemental.initHP(100);
		zone.add(elemental);
	}
}
