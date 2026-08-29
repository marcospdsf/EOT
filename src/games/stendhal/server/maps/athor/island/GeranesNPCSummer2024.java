/* $Id: GeranesNPCSummer2024Summer2024.java,v 1.20 2024/06/25 22:17:58 davvids Exp $ */
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
package games.stendhal.server.maps.athor.island;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.SpeakerNPC;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Builds ados Geranes NPC.
 * He may give an items quest later
 * Now he sells ados scrolls
 * @author kymara
 */
public class GeranesNPCSummer2024 implements ZoneConfigurator {
	

	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildGeranes(zone);
	}

	private void buildGeranes(final StendhalRPZone zone) {
		final SpeakerNPC Geranes = new SpeakerNPC("Geranes") {

			@Override
			protected void createDialog() {
				addGreeting("Witaj, wojowniku!");
				addJob("Szukam kogoś kto przyniesie mi troche skór.");
				addHelp("Szukam kogoś kto przyniesie mi troche skor, jeżeli jesteś zainteresowany to możemy porozmawiać o jednym #zadaniu");
				addGoodbye("Żegnaj!");
			}
		};

		Geranes.setDescription("Widzisz Geranesa. Wygląda na zmęczonego.");
		Geranes.setEntityClass("blackwizardpriestnpc");
		Geranes.setPosition(109, 61);
		Geranes.initHP(100);
		zone.add(Geranes);
	}
}
