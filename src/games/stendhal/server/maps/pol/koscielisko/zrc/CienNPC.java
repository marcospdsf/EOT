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
package games.stendhal.server.maps.pol.koscielisko.zrc;

import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.SpeakerNPCFactory;
/**
 * A shadow (original name: Straznik) who heals players without charge. 
 */
public class CienNPC extends SpeakerNPCFactory {

	@Override
	public void createDialog(final SpeakerNPC npc) {
		npc.addGreeting("Greetings, traveler.");
		npc.addJob("I lead a tranquil existence guarding the gateway to the Order of Shadow Knights.");
		npc.addHelp("Hm.");
		npc.addGoodbye("Farewell, traveler.");
		
	}
}
