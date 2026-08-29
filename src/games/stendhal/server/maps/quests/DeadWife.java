/* $Id: HerbsForCarmen.java,v 1.21 2011/11/13 17:13:16 kymara edited by szyg Exp $ */
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
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.QuestActiveCondition;
import games.stendhal.server.entity.npc.condition.QuestInStateCondition;
import games.stendhal.server.entity.npc.condition.QuestNotStartedCondition;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.Region;
import games.stendhal.server.util.ItemCollection;


public class DeadWife extends AbstractQuest {

	public static final String QUEST_SLOT = "dead_wife";

	/**
	 * required items for the quest.
	 */
	protected static final String NEEDED_ITEMS = "blood robe=1;blood ring=1";

	@Override
	public List<String> getHistory(final Player player) {
		final List<String> res = new ArrayList<String>();
		if (!player.hasQuest(QUEST_SLOT)) {
			return res;
		}
		res.add("Adalbert, a widower in Kirdneh, asked me to recover his late wife's belongings from the Semos mountains.");
		final String questState = player.getQuest(QUEST_SLOT);
		if ("rejected".equals(questState)) {
			res.add("I refused to help Adalbert.");
		} else if (!"done".equals(questState)) {
			final ItemCollection missingItems = new ItemCollection();
			missingItems.addFromQuestStateString(questState);
			res.add("I still need to bring him: " + Grammar.enumerateCollection(missingItems.toStringList()) + ".");
		} else {
			res.add("I brought the keepsakes to Adalbert and received a blood ring as a reward.");
		}
		return res;
	}

	private void prepareRequestingStep() {
		final SpeakerNPC npc = npcs.get("Adalbert");

		npc.add(ConversationStates.ATTENDING, 
				ConversationPhrases.QUEST_MESSAGES,
			new AndCondition(
					new LevelGreaterThanCondition(2),
					new QuestNotStartedCondition(QUEST_SLOT),
					new NotCondition(new QuestInStateCondition(QUEST_SLOT,"rejected"))),
			ConversationStates.QUESTION_1, 
			"Hey, are you looking for a #quest?", null);

		npc.add(ConversationStates.ATTENDING, 
			ConversationPhrases.QUEST_MESSAGES,
			new QuestInStateCondition(QUEST_SLOT,"rejected"),
			ConversationStates.QUEST_OFFERED, 
			"Hey, do you want to #help me?", null);

		npc.add(
			ConversationStates.QUESTION_1,
			ConversationPhrases.YES_MESSAGES,
			new QuestNotStartedCondition(QUEST_SLOT),
			ConversationStates.ATTENDING,
			"Would you like to #help me recover my late wife's belongings?",
			null);

		npc.add(
			ConversationStates.QUESTION_1,
			ConversationPhrases.NO_MESSAGES,
			new QuestNotStartedCondition(QUEST_SLOT),
			ConversationStates.ATTENDING,
			"I am Adalbert, a widower. My wife perished in the Semos mountains and I have no keepsake left. Perhaps you could #help me?",
			null);

		npc.add(
			ConversationStates.ATTENDING,
			Arrays.asList("wife", "help", "quest", "żona", "pomóc", "zadanie"),
			new QuestNotStartedCondition(QUEST_SLOT),
			ConversationStates.QUEST_OFFERED,
			"My wife journeyed into the Semos mountains and was slain by ghosts. Could you bring back the belongings left behind?",
			null);

		npc.add(
			ConversationStates.QUEST_OFFERED,
			ConversationPhrases.YES_MESSAGES,
			null,
			ConversationStates.ATTENDING,
			null,
			new MultipleActions(new SetQuestAndModifyKarmaAction(QUEST_SLOT, NEEDED_ITEMS, 5.0),
								new SayRequiredItemsFromCollectionAction(QUEST_SLOT, "Thank you! Please bring me my wife's belongings: [items].")));

		npc.add(
			ConversationStates.QUEST_OFFERED,
			ConversationPhrases.NO_MESSAGES,
			null,
			ConversationStates.ATTENDING,
			"...",
			new SetQuestAndModifyKarmaAction(QUEST_SLOT, "rejected", -5.0));

	}

