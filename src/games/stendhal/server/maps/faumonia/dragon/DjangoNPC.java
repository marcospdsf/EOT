/* $Id: DjangoNPC.java,v 1.20 2012/05/21 21:54:46 bluelads99 Exp $ */
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
package games.stendhal.server.maps.faumonia.dragon;

import games.stendhal.common.Direction;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.RPEntity;
import games.stendhal.server.entity.npc.SpeakerNPC;

import java.util.Map;

/**
 * An old man (original name: Django) who stands around and gives directions
 * to newcomers. He's the brother of RetireeNPC (original name: Diogenes).
 *
 * @see games.stendhal.server.maps.quests.KillHigh

 */
public class DjangoNPC implements ZoneConfigurator {

	public void configureZone(StendhalRPZone zone,
			Map<String, String> attributes) {
		buildNPC(zone);
	}
	
	private void buildNPC(final StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Django") {
			@Override
			public void createDialog() {
				addGreeting("Greetings.");
				addJob("Lubie smoki  ale czasami jest ich za dużo i zagrażają naszej krainie. Jestem już za stary na polowanie więc szukam kogoś kto by mi pomógł. Zabijesz kilka smoków dla mnie ?.");
				addGoodbye("Dowidzenia.");
				// All further behaviour is defined in quest classes.
			}

			@Override
			protected void onGoodbye(RPEntity player) {
				setDirection(Direction.LEFT);
			}
			
			
			
		};
		npc.setPosition(5, 3);
		npc.setEntityClass("djangonpc");
		npc.setDescription("Widzisz Django.. Bardzo lubi smoki.");
		npc.setDirection(Direction.DOWN);
		zone.add(npc);
	}

}