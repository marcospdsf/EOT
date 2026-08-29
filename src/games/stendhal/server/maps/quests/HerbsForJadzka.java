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
// Based on Herbs For Carmen 

package games.stendhal.server.maps.quests;

import games.stendhal.common.grammar.Grammar;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.CollectRequestedItemsAction;
import games.stendhal.server.entity.npc.action.EquipItemAction;
import games.stendhal.server.entity.npc.action.ExamineChatAction;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * QUEST: Herbs For Jadzka
 * 
 * PARTICIPANTS:
 * <ul>
 * <li>Carmen (the healer in Zakopane)</li>
 * </ul>
 * 
 * STEPS:
 * <ul>
 * <li>Jadzka introduces herself and asks for some items to help her heal people.</li>
 * <li>You collect the items.</li>
 * <li>Jadzka sees yours items, asks for them then thanks you.</li>
 * </ul>
 * 
 * REWARD:
 * <ul>
 * <li>50 XP</li>
 * <li>4 antidote</li>
 * <li>Karma: 10</li>
 * </ul>
 * 
 * REPETITIONS:
 * <ul>
 * <li>None</li>
 * </ul>
 */
public class HerbsForJadzka extends AbstractQuest {

	public static final String QUEST_SLOT = "herbs_for_jadzka";

	/**
	 * required items for the quest.
	 */
	protected static final String NEEDED_ITEMS = "arandula=5;porcino=1;apple=3;wood=2;mushroom=1";

	@Override
	public List<String> getHistory(final Player player) {
		final List<String> res = new ArrayList<String>();
		if (!player.hasQuest(QUEST_SLOT)) {
			return res;
		}
		res.add("Gaździna Jadźka asked me to gather healing ingredients so she can brew medicines for the injured.");
		final String questState = player.getQuest(QUEST_SLOT);
		if ("rejected".equals(questState)) {
			res.add("I declined to help Gaździna Jadźka.");
		} else if (!"done".equals(questState)) {
			final ItemCollection missingItems = new ItemCollection();
			missingItems.addFromQuestStateString(questState);
			res.add("I still need to bring Gaździna Jadźka " + Grammar.enumerateCollection(missingItems.toStringList()) + ".");
		} else {
			res.add("I brought all ingredients to Gaździna Jadźka so she can heal the townspeople.");
		}
		return res;
	}

	private void prepareRequestingStep() {
		final SpeakerNPC npc = npcs.get("Gaździna Jadźka");

		npc.add(ConversationStates.ATTENDING, 
				ConversationPhrases.QUEST_MESSAGES,
			new AndCondition(
					new LevelGreaterThanCondition(2),
					new QuestNotStartedCondition(QUEST_SLOT),
					new NotCondition(new QuestInStateCondition(QUEST_SLOT,"rejected"))),
			ConversationStates.QUESTION_1, 
			"Hey you! Yes, I'm talking to you! Do you know who I am?", null);

		npc.add(ConversationStates.ATTENDING, 
			ConversationPhrases.QUEST_MESSAGES,
			new QuestInStateCondition(QUEST_SLOT,"rejected"),
			ConversationStates.QUEST_OFFERED, 
			"Hey, would you like to #help me?", null);

		npc.add(
			ConversationStates.QUESTION_1,
			ConversationPhrases.YES_MESSAGES,
			new QuestNotStartedCondition(QUEST_SLOT),
			ConversationStates.ATTENDING,
			"Great, then you know my craft. My stores of healing #herbs are running dangerously low.",
			null);

		npc.add(
			ConversationStates.QUESTION_1,
			ConversationPhrases.NO_MESSAGES,
			new QuestNotStartedCondition(QUEST_SLOT),
			ConversationStates.ATTENDING,
			"I am Gaździna Jadźka. I provide free healing to travelers whose wounds are not overly severe. So many warriors seek my aid that my stores of #herbs are running out.",
			null);

		npc.add(
			ConversationStates.ATTENDING,(Arrays.asList
			("ingredients", "leczniczych", "lecznicze")),
			new QuestNotStartedCondition(QUEST_SLOT),
			ConversationStates.QUEST_OFFERED,
			"So many adventurers ask for my help, and brewing remedies requires fresh ingredients. Could you bring me the missing herbs?",
			null);

		npc.add(
			ConversationStates.QUEST_OFFERED,
			ConversationPhrases.YES_MESSAGES,
			null,
			ConversationStates.ATTENDING,
			null,
			new MultipleActions(new SetQuestAndModifyKarmaAction(QUEST_SLOT, NEEDED_ITEMS, 5.0),
								new SayRequiredItemsFromCollectionAction(QUEST_SLOT, "How wonderful! Please bring me: [items].")));

		npc.add(
			ConversationStates.QUEST_OFFERED,
			ConversationPhrases.NO_MESSAGES,
			null,
			ConversationStates.ATTENDING,
			"Hmph... So be it. Remember though, when I am forced to turn away injured travelers, I will have to tell them it was because of your refusal!",
			new SetQuestAndModifyKarmaAction(QUEST_SLOT, "rejected", -5.0));

		npc.add(
			ConversationStates.ATTENDING,
			"jabłko",
			null,
			ConversationStates.ATTENDING,
			"Apples are packed with nutrients. They grow east of Semos and in fruit orchards.",
			null);

		npc.add(
			ConversationStates.ATTENDING,
			"wood",
			null,
			ConversationStates.ATTENDING,
			"Wood is a versatile material. You can chop down trees in the forest to gather timber.",
			null);

		npc.add(
			ConversationStates.ATTENDING,
			Arrays.asList("pieczarka","borowik"),
			null,
			ConversationStates.ATTENDING,
			"I have seen entire clearings filled with mushrooms in the mountains of Zakopane.",
			null);

		npc.add(
			ConversationStates.ATTENDING,
			"arandula",
			null,
			ConversationStates.ATTENDING,
			"North of Semos near the groves grows the arandula herb. Here is a sketch showing what it looks like.",
			new ExamineChatAction("arandula.png", "Jadźka's Sketch", "Arandula"));

	}

