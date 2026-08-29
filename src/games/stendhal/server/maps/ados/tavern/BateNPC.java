/* $Id: BateNPC.java,v 1.5 2020/12/03 14:08:04 davvids Exp $ */
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
package games.stendhal.server.maps.ados.tavern;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.common.Direction;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Ados Tavern (Inside / Level 0).
 *
 * @author kymara
 */
public class BateNPC implements ZoneConfigurator {

	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildTavern(zone, attributes);
	}

	private void buildTavern(final StendhalRPZone zone, final Map<String, String> attributes) {
		final SpeakerNPC barman = new SpeakerNPC("Bate") {

			@Override
			protected void createPath() {
				// NPC doesn't move
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting("Witaj, czego chcesz?");
				addJob("Na razie niczego nie potrzebuje.");
				addQuest("Na razie niczego nie potrzebuje.");
				addHelp("Nie jestem od pomagania.");
				final Map<String, Integer> offerings = new HashMap<String, Integer>();
				addGoodbye("Na razie.");
			}
		};

		barman.setEntityClass("barracksbuyernpc");
		barman.setPosition(6, 14);
		barman.setDirection(Direction.RIGHT);
		barman.initHP(100);
		barman.setDescription("You see Bate, a mysterious figure. Perhaps you should speak with him.");
		zone.add(barman);
	}
}
