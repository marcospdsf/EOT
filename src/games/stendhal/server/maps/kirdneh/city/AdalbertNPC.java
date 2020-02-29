/* $Id: AdalbertNPC.java,v 1.12 2011/03/31 15:30:57 bluelads99 edited by szyg Exp $ */
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
package games.stendhal.server.maps.kirdneh.city;

import java.util.Map;

import games.stendhal.common.Direction;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.RPEntity;
import games.stendhal.server.entity.npc.SpeakerNPC;

/**
 * Builds a information giving NPC in Kirdneh city. 
 *
 * @author kymara
 */
public class AdalbertNPC implements ZoneConfigurator {
	//
	// ZoneConfigurator
	//

	/**
	 * Configure a zone.
	 *
	 * @param zone
	 *            The zone to be configured.
	 * @param attributes
	 *            Configuration attributes.
	 */
	public void configureZone(final StendhalRPZone zone,
			final Map<String, String> attributes) {
		buildNPC(zone, attributes);
	}

	private void buildNPC(final StendhalRPZone zone, final Map<String, String> attributes) {
		final SpeakerNPC npc = new SpeakerNPC("Adalbert") {

			@Override
			protected void createPath() {
				setPath(null);
			}

			protected void onGoodbye(RPEntity player) {
				setDirection(Direction.DOWN);
			}

			@Override
			protected void createDialog() {
				addGreeting("Witaj...");
				addJob("...");
				addHelp("Nie mam jak Ci pomóc..");
				addOffer("Nic nie sprzedaję...");
				addQuest("Chcesz mi #pomóc?");
				addGoodbye("Żegnaj...");
			}
		};

		npc.setEntityClass("kid6npc");
		npc.setPosition(15, 15);
		npc.initHP(100);
		npc.setDescription("Oto Adalbert. Jest smutny.");
		zone.add(npc);
	}
}
