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

import games.stendhal.common.MathHelper;
import games.stendhal.common.grammar.Grammar;
import games.stendhal.common.parser.Sentence;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.DropItemAction;
import games.stendhal.server.entity.npc.action.EquipItemAction;
import games.stendhal.server.entity.npc.action.DropRecordedItemAction;
import games.stendhal.server.entity.npc.action.IncreaseKarmaAction;
import games.stendhal.server.entity.npc.action.IncreaseXPAction;
import games.stendhal.server.entity.npc.action.MultipleActions;
import games.stendhal.server.entity.npc.action.SetQuestAction;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.QuestInStateCondition;
import games.stendhal.server.entity.npc.condition.GreetingMatchesNameCondition;
import games.stendhal.server.entity.npc.action.SetQuestAndModifyKarmaAction;
import games.stendhal.server.entity.npc.condition.PlayerHasItemWithHimCondition;
import games.stendhal.server.entity.npc.condition.PlayerHasRecordedItemWithHimCondition;
import games.stendhal.server.entity.npc.condition.QuestStartedCondition;
import games.stendhal.server.entity.npc.condition.QuestStateStartsWithCondition;
import games.stendhal.server.entity.npc.condition.QuestCompletedCondition;
import games.stendhal.server.entity.npc.condition.QuestNotCompletedCondition;
import games.stendhal.server.entity.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;

import org.apache.log4j.Logger;

public class BramaZrc extends AbstractQuest {

	private static final String QUEST_SLOT = "brama_zrc";

	private static final String ARMOR_DAGOBERT_QUEST_SLOT = "armor_dagobert";

