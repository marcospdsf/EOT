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

import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.EquipItemAction;
import games.stendhal.server.entity.npc.action.ExamineChatAction;
import games.stendhal.server.entity.npc.action.IncreaseXPAction;
import games.stendhal.server.entity.npc.action.MultipleActions;
import games.stendhal.server.entity.npc.action.SetQuestAction;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.KilledForQuestCondition;
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.QuestCompletedCondition;
import games.stendhal.server.entity.npc.condition.QuestInStateCondition;
import games.stendhal.server.entity.npc.condition.QuestNotCompletedCondition;
import games.stendhal.server.entity.player.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * QUEST: Speak with Hayunn 
 * <p>
 * PARTICIPANTS: <ul><li> Hayunn Naratha</ul>
 *
 * STEPS: <ul>
 * <li> Talk to Hayunn to activate the quest.
 * <li> He asks you to kill a rat, also offering to teach you how
 * <li> Return and learn how to loot, identify items and heal
 * <li> Return and learn how to double click move, and get some URLs
 * </ul>
 *
 * REWARD: <ul><li> 20 XP <li> 5 gold coins <li> studded shield </ul>
 *
 * REPETITIONS: <ul><li> Get the URLs as much as wanted but you only get the reward once.</ul>
 */
public class MeetPietrek extends AbstractQuest {

	private static final String QUEST_SLOT = "meet_pietrek";

