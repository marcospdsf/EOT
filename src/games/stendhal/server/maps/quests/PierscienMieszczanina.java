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
// Based on UltimateCollector and HelpMrsYeti.
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
import games.stendhal.server.entity.npc.condition.GreetingMatchesNameCondition;
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
import games.stendhal.server.entity.npc.condition.TimePassedCondition;
import games.stendhal.server.entity.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

	public class PierscienMieszczanina extends AbstractQuest {

	private static final String QUEST_SLOT = "pierscien_mieszczanina";

	private static final String MRSYETI_QUEST_SLOT = "mrsyeti";

	private static final String ANDRZEJ_MAKE_ZLOTA_CIUPAGA_QUEST_SLOT = "andrzej_make_zlota_ciupaga";

	private static Logger logger = Logger.getLogger(PierscienMieszczanina.class);

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
		res.add("I met Blacksmith Marianek.");
		res.add("He offered to craft a burgher ring for me.");
		final String questState = player.getQuest(QUEST_SLOT);
		if (questState.equals("rejected")) {
			res.add("I have no need for trinkets.");
			return res;
		} 
		if (questState.equals("start")) {
			return res;
		} 
		res.add("Blacksmith Marianek asked me to deliver 200000 money, 1 highlander walking axe, 50 gold bars, and 20 mithril nuggets. Once gathered, I should say \"ring\".");
		if (questState.equals("start")) {
			return res;
		} 
		res.add("Blacksmith Marianek was pleased with my efforts. In return, I received the burgher ring.");

		if (isCompleted(player)) {
			return res;
		} 

		// if things have gone wrong and the quest state didn't match any of the above, debug a bit:
		final List<String> debug = new ArrayList<String>();
		debug.add("Stan zadania to: " + questState);
		logger.error("Historia nie pasuje do stanu poszukiwania " + questState);
		return debug;
	}

	private void checkLevelHelm() {
		final SpeakerNPC npc = npcs.get("Marianek");

		npc.add(ConversationStates.ATTENDING,
			ConversationPhrases.QUEST_MESSAGES, null,
			ConversationStates.QUEST_OFFERED, null,
			new ChatAction() {
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					if (player.isBadBoy()){ 
						raiser.say("You have slain a knight! You have no business here until you rid yourself of the skull mark. Out of my sight!");
					} else {
						if (player.isQuestCompleted(MRSYETI_QUEST_SLOT)) {
							if (player.getLevel() >= 150) {
								if (!player.hasQuest(QUEST_SLOT) || "rejected".equals(player.getQuest(QUEST_SLOT))) {
									raiser.say("Do you wish to earn the burgher ring?");
								} else if (player.isQuestCompleted(QUEST_SLOT)) {
									raiser.say("You have already claimed your ring. Farewell.");
									raiser.setCurrentState(ConversationStates.ATTENDING);
								}
							} else {
								npc.say("Your social standing is too low for this task. Return when you reach level 150.");
								raiser.setCurrentState(ConversationStates.ATTENDING);
							}
						} else {
							npc.say("I see you have not helped Mrs. Yeti! Return once you have assisted her.");
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
					raiser.say("First I must verify you have completed previous tasks before awarding the #ring.");
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
		final SpeakerNPC npc = npcs.get("Marianek");

		npc.add(
			ConversationStates.IDLE,
			ConversationPhrases.GREETING_MESSAGES,
			new AndCondition(new GreetingMatchesNameCondition(npc.getName()),
							 new QuestCompletedCondition(MRSYETI_QUEST_SLOT),
							 new QuestNotStartedCondition(QUEST_SLOT)),
			ConversationStates.ATTENDING,
			"Greetings, friend. I have a challenge for you to earn the #burgher #ring.",
			null);
			
			npc.add(ConversationStates.ATTENDING,
			Arrays.asList("challenge", "ring", "burgher ring", "pierścień", "pierscien"), 
			new AndCondition( new QuestCompletedCondition(MRSYETI_QUEST_SLOT),
							 new QuestNotStartedCondition(QUEST_SLOT),
							 new QuestNotCompletedCondition(ANDRZEJ_MAKE_ZLOTA_CIUPAGA_QUEST_SLOT)),
			ConversationStates.ATTENDING, 
			"You still have an unfinished task for Blacksmith Andrzej!",
			null);
	}

	private void requestItem() {
		final SpeakerNPC npc = npcs.get("Marianek");

		npc.add(ConversationStates.ATTENDING,
			Arrays.asList("challenge", "ring", "burgher ring", "pierścień", "pierscien"),
			new AndCondition(
					new QuestCompletedCondition(MRSYETI_QUEST_SLOT),
					new QuestNotStartedCondition(QUEST_SLOT)),
			ConversationStates.ATTENDING, "To earn the #ring, you must bring several items.",
			new SetQuestAction(QUEST_SLOT, "start" ));

		final List<ChatAction> amuletactions = new LinkedList<ChatAction>();
		amuletactions.add(new DropItemAction("money",200000));
		amuletactions.add(new DropItemAction("highlander walking axe",1));
		amuletactions.add(new DropItemAction("gold bar",50));
		amuletactions.add(new DropItemAction("mithril nugget",20));
		amuletactions.add(new EquipItemAction("burgher ring", 1, true));
		amuletactions.add(new IncreaseXPAction(1000));
		amuletactions.add(new SetQuestAction(QUEST_SLOT, "done"));

		npc.add(ConversationStates.ATTENDING, Arrays.asList("challenge", "ring", "burgher ring", "pierścień", "pierscien"),
			new AndCondition(
					new QuestInStateCondition(QUEST_SLOT,"start"),
					new PlayerHasItemWithHimCondition("money",200000),
					new PlayerHasItemWithHimCondition("highlander walking axe",1),
					new PlayerHasItemWithHimCondition("gold bar",50),
					new PlayerHasItemWithHimCondition("mithril nugget",20)),
			ConversationStates.ATTENDING, "I see you have brought everything I requested. Here is your burgher ring.",
			new MultipleActions(amuletactions));

		npc.add(ConversationStates.ATTENDING, Arrays.asList("challenge", "ring", "burgher ring", "pierścień", "pierscien"),
			new AndCondition(
					new QuestInStateCondition(QUEST_SLOT,"start"),
					new NotCondition(
							new AndCondition(new PlayerHasItemWithHimCondition("money",200000),
							new PlayerHasItemWithHimCondition("highlander walking axe",1),
							new PlayerHasItemWithHimCondition("gold bar",50),
							new PlayerHasItemWithHimCondition("mithril nugget", 20)))),
			ConversationStates.ATTENDING, "I need:\n"
									+"#'200000 money'\n"
									+"#'1 highlander walking axe'\n"
									+"#'50 gold bars'\n"
									+"#'20 mithril nuggets'\n"
									+" Please bring everything at once. Keyword is #'/ring/'. Thank you!", null);
	}

	@Override
	public void addToWorld() {
		fillQuestInfo(
			"Burgher Ring",
			"Overcome the challenges set before you by Blacksmith Marianek to earn the Burgher Ring.",
			true);

		checkLevelHelm(); 
		checkCollectingQuests();
		requestItem();
	}

	@Override
	public String getName() {
		return "PierscienMieszczanina";
	}

	@Override
	public String getNPCName() {
		return "Marianek";
	}
}
