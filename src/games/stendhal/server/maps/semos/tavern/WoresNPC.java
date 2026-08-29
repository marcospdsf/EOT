/* $Id: WoresNPC.java,v 1.20 2012/05/21 21:54:46 bluelads99 Exp $ */
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
package games.stendhal.server.maps.semos.tavern;

import games.stendhal.common.Direction;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.RPEntity;
import games.stendhal.server.entity.npc.SpeakerNPC;

import java.util.Map;

/**
 * An old man (original name: Sedro) who stands around and gives directions
 * to newcomers. He's the brother of RetireeNPC (original name: Diogenes).
 *
 * @see games.stendhal.server.maps.quests.KillHigh

 */
public class WoresNPC implements ZoneConfigurator {

	public void configureZone(StendhalRPZone zone,
			Map<String, String> attributes) {
		buildNPC(zone);
	}
	
	private void buildNPC(final StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Wores") {
			@Override
			public void createDialog() {
				addGreeting("Greetings.");
				addJob("I am a traveler. I don't need anything at the moment.");
				addOffer("I am just journeying through.");
				addGoodbye("Farewell, and thank you for the pleasant chat.");
				// All further behaviour is defined in quest classes.
			}

			@Override
			protected void onGoodbye(RPEntity player) {
				setDirection(Direction.LEFT);
			}
			
			
			
		};
		npc.setPosition(27, 13);
		npc.setEntityClass("woresnpc");
		npc.setDescription("You see Wores. Ask him about a quest.");
		npc.setDirection(Direction.LEFT);
		zone.add(npc);
	}

}