	//This is 1 minute at 300 ms per turn
	private static final int TIME_OUT = 200;


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
		res.add("FIRST_CHAT");
		if (isCompleted(player)) {
			res.add("DONE");
		}
		return res;
	}

	private void preparePietrek() {

		final SpeakerNPC npc = npcs.get("Pietrek");

		// player wants to learn how to attack
		npc.add(
			ConversationStates.ATTENDING,
			ConversationPhrases.YES_MESSAGES,
			new QuestInStateCondition(QUEST_SLOT, 0, "start"),
			ConversationStates.ATTENDING,
			"When I was on the road adventuring, I right-clicked on monsters and selected ATTACK. Would you like to know why one risks their life fighting beasts? #Yes?",
			null);

		//player doesn't want to learn how to attack
		npc.add(
			ConversationStates.ATTENDING,
			ConversationPhrases.NO_MESSAGES,
			new QuestInStateCondition(QUEST_SLOT, 0, "start"),
			ConversationStates.ATTENDING,
			"Very well, you look sharp and capable. I'm sure you will manage fine!",
			null);

		//player returns to Hayunn not having killed a rat
		npc.add(ConversationStates.IDLE,
			ConversationPhrases.GREETING_MESSAGES,
			new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0, "start"), new NotCondition(new KilledForQuestCondition(QUEST_SLOT,1))),
			ConversationStates.ATTENDING,
			"I see you haven't defeated that rat yet. Would you like me to tell you how to fight them? #Yes?",
			null);

		//player returns to Hayunn having killed a rat
		final List<ChatAction> actions = new LinkedList<ChatAction>();
		actions.add(new IncreaseXPAction(10));
		actions.add(new SetQuestAction(QUEST_SLOT, "killed"));

		npc.add(
			ConversationStates.IDLE,
			ConversationPhrases.GREETING_MESSAGES,
			new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0, "start"), new KilledForQuestCondition(QUEST_SLOT, 1)),
			ConversationStates.INFORMATION_1,
			"You defeated the rat! Now, would you like to hear about the rewards of risking your neck in battle? #Yes?",
			new MultipleActions(actions));

		// player wants to learn more from Hayunn
		npc.add(
			ConversationStates.INFORMATION_1,
			ConversationPhrases.YES_MESSAGES,
			null,
			ConversationStates.INFORMATION_2,
			"Aha! You can gather valuable loot from fallen foes. To do that, right-click on the corpse and choose LOOT once you're close enough to reach it. You can drag items directly into your bag. Would you like to know how to examine items? #Yes?",
			null);

		npc.add(
			ConversationStates.INFORMATION_2,
			ConversationPhrases.YES_MESSAGES,
			null,
			ConversationStates.INFORMATION_3,
			"Right-click an item and choose LOOK to see its description and stats. Now, you might wonder: how do you survive tough encounters without falling in battle? Would you like to know? #Yes?",
			null);

		npc.add(
			ConversationStates.INFORMATION_3,
			ConversationPhrases.YES_MESSAGES,
			null,
			ConversationStates.INFORMATION_4,
			"Eat regular meals! Right-click on food in your backpack or on the ground. Each bite will gradually restore your health. There are several ways to recover health in the wild... would you like to hear more? #Yes?",
			null);

		npc.add(
			ConversationStates.INFORMATION_4,
			ConversationPhrases.YES_MESSAGES,
			null,
			ConversationStates.INFORMATION_5,
			"Once you save up enough coin, visit our local healer in Zakopane, Gaździna Jadźka, to purchase healing potions. Potions are lifesavers when traveling alone in the mountains. Would you like directions around Zakopane? #Yes?",
			null);

		// The player has had enough info for now. Send them to explore.
		final List<ChatAction> reward = new LinkedList<ChatAction>();
		reward.add(new EquipItemAction("money", 5));
		reward.add(new IncreaseXPAction(10));
		reward.add(new SetQuestAction(QUEST_SLOT, "taught"));
		reward.add(new ExamineChatAction("npcgenowefa.png", "Gaździna Jadźka", "Eastern part of Zakopane town center."));

		npc.add(
			ConversationStates.INFORMATION_5,
			ConversationPhrases.YES_MESSAGES,
			null,
			ConversationStates.IDLE,
			"Step outside this cottage into the town center of Zakopane. Take some time to explore the valley and mountains. Good luck!",
			new MultipleActions(reward));

		npc.add(ConversationStates.IDLE,
			ConversationPhrases.GREETING_MESSAGES,
			new QuestInStateCondition(QUEST_SLOT, "killed"),
			ConversationStates.INFORMATION_1,
			"You returned quickly after defeating the rat! I have several useful tips for you. Would you like to hear them? #Yes?",
			null);

		// Player has returned to say hi again.
		npc.add(ConversationStates.IDLE,
			ConversationPhrases.GREETING_MESSAGES,
			new QuestInStateCondition(QUEST_SLOT, "taught"),
			ConversationStates.INFORMATION_6,
			"Welcome back! Have you returned to learn more survival wisdom? #Yes?",
			null);

		npc.add(
			ConversationStates.INFORMATION_6,
			ConversationPhrases.YES_MESSAGES,
			null,
			ConversationStates.INFORMATION_7,
			"You have likely walked through the dense mountain woods by now. The trails are winding, so moving swiftly and deftly is key. Would you like to hear about efficient movement? #Yes?",
			null);

		npc.add(
			ConversationStates.INFORMATION_7,
			ConversationPhrases.YES_MESSAGES,
			null,
			ConversationStates.INFORMATION_8,
			"It is quite simple: double-click on any destination tile to pathfind there automatically. Would you like a sturdy piece of defensive gear to aid your journey? #Yes?",
			null);

		final String epilog = "Remember to keep your wits about you in the mountain crags!\n";

		// This is used if the player returns, asks for #help and then says #yes
		npc.add(ConversationStates.ATTENDING,
			ConversationPhrases.YES_MESSAGES, new QuestCompletedCondition(QUEST_SLOT),
			ConversationStates.ATTENDING, 
			epilog + "You remind me of myself in my younger years.",
			null);

		final List<ChatAction> reward2 = new LinkedList<ChatAction>();
		reward2.add(new EquipItemAction("studded shield"));
		reward2.add(new IncreaseXPAction(20));
		reward2.add(new SetQuestAction(QUEST_SLOT, "done"));

		npc.add(ConversationStates.INFORMATION_8,
			ConversationPhrases.YES_MESSAGES, new QuestNotCompletedCondition(QUEST_SLOT),
			ConversationStates.IDLE, 
			epilog + "May fortune favor your blade! This studded shield should protect you well against wild beasts. Safe travels!",
			new MultipleActions(reward2));

		npc.add(new ConversationStates[] { ConversationStates.ATTENDING,
					ConversationStates.INFORMATION_1,
					ConversationStates.INFORMATION_2,
					ConversationStates.INFORMATION_3,
					ConversationStates.INFORMATION_4,
					ConversationStates.INFORMATION_5,
					ConversationStates.INFORMATION_6,
					ConversationStates.INFORMATION_7,
					ConversationStates.INFORMATION_8},
				ConversationPhrases.NO_MESSAGES, new NotCondition(new QuestInStateCondition(QUEST_SLOT, "start")), ConversationStates.IDLE,
				"I hope someone stops by to chat again soon.",
				null);

		npc.setPlayerChatTimeout(TIME_OUT); 
	}

	@Override
	public void addToWorld() {
		fillQuestInfo(
				"Meet Pietrek",
				"Pietrek guides new adventurers in learning combat, looting, and survival basics.",
				false);
		preparePietrek();
	}

	@Override
	public String getName() {
		return "MeetPietrek";
	}
	@Override
	public String getNPCName() {
		return "Pietrek";
	}
}