	private void prepareBringingStep() {
		final SpeakerNPC npc = npcs.get("Gaździna Jadźka");

		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new GreetingMatchesNameCondition(npc.getName()),
						new QuestActiveCondition(QUEST_SLOT)),
				ConversationStates.ATTENDING,
				"Welcome back! Did you bring the requested healing #herbs?",
				null);

		/* player asks what exactly is missing (says ingredients) */
		npc.add(ConversationStates.ATTENDING, "zapasy", null,
				ConversationStates.QUESTION_2, null,
				new SayRequiredItemsFromCollectionAction(QUEST_SLOT, "I need [items]. Do you have any of them with you?"));

		npc.add(ConversationStates.ATTENDING, 
				ConversationPhrases.QUEST_MESSAGES,
				new QuestActiveCondition(QUEST_SLOT),
			ConversationStates.QUESTION_2, 
			null, new SayRequiredItemsFromCollectionAction(QUEST_SLOT, "I need [items]. Do you have them?"));		

		/* player says he has a required item with him (says yes) */
		npc.add(ConversationStates.QUESTION_2,
				ConversationPhrases.YES_MESSAGES, null,
				ConversationStates.QUESTION_2, "Dobrze, a co jeszcze masz?",
				null);

		ChatAction completeAction = new  MultipleActions(
				new SetQuestAction(QUEST_SLOT, "done"),
				new SayTextAction("Wonderful! Now I can continue tending to wounded travelers without charge! Thank you, please accept this token of appreciation for your efforts."),
				new IncreaseXPAction(50),
				new IncreaseKarmaAction(5),
				new EquipItemAction("antidote", 7)
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
			if (itemName.equals("borowik")) {
				sl.add("borowik");
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
				"Alright, let me know if I can #help with anything else.", null);

    /* says quest and quest can't be started nor is active*/
		npc.add(ConversationStates.ATTENDING, 
				ConversationPhrases.QUEST_MESSAGES,
				null,
				ConversationStates.ATTENDING, 
				"I don't need anything right now, thank you.",
				null);
	}

	@Override
	public void addToWorld() {
		fillQuestInfo(
				"Herbs for Jadźka",
				"The healer Gaździna Jadźka in Zakopane seeks ingredients to brew medicines and antidotes. Can you bring her what she needs?",
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
		return "HerbsForJadzka";
	}

	public String getTitle() {
		
		return "Zioła dla Jadzki";
	}

	@Override
	public int getMinLevel() {
		return 3;
	}
	
	@Override
	public String getRegion() {
		return Region.ZAKOPANE_CITY;
	}
	@Override
	public String getNPCName() {
		return "Gaździna Jadźka";
	}
}
