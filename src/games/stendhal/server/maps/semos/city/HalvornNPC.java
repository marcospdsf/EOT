/* $Id: HalvornNPC.java,v 1.20 2024/12/04 18:54:46 davvids Exp $ */
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
package games.stendhal.server.maps.semos.city;

import games.stendhal.common.Direction;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.RPEntity;
import games.stendhal.server.entity.npc.SpeakerNPC;

import java.util.Map;

/**
 * An old man (original name: Halvorn) who stands around and gives directions
 * to newcomers. He's the brother of RetireeNPC (original name: Diogenes).
 *
 * @see games.stendhal.server.maps.quests.KillHigh

 */
public class HalvornNPC implements ZoneConfigurator {

	public void configureZone(StendhalRPZone zone,
			Map<String, String> attributes) {
		buildNPC(zone);
	}
	
	private void buildNPC(final StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Halvorn") {
			@Override
			public void createDialog() {
				addGreeting("Ah, greetings traveler. Do you feel it? The icy wind carries tales of a winter such as Semos hasn't witnessed in ages. Snow blankets every corner, stirring legends once thought to be mere fairy tales.");
				addJob("In these frosty times, more than just snow has arrived. Frost monsters grow bolder, emerging from their lairs. Giants, golems, and even arctic dragons are returning as if guarding something precious. Legends say they know where the Wawel Dragon sleeps. If you have courage, face them... but remember, only the bravest survive.");
				addGoodbye("May your resolve remain unbroken like eternal ice, traveler. Watch your step in these frosty lands—winter in Faumonia forgives no weakness.");
				// All further behaviour is defined in quest classes.
			}

			@Override
			protected void onGoodbye(RPEntity player) {
				setDirection(Direction.LEFT);
			}
			
			
			
		};
		npc.setPosition(78, 26);
		npc.setEntityClass("npcmagik");
		npc.setDescription("You see Halvorn, an aged wanderer resting on the bench before the bank. In his eyes lies ancient wisdom and concern.");
		npc.setDirection(Direction.DOWN);
		zone.add(npc);
	}

}