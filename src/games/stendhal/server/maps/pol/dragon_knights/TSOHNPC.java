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
package games.stendhal.server.maps.pol.dragon_knights;

import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.SpeakerNPCFactory;
import games.stendhal.server.entity.npc.action.MultipleActions;
import games.stendhal.server.entity.npc.action.SetQuestAction;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.QuestActiveCondition;
import games.stendhal.server.entity.npc.condition.QuestCompletedCondition;
import games.stendhal.server.entity.npc.condition.QuestNotStartedCondition;

import java.util.LinkedList;
import java.util.List;


/**
 * An old hero (original name: Hayunn Naratha) who players meet when they enter the semos guard house.
 *
 * @see games.stendhal.server.maps.quests.BeerForHayunn
 * @see games.stendhal.server.maps.quests.MeetHayunn
 */
public class TSOHNPC extends SpeakerNPCFactory {

	@Override
	public void createDialog(final SpeakerNPC npc) {
		// A little trick to make NPC remember if it has met
		// player before and react accordingly
		// NPC_name quest doesn't exist anywhere else neither is
		// used for any other purpose

		final List<ChatAction> actions = new LinkedList<ChatAction>();
		actions.add(new SetQuestAction("meet_tsoh", "start"));

		npc.add(ConversationStates.IDLE,
				ConversationPhrases.GREETING_MESSAGES,
				new QuestNotStartedCondition("meet_tsoh"),
				ConversationStates.ATTENDING,
				"Welcome! I see you want to get to know The Soldiers Of Honor clan better?",
				new MultipleActions(actions));

		npc.add(ConversationStates.IDLE,
				ConversationPhrases.GREETING_MESSAGES,
				null,
				ConversationStates.ATTENDING,
				"Welcome! I bet you want to join our The Soldiers Of Honor clan.",
				null);

		npc.addHelp("I can tell you about TSOH.");
		npc.addJob("He works for the elite clan The Soldiers Of Honor. I am promoting my clan.");
		npc.addGoodbye("Goodbye. You will always be welcome and think about joining our clan.");
		// further behaviour is defined in quests.
	}
}
