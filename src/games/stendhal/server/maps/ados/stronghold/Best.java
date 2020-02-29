/* $Id: elemental1NPC.java,v 1.19 2010/09/19 09:45:27 kymara Exp $ */
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

public class Best implements ZoneConfigurator {
	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildelemental1Area(zone);
	}

	private void buildelemental1Area(final StendhalRPZone zone) {
		final SpeakerNPC elemental1 = new SpeakerNPC("Major Klykh") {

			@Override
			// he doesn't move.
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting("Witaj, jestem dowódcą tej jednostki.");
				addGoodbye("Żegnaj!");
				addHelp("Jeśli wykonasz #zadanie, to odznaczę Cię obsidianowym medalem, pod warunkiem że posiadasz rubyowy.");
				addJob("Jeśli wykonasz #zadanie, to odznaczę Cię obsidianowym medalem, pod warunkiem że posiadasz rubyowy.");
				
				add(
				        ConversationStates.ATTENDING,
				        Arrays.asList("odznaka", "medal"),
				        null,
				        ConversationStates.ATTENDING,
				        "Naprawdę chciałbyś wykonać #zadanie?",
				        null);
			}
		};

		elemental1.setDescription("Oto dowódca jednostki obronnej zamku Ados.");
		elemental1.setEntityClass("najlepszy");
		elemental1.setPosition(60, 9);
		elemental1.setDirection(Direction.LEFT);
		elemental1.initHP(100);
		zone.add(elemental1);
	}
}
