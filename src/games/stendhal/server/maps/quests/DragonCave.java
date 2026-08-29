/* $Id: DragonCave.java,v 1.21 2011/11/13 17:13:16 kymara Exp $ */
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
import java.util.List;
import java.util.Map;

import games.stendhal.common.grammar.Grammar;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.CollectRequestedItemsAction;
import games.stendhal.server.entity.npc.action.EquipItemAction;
import games.stendhal.server.entity.npc.action.IncreaseAtkXPAction;
import games.stendhal.server.entity.npc.action.IncreaseDefXPAction;
import games.stendhal.server.entity.npc.action.IncreaseKarmaAction;
import games.stendhal.server.entity.npc.action.IncreaseXPAction;
import games.stendhal.server.entity.npc.action.MultipleActions;
import games.stendhal.server.entity.npc.action.SayRequiredItemsFromCollectionAction;
import games.stendhal.server.entity.npc.action.SayTextAction;
import games.stendhal.server.entity.npc.action.SetQuestAction;
import games.stendhal.server.entity.npc.action.SetQuestAndModifyKarmaAction;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.GreetingMatchesNameCondition;
import games.stendhal.server.entity.npc.condition.LevelGreaterThanCondition;
import games.stendhal.server.entity.npc.condition.LevelLessThanCondition;
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.QuestActiveCondition;
import games.stendhal.server.entity.npc.condition.QuestInStateCondition;
import games.stendhal.server.entity.npc.condition.QuestNotStartedCondition;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.Region;
import games.stendhal.server.util.ItemCollection;

/**
 * QUEST: Dragon Cave
 *
 * author: davvids
 */
public class DragonCave extends AbstractQuest {
	
	private static final int REQUIRED_LEVEL = 100;

	public static final String QUEST_SLOT = "dragon_cave";

	/**
	 * required items for the quest.
	 */
	protected static final String NEEDED_ITEMS = "green dragon leather=2;blue dragon leather=1;emerald=5;sapphire=3;bone dragon cloak=1";

	@Override
	public List<String> getHistory(final Player player) {
		final List<String> res = new ArrayList<String>();
		if (!player.hasQuest(QUEST_SLOT)) {
			return res;
		}
		res.add("I need to bring Terry a few dragon-related items so he will give me the key to the dragon cave.");
		final String questState = player.getQuest(QUEST_SLOT);
		if ("rejected".equals(questState)) {
			res.add("I do not need the dragon cave key.");
		} else if (!"done".equals(questState)) {
			final ItemCollection missingItems = new ItemCollection();
			missingItems.addFromQuestStateString(questState);
			res.add("I still need to bring Terry " + Grammar.enumerateCollection(missingItems.toStringList()) + ".");
		} else {
			res.add("I brought Terry all the items he requested.");
		}
		return res;
	}

	private void prepareRequestingStep() {
		final SpeakerNPC npc = npcs.get("Terry");

		npc.add(ConversationStates.ATTENDING, 
				ConversationPhrases.QUEST_MESSAGES,
			new AndCondition(
					new LevelGreaterThanCondition(2),
					new QuestNotStartedCondition(QUEST_SLOT),
					new NotCondition(new QuestInStateCondition(QUEST_SLOT,"rejected"))),
			ConversationStates.QUESTION_1, 
			"Hey you! Yes, I'm talking to you! Could you help me out?", null);

		npc.add(ConversationStates.ATTENDING, 
			ConversationPhrases.QUEST_MESSAGES,
			new QuestInStateCondition(QUEST_SLOT,"rejected"),
			ConversationStates.QUEST_OFFERED, 
			"Hey, do you want to help me after all?", null);

		npc.add(
			ConversationStates.QUESTION_1,
			ConversationPhrases.YES_MESSAGES,
			new QuestNotStartedCondition(QUEST_SLOT),
			ConversationStates.ATTENDING,
			"Great! I am fascinated by everything dragon-related. I need a few #items for my collection. If you bring them, I'll give you a dragon cave key.",
			null);

		npc.add(
			ConversationStates.QUESTION_1,
			ConversationPhrases.NO_MESSAGES,
			new QuestNotStartedCondition(QUEST_SLOT),
			ConversationStates.ATTENDING,
			"I'm Terry. I study dragons. If you change your mind and bring me some #items for my collection, I'll give you the key to the real dragon cave.",
			null);

		npc.add(
			ConversationStates.ATTENDING,
			Arrays.asList("things", "items", "przedmiotów", "rzeczy"),
			new QuestNotStartedCondition(QUEST_SLOT),
			ConversationStates.QUEST_OFFERED,
			"Yes... I need a few items for my collection. Can you fetch them for me?",
			null);

		npc.add(
			ConversationStates.QUEST_OFFERED,
			ConversationPhrases.YES_MESSAGES,
			null,
			ConversationStates.ATTENDING,
			null,
			new MultipleActions(new SetQuestAndModifyKarmaAction(QUEST_SLOT, NEEDED_ITEMS, 5.0),
								new SayRequiredItemsFromCollectionAction(QUEST_SLOT, "Wonderful! Please bring me these items: [items].")));

		npc.add(
			ConversationStates.QUEST_OFFERED,
			ConversationPhrases.NO_MESSAGES,
			null,
			ConversationStates.ATTENDING,
			"Ah well... That's too bad. I will look for someone else.",
			new SetQuestAndModifyKarmaAction(QUEST_SLOT, "rejected", -5.0));

		npc.add(
			ConversationStates.ATTENDING,
			"emerald",
			null,
			ConversationStates.ATTENDING,
			"Emeralds are valuable green gems. Try searching for them among green dragons.",
			null);

		npc.add(
			ConversationStates.ATTENDING,
			"sapphire",
			null,
			ConversationStates.ATTENDING,
			"Sapphires are beautiful blue crystals. Try searching for them among blue dragons.",
			null);

		npc.add(
			ConversationStates.ATTENDING,
			Arrays.asList("green dragon leather", "blue dragon leather", "dragon leather"),
			null,
			ConversationStates.ATTENDING,
			"Naturally, you can skin leather from slain dragons.",
			null);

		npc.add(
			ConversationStates.ATTENDING,
			"bone dragon cloak",
			null,
			ConversationStates.ATTENDING,
			"I'm not sure where to find it. It is very rare, which is why I want it in my collection.",
			null);

	}

