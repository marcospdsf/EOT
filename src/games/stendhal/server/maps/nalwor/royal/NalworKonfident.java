/* $Id: NalworKonfident.java,v 1.19 2010/09/19 09:45:27 kymara Exp $ */
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
package games.stendhal.server.maps.nalwor.royal;

import games.stendhal.common.Direction;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;

import java.util.Arrays;
import java.util.Map;

public class NalworKonfident implements ZoneConfigurator {
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
		final SpeakerNPC elemental = new SpeakerNPC("Urzędnik Nalwor") {

			@Override
			// he doesn't move.
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting("Good day, I am the clerk of Nalwor. I handle administrative affairs and award civic #badges.");
				addGoodbye("Goodbye.");
				addHelp("Many of our citizens require assistance. Inquire around town to help them.");
				addJob("Ask about our civic #badge if you wish to earn municipal honors.");
				
				add(
				        ConversationStates.ATTENDING,
				        Arrays.asList("spierdalaj", "znikaj"),
				        null,
				        ConversationStates.ATTENDING,
				        "Goodbye.",
				        null);
			}
		};

		elemental.setDescription("You see the Nalwor Royal Clerk.");
		elemental.setEntityClass("urzedniknpc");
		elemental.setPosition(21, 2);
		elemental.setDirection(Direction.RIGHT);
		elemental.initHP(100);
		zone.add(elemental);
	}
}
