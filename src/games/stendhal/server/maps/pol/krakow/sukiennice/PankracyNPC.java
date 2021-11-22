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
package games.stendhal.server.maps.pol.krakow.sukiennice;

import games.stendhal.common.Direction;
import games.stendhal.common.parser.Sentence;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.Entity;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ChatCondition;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.SayTextAction;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.player.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PankracyNPC implements ZoneConfigurator {
	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildKrakowSukienniceArea(zone);
	}

	private void buildKrakowSukienniceArea(final StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Pankracy") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(12, 23));
				nodes.add(new Node(12, 38));
				nodes.add(new Node(19, 38));
				nodes.add(new Node(20, 4));
				nodes.add(new Node(12, 4));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				add(ConversationStates.IDLE,
					ConversationPhrases.GREETING_MESSAGES,
					new AndCondition(
						new ChatCondition() {
							@Override
							public boolean fire(final Player player, final Sentence sentence, final Entity entity) {
								return !player.isGhost();
							}
						}),
					ConversationStates.ATTENDING,
					null,
					new SayTextAction("[name]! Vegetables and fruits! Fish and Meat! Costumes and armor! Buy and sell!"));

				add(ConversationStates.IDLE,
					ConversationPhrases.GREETING_MESSAGES,
					null,
					ConversationStates.ATTENDING,
					"Welcome to the market in the cloth hall.",
					null);
				addGoodbye();            

			}
		};

		npc.setEntityClass("oldheronpc");
		npc.setDescription("Master Pankracy was in charge of the reconstruction of the cloth hall after the fire in 1555.");
		npc.setPosition(12, 23);
		npc.initHP(100);
		zone.add(npc);
	}
}