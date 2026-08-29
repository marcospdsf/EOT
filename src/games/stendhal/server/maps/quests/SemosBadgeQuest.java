/* $Id: UltimateCollector.java 2017/07/19 02:17:41 szyg $ */
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import games.stendhal.common.grammar.Grammar;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.DropRecordedItemAction;
import games.stendhal.server.entity.npc.action.EquipItemAction;
import games.stendhal.server.entity.npc.action.IncreaseAtkXPAction;
import games.stendhal.server.entity.npc.action.IncreaseDefXPAction;
import games.stendhal.server.entity.npc.action.IncreaseKarmaAction;
import games.stendhal.server.entity.npc.action.IncreaseXPAction;
import games.stendhal.server.entity.npc.action.MultipleActions;
import games.stendhal.server.entity.npc.action.SayRequiredItemAction;
import games.stendhal.server.entity.npc.action.SetQuestAction;
import games.stendhal.server.entity.npc.action.StartRecordingRandomItemCollectionAction;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.GreetingMatchesNameCondition;
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.PlayerHasRecordedItemWithHimCondition;
import games.stendhal.server.entity.npc.condition.QuestActiveCondition;
import games.stendhal.server.entity.npc.condition.QuestCompletedCondition;
import games.stendhal.server.entity.npc.condition.QuestNotCompletedCondition;
import games.stendhal.server.entity.npc.condition.QuestNotStartedCondition;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.Region;

public class SemosBadgeQuest extends AbstractQuest {

