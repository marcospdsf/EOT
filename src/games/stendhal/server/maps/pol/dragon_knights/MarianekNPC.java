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
// Base on ../games/stendhal/server/maps/ados/barracks/BuyerNPC.java
package games.stendhal.server.maps.pol.dragon_knights;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.SpeakerNPC;

import java.util.Map;

/**
 * Builds an NPC to buy previously un bought armor.
 *
 * @author kymara
 */
public class MarianekNPC implements ZoneConfigurator {
/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildNPC(zone);
	}

	private void buildNPC(final StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Marianek") {

			@Override
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting("Greetings, brave knight!");
				addJob("I am the blacksmith for this domain. Perhaps I will have a #quest for you.");
				addHelp("I require nothing right now.");
				addGoodbye("Farewell.");
			}
		};

		npc.setDescription("You see Marianek, the castle blacksmith.");
		npc.setEntityClass("blacksmithnpc");
		npc.setPosition(5, 4);
		npc.initHP(1000);
		zone.add(npc);
	}
}