	private static Logger logger = Logger.getLogger(BramaZrc.class);


	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}

	private void step_1() {
		final SpeakerNPC npc = npcs.get("Cień");

		npc.add(ConversationStates.ATTENDING,
			ConversationPhrases.QUEST_MESSAGES, null,
			ConversationStates.QUEST_OFFERED, null,
			new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					if (player.isBadBoy()) {
						raiser.say("You have slain a fellow knight! Clear your skull mark before daring to speak with the Shadow Knights!");
					} else {
						if (player.isQuestCompleted(ARMOR_DAGOBERT_QUEST_SLOT)) {
							if (player.getLevel() >= 50) {
								if (player.hasKilledSolo("huge black dragon") || player.hasKilledSolo("czarne smoczysko")) {
									if (!player.hasQuest(QUEST_SLOT) || "rejected".equals(player.getQuest(QUEST_SLOT))) {
										raiser.say("Do you wish to pass through the gates of the Order of Shadow Knights and learn ancient secrets?");
									} else if (player.isQuestCompleted(QUEST_SLOT)) {
										raiser.say("You have returned, but our gate remains open to you and our food cellars are full.");
										raiser.setCurrentState(ConversationStates.ATTENDING);
									}
								} else {
									npc.say("I speak only with warriors who have proven their valor by slaying the huge black dragon solo.");
									raiser.setCurrentState(ConversationStates.ATTENDING);
								}
							} else {
								npc.say("You are not yet experienced enough for the Order. Reach at least level 50 first.");
								raiser.setCurrentState(ConversationStates.ATTENDING);
							}
						} else {
							npc.say("Aid my old friend Dagobert in Semos first, then return!");
							raiser.setCurrentState(ConversationStates.ATTENDING);
						}
					}
				}
			});

		npc.add(ConversationStates.QUEST_OFFERED,
			ConversationPhrases.YES_MESSAGES, null,
			ConversationStates.ATTENDING, null,
			new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {

					raiser.say("I will need several #provisions for our food pantry.");
					player.setQuest(QUEST_SLOT, "start");
					player.addKarma(10);

				}
			});

		npc.add(ConversationStates.QUEST_OFFERED,
			ConversationPhrases.NO_MESSAGES, null,
			ConversationStates.IDLE,
			"As you wish.",
			new SetQuestAndModifyKarmaAction(QUEST_SLOT, "rejected", -10.0));

	}

	private void step_2() {
		final SpeakerNPC npc = npcs.get("Cień");

		npc.add(ConversationStates.ATTENDING, Arrays.asList("things", "provisions", "items", "rzeczy", "przedmiotów"),
				new QuestInStateCondition(QUEST_SLOT, "start"),
				ConversationStates.ATTENDING, "Our pantry is bare and our knights need sustenance. Here is the #list of needed provisions.",
				new SetQuestAction(QUEST_SLOT, "dostawca"));

	}

	private void step_3() {
		final SpeakerNPC npc = npcs.get("Cień");

		npc.add(ConversationStates.ATTENDING, Arrays.asList("list", "provisions", "supplies", "lista", "listę", "artykuły"),
				new AndCondition(new QuestInStateCondition(QUEST_SLOT,"dostawca"),
								 new NotCondition(
								 new AndCondition(new PlayerHasItemWithHimCondition("steak",20),
												  new PlayerHasItemWithHimCondition("cheese",30),
												  new PlayerHasItemWithHimCondition("ham",20),
												  new PlayerHasItemWithHimCondition("meat",30),
												  new PlayerHasItemWithHimCondition("bread",10),
												  new PlayerHasItemWithHimCondition("sandwich",7),
												  new PlayerHasItemWithHimCondition("apple",25),
												  new PlayerHasItemWithHimCondition("icecream",5),
												  new PlayerHasItemWithHimCondition("water",10)))),
				ConversationStates.ATTENDING, "I need:\n" 
									+ "- 20 steaks\n" 
									+ "- 30 cheese\n" 
									+ "- 20 ham\n" 
									+ "- 30 meat\n"
									+ "- 10 bread\n" 
									+ "- 7 sandwiches\n" 
									+ "- 25 apples\n" 
									+ "- 5 icecreams\n" 
									+ "- 10 bottles of water\n" 
									+ "Please bring everything at once. Say #provisions when ready. Thank you!", null);

		final List<ChatAction> cienactions = new LinkedList<ChatAction>();
		cienactions.add(new DropItemAction("steak",20));
		cienactions.add(new DropItemAction("cheese",30));
		cienactions.add(new DropItemAction("ham",20));
		cienactions.add(new DropItemAction("meat",30));
		cienactions.add(new DropItemAction("bread",10));
		cienactions.add(new DropItemAction("sandwich",7));
		cienactions.add(new DropItemAction("apple",25));
		cienactions.add(new DropItemAction("icecream",5));
		cienactions.add(new DropItemAction("water",10));
		cienactions.add(new EquipItemAction("order gate key", 1, true));
		cienactions.add(new IncreaseXPAction(8000));
		cienactions.add(new IncreaseKarmaAction(100));
		cienactions.add(new SetQuestAction(QUEST_SLOT, "done"));


		npc.add(ConversationStates.ATTENDING, Arrays.asList("list", "provisions", "supplies", "lista", "listę", "artykuły"),
			new AndCondition(new QuestInStateCondition(QUEST_SLOT,"dostawca"),
							 new PlayerHasItemWithHimCondition("steak",20),
							 new PlayerHasItemWithHimCondition("cheese",30),
							 new PlayerHasItemWithHimCondition("ham",20),
							 new PlayerHasItemWithHimCondition("meat",30),
							 new PlayerHasItemWithHimCondition("bread",10),
							 new PlayerHasItemWithHimCondition("sandwich",7),
							 new PlayerHasItemWithHimCondition("apple",25),
							 new PlayerHasItemWithHimCondition("icecream",5),
							 new PlayerHasItemWithHimCondition("water",10)),
			ConversationStates.ATTENDING, "Splendid! Our pantry is well-stocked. As a reward, take this key." +
										  " Enter the castle to master the ancient wisdom of the Shadow Knights." +
										  " The gates are now open to you!",
			new MultipleActions(cienactions));
	}


	@Override
	public void addToWorld() {
		fillQuestInfo(
				"Gate of the Shadow Order",
				"Shadow, the gatekeeper of the Shadow Knights Order, has a task for you.",
				true);
		step_1();
		step_2();
		step_3();
	}

	@Override
	public List<String> getHistory(final Player player) {
		final List<String> res = new ArrayList<String>();
		if (!player.hasQuest(QUEST_SLOT)) {
			return res;
		}
		final String questState = player.getQuest(QUEST_SLOT);
		res.add("I met Shadow, the gatekeeper of the Shadow Knights Order.");
		res.add("Shadow asked me to resupply their food pantry.");
		if ("rejected".equals(questState)) {
			res.add("I did not feel like helping Shadow...");
			return res;
		} 
		if ("start".equals(questState)) {
			return res;
		} 
		res.add("Shadow asked me to deliver: 20 steaks, 30 cheese, 20 ham, 30 meat, 10 bread, 7 sandwiches, 25 apples, 5 icecreams, and 10 water.");
		if ("dostawca".equals(questState)) {
			return res;
		} 
		res.add("Shadow is pleased with my assistance and rewarded me with the order gate key.");
		if (isCompleted(player)) {
			return res;
		} 

		// if things have gone wrong and the quest state didn't match any of the above, debug a bit:
		final List<String> debug = new ArrayList<String>();
		debug.add("Quest state is: " + questState);
		logger.error("History does not match quest state " + questState);
		return debug;
	}

	@Override
	public String getName() {
		return "BramaZrc";
	}
	@Override
	public String getNPCName() {
		return "Cień";
	}
}
