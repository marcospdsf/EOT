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

public class PierscienMagnata extends AbstractQuest {

	private static final String QUEST_SLOT = "pierscien_magnata";

	private static final String CLUB_THORNS_QUEST_SLOT = "club_thorns";

	private static final String KILL_DRAGONS_QUEST_SLOT = "kill_dragons"; 

	private static final String VAMPIRE_SWORD_QUEST_SLOT = "vs_quest"; 

	private static final String IMMORTAL_SWORD_QUEST_SLOT = "immortalsword_quest";

	private static final String FIND_RAT_KIDS_QUEST_SLOT = "find_rat_kids";

	private static final String FIND_GHOSTS_QUEST_SLOT = "find_ghosts";

	private static final String SAD_SCIENTIST_QUEST_SLOT = "sad_scientist"; 

	private static final String PIERSCIEN_BARONA_QUEST_SLOT = "pierscien_barona";

	private static Logger logger = Logger.getLogger(PierscienMagnata.class);

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
		res.add("I met Jeweler Zdzichu.");
		res.add("He offered to grant me the magnate ring.");
		final String questState = player.getQuest(QUEST_SLOT);
		if (questState.equals("rejected")) {
			res.add("I have no need for trinkets.");
			return res;
		} 
		if (questState.equals("start")) {
			return res;
		} 
		res.add("Jeweler Zdzichu asked me to deliver 70 silver bars, 70 mithril bars, 250 gold bars, 100 mithril nuggets, 200 gold nuggets, 500000 money, and 1 baron ring.");
		if (questState.equals("start")) {
			return res;
		} 
		res.add("Jeweler Zdzichu was pleased with my deeds. In return, I received the magnate ring.");

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
		final SpeakerNPC npc = npcs.get("Jubiler Zdzichu");

