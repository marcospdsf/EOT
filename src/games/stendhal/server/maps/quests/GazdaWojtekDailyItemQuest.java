/***************************************************************************
 *                   (C) Copyright 2003-2011 - Stendhal                    *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
//Na podstawie taska DailyItemQuest.
package games.stendhal.server.maps.quests;

import games.stendhal.common.grammar.Grammar;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.DropRecordedItemAction;
import games.stendhal.server.entity.npc.action.IncreaseKarmaAction;
import games.stendhal.server.entity.npc.action.IncreaseXPDependentOnLevelAction;
import games.stendhal.server.entity.npc.action.IncrementQuestAction;
import games.stendhal.server.entity.npc.action.MultipleActions;
import games.stendhal.server.entity.npc.action.SetQuestAction;
import games.stendhal.server.entity.npc.action.SetQuestToTimeStampAction;
import games.stendhal.server.entity.npc.action.StartRecordingRandomItemCollectionAction;
import games.stendhal.server.entity.npc.action.SayRequiredItemAction;
import games.stendhal.server.entity.npc.action.SayTimeRemainingAction;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.OrCondition;
import games.stendhal.server.entity.npc.condition.PlayerHasRecordedItemWithHimCondition;
import games.stendhal.server.entity.npc.condition.QuestActiveCondition;
import games.stendhal.server.entity.npc.condition.QuestCompletedCondition;
import games.stendhal.server.entity.npc.condition.QuestNotActiveCondition;
import games.stendhal.server.entity.npc.condition.QuestNotStartedCondition;
import games.stendhal.server.entity.npc.condition.TimePassedCondition;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.Region;
import games.stendhal.server.util.TimeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * QUEST: Daily Gazda Wojtek Item Fetch Quest.
 * <p>
 * PARTICIPANTS:
 * <li> Mayor of Zakopane
 * <li> some items
 * <p>
 * STEPS:
 * <li> talk to Mayor of Zakopane to get a quest to fetch an item
 * <li> bring the item to the mayor
 * <li> if you cannot bring it in one week he offers you the chance to fetch
 * another instead
 * <p>
 * REWARD:
 * <li> xp 
 * <li> 10 Karma
 * <p>
 * REPETITIONS:
 * <li> once a day
 */
public class GazdaWojtekDailyItemQuest extends AbstractQuest {

	private static final String QUEST_SLOT = "gazda_wojtek_daily_item";

	/** How long until the player can give up and start another quest */
	private static final int expireDelay = TimeUtil.MINUTES_IN_WEEK; 

	/** How often the quest may be repeated */
	private static final int delay = TimeUtil.MINUTES_IN_WEEK; 

	/**
	 * All items which are possible/easy enough to find. If you want to do
	 * it better, go ahead. *
	 * not to use yet, just getting it ready.
	 */
	private static Map<String,Integer> items;

		private static void buildItemsMap() {
		items = new HashMap<String, Integer>();
		items.put("wooden bolt", 10);
		items.put("steel bolt", 5);
		items.put("golden bolt", 25);
		items.put("flaming bolt", 10);
		items.put("woodland hatchet", 1);
		items.put("highlander walking axe", 1);
		items.put("empty waterskin", 5);
		items.put("water", 10);
		items.put("honey fungus", 10);
		items.put("goat cheese", 5);
		items.put("sausage", 10);
		items.put("barbarian helmet", 1);
		items.put("leather gloves", 1);
		items.put("leather enhanced gloves", 1);
		items.put("flame magic scroll", 10);
		items.put("earth magic scroll", 10);
		items.put("rain magic scroll", 10);
		items.put("dark magic scroll", 5);
		items.put("light magic scroll", 5);
		items.put("bandit leader feather", 5);
		items.put("lion pelt", 2);
		items.put("tiger pelt", 3);
		items.put("animal pelt", 10);
		items.put("dog bone", 20);
		items.put("small feather", 30);
		items.put("wolf fang", 20);
		items.put("tiger fang", 10);
		items.put("bear fang", 15);
		items.put("wolf claws", 10);
		items.put("tiger claws", 10);
		items.put("bear claws", 10);
		items.put("white tiger pelt", 10);
		items.put("xenocium hide", 2);
		items.put("silver ore", 10);
		items.put("silver bar", 1);
		items.put("amethyst crystal", 10);
		items.put("emerald crystal", 10);
		items.put("sapphire crystal", 10);
		items.put("ruby crystal", 10);
		items.put("obsidian crystal", 10);
		items.put("raw potatoes", 10);
		items.put("salt", 10);
		items.put("sulfur", 10);
		items.put("coal", 10);
		items.put("football", 1);
	}

	private ChatAction startQuestAction() {
		// common place to get the start quest actions as we can both starts it and abort and start again

		final List<ChatAction> actions = new LinkedList<ChatAction>();
		actions.add(new StartRecordingRandomItemCollectionAction(QUEST_SLOT,0,items,"Zakopane requires supplies. Please acquire [item] and say #done once you bring it."));
		actions.add(new SetQuestToTimeStampAction(QUEST_SLOT, 1));
		
		return new MultipleActions(actions);
	}
	
	private void getQuest() {
		final SpeakerNPC npc = npcs.get("Gazda Wojtek");
		npc.add(ConversationStates.ATTENDING, ConversationPhrases.QUEST_MESSAGES,
				new AndCondition(new QuestActiveCondition(QUEST_SLOT),
								 new NotCondition(new TimePassedCondition(QUEST_SLOT,1,expireDelay))), 
				ConversationStates.ATTENDING,
				null,
				new SayRequiredItemAction(QUEST_SLOT,0,"You have already been asked to bring [item]. Say #done once you bring it!"));

		npc.add(ConversationStates.ATTENDING, ConversationPhrases.QUEST_MESSAGES,
				new AndCondition(new QuestActiveCondition(QUEST_SLOT),
								 new TimePassedCondition(QUEST_SLOT,1,expireDelay)), 
				ConversationStates.ATTENDING,
				null,
				new SayRequiredItemAction(QUEST_SLOT,0,"You are searching for [item]. Say #done when you bring it. If you cannot find it, you may ask for #another item or bring what I originally requested."));

		npc.add(ConversationStates.ATTENDING, ConversationPhrases.QUEST_MESSAGES,
				new AndCondition(new QuestCompletedCondition(QUEST_SLOT),
								 new NotCondition(new TimePassedCondition(QUEST_SLOT,1,delay))), 
				ConversationStates.ATTENDING,
				null,
				new SayTimeRemainingAction(QUEST_SLOT,1, delay, "You can only receive one task per week. Please return in"));

		npc.add(ConversationStates.ATTENDING, ConversationPhrases.QUEST_MESSAGES,
				new OrCondition(new QuestNotStartedCondition(QUEST_SLOT),
								new AndCondition(new QuestCompletedCondition(QUEST_SLOT),
												 new TimePassedCondition(QUEST_SLOT,1,delay))), 
				ConversationStates.ATTENDING,
				null,
				startQuestAction());
	}

	private void completeQuest() {
		final SpeakerNPC npc = npcs.get("Gazda Wojtek");

		npc.add(ConversationStates.ATTENDING,
				ConversationPhrases.FINISH_MESSAGES, 
				new QuestNotStartedCondition(QUEST_SLOT),
				ConversationStates.ATTENDING, 
				"I am afraid I have not given you a #task yet.",
				null);

		npc.add(ConversationStates.ATTENDING,
				ConversationPhrases.FINISH_MESSAGES, 
				new QuestCompletedCondition(QUEST_SLOT),
				ConversationStates.ATTENDING, 
				"You have already completed the latest task I gave you.",
				null);

		final List<ChatAction> actions = new LinkedList<ChatAction>();
		actions.add(new DropRecordedItemAction(QUEST_SLOT,0));
		actions.add(new SetQuestToTimeStampAction(QUEST_SLOT, 1));
		actions.add(new IncrementQuestAction(QUEST_SLOT, 2, 1));
		actions.add(new SetQuestAction(QUEST_SLOT, 0, "done"));
		actions.add(new IncreaseXPDependentOnLevelAction(8, 90.0));
		actions.add(new IncreaseKarmaAction(15.0));

		npc.add(ConversationStates.ATTENDING,
				ConversationPhrases.FINISH_MESSAGES,
				new AndCondition(new QuestActiveCondition(QUEST_SLOT),
								 new PlayerHasRecordedItemWithHimCondition(QUEST_SLOT,0)),
				ConversationStates.ATTENDING, 
				"Great work! Allow me to thank you on behalf of the citizens of Zakopane!",
				new MultipleActions(actions));

		npc.add(ConversationStates.ATTENDING,
				ConversationPhrases.FINISH_MESSAGES,
				new AndCondition(new QuestActiveCondition(QUEST_SLOT),
								 new NotCondition(new PlayerHasRecordedItemWithHimCondition(QUEST_SLOT,0))),
				ConversationStates.ATTENDING, 
				null,
				new SayRequiredItemAction(QUEST_SLOT,0,"You have not brought [item] yet. Go find it, then return and say #done when finished."));

	}

	private void abortQuest() {
		final SpeakerNPC npc = npcs.get("Gazda Wojtek");

		npc.add(ConversationStates.ATTENDING,
				ConversationPhrases.ABORT_MESSAGES,
				new AndCondition(new QuestActiveCondition(QUEST_SLOT),
						 		 new TimePassedCondition(QUEST_SLOT,1,expireDelay)), 
				ConversationStates.ATTENDING, 
				null, 
				// start quest again immediately
				startQuestAction());

		npc.add(ConversationStates.ATTENDING,
				ConversationPhrases.ABORT_MESSAGES,
				new AndCondition(new QuestActiveCondition(QUEST_SLOT),
						 		 new NotCondition(new TimePassedCondition(QUEST_SLOT,1,expireDelay))), 
				ConversationStates.ATTENDING, 
				"Not enough time has passed since you took this task. I will not let you give up so easily.", 
				null);

		npc.add(ConversationStates.ATTENDING,
				ConversationPhrases.ABORT_MESSAGES,
				new QuestNotActiveCondition(QUEST_SLOT),
				ConversationStates.ATTENDING, 
				"I am afraid I have not given you a #task yet.", 
				null);

	}

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
		res.add("I met Gazda Wojtek in Zakopane.");
		final String questState = player.getQuest(QUEST_SLOT);
		if ("rejected".equals(questState)) {
			res.add("I declined to help Zakopane.");
			return res;
		}

		res.add("I want to assist the citizens of Zakopane.");
		if (player.hasQuest(QUEST_SLOT) && !player.isQuestCompleted(QUEST_SLOT)) {
			String questItem = player.getRequiredItemName(QUEST_SLOT,0);
			int amount = player.getRequiredItemQuantity(QUEST_SLOT,0);
			if (!player.isEquipped(questItem, amount)) {
				res.add("I was asked to bring " + Grammar.quantityplnoun(amount, questItem, "") + " to assist Zakopane. I do not have it yet.");
			} else {
				res.add("I found " + Grammar.quantityplnoun(amount, questItem, "") + " to help Zakopane and need to deliver them.");
			}
		}
		int repetitions = player.getNumberOfRepetitions(getSlotName(), 2);
		if (repetitions > 0) {
			res.add("I helped Zakopane with deliveries " + Grammar.quantityplnoun(repetitions, "time", "times") + " so far.");
		}
		if (isRepeatable(player)) {
			res.add("I delivered the last item to Gazda Wojtek and now Zakopane needs supplies again.");
		} else if (isCompleted(player)){
			res.add("I delivered the last item to Gazda Wojtek and claimed my reward recently.");
		}
		return res;
	}

	@Override
	public void addToWorld() {
		fillQuestInfo(
		"Gazda Wojtek's Daily Item Quest",
		"Gazda Wojtek needs item supplies delivered to Zakopane.",
		true);

		buildItemsMap();

		getQuest();
		completeQuest();
		abortQuest();
	}

	@Override
	public String getName() {
		return "GazdaWojtekDailyItemQuest";
	}

	@Override
	public int getMinLevel() {
		return 0;
	}

	@Override
	public boolean isRepeatable(final Player player) {
		return new AndCondition(new QuestCompletedCondition(QUEST_SLOT),
								new TimePassedCondition(QUEST_SLOT,1,delay)).fire(player, null, null);
	}
	
	@Override
	public String getRegion() {
		return Region.ZAKOPANE_CITY;
	}

	@Override
	public String getNPCName() {
		return "Gazda Wojtek";
	}
}
