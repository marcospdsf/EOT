/* $Id: SouthKonfident.java,v 1.19 2010/09/19 09:45:27 kymara Exp $ */
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
package games.stendhal.server.maps.kirdneh.townhall;

import games.stendhal.common.Direction;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;

import java.util.Arrays;
import java.util.Map;

public class SouthKonfident implements ZoneConfigurator {
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
		final SpeakerNPC elemental = new SpeakerNPC("Urzędnik Kirdneh") {

			@Override
			// he doesn't move.
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting("Dzień dobry, jestem urzędnikiem Kirdneh. Odpowiadam za miasta południowe naszej krainy - Fado Kirdneh i Kalavan. Zajmuje się sprawami związanymi z administracją i przyznaje odznaczenia.");
				addGoodbye("Dowidzenia");
				addHelp("Wielu naszych mieszkańców ciągle potrzebuje pomocy. Proszę zapytać w mieście.");
				addJob("Nie mamy na razie żadnych urzędowych zadań.");
				
				add(
				        ConversationStates.ATTENDING,
				        Arrays.asList("spierdalaj", "znikaj"),
				        null,
				        ConversationStates.ATTENDING,
				        "Dowidzenia",
				        null);
			}
		};

		elemental.setDescription("Oto Urzędnik Kirdneh.");
		elemental.setEntityClass("urzedniknpc");
		elemental.setPosition(8, 3);
		elemental.setDirection(Direction.DOWN);
		elemental.initHP(100);
		zone.add(elemental);
	}
}
