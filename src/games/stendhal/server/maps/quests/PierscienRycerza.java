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
// Based on UltimateCollector
package games.stendhal.server.maps.quests;

import games.stendhal.common.parser.Sentence;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.DropItemAction;
import games.stendhal.server.entity.npc.action.EquipItemAction;
import games.stendhal.server.entity.npc.action.DropRecordedItemAction;
import games.stendhal.server.entity.npc.action.IncreaseXPAction;
import games.stendhal.server.entity.npc.action.MultipleActions;
import games.stendhal.server.entity.npc.action.SayTimeRemainingAction;
import games.stendhal.server.entity.npc.action.SetQuestAction;
import games.stendhal.server.entity.npc.action.StartRecordingRandomItemCollectionAction;
import games.stendhal.server.entity.npc.action.SetQuestAndModifyKarmaAction;
import games.stendhal.server.entity.npc.action.SayRequiredItemAction;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.OrCondition;
import games.stendhal.server.entity.npc.condition.QuestInStateCondition;
import games.stendhal.server.entity.npc.condition.PlayerHasItemWithHimCondition;
import games.stendhal.server.entity.npc.condition.PlayerHasRecordedItemWithHimCondition;
import games.stendhal.server.entity.npc.condition.QuestActiveCondition;
import games.stendhal.server.entity.npc.condition.QuestCompletedCondition;
import games.stendhal.server.entity.npc.condition.QuestInStateCondition;
import games.stendhal.server.entity.npc.condition.QuestNotCompletedCondition;
import games.stendhal.server.entity.npc.condition.QuestNotStartedCondition;
import games.stendhal.server.entity.player.Player;


