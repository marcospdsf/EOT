/* $Id: Glut3NPC.java,v 1.5 2020/12/08 16:26:04 davvids Exp $ */
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
package games.stendhal.server.maps.pol.desert.house;

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
public class Glut3NPC implements ZoneConfigurator {

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
		final SpeakerNPC barman = new SpeakerNPC("Niebieski Glut") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(21, 13));
				nodes.add(new Node(21, 20));
				nodes.add(new Node(28, 20));
				nodes.add(new Node(28, 7));
				nodes.add(new Node(12, 7));
				nodes.add(new Node(16, 7));
				nodes.add(new Node(21, 15));
				nodes.add(new Node(12, 13));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				addGreeting(".. gll... ...");
				final Map<String, Integer> offerings = new HashMap<String, Integer>();
				addGoodbye(".. geee.. ...");
			}
		};

		barman.setEntityClass("glut3niebieskinpc");
		barman.setPosition(9, 13);
		barman.initHP(100);
		barman.setDescription("Oto Niebieski Glut. Bleeee.");
		zone.add(barman);
	}
}
