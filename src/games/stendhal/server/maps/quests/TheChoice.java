/* $Id$ */
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
package games.stendhal.server.maps.quests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import games.stendhal.common.Direction;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.MultipleActions;
import games.stendhal.server.entity.npc.action.SetQuestAction;
import games.stendhal.server.entity.npc.action.TeleportAction;
import games.stendhal.server.entity.npc.condition.QuestNotStartedCondition;
import games.stendhal.server.entity.player.Player;

public class TheChoice extends AbstractQuest {

	private boolean choice = false;
	private static final String QUEST_SLOT = "thechoice";

	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}

	@Override
	public boolean isCompleted(final Player player) {
		return player.hasQuest(QUEST_SLOT) && !"start".equals(player.getQuest(QUEST_SLOT)) && !"rejected".equals(player.getQuest(QUEST_SLOT));
	}

	@Override
	public List<String> getHistory(final Player player) {
		final List<String> res = new ArrayList<String>();
		if (!player.hasQuest(QUEST_SLOT)) {
			return res;
		}
		res.add("I have met the Universe itself...");
		res.add("He asked me to choose where to live...");
		if (isCompleted(player)) {
			res.add("I have made my choice, there is no turning back now...");
		}
		return res;
	}



	private void prepareRequestingStep1() {
		final SpeakerNPC npc = npcs.get("Universe");
		
		npc.add(ConversationStates.ATTENDING, Arrays.asList("semos","Semos","default"),
				new QuestNotStartedCondition(QUEST_SLOT),
				 ConversationStates.QUESTION_1, "Oh semos! A awesome place, for the experienced players it is like"
				 		+ " home, for new players, it is a big adventure! Veteran players will enjoy again the"
				 		+ " the places they have already visited, but, there are new dangerous places to be,"
				 		+ " do you wish to be teleported there?",
				 		null);
		
		npc.add(ConversationStates.QUESTION_1, ConversationPhrases.NO_MESSAGES, null,
				 ConversationStates.IDLE, "Well, okay then, I shall not teleport you.",
				 null);

	}
	
	private void prepareRequestingStep2() {
		final SpeakerNPC npc = npcs.get("Universe");
		
		npc.add(ConversationStates.ATTENDING, Arrays.asList("zakopane", "new", "different"), 
				new QuestNotStartedCondition(QUEST_SLOT),
				 ConversationStates.QUESTION_2,  "Oh Zakopane! It is a whole new place to be explored, so cool,"
				 		+ " so new, so fresh... It is a nice place to start a diferent adventure. Do you"
				 		+ " wish to be teleported there?",
				 		null);
		
		npc.add(ConversationStates.QUESTION_2, ConversationPhrases.NO_MESSAGES, null,
				 ConversationStates.IDLE, "Well, okay then, I shall not teleport you.",
				 null);
		
	}

	private void ifSemos() {
		final SpeakerNPC npc = npcs.get("Universe");

		final List<ChatAction> reward = new LinkedList<ChatAction>();
		reward.add(new SetQuestAction(QUEST_SLOT, "start"));
		reward.add(new SetQuestAction(QUEST_SLOT, "done"));
		reward.add(new TeleportAction("int_semos_real_guard_house", 10, 10, Direction.DOWN));
		
		npc.add(ConversationStates.QUESTION_1, ConversationPhrases.YES_MESSAGES, null,
				 ConversationStates.ATTENDING, "I hope you enjoy it!",
				 new MultipleActions(reward));
		
	}
	
	private void ifZakopane() {
		final SpeakerNPC npc = npcs.get("Universe");
		

		final List<ChatAction> reward = new LinkedList<ChatAction>();
		reward.add(new SetQuestAction(QUEST_SLOT, "start"));
		reward.add(new SetQuestAction(QUEST_SLOT, "done"));
		reward.add(new TeleportAction("int_zakopane_home", 15, 10, Direction.DOWN));
		
		npc.add(ConversationStates.QUESTION_2, ConversationPhrases.YES_MESSAGES, null,
				 ConversationStates.ATTENDING, "I hope you enjoy it!",
				 new MultipleActions(reward));
		
	}
	
	@Override
	public void addToWorld() {
			fillQuestInfo(
					"The Choice",
					null,
					false);
			prepareRequestingStep1();
			ifSemos();
			fillQuestInfo(
					"The Choice",
					null,
					false);
			prepareRequestingStep2();
			ifZakopane();
		}

	@Override
	public String getName() {
		return "TheChoice";
	}

	@Override
	public int getMinLevel() {
		return 0;
	}

	@Override
	public String getNPCName() {
		return "Universe";
	}

}