	//this quest slot
	private static final String QUEST_SLOT = "semos_badge";
	// quest to retrieve boy's toy, quest giver: Plink
	private static final String FIRST_QUEST_SLOT = "plinks_toy";
	// quest to help Sato with growing his sheep_growing, quest giver: Sato
	private static final String SECOND_QUEST_SLOT = "herbs_for_carmen";
	// quest to help Xoderos with feeding his hungry brother, quest giver: Xoderos
	private static final String THIRD_QUEST_SLOT = "hungry_joshua";
	
	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}

	@Override
	public List<String> getHistory(final Player player) {
		final List<String> res = new ArrayList<String>();
		if (!player.hasQuest(QUEST_SLOT)) {
			return res;
		}
		res.add("I can earn the Semos Badge.");
		final String questState = player.getQuest(QUEST_SLOT);
		if (questState.equals("rejected")) {
			res.add("I declined the Semos Badge.");
			return res;
		}
		res.add("I accepted the quest to assist the citizens of Semos.");
		if (!isCompleted(player)) {
			res.add("To receive the Semos Badge, I must bring the clerk " + Grammar.a_noun(player.getRequiredItemName(QUEST_SLOT,0)) + ".");
		}
		if (isCompleted(player)) {
			res.add("I was decorated with the Semos Badge.");
		}
		return res;
	}
	
	private void checkCollectingQuests() {
		final SpeakerNPC npc = npcs.get("Urzędnik Semos");
		
		
		npc.add(
		ConversationStates.IDLE,
		ConversationPhrases.GREETING_MESSAGES,
			new AndCondition(new GreetingMatchesNameCondition(npc.getName()),
			new QuestNotStartedCondition(QUEST_SLOT)),
		ConversationStates.ATTENDING,
		"Greetings. I am the Semos clerk. I manage civic affairs and award #badges for helping our town.",
		null);
		
		npc.add(ConversationStates.ATTENDING,
			Arrays.asList("badges", "badge", "odznaki", "zadanie", "medal", "odznakę"),
			new AndCondition(new QuestNotCompletedCondition(FIRST_QUEST_SLOT),
					new QuestNotStartedCondition(QUEST_SLOT)),
			ConversationStates.ATTENDING,
			"Before I can award you a badge, you must help Plink with his toy.",
			null);
			
		npc.add(ConversationStates.ATTENDING,
			Arrays.asList("badges", "badge", "odznaki", "zadanie", "medal", "odznakę"),
			new AndCondition(new QuestNotCompletedCondition(SECOND_QUEST_SLOT),
					new QuestNotStartedCondition(QUEST_SLOT)),
			ConversationStates.ATTENDING,
			"Before I can award you a badge, you must help Carmen with her herbs.",
			null);	
					
		npc.add(ConversationStates.ATTENDING,
			Arrays.asList("badges", "badge", "odznaki", "zadanie", "medal", "odznakę"),
			new AndCondition(new QuestNotCompletedCondition(THIRD_QUEST_SLOT),
					new QuestNotStartedCondition(QUEST_SLOT)),
			ConversationStates.ATTENDING,
			"Before I can award you a badge, you must help Xoderos and his brother.",
			null);
			
	}

	private void requestItem() {
		
		final SpeakerNPC npc = npcs.get("Urzędnik Semos");
		final Map<String,Integer> items = new HashMap<String, Integer>();
		
		
		
		items.put("apple",15);
		items.put("flour",30);
		items.put("sausage",30);
		items.put("egg",10);
		
		npc.add(ConversationStates.ATTENDING,
				Arrays.asList("badges", "badge", "odznaki", "zadanie", "medal", "odznakę"),
				new AndCondition(
						new QuestNotStartedCondition(QUEST_SLOT),
						new QuestCompletedCondition(SECOND_QUEST_SLOT),
						new QuestCompletedCondition(FIRST_QUEST_SLOT),
						new QuestCompletedCondition(THIRD_QUEST_SLOT)),
					ConversationStates.ATTENDING,
					null,
					new StartRecordingRandomItemCollectionAction(QUEST_SLOT, items, "You have aided many citizens of Semos. To prove your devotion, please bring [item]."));
	}
						
	private void collectItem() {
			final SpeakerNPC npc = npcs.get("Urzędnik Semos");
			
					npc.add(ConversationStates.IDLE,
				ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new GreetingMatchesNameCondition(npc.getName()),
						new QuestActiveCondition(QUEST_SLOT)),
				ConversationStates.QUEST_ITEM_QUESTION, 
				"Did you bring the supplies I requested?",
				null);
				
			npc.add(ConversationStates.QUEST_ITEM_QUESTION,
				ConversationPhrases.YES_MESSAGES, 
				new AndCondition(new QuestActiveCondition(QUEST_SLOT),
								new NotCondition(new PlayerHasRecordedItemWithHimCondition(QUEST_SLOT))),
				ConversationStates.ATTENDING, 
				null,
				new SayRequiredItemAction(QUEST_SLOT, "You do not have [item] with you."));

					npc.add(ConversationStates.QUEST_ITEM_QUESTION,
				ConversationPhrases.YES_MESSAGES, 
				new AndCondition(new QuestActiveCondition(QUEST_SLOT),
								new PlayerHasRecordedItemWithHimCondition(QUEST_SLOT)),
				ConversationStates.ATTENDING, 
				"On behalf of the citizens of Semos, I thank you. Here is your badge!",
				new MultipleActions(new DropRecordedItemAction(QUEST_SLOT), 
									new SetQuestAction(QUEST_SLOT, "done"),
									new IncreaseXPAction(3000),
									new IncreaseAtkXPAction(2500),
									new IncreaseDefXPAction(2500),
									new EquipItemAction("semos badge", 1, true),
									new IncreaseKarmaAction(10)));
									
				npc.add(ConversationStates.QUEST_ITEM_QUESTION,
				ConversationPhrases.NO_MESSAGES, 
				null,
				ConversationStates.ATTENDING, 
				null,
				new SayRequiredItemAction(QUEST_SLOT, "Return once you have [item] with you."));
	}			

	@Override
	public void addToWorld() {
		fillQuestInfo(
				"Semos Badge",
				"The Semos clerk will award you a badge if you assist the citizens.",
				true);
		
		checkCollectingQuests();
		requestItem();
		collectItem();

	}	
	
		@Override
	public String getName() {
		return "SemosBadgeQuest";
	}
	
	@Override
	public int getMinLevel() {
		return 0;
	}

	@Override
	public String getNPCName() {
		return "Urzędnik Semos";
	}
	
	@Override
	public String getRegion() {
		return Region.SEMOS_CITY;
	}
}
		
	        	