import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

	public class PierscienRycerza extends AbstractQuest {

	private static final String QUEST_SLOT = "pierscien_rycerza";

	private static final String MITHRILSHIELD_QUEST_SLOT = "mithrilshield_quest";  

	private static Logger logger = Logger.getLogger(PierscienRycerza.class);

	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}

	private void checkLevelHelm() {
		final SpeakerNPC npc = npcs.get("Edgard");

		npc.add(ConversationStates.ATTENDING,
			ConversationPhrases.QUEST_MESSAGES, null,
			ConversationStates.QUEST_OFFERED, null,
			new ChatAction() {
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					if (player.isBadBoy()){ 
						raiser.say("You have slain a knight! You have no business here until you rid yourself of the skull mark. Out of my sight!");
					} else {
							if (player.getLevel() >= 250) {
								if (!player.hasQuest(QUEST_SLOT) || "rejected".equals(player.getQuest(QUEST_SLOT))) {
									raiser.say("Do you seek to earn the knight ring? Are you interested?");
								} else if (player.isQuestCompleted(QUEST_SLOT)) {
									raiser.say("If you came for another ring, forget it. Do not disturb my flight training.");
									raiser.setCurrentState(ConversationStates.ATTENDING);
								}
							} else  {
								npc.say("Your social standing is too low for this task. Return when you reach level 250.");
								raiser.setCurrentState(ConversationStates.ATTENDING);
							}
					}
				}
			}); 

		npc.add(ConversationStates.QUEST_OFFERED,
			ConversationPhrases.YES_MESSAGES, null,
			ConversationStates.ATTENDING, null,
			new ChatAction() {
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					raiser.say("First I will check if you completed prerequisite tasks before giving you the #list of required items.");
					player.addKarma(10);
				}
			});

		npc.add(ConversationStates.QUEST_OFFERED,
			ConversationPhrases.NO_MESSAGES, null,
			ConversationStates.IDLE,
			"As you wish.",
			new SetQuestAndModifyKarmaAction(QUEST_SLOT, "rejected", -10.0));

	}

	private void checkCollectingQuests() {
		final SpeakerNPC npc = npcs.get("Edgard");

		npc.add(
			ConversationStates.IDLE,
			ConversationPhrases.GREETING_MESSAGES,
			new AndCondition(new QuestCompletedCondition(MITHRILSHIELD_QUEST_SLOT),
					 new QuestNotStartedCondition(QUEST_SLOT)),
			ConversationStates.ATTENDING,
			"Greetings, friend. Would you like the #quest for the knight ring?",
			null);

		npc.add(ConversationStates.ATTENDING,
			Arrays.asList("equipment", "list", "wyposażenie", "wyposazenie", "listę"), 
			new AndCondition(
					 new QuestNotStartedCondition(QUEST_SLOT),
					 new QuestNotCompletedCondition(MITHRILSHIELD_QUEST_SLOT)),
			ConversationStates.ATTENDING,
			"First complete the quest for the mithril shield, then we can talk about the ring.",
			null);
	}

	private void requestItem() {
		final SpeakerNPC npc = npcs.get("Edgard");

		npc.add(ConversationStates.ATTENDING,
				Arrays.asList("equipment", "list", "wyposażenie", "wyposazenie", "listę"),
				new AndCondition(
						new QuestNotStartedCondition(QUEST_SLOT),
						new QuestCompletedCondition(MITHRILSHIELD_QUEST_SLOT)),
					ConversationStates.ATTENDING, "You will earn the ring by bringing me the required #items.",
					new SetQuestAction(QUEST_SLOT, "przedmioty" ));

		final List<ChatAction> monetaactions = new LinkedList<ChatAction>();
		monetaactions.add(new DropItemAction("iron",40));
		monetaactions.add(new DropItemAction("gold bar",40));
		monetaactions.add(new DropItemAction("mithril nugget",30));
		monetaactions.add(new DropItemAction("burgher ring",1));
		monetaactions.add(new DropItemAction("skull shield",1));
		monetaactions.add(new DropItemAction("elvish legs",1));
		monetaactions.add(new EquipItemAction("knight ring", 1, true));
		monetaactions.add(new IncreaseXPAction(1000));
		monetaactions.add(new SetQuestAction(QUEST_SLOT, "done"));

		npc.add(ConversationStates.ATTENDING, Arrays.asList("equipment", "items", "wyposażenie", "wyposazenie", "przedmioty"),
			new AndCondition(new QuestInStateCondition(QUEST_SLOT,"przedmioty"),
			new PlayerHasItemWithHimCondition("iron",40),
			new PlayerHasItemWithHimCondition("gold bar",40),
			new PlayerHasItemWithHimCondition("mithril nugget",30),
			new PlayerHasItemWithHimCondition("burgher ring",1),
			new PlayerHasItemWithHimCondition("skull shield",1),
			new PlayerHasItemWithHimCondition("elvish legs",1)),
			ConversationStates.ATTENDING, "I see you have brought everything I requested. Here is your knight ring.",
		new MultipleActions(monetaactions));

		npc.add(ConversationStates.ATTENDING, Arrays.asList("equipment", "items", "wyposażenie", "wyposazenie", "przedmioty"),
				new AndCondition(new QuestInStateCondition(QUEST_SLOT,"przedmioty"),
								 new NotCondition(
								 new AndCondition(new PlayerHasItemWithHimCondition("iron",40),
												  new PlayerHasItemWithHimCondition("gold bar",40),
												  new PlayerHasItemWithHimCondition("mithril nugget",30),
												  new PlayerHasItemWithHimCondition("burgher ring",1),
												  new PlayerHasItemWithHimCondition("skull shield",1),
												  new PlayerHasItemWithHimCondition("elvish legs",1)))),
				ConversationStates.ATTENDING, "Bring me:\n"
									+"#'40 iron'\n"
									+"#'40 gold bar'\n"
									+"#'30 mithril nugget'\n"
									+"#'1 burgher ring'\n"
									+"#'1 skull shield'\n"
									+"#'1 elvish legs'\n"
									+" Please bring everything at once. Keyword is #'/items/'. Thank you!", null);
	}

	@Override
	public void addToWorld() {
		fillQuestInfo(
				"Knight Ring",
				"Overcome the challenges set before you by Edgard to earn the Knight Ring.",
				true);

		checkLevelHelm(); 
		checkCollectingQuests();
		requestItem();
	}

	@Override
	public List<String> getHistory(final Player player) {
			final List<String> res = new ArrayList<String>();
			if (!player.hasQuest(QUEST_SLOT)) {
				return res;
			}
			final String questState = player.getQuest(QUEST_SLOT);
			res.add("I met Edgard in the Order grounds.");
			res.add("He offered me the knight ring in exchange for several items.");
			if ("rejected".equals(questState)) {
				res.add("I have no need for the knight ring.");
				return res;
			} 
			if ("start".equals(questState)) {
				return res;
			} 
			res.add("Edgard asked me to deliver the required items: 40 iron, 40 gold bars, 30 mithril nuggets, 1 burgher ring, 1 skull shield, and 1 elvish legs.");
			if ("przedmioty".equals(questState)) {
				return res;
			} 
			res.add("Edgard was pleased with my deeds. In return, I received the knight ring.");
			if (isCompleted(player)) {
				return res;
			} 

			// if things have gone wrong and the quest state didn't match any of the above, debug a bit:
			final List<String> debug = new ArrayList<String>();
			debug.add("Stan zadania to: " + questState);
			logger.error("Historia nie pasuje do stanu poszukiwania " + questState);
			return debug;
	}

	@Override
	public String getName() {
		return "PierscienRycerza";
	}

	@Override
	public String getNPCName() {
		return "Edgard";
	}
}
