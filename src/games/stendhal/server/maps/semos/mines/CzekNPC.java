/* $Id: CzekNPC.java,v 1.5 2020/12/07 16:05:04 davvids Exp $ */
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
package games.stendhal.server.maps.semos.mines;

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
public class CzekNPC implements ZoneConfigurator {

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
		final SpeakerNPC barman = new SpeakerNPC("Czek") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(122, 56));
				nodes.add(new Node(119, 56));
	            nodes.add(new Node(119, 50));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				addGreeting("What is it?! I have no time to spare, work is piling up...");
				addJob("Na razie niczego nie potrzebuje.");
				addQuest("Na razie niczego nie potrzebuje.");
				final Map<String, Integer> offerings = new HashMap<String, Integer>();
				addGoodbye("Be quicker next time!");
			}
		};

		barman.setEntityClass("dwarf_guardiannpc");
		barman.setPosition(122, 50);
		barman.initHP(100);
		barman.setDescription("You see Czek. He looks exhausted from mining.");
		zone.add(barman);
	}
}
