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
package games.stendhal.server.maps.pol.krakow.planty;

import games.stendhal.common.Direction;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.action.SayTextAction;
import games.stendhal.server.entity.npc.SpeakerNPC;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/*
 * Food and drink seller,  Inside Semos Tavern - Level 0 (ground floor)
 * Sells the flask required for Tad's quest IntroducePlayers
 */
public class EdragonNPC implements ZoneConfigurator {

	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildNPC(zone);
	}

	private void buildNPC(final StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("eDragon") {

			@Override
			protected void createPath() {
				// NPC does not move
				setPath(null);
			}
			@Override
			protected void createDialog() {
				addGreeting(null, new SayTextAction("Greetings, [name]! I hope you are not a spy—otherwise I would have to devour you."));
				addJob("I have no regular work for you... But you could embark on the #quest for the Baron's Ring.");
				addHelp("Help me? Ha ha... Wait, you could attempt the #quest for the Baron's Ring.");
				addGoodbye("Good luck!");
			}
		};
		
		npc.setDescription("You see eDragon, who fled from the sanctuary before it was destroyed.");
		npc.setEntityClass("npc_eDragon");
		npc.setPosition(123, 71);
		npc.setDirection(Direction.RIGHT);
		npc.initHP(100);
		zone.add(npc);
	}
}
