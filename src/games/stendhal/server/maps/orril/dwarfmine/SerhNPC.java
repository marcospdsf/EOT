/* $Id: SerhNPC.java,v 1.19 2020/12/08 00:55:27 davvids Exp $ */
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
package games.stendhal.server.maps.orril.dwarfmine;

import games.stendhal.common.Direction;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;

import java.util.Arrays;
import java.util.Map;

public class SerhNPC implements ZoneConfigurator {
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
		final SpeakerNPC elemental = new SpeakerNPC("Serh") {

			@Override
			// he doesn't move.
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting("Pomocy...");
				addGoodbye("... ratunku...");
			}
		};

		elemental.setDescription("Oto Serh. Wygląda na przerażonego.");
		elemental.setEntityClass("olddwarfnpc");
		elemental.setPosition(11, 44);
		elemental.setDirection(Direction.DOWN);
		elemental.initHP(100);
		zone.add(elemental);
	}
}
