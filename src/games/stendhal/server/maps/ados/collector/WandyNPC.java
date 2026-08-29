/* $Id: WandyNPC.java,v 1.9 2011/09/08 17:20:59 bluelads99 Exp $ */
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
package games.stendhal.server.maps.ados.collector;

import games.stendhal.common.Direction;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.RPEntity;
import games.stendhal.server.entity.npc.SpeakerNPC;
import java.util.Arrays;
import java.util.Map;

/**
 * Builds a Curator NPC in Kirdneh museum .
 *
 * @author kymara
 */
public class WandyNPC implements ZoneConfigurator {
	
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
		final SpeakerNPC npc = new SpeakerNPC("Wandy") {

			@Override
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting("Witaj przybyszu.");
				addJob("Jestem mieszkańcem stolicy Ados. Od czasu do czasu lubie zbierać rzadkie przedmioty.");
				addHelp("Nie potrzebuje pomocy.");
				addReply(Arrays.asList("exhibits", "eksponaty", "eksponatów"),"Być może będziesz mieć dryg do wyszukiwania przedmiotów i chciałbyś zrobić #zadanie dla mnie.");
				// remaining behaviour defined in games.stendhal.server.maps.quests.WeeklyItemQuest
				addGoodbye("Dowidzenia. Miło się z tobą rozmawiało.");
			}
			
			protected void onGoodbye(RPEntity player) {
				setDirection(Direction.DOWN);
			}
		};

		npc.setEntityClass("wandynpc");
		npc.setPosition(8, 2);
		npc.setDirection(Direction.DOWN);
		npc.initHP(100);
		npc.setDescription("Oto Wandy.");
		zone.add(npc);
	}
}