		npc.add(ConversationStates.ATTENDING,
			ConversationPhrases.QUEST_MESSAGES, null,
			ConversationStates.QUEST_OFFERED, null,
			new ChatAction() {
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					if (player.isBadBoy()){ 
						raiser.say("You have slain a knight! You have no business here until you rid yourself of the skull mark. Out of my sight!");
					} else {
						if (player.isQuestCompleted(PIERSCIEN_BARONA_QUEST_SLOT)) {
							if (player.getLevel() >= 500) {
								if (!player.hasQuest(QUEST_SLOT) || "rejected".equals(player.getQuest(QUEST_SLOT))) {
									raiser.say("Do you seek to earn the magnate ring?");
								} else if (player.isQuestCompleted(QUEST_SLOT)) {
									raiser.say("You have already claimed your ring. Farewell.");
									raiser.setCurrentState(ConversationStates.ATTENDING);
								}
							} else {
								npc.say("Your social standing is too low for this task. Return when you reach level 500.");
								raiser.setCurrentState(ConversationStates.ATTENDING);
							}
						} else {
							npc.say("I see you do not possess the baron ring. Return once you have earned it.");
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
					raiser.say("First I will check if you completed previous tasks before giving you the #ring.");
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
		final SpeakerNPC npc = npcs.get("Jubiler Zdzichu");

		npc.add(
			ConversationStates.IDLE,
			ConversationPhrases.GREETING_MESSAGES,
			new AndCondition(new GreetingMatchesNameCondition(npc.getName()),
							 new QuestCompletedCondition(PIERSCIEN_BARONA_QUEST_SLOT),
							 new QuestNotStartedCondition(QUEST_SLOT)),
			ConversationStates.ATTENDING,
			"Greetings, friend. I have a challenge for you to earn the #magnate #ring.",
			null);

		npc.add(ConversationStates.ATTENDING,
			Arrays.asList("challenge", "ring", "magnate ring", "pierścień", "pierscien"), 
			new AndCondition(new QuestCompletedCondition(PIERSCIEN_BARONA_QUEST_SLOT),
							 new QuestNotStartedCondition(QUEST_SLOT),
							 new OrCondition(new QuestNotCompletedCondition(CLUB_THORNS_QUEST_SLOT),
							 new QuestNotCompletedCondition(IMMORTAL_SWORD_QUEST_SLOT))),
			ConversationStates.ATTENDING, 
			"You still have unfinished tasks in Fado. Complete them to earn the ring!",
			null);

		npc.add(ConversationStates.ATTENDING,
			Arrays.asList("challenge", "ring", "magnate ring", "pierścień", "pierscien"), 
			new AndCondition(new QuestCompletedCondition(PIERSCIEN_BARONA_QUEST_SLOT),
							 new QuestNotStartedCondition(QUEST_SLOT),
							 new OrCondition(new QuestNotCompletedCondition(KILL_DRAGONS_QUEST_SLOT),
							 new QuestNotCompletedCondition(VAMPIRE_SWORD_QUEST_SLOT))),
			ConversationStates.ATTENDING, 
			"You have not helped little Alice or you are missing the vampire sword.",
			null);

		npc.add(ConversationStates.ATTENDING,
			Arrays.asList("challenge", "ring", "magnate ring", "pierścień", "pierscien"),
			new AndCondition(new QuestCompletedCondition(PIERSCIEN_BARONA_QUEST_SLOT),
							 new QuestNotStartedCondition(QUEST_SLOT),
							 new OrCondition(new QuestNotCompletedCondition(FIND_RAT_KIDS_QUEST_SLOT),
							 	 	 	 	 new QuestNotCompletedCondition(FIND_GHOSTS_QUEST_SLOT))),
			ConversationStates.ATTENDING, 
			"You must find the wandering ghosts or Agnes's children.",
			null);

		npc.add(ConversationStates.ATTENDING,
			Arrays.asList("challenge", "ring", "magnate ring", "pierścień", "pierscien"),
			new AndCondition(new QuestCompletedCondition(PIERSCIEN_BARONA_QUEST_SLOT),
							 new QuestNotStartedCondition(QUEST_SLOT),
							 new QuestNotCompletedCondition(SAD_SCIENTIST_QUEST_SLOT)),
			ConversationStates.ATTENDING, 
			"You have collected many special items, but you must still help the sad scientist.",
			null);
	}

	private void requestItem() {
		final SpeakerNPC npc = npcs.get("Jubiler Zdzichu");

		npc.add(ConversationStates.ATTENDING,
			Arrays.asList("challenge", "ring", "magnate ring", "pierścień", "pierscien"),
			new AndCondition(
					new QuestCompletedCondition(PIERSCIEN_BARONA_QUEST_SLOT),
					new QuestNotStartedCondition(QUEST_SLOT),
					new QuestCompletedCondition(SAD_SCIENTIST_QUEST_SLOT),
					new QuestCompletedCondition(FIND_GHOSTS_QUEST_SLOT),
					new QuestCompletedCondition(FIND_RAT_KIDS_QUEST_SLOT),
					new QuestCompletedCondition(VAMPIRE_SWORD_QUEST_SLOT),
					new QuestCompletedCondition(KILL_DRAGONS_QUEST_SLOT),
					new QuestCompletedCondition(CLUB_THORNS_QUEST_SLOT),
					new QuestCompletedCondition(IMMORTAL_SWORD_QUEST_SLOT)),
			ConversationStates.ATTENDING, "To earn the #ring, you must bring several items.",
			new SetQuestAction(QUEST_SLOT, "start" ));

		final List<ChatAction> amuletactions = new LinkedList<ChatAction>();
		amuletactions.add(new DropItemAction("silver bar",70));
		amuletactions.add(new DropItemAction("mithril bar",70));
		amuletactions.add(new DropItemAction("gold bar",250)); 
		amuletactions.add(new DropItemAction("mithril nugget",100));
		amuletactions.add(new DropItemAction("gold nugget",200));
		amuletactions.add(new DropItemAction("money",500000));
		amuletactions.add(new DropItemAction("baron ring",1));
		amuletactions.add(new EquipItemAction("magnate ring", 1, true));
		amuletactions.add(new IncreaseXPAction(1000));
		amuletactions.add(new SetQuestAction(QUEST_SLOT, "done"));

		npc.add(ConversationStates.ATTENDING, Arrays.asList("challenge", "ring", "magnate ring", "pierścień", "pierscien"),
			new AndCondition(
					new QuestInStateCondition(QUEST_SLOT,"start"),
					new PlayerHasItemWithHimCondition("silver bar",70),
					new PlayerHasItemWithHimCondition("mithril bar",70),
					new PlayerHasItemWithHimCondition("gold bar",250),
					new PlayerHasItemWithHimCondition("mithril nugget",100),
					new PlayerHasItemWithHimCondition("gold nugget",200),
					new PlayerHasItemWithHimCondition("money",500000),
					new PlayerHasItemWithHimCondition("baron ring",1)),
			ConversationStates.ATTENDING, "I see you have brought everything I requested. Here is your magnate ring.",
			new MultipleActions(amuletactions));

		npc.add(ConversationStates.ATTENDING, Arrays.asList("challenge", "ring", "magnate ring", "pierścień", "pierscien"),
			new AndCondition(
					new QuestInStateCondition(QUEST_SLOT,"start"),
					new NotCondition(
							new AndCondition(new PlayerHasItemWithHimCondition("silver bar",70),
							new PlayerHasItemWithHimCondition("mithril bar",70),
							new PlayerHasItemWithHimCondition("gold bar",250),
							new PlayerHasItemWithHimCondition("mithril nugget",100),
							new PlayerHasItemWithHimCondition("gold nugget",200),
							new PlayerHasItemWithHimCondition("money",500000),
							new PlayerHasItemWithHimCondition("baron ring",1)))),
			ConversationStates.ATTENDING, "I need:\n"
									+"#'70 silver bars'\n"
									+"#'70 mithril bars'\n"
									+"#'250 gold bars'\n"
									+"#'100 mithril nuggets'\n"
									+"#'200 gold nuggets'\n"
									+"#'500000 money'\n"
									+"#'1 baron ring'\n"
									+" Please bring everything at once. Keyword is #'/ring/'. Thank you!", null);
	}

	@Override
	public void addToWorld() {
		fillQuestInfo(
			"Magnate Ring",
			"Overcome the challenges set before you by Jeweler Zdzichu to earn the Magnate Ring.",
			true);

		checkLevelHelm(); 
		checkCollectingQuests();
		requestItem();
	}

	@Override
	public String getName() {
		return "PierscienMagnata";
	}
	@Override
	public String getNPCName() {
		return "Jubiler Zdzichu";
	}
}
