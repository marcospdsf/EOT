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

public class NalworBadgeQuest extends AbstractQuest {

	//this quest slot
	private static final String QUEST_SLOT = "nalwor_badge";
	// quest to retrieve boy's toy, quest giver: Plink
	private static final String FIRST_QUEST_SLOT = "elvish_armor";
	// quest to help Sato with growing his sheep_growing, quest giver: Sato
	private static final String SECOND_QUEST_SLOT = "kill_dark_elves";
	// quest to help Xoderos with feeding his hungry brother, quest giver: Xoderos
	private static final String THIRD_QUEST_SLOT = "grafindle_gold";
	
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
		res.add("Mogę dostać odznakę Nalwor");
		final String questState = player.getQuest(QUEST_SLOT);
		if (questState.equals("rejected")) {
			res.add("Nie chcę odznaki Nalwor");
			return res;
		}
		res.add("Zaakceptowałem zadanie i muszę pomóc mieszkańcom Nalwor.");
		if (!isCompleted(player)) {
			res.add("Żeby dostać odznakę Nalwor muszę przynieść urzędnikowi " + Grammar.a_noun(player.getRequiredItemName(QUEST_SLOT,0)) + ".");
		}
		if (isCompleted(player)) {
			res.add("Zostałem odznaczony odznaką Nalwor.");
		}
		return res;
	}
	
	private void checkCollectingQuests() {
		final SpeakerNPC npc = npcs.get("Urzędnik Nalwor");
		
		
		npc.add(
		ConversationStates.IDLE,
		ConversationPhrases.GREETING_MESSAGES,
			new AndCondition(new GreetingMatchesNameCondition(npc.getName()),
			new QuestNotStartedCondition(QUEST_SLOT)),
		ConversationStates.ATTENDING,
		"Witaj. Jestem urzędnikiem Nalwor. Zajmuje się sprawami urzędowymi i przyznaję #odznaki za Nalwor.",
		null);
		
		npc.add(ConversationStates.ATTENDING,
			Arrays.asList("odznaki", "zadanie", "medal", "odznakę"),
			new AndCondition(new QuestNotCompletedCondition(FIRST_QUEST_SLOT),
					new QuestNotStartedCondition(QUEST_SLOT)),
			ConversationStates.ATTENDING,
			"Zanim przyznam ci odznakę musisz dostarczyć Luposowi elficki ekwipunek.",
			null);
			
		npc.add(ConversationStates.ATTENDING,
			Arrays.asList("odznaki", "zadanie", "medal", "odznakę"),
			new AndCondition(new QuestNotCompletedCondition(SECOND_QUEST_SLOT),
					new QuestNotStartedCondition(QUEST_SLOT)),
			ConversationStates.ATTENDING,
			"Zanim przyznam ci odznakę musisz pomóc Maerionowi z elfami",
			null);	
					
		npc.add(ConversationStates.ATTENDING,
			Arrays.asList("odznaki", "zadanie", "medal", "odznakę"),
			new AndCondition(new QuestNotCompletedCondition(THIRD_QUEST_SLOT),
					new QuestNotStartedCondition(QUEST_SLOT)),
			ConversationStates.ATTENDING,
			"Zanim przyznam ci odznakę musisz pomóc ze złotem w banku.",
			null);
			
	}

	private void requestItem() {
		
		final SpeakerNPC npc = npcs.get("Urzędnik Nalwor");
		final Map<String,Integer> items = new HashMap<String, Integer>();
		
		
		
		items.put("zbroja elficka",1);
		items.put("spodnie elfickie",1);
		items.put("elvish shield",1);
		items.put("buty elfickie",1);
		items.put("hełm elficki",1);

		
		npc.add(ConversationStates.ATTENDING,
				Arrays.asList("odznaki", "zadanie", "medal", "odznakę"),
				new AndCondition(
						new QuestNotStartedCondition(QUEST_SLOT),
						new QuestCompletedCondition(SECOND_QUEST_SLOT),
						new QuestCompletedCondition(FIRST_QUEST_SLOT),
						new QuestCompletedCondition(THIRD_QUEST_SLOT)),
					ConversationStates.ATTENDING,
					null,
					new StartRecordingRandomItemCollectionAction(QUEST_SLOT, items, "Pomogłeś już wielu mieszkańcom Nalwor. Aby ostatecznie udowodnić mi, że jesteś oddany Nalwor,"
						+ " proszę przynieś [item]."));
	}
						
	private void collectItem() {
			final SpeakerNPC npc = npcs.get("Urzędnik Nalwor");
			
					npc.add(ConversationStates.IDLE,
				ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new GreetingMatchesNameCondition(npc.getName()),
						new QuestActiveCondition(QUEST_SLOT)),
				ConversationStates.QUEST_ITEM_QUESTION, 
				"Przyniosłeś przedmioty, o które cię prosiłem?",
				null);
				
			npc.add(ConversationStates.QUEST_ITEM_QUESTION,
				ConversationPhrases.YES_MESSAGES, 
				new AndCondition(new QuestActiveCondition(QUEST_SLOT),
								new NotCondition(new PlayerHasRecordedItemWithHimCondition(QUEST_SLOT))),
				ConversationStates.ATTENDING, 
				null,
				new SayRequiredItemAction(QUEST_SLOT, "Nie masz przy sobie [item]."));

					npc.add(ConversationStates.QUEST_ITEM_QUESTION,
				ConversationPhrases.YES_MESSAGES, 
				new AndCondition(new QuestActiveCondition(QUEST_SLOT),
								new PlayerHasRecordedItemWithHimCondition(QUEST_SLOT)),
				ConversationStates.ATTENDING, 
				"Dziękuje Ci w imieniu mieszkańców Nalwor. Oto Twoja odznaka.",
				new MultipleActions(new DropRecordedItemAction(QUEST_SLOT), 
									new SetQuestAction(QUEST_SLOT, "done"),
									new IncreaseXPAction(50000),
									new IncreaseAtkXPAction(10000),
									new IncreaseDefXPAction(10000),
									new EquipItemAction("odznaka nalwor", 1, true),
									new IncreaseKarmaAction(20)));
									
				npc.add(ConversationStates.QUEST_ITEM_QUESTION,
				ConversationPhrases.NO_MESSAGES, 
				null,
				ConversationStates.ATTENDING, 
				null,
				new SayRequiredItemAction(QUEST_SLOT, "Wróć kiedy będziesz mieć [item] ze sobą."));
	}			

	@Override
	public void addToWorld() {
		fillQuestInfo(
				"Odznaka Nalwor",
				"Urzędnik Nalwor wręczy ci odznake jeśli pomożesz mieszkańcom.",
				true);
		
		checkCollectingQuests();
		requestItem();
		collectItem();

	}	
	
		@Override
	public String getName() {
		return "NalworBadgeQuest";
	}
	
	@Override
	public int getMinLevel() {
		return 0;
	}

	@Override
	public String getNPCName() {
		return "Urzędnik Nalwor";
	}
	
	@Override
	public String getRegion() {
		return Region.SEMOS_CITY;
	}
}
		
	        	