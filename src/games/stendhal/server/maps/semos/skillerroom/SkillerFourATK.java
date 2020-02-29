/* $Id: SkillerFourATK.java,v 1.4 2010/09/19 02:30:47 nhnb Exp $ */
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
package games.stendhal.server.maps.semos.skillerroom;

import java.util.Map;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.SpeakerNPC;

/**
 * @author davvids
 */
public class SkillerFourATK implements ZoneConfigurator {
	//
	// ZoneConfigurator
	//

	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	@Override
	public void configureZone(final StendhalRPZone zone,
			final Map<String, String> attributes) {
		buildNPC(zone, attributes);
	}

	private void buildNPC(final StendhalRPZone zone, final Map<String, String> attributes) {
		final SpeakerNPC SkillerFourATK = new SpeakerNPC("Sebastian") {

			@Override
			protected void createDialog() {
				addGreeting("Witaj wojowniku!");
				addJob("Jestem doświadczonym wojownikiem. Moge nauczyć cie jak się walczy jeśli przyniesiesz mi specjalne #kartki.");
				addHelp("Moge nauczyć cie jak się walczy, jeśli przyniesiesz mi specjalne #kartki.");
				addReply("kartki","Kartki możesz zdobyć od specjalnych potworów lub kupić od graczy. Jeśli przyniesiesz mi kartke to magicznie dodam ci troche punktów umiejętności.");
				addGoodbye("Dowidzenia.");
			}
		};

		SkillerFourATK.setEntityClass("AtkNPC5");
		SkillerFourATK.setPosition(40, 14);
		SkillerFourATK.initHP(100);
		SkillerFourATK.setDescription("Oto Sebastian.");
		zone.add(SkillerFourATK);
	}
}
