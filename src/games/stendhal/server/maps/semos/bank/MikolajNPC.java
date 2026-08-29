/* $Id: MikolajNPC.java,v 1.19 2023/11/26 23:59:27 davvids Exp $ */
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
package games.stendhal.server.maps.semos.bank;

import games.stendhal.common.Direction;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;

import java.util.Arrays;
import java.util.Map;

public class MikolajNPC implements ZoneConfigurator {
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
		final SpeakerNPC elemental = new SpeakerNPC("Święty Mikołaj") {

			@Override
			// he doesn't move.
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting("Ho ho ho! Greetings, my friend! How wonderful to see you in these joyous times.");
				addGoodbye("May holiday warmth fill your heart with joy! Ho ho ho! Farewell and keep the holiday spirit alive.");
			}
		};

		elemental.setDescription("You see Santa Claus, the legendary bearer of gifts and holiday cheer with his crimson coat and flowing white beard.");
		elemental.setEntityClass("santaclausnpc");
		elemental.setPosition(36, 10);
		elemental.setDirection(Direction.LEFT);
		elemental.initHP(100);
		zone.add(elemental);
	}
}
