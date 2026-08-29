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

	public class PierscienBarona extends AbstractQuest {

	private static final String QUEST_SLOT = "pierscien_barona";

	private static final String HUNGRY_JOSHUA_QUEST_SLOT = "hungry_joshua"; 

	private static final String FISHERMANS_LICENSE2_QUEST_SLOT = "fishermans_license2";

	private static final String OBSIDIAN_KNIFE_QUEST_SLOT = "obsidian_knife";

	private static final String MITHRIL_CLOAK_QUEST_SLOT = "mithril_cloak";

	private static final String CIUPAGA_DWA_WASY_QUEST_SLOT = "ciupaga_dwa_wasy";

	private static Logger logger = Logger.getLogger(PierscienBarona.class);

	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}

	private void checkLevelHelm() {
		final SpeakerNPC npc = npcs.get("eDragon");

		npc.add(ConversationStates.ATTENDING,
			ConversationPhrases.QUEST_MESSAGES, null,
			ConversationStates.QUEST_OFFERED, null,
			new ChatAction() {
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					if (player.isBadBoy()){ 
						raiser.say("You have slain a knight! You have no business here until you rid yourself of the skull mark. Out of my sight!");
					} else {
						if (player.isQuestCompleted(CIUPAGA_DWA_WASY_QUEST_SLOT)) {
							if (player.getLevel() >= 350) {
								if (!player.hasQuest(QUEST_SLOT) || "rejected".equals(player.getQuest(QUEST_SLOT))) {
									raiser.say("Do you seek to earn the baron ring?");
								} else if (player.isQuestCompleted(QUEST_SLOT)) {
									raiser.say("You have already claimed the ring.");
									raiser.setCurrentState(ConversationStates.ATTENDING);
								}
							} else {
								npc.say("Your social standing is too low for this task. Return when you reach level 350.");
								raiser.setCurrentState(ConversationStates.ATTENDING);
							}
						} else {
							npc.say("I see you do not possess the golden highlander axe with two notches. Return once you obtain it.");
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
					raiser.say("First I will check if you completed previous tasks before giving you the #list.");
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
		final SpeakerNPC npc = npcs.get("eDragon");

		npc.add(
			ConversationStates.IDLE,
			ConversationPhrases.GREETING_MESSAGES,
			new AndCondition(new QuestCompletedCondition(CIUPAGA_DWA_WASY_QUEST_SLOT),
					 new QuestNotStartedCondition(QUEST_SLOT)),
			ConversationStates.ATTENDING,
			"Greetings, friend. Would you like to earn the #baron #ring?",
			null);

		npc.add(ConversationStates.ATTENDING,
			Arrays.asList("list", "ring", "baron ring", "listę", "liste", "pierścień"),
			new AndCondition(new QuestCompletedCondition(CIUPAGA_DWA_WASY_QUEST_SLOT),
					 new QuestNotStartedCondition(QUEST_SLOT),
					 new QuestNotCompletedCondition(HUNGRY_JOSHUA_QUEST_SLOT)),
			ConversationStates.ATTENDING, 
			"Help Joshua first, then we will speak of the ring.",
			null);

		npc.add(ConversationStates.ATTENDING,
			Arrays.asList("list", "ring", "baron ring", "listę", "liste", "pierścień"),
			new AndCondition(new QuestCompletedCondition(CIUPAGA_DWA_WASY_QUEST_SLOT),
					 new QuestNotStartedCondition(QUEST_SLOT),
					 new QuestNotCompletedCondition(FISHERMANS_LICENSE2_QUEST_SLOT)),
			ConversationStates.ATTENDING, 
			"Obtain the fisherman license first, then we will speak of the ring.",
			null);

		npc.add(ConversationStates.ATTENDING,
			Arrays.asList("list", "ring", "baron ring", "listę", "liste", "pierścień"),
			new AndCondition(new QuestCompletedCondition(CIUPAGA_DWA_WASY_QUEST_SLOT),
					 new QuestNotStartedCondition(QUEST_SLOT),
					 new QuestNotCompletedCondition(OBSIDIAN_KNIFE_QUEST_SLOT)),
			ConversationStates.ATTENDING, 
			"Complete the quest for the obsidian knife first, then we will speak of the ring.",
			null);

		npc.add(ConversationStates.ATTENDING,
			Arrays.asList("list", "ring", "baron ring", "listę", "liste", "pierścień"),
			new AndCondition(new QuestCompletedCondition(CIUPAGA_DWA_WASY_QUEST_SLOT),
					 new QuestNotStartedCondition(QUEST_SLOT),
					 new QuestNotCompletedCondition(MITHRIL_CLOAK_QUEST_SLOT)),
			ConversationStates.ATTENDING, 
			"First complete the quest for the mithril cloak, then we will speak of the ring.",
			null);

	}

	private void requestItem() {
		final SpeakerNPC npc = npcs.get("eDragon");

		npc.add(ConversationStates.ATTENDING,
				Arrays.asList("list", "ring", "baron ring", "listę", "liste", "pierścień"),
				new AndCondition( 
						new QuestCompletedCondition(CIUPAGA_DWA_WASY_QUEST_SLOT),
						new QuestNotStartedCondition(QUEST_SLOT),
						new QuestCompletedCondition(HUNGRY_JOSHUA_QUEST_SLOT),
						new QuestCompletedCondition(FISHERMANS_LICENSE2_QUEST_SLOT),
						new QuestCompletedCondition(OBSIDIAN_KNIFE_QUEST_SLOT),
						new QuestCompletedCondition(MITHRIL_CLOAK_QUEST_SLOT)),
				ConversationStates.ATTENDING, "I see you have completed the prerequisite tasks. Here is the list of required #items.",
				new SetQuestAction(QUEST_SLOT, "lista" ));

		final List<ChatAction> amuletactions = new LinkedList<ChatAction>();
		amuletactions.add(new DropItemAction("knight ring",1));
		amuletactions.add(new DropItemAction("iron ore",100));
		amuletactions.add(new DropItemAction("gold nugget",150));
		amuletactions.add(new DropItemAction("wood",200));
		amuletactions.add(new DropItemAction("coal",100));
		amuletactions.add(new DropItemAction("sulfur",100));
		amuletactions.add(new DropItemAction("salt",100));
		amuletactions.add(new EquipItemAction("baron ring", 1, true));
		amuletactions.add(new IncreaseXPAction(1000));
		amuletactions.add(new SetQuestAction(QUEST_SLOT, "done"));

		npc.add(ConversationStates.ATTENDING, Arrays.asList("items", "przedmiotów", "przedmioty"),
				new AndCondition(new QuestInStateCondition(QUEST_SLOT,"lista"),
				new PlayerHasItemWithHimCondition("knight ring",1),
				new PlayerHasItemWithHimCondition("iron ore",100),
				new PlayerHasItemWithHimCondition("gold nugget",150),
				new PlayerHasItemWithHimCondition("wood",200),
				new PlayerHasItemWithHimCondition("coal",100),
				new PlayerHasItemWithHimCondition("sulfur",100),
				new PlayerHasItemWithHimCondition("salt",100)),
				ConversationStates.ATTENDING, "I see you have brought everything I requested. Here is your baron ring.",
				new MultipleActions(amuletactions));

		npc.add(ConversationStates.ATTENDING, Arrays.asList("items", "przedmioty", "przedmiotów"),
				new AndCondition(new QuestInStateCondition(QUEST_SLOT,"lista"),
								 new NotCondition(
								 new AndCondition(new PlayerHasItemWithHimCondition("knight ring",1),
												  new PlayerHasItemWithHimCondition("iron ore",100),
												  new PlayerHasItemWithHimCondition("gold nugget",150),
												  new PlayerHasItemWithHimCondition("wood",200),
												  new PlayerHasItemWithHimCondition("coal",100),
												  new PlayerHasItemWithHimCondition("sulfur",100),
												  new PlayerHasItemWithHimCondition("salt",100)))),
				ConversationStates.ATTENDING, "Bring me:\n"
									+"#'1 knight ring'\n"
									+"#'100 iron ore'\n"
									+"#'150 gold nuggets'\n"
									+"#'200 wood'\n"
									+"#'100 coal'\n"
									+"#'100 sulfur'\n"
									+"#'100 salt'\n"
									+" Please bring everything at once. Keyword is #'/items/'. Thank you!", null);
	}

	@Override
	public void addToWorld() {
		fillQuestInfo(
				"Baron Ring",
				"Overcome the challenges set before you by eDragon to earn the Baron Ring.",
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
			res.add("I met the dragon eDragon.");
			res.add("He offered to grant me the baron ring.");
			if ("rejected".equals(questState)) {
				res.add("I have no need for the ring.");
				return res;
			} 
			if ("start".equals(questState)) {
				return res;
			} 
			res.add("The dragon eDragon asked me to deliver: 1 knight ring, 100 iron ore, 150 gold nuggets, 200 wood, 100 coal, 100 sulfur, and 100 salt.");
			if ("lista".equals(questState)) {
				return res;
			} 
			res.add("The dragon eDragon was pleased with my deeds. In return, I received the baron ring.");
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
		return "EdragonNPC";
	}

	@Override
	public String getNPCName() {
		return "eDragon";
	}
}