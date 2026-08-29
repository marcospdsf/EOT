/* $Id: WaltNPC.java,v 1.19 2025/06/22 11:45:27 davvids Exp $ */
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
package games.stendhal.server.maps.ados.city;

import games.stendhal.common.Direction;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;

import java.util.Arrays;
import java.util.Map;

public class WaltNPC implements ZoneConfigurator {

	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildWaltNPCArea(zone);
	}

	private void buildWaltNPCArea(final StendhalRPZone zone) {
		final SpeakerNPC WaltNPC = new SpeakerNPC("Walt") {

			@Override
			// he doesn't move.
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting("Witaj, jak Ci mija dzień? Widziałeś te sfory pająków które ostatnio się zalęgły?");
				addGoodbye("Dowidzenia, dziękuje za rozmowę.");
				addHelp("Ta pajęcza sfora bywa problematyczna. Mój syn bardzo obawia się pajęczaków.");
				addJob("Przydałaby mi się mała pomoc z tymi pajęczakami...");
			}
		};

		WaltNPC.setDescription("You see Walt, a taciturn smith with eyes like thawing ice. Rumor has it he can forge simple axes into weapons of immense power.");
		WaltNPC.setEntityClass("shopnpc2");
		WaltNPC.setPosition(112, 73);
		WaltNPC.setDirection(Direction.DOWN);
		WaltNPC.initHP(100);
		zone.add(WaltNPC);
	}
}
