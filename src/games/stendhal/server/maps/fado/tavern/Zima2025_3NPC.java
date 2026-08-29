/* $Id: Zima2025_3NPC.java,v 1.12 2025/11/16 19:18:40 davvids Exp $ */
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
package games.stendhal.server.maps.fado.tavern;

import games.stendhal.common.Direction;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.SpeakerNPC;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Builds the NPC who deals in rainbow beans.
 * Other behaviour defined in maps/quests/RainbowBeans.java
 *
 * @author kymara
 */
public class Zima2025_3NPC implements ZoneConfigurator {
	//
	// ZoneConfigurator
	//

	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildNPC(zone, attributes);
	}

	//
	// IL0_GreeterNPC
	//

	private void buildNPC(final StendhalRPZone zone, final Map<String, String> attributes) {
		final SpeakerNPC Zima2025_3NPC = new SpeakerNPC("Miraval") {

			@Override
			protected void createPath() {
				// doesn't move
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addJob("Zima w pełni… a ja zajmuję się zamianą kryształów na zwoje. Prosta robota, ale przydatna.");
				addHelp("Nic wielkiego nie potrzebuję - tylko twoje kryształy. Resztą zajmę się sam.");
				addQuest("Nie marudź na chłód, kolego. W tych murach i tak jest cieplej niż na arenie łuczników.");
				addOffer("Jeśli masz szlachetne kryształy, zamienię je na zimowy zwój do tutejszej areny.");
				addGoodbye("Trzymaj się. I uważaj na gołoledź, nie każdy upadek da się wyleczyć eliksirem.");
			}
		};

		Zima2025_3NPC.setEntityClass("oldkingnpc");
		Zima2025_3NPC.setPosition(44, 3);
		Zima2025_3NPC.setDirection(Direction.LEFT);
		Zima2025_3NPC.initHP(100);
		Zima2025_3NPC.setDescription("Widzisz Miravala, rzemieślnika, który potrafi przemienić szlachetne kryształy w zimowy zwój prowadzący na tutejszą arenę łuczników.");
		zone.add(Zima2025_3NPC);
	}
}
