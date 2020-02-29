/* $Id: Urzednik2NPC.java,v 1.18 2010/09/19 02:35:31 nhnb Exp $ */
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
package games.stendhal.server.maps.semos.townhall;

import java.util.Map;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.SpeakerNPC;

public class Urzednik2NPC implements ZoneConfigurator {
	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildSemosTownhallAreaMayor(zone);
	}

	/**
	 * Adds a Mayor to the townhall who gives out daily quests.
	 * @param zone zone to be configured with this
	 */
	private void buildSemosTownhallAreaMayor(final StendhalRPZone zone) {
		// We create an NPC
		final SpeakerNPC npc = new SpeakerNPC("Urzędnik Bill") {

			@Override
			protected void createDialog() {
				addGreeting("Witam obywatelu! Potrzebujesz #pomocy?");
				addJob("Jestem jednym z urzędników miasta Semos.");
				addHelp("W Semos spotkasz wielu ludzi, na pewno ktoś będzie potrzebował pomocy.");
				addGoodbye("Życzę miłego dnia!");
			}
		};

		npc.setEntityClass("urzedniknpc");
		npc.setDescription("Oto Urzędnik Bill.");
		npc.setPosition(27, 15);
		npc.initHP(100);
		zone.add(npc);
	}
}
