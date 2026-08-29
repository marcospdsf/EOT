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
// based on ../games/stendhal/server/maps/ados/abandonedkeep/OrcKillGiantDwarfNPC.java
package games.stendhal.server.maps.pol.zakopane.city;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.SpeakerNPC;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Builds the orc kill diant dwarf NPC.
 *
 * @author Teiv
 */
public class GazdaJedrzejNPC implements ZoneConfigurator {

	//
	// ZoneConfigurator
	//

	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	
	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildNPC(zone);
	}


	private void buildNPC(final StendhalRPZone zone) {
		final SpeakerNPC gazdajedrzejNPC = new SpeakerNPC("Gazda Jędrzej") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(31, 125));
				nodes.add(new Node(35, 125));
				setPath(new FixedPath(nodes, true));

			}

			@Override
			protected void createDialog() {
				addGreeting("Welcome to our lands, currently plagued by hordes of mountain brigands.");
				addJob("I await brave warriors to strike down the #brigands raiding our valleys.");
				addReply(Arrays.asList("mission for a true knight", "misyjkę dla prawdziwego rycerza", "misyjka dla prawdziwego rycerza"), "Ah, Andrzej and his fancy titles! Ha ha ha. Just ask me for a #quest.");
				addReply(Arrays.asList("brigand", "brigands", "zbójnik", "zbójników"), "They appeared out of the crags and sowed fear and lawlessness. Will you help us solve this menace and take on the #quest?");
				addHelp("We once led peaceful, happy lives until cruel brigands occupied our mountain passes. They constantly ambush travelers and rob our stores. Now we seek a #true #knight who can brave this peril.");
				addGoodbye("May strength and good fortune accompany your expeditions.");
			}
		};

		gazdajedrzejNPC.setEntityClass("npcgazdajedrzej");
		gazdajedrzejNPC.setPosition(31, 125);
		gazdajedrzejNPC.initHP(1000);
		zone.add(gazdajedrzejNPC);
	}
}
