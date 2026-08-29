/* $Id: KolekcjonerAndrzejNPC.java,v 1.16 2010/09/19 02:31:52 nhnb Exp $ */
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
package games.stendhal.server.maps.fado.hotel;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.SpeakerNPC;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Builds Kolekcjoner Andrzej NPC (Cloak Collector).
 *
 * @author kymara
 */
public class KolekcjonerAndrzejNPC implements ZoneConfigurator {
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
	// IL0_KolekcjonerAndrzejNPC - Kolekcjoner Andrzej, the Cloaks Collector
	//

	private void buildNPC(final StendhalRPZone zone, final Map<String, String> attributes) {
		final SpeakerNPC woman = new SpeakerNPC("Kolekcjoner Andrzej") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(112, 3));
				nodes.add(new Node(105, 3));
				nodes.add(new Node(105, 9));
				nodes.add(new Node(111, 9));
				nodes.add(new Node(111, 7));
				nodes.add(new Node(107, 7));
				nodes.add(new Node(107, 3));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				addGreeting();
				addJob("Gdybym mogł to przynosiłbym przepiękne artefakty do muzeum!");
				addHelp("Nic nie potrzebuje. Dziękuje.");
				addGoodbye("Dowidzenia, dowidzenia!");
			}
		};

		woman.setDescription("Oto klient tego hotelu. Podobno zarobił dużo pieniędzy na handlu i teraz kolekcjonuje różne cenne przedmioty.");
		woman.setEntityClass("kolekcjonerandrzejnpc");
		woman.setPosition(112, 3);
		woman.initHP(100);
		zone.add(woman);
	}
}
