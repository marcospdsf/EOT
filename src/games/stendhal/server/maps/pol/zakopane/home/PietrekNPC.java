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
 //Na podstawie pliku RetiredAdventurerNPC z Semos/guardhouse
 
package games.stendhal.server.maps.pol.zakopane.home;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.MultipleActions;
import games.stendhal.server.entity.npc.action.SetQuestAction;
import games.stendhal.server.entity.npc.action.StartRecordingKillsAction;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.QuestActiveCondition;
import games.stendhal.server.entity.npc.condition.QuestCompletedCondition;
import games.stendhal.server.entity.npc.condition.QuestNotStartedCondition;
import games.stendhal.server.maps.quests.BeerForHayunn;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * An old hero (original name: Hayunn Naratha) who players meet when they enter the semos guard house.
 *
 * @see games.stendhal.server.maps.quests.BeerForHayunn
 * @see games.stendhal.server.maps.quests.MeetHayunn
 */
public class PietrekNPC implements ZoneConfigurator {
	private final String QUEST_SLOT="meet_pietrek";

	@Override
	public void configureZone(StendhalRPZone zone,
			Map<String, String> attributes) {
		buildNPC(zone);
	}

	private void buildNPC(StendhalRPZone zone) {
		SpeakerNPC npc = new SpeakerNPC("Pietrek") {

			@Override
			public void createDialog() {
				// A little trick to make NPC remember if it has met
				// player before and react accordingly
				// NPC_name quest doesn't exist anywhere else neither is
				// used for any other purpose

				final List<ChatAction> actions = new LinkedList<ChatAction>();
				actions.add(new SetQuestAction(QUEST_SLOT, 0, "start"));
				actions.add(new StartRecordingKillsAction(QUEST_SLOT, 1, "rat", 0, 1));

				add(ConversationStates.IDLE,
						ConversationPhrases.GREETING_MESSAGES,
						new QuestNotStartedCondition(QUEST_SLOT),
						ConversationStates.ATTENDING,
						"Greetings! I wager you were sent here to learn about the adventures that await you in these lands. First, let's see what you're made of. Go out and slay a rat prowling outside. Would you like to learn how to attack?",
						new MultipleActions(actions));

				add(ConversationStates.IDLE,
						ConversationPhrases.GREETING_MESSAGES,
						new AndCondition(new QuestCompletedCondition(QUEST_SLOT)),
						ConversationStates.ATTENDING,
						"Welcome back. What #help may I provide you with now?",
						null);

				addHelp("As I mentioned, I was once an adventurer, and now I teach newcomers. Would you like me to share what I've learned?");
				addJob("My duty is protecting the good people of Zakopane from wild beasts wandering near town. Ever since many young folk departed, monsters have grown bolder and draw dangerously close. Zakopane counts on help from adventurers like you. Speak to Gazda Wojtek at the town hall—he is sure to have tasks for you.");
				addGoodbye();
				// further behaviour is defined in quests.
			}

			@Override
			protected void createPath() {
				List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(22, 3));
				nodes.add(new Node(28, 3));
				setPath(new FixedPath(nodes, true));
			}
		};

		npc.setPosition(22, 3);
		npc.setEntityClass("oldheronpc");
		npc.setDescription("You see Pietrek. Beneath his silver hair and battle-worn armor, you notice keen eyes and seasoned warrior reflexes.");
		npc.setBaseHP(100);
		npc.setHP(100);
		zone.add(npc);
	}
}
