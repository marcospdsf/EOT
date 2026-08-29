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
// Based on HatForMonogenes.
package games.stendhal.server.maps.quests;

import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.DropItemAction;
import games.stendhal.server.entity.npc.action.IncreaseKarmaAction;
import games.stendhal.server.entity.npc.action.IncreaseXPAction;
import games.stendhal.server.entity.npc.action.MultipleActions;
import games.stendhal.server.entity.npc.action.SetQuestAction;
import games.stendhal.server.entity.npc.action.SetQuestAndModifyKarmaAction;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.GreetingMatchesNameCondition;
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.PlayerHasItemWithHimCondition;
import games.stendhal.server.entity.npc.condition.QuestCompletedCondition;
import games.stendhal.server.entity.npc.condition.QuestInStateCondition;
import games.stendhal.server.entity.npc.condition.QuestNotCompletedCondition;
import games.stendhal.server.entity.player.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * QUEST: Scythe For Fryderyk 
 * 
 * PARTICIPANTS: 
 * <ul>
 * <li>Monogenes, an old man in Semos city.</li>
 * </ul>
 * 
 * STEPS:
 * <ul> 
 * <li> Monogenes asks you to buy a hat for him.</li>
 * <li> Xin Blanca sells you a leather helmet.</li>
 * <li> Monogenes sees your leather helmet and asks for it and then thanks you.</li>
 * </ul>
 * 
 * REWARD: 
 * <ul>
 * <li>50 XP</li>
 * <li>Karma: 10</li>
 * </ul>
 * 
 * REPETITIONS: - None.
 */
public class ScytheForFryderyk extends AbstractQuest {
	private static final String QUEST_SLOT = "scythe_fryderyk";

	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}

	@Override
	public List<String> getHistory(final Player player) {
		final List<String> res = new ArrayList<String>();
		if (player.hasQuest(QUEST_SLOT)) {
			res.add("I met Fryderyk in Zakopane.");
		}
		if (!player.hasQuest(QUEST_SLOT)) {
			return res;
		}
		res.add("I promised to bring Fryderyk a scythe.");
		if ((player.isQuestInState(QUEST_SLOT, "start") 
				&& player.isEquipped("scythe"))
				|| player.isQuestCompleted(QUEST_SLOT)) {
			res.add("I found a scythe.");
		}
		if (player.isQuestCompleted(QUEST_SLOT)) {
			res.add("I delivered the scythe to Fryderyk.");
		}
		return res;
	}

	private void createRequestingStep() {
		final SpeakerNPC fryderyk = npcs.get("Fryderyk");

		fryderyk.add(ConversationStates.ATTENDING,
			ConversationPhrases.QUEST_MESSAGES,
			new QuestNotCompletedCondition(QUEST_SLOT),
			ConversationStates.QUEST_OFFERED, 
			"Could you bring me a #scythe? I need one for the harvest season.",
			null);

		fryderyk.add(ConversationStates.ATTENDING,
			ConversationPhrases.QUEST_MESSAGES,
			new QuestCompletedCondition(QUEST_SLOT),
			ConversationStates.ATTENDING,
			"Thank you for the offer, good traveler, but this scythe will last me for years and I have no need for another.",
			null);

		fryderyk.add(
			ConversationStates.QUEST_OFFERED,
			ConversationPhrases.YES_MESSAGES,
			null,
			ConversationStates.ATTENDING,
			"Thank you, friend! I will await your return here.",
			new SetQuestAndModifyKarmaAction(QUEST_SLOT, "start", 5.0));

		fryderyk.add(
			ConversationStates.QUEST_OFFERED,
			ConversationPhrases.NO_MESSAGES,
			null,
			ConversationStates.ATTENDING,
			"I'm sure you have more important matters to attend to. I shall just stand here while my crops wither in the field... *sniff*",
			new SetQuestAndModifyKarmaAction(QUEST_SLOT, "rejected", -5.0));

		fryderyk.add(
			ConversationStates.QUEST_OFFERED,
			"scythe",
			null,
			ConversationStates.QUEST_OFFERED,
			"You don't know what a scythe is?! It's the quintessential agricultural tool for harvesting grain—a curved steel blade mounted on a long wooden haft. Will you fetch one for me?",
			null);
	}

	private void createBringingStep() {
		final SpeakerNPC fryderyk = npcs.get("Fryderyk");

		fryderyk.add(ConversationStates.IDLE,
			ConversationPhrases.GREETING_MESSAGES,
			new AndCondition(new GreetingMatchesNameCondition(fryderyk.getName()),
					new QuestInStateCondition(QUEST_SLOT, "start"),
					new PlayerHasItemWithHimCondition("scythe")),
			ConversationStates.QUEST_ITEM_BROUGHT,
			"Hey there! Is that scythe for me?", null);

		fryderyk.add(ConversationStates.IDLE,
			ConversationPhrases.GREETING_MESSAGES,
			new AndCondition(new GreetingMatchesNameCondition(fryderyk.getName()),
					new QuestInStateCondition(QUEST_SLOT, "start"),
					new NotCondition(new PlayerHasItemWithHimCondition("scythe"))),
			ConversationStates.ATTENDING,
			"Hello, my friend! Do you remember the scythe you promised me? My ripe fields are waiting...",
			null);

		final List<ChatAction> reward = new LinkedList<ChatAction>();
		reward.add(new DropItemAction("scythe"));
		reward.add(new IncreaseXPAction(50));
		reward.add(new IncreaseKarmaAction(10));
		reward.add(new SetQuestAction(QUEST_SLOT, "done"));

		// make sure the player isn't cheating by putting the
		// item away and then saying "yes"
		fryderyk.add(
			ConversationStates.QUEST_ITEM_BROUGHT,
			ConversationPhrases.YES_MESSAGES,
			new PlayerHasItemWithHimCondition("scythe"),
			ConversationStates.ATTENDING,
			"Bless you, my good friend! Now I can finally harvest my grain before the frost comes.",
			new MultipleActions(reward));

		fryderyk.add(
			ConversationStates.QUEST_ITEM_BROUGHT,
			ConversationPhrases.NO_MESSAGES,
			null,
			ConversationStates.ATTENDING,
			"Someone was feeling generous today... *achoo*.",
			null);
	}

	@Override
	public void addToWorld() {
    		fillQuestInfo(
				"Scythe for Fryderyk",
				"Fryderyk needs a sturdy scythe to harvest his grain. Bring one to him.",
				false);
		createRequestingStep();
		createBringingStep();
	}

	@Override
	public String getName() {
		return "ScytheForFryderyk";
	}
	@Override
	public String getNPCName() {
		return "Fryderyk";
	}
}