	private void prepareBringingStep() {
		final SpeakerNPC npc = npcs.get("Terry");
	
		npc.add(ConversationStates.INFORMATION_1, 
			Arrays.asList("things", "items", "quest", "rzeczy", "przedmioty", "zadanie"),
			new AndCondition(
					new QuestNotStartedCondition(QUEST_SLOT), 
					new LevelLessThanCondition(REQUIRED_LEVEL)),
			ConversationStates.ATTENDING, 
			"You are too inexperienced to face dragons. Return when you are stronger!",
			null);
			
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new GreetingMatchesNameCondition(npc.getName()),
						new QuestActiveCondition(QUEST_SLOT)),
				ConversationStates.ATTENDING,
				"Welcome back. Speak with me when you have brought all the #items.",
				null);

				
		/* player asks what exactly is missing */
		npc.add(ConversationStates.ATTENDING, Arrays.asList("things", "items", "rzeczy"), null,
				ConversationStates.QUESTION_2, null,
				new SayRequiredItemsFromCollectionAction(QUEST_SLOT, "I need [items]. Do you have anything for me?"));

		npc.add(ConversationStates.ATTENDING, 
				ConversationPhrases.QUEST_MESSAGES,
				new QuestActiveCondition(QUEST_SLOT),
			ConversationStates.QUESTION_2, 
			null, new SayRequiredItemsFromCollectionAction(QUEST_SLOT, "I need [items]. Do you have them?"));		

		/* player says he has a required item with him (says yes) */
		npc.add(ConversationStates.QUESTION_2,
				ConversationPhrases.YES_MESSAGES, null,
				ConversationStates.QUESTION_2, "Great, what did you bring?",
				null);

		ChatAction completeAction = new  MultipleActions(
				new SetQuestAction(QUEST_SLOT, "done"),
				new SayTextAction("Wonderful! I have everything I needed! My collection is magnificent! As promised, here is your dragon cave key. Beware, the dragons within are deadly!"),
				new IncreaseXPAction(10000),
				new IncreaseAtkXPAction(5000),
				new IncreaseDefXPAction(5000),
				new IncreaseKarmaAction(20),
				new EquipItemAction("dragon cave key", 1)
				);

		/* add triggers for the item names */
		final ItemCollection items = new ItemCollection();
		items.addFromQuestStateString(NEEDED_ITEMS);
		for (final Map.Entry<String, Integer> entry : items.entrySet()) {
			String itemName = entry.getKey();

			String singular = Grammar.singular(itemName);
			List<String> sl = new ArrayList<String>();
			sl.add(itemName);

			if (!singular.equals(itemName)) {
				sl.add(singular);
			}

			npc.add(ConversationStates.QUESTION_2, sl, null,
					ConversationStates.QUESTION_2, null,
					new CollectRequestedItemsAction(
							itemName, QUEST_SLOT,
							"Great, do you have anything else?"," You have already brought enough " +
							Grammar.quantityplnoun(entry.getValue(), itemName) + ", but thank you anyway.",
							completeAction, ConversationStates.ATTENDING));
		}

		/* player says he didn't bring any items (says no) */
		npc.add(ConversationStates.ATTENDING, ConversationPhrases.NO_MESSAGES,
				new QuestActiveCondition(QUEST_SLOT),
				ConversationStates.ATTENDING,
				"Alright, let me know if you need help with anything else.", 
				null);

		/* player says he didn't bring any items to different question */
		npc.add(ConversationStates.QUESTION_2,
				ConversationPhrases.NO_MESSAGES,
				new QuestActiveCondition(QUEST_SLOT),
				ConversationStates.ATTENDING,
				"Alright, let me know when you find more items.", null);

		/* says quest and quest can't be started nor is active*/
		npc.add(ConversationStates.ATTENDING, 
				ConversationPhrases.QUEST_MESSAGES,
				null,
			    ConversationStates.ATTENDING, 
			    "I do not need anything right now, thank you.",
			    null);
	}
	
	@Override
	public void addToWorld() {
		fillQuestInfo(
				"Terry's Dragon Collection",
				"Terry promised to give me a dragon cave key if I bring items for his collection.",
				true);
		prepareRequestingStep();
		prepareBringingStep();
	}

	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}

	@Override
	public String getName() {
		return "DragonCave";
	}

	public String getTitle() {
		
		return "Dragon Cave";
	}
	
	@Override
	public int getMinLevel() {
		return 100;
	}
	
	@Override
	public String getRegion() {
		return Region.SEMOS_CITY;
	}

	@Override
	public String getNPCName() {
		return "Terry";
	}
}