	private void prepareBringingStep() {
		final SpeakerNPC npc = npcs.get("Adalbert");
	
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new GreetingMatchesNameCondition(npc.getName()),
						new QuestActiveCondition(QUEST_SLOT)),
				ConversationStates.ATTENDING,
				"Greetings. Do you have what I #asked for?",
				null);

		/* player asks what exactly is missing (says ingredients) */
		npc.add(ConversationStates.ATTENDING, Arrays.asList("asked", "prosiłem"), null,
				ConversationStates.QUESTION_2, null,
				new SayRequiredItemsFromCollectionAction(QUEST_SLOT, "I need [items]. Do you happen to have any of those items with you?"));

		npc.add(ConversationStates.ATTENDING, 
				ConversationPhrases.QUEST_MESSAGES,
				new QuestActiveCondition(QUEST_SLOT),
			ConversationStates.QUESTION_2, 
			null, new SayRequiredItemsFromCollectionAction(QUEST_SLOT, "I need [items]. Do you have them?"));		

		/* player says he has a required item with him (says yes) */
		npc.add(ConversationStates.QUESTION_2,
				ConversationPhrases.YES_MESSAGES, null,
				ConversationStates.QUESTION_2, "Thank you! Do you have anything else?",
				null);

		ChatAction completeAction = new  MultipleActions(
				new SetQuestAction(QUEST_SLOT, "done"),
				new SayTextAction("Thank you so much! Please accept this humble gift, passed down to me from my father..."),
				new IncreaseXPAction(25000),
				new IncreaseAtkXPAction(5000),
				new IncreaseDefXPAction(5000),
				new IncreaseKarmaAction(10),
				new EquipItemAction("blood ring", 1)
				);

		/* add triggers for the item names */
		final ItemCollection items = new ItemCollection();
		items.addFromQuestStateString(NEEDED_ITEMS);
		for (final Map.Entry<String, Integer> entry : items.entrySet()) {
			String itemName = entry.getKey();

			String singular = Grammar.singular(itemName);
			List<String> sl = new ArrayList<String>();
			sl.add(itemName);

			// handle the porcino/porcini singular/plural case with item name "borowik"
			if (!singular.equals(itemName)) {
				sl.add(singular);
			}
			// also allow to understand the misspelled "porcinis"
			if (itemName.equals("blood robe")) {
				sl.add("blood robe");
			}

			npc.add(ConversationStates.QUESTION_2, sl, null,
					ConversationStates.QUESTION_2, null,
					new CollectRequestedItemsAction(
							itemName, QUEST_SLOT,
							"Great, do you have anything else?"," " +
							Grammar.quantityplnoun(entry.getValue(), itemName) + " you already brought to me, but thank you anyway.",
							completeAction, ConversationStates.ATTENDING));
		}

		/* player says he didn't bring any items (says no) */
		npc.add(ConversationStates.ATTENDING, ConversationPhrases.NO_MESSAGES,
				new QuestActiveCondition(QUEST_SLOT),
				ConversationStates.ATTENDING,
				"Alright, let me know if I can ever #help you.", 
				null);

		/* player says he didn't bring any items to different question */
		npc.add(ConversationStates.QUESTION_2,
				ConversationPhrases.NO_MESSAGES,
				new QuestActiveCondition(QUEST_SLOT),
				ConversationStates.ATTENDING,
				"Let me know if I can help with anything else.", null);

    /* says quest and quest can't be started nor is active*/
		npc.add(ConversationStates.ATTENDING, 
				ConversationPhrases.QUEST_MESSAGES,
				null,
			    ConversationStates.ATTENDING, 
			    "Thank you for your help!",
			    null);
	}
	
	@Override
	public void addToWorld() {
		//super.addToWorld();
		fillQuestInfo(
				"Belongings for Adalbert",
				"",
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
		return "DeadWife";
	}

	public String getTitle() {
		
		return "Dead Wife";
	}
	
	@Override
	public int getMinLevel() {
		return 3;
	}
	
	@Override
	public String getRegion() {
		return Region.KIRDNEH;
	}

	@Override
	public String getNPCName() {
		return "Adalbert";
	}
}
