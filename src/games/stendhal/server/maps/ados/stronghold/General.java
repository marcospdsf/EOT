/* $Id: elemental2NPC.java,v 1.19 2010/09/19 09:45:27 kymara Exp $ */
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

import java.util.Map;

import games.stendhal.common.Direction;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.SpeakerNPC;

public class General implements ZoneConfigurator {
	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildelemental2Area(zone);
	}

	private void buildelemental2Area(final StendhalRPZone zone) {
		final SpeakerNPC elemental2 = new SpeakerNPC("Generał Jan") {

			@Override
			// he doesn't move.
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting("Witaj, jestem generałem wojsk Ados.");
				addGoodbye("Na razie");
				addHelp("Mam dla Ciebie #zadanie, jeśli nie boisz się potworów!");
				addJob("Mam dla Ciebie #zadanie, jeśli nie boisz się potworów!");
				
				
			}
		};

		elemental2.setDescription("Oto generał wojsk Ados.");
		elemental2.setEntityClass("bman");
		elemental2.setPosition(9, 2);
		elemental2.setDirection(Direction.DOWN);
		elemental2.initHP(100);
		zone.add(elemental2);
	}
}
