/* $Id: KowalLudwikNPC.java,v 1.19 2020/11/01 8:24:27 szyg edited by davvids Exp $ */
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
package games.stendhal.server.maps.bandit.blacksmith;

import games.stendhal.common.Direction;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;

import java.util.Arrays;
import java.util.Map;

public class KowalLudwikNPC implements ZoneConfigurator {
	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildKowalLudwikNPCArea(zone);
	}

	private void buildKowalLudwikNPCArea(final StendhalRPZone zone) {
		final SpeakerNPC KowalLudwikNPC = new SpeakerNPC("Ludwik") {

			@Override
			// he doesn't move.
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting("Witaj, wojowniku. Patrząc na Ciebie widzę... potencjał.");
				addGoodbye("Nie każdy jest gotów na prawdziwe ulepszenie.");
				addHelp("Słyszałeś o ciupadze z wąsem? Nie? To dobrze. To broń dla nielicznych.");
				addJob("Nie zawsze byłem kowalem. Ale odkąd pamiętam – ulepszałem to, czego inni nawet nie potrafili utrzymać w rękach.");

				add(
					ConversationStates.ATTENDING,
					Arrays.asList("ciupaga", "ulepsz", "zadanie", "dwa wąsy", "wąsy"),
					null,
					ConversationStates.ATTENDING,
					"Jeśli przyszedłeś tu z myślą o wyjątkowej broni... być może trafiłeś dobrze. Ale najpierw #zadanie. Dopiero potem porozmawiamy o wąsach.",
					null);
			}
		};

		KowalLudwikNPC.setDescription("Ludwik – milczący kowal o spojrzeniu jak topniejący lód. Podobno potrafi zmienić zwykłą ciupagę w coś... niepokojąco potężnego.");
		KowalLudwikNPC.setEntityClass("npc_flisak2");
		KowalLudwikNPC.setPosition(3, 10);
		KowalLudwikNPC.setDirection(Direction.DOWN);
		KowalLudwikNPC.initHP(100);
		zone.add(KowalLudwikNPC);
	}
}
