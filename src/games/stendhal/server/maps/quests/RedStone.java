/* $Id: RedStone.java,v 1.65 2012/11/18 03:53:53 tigertoes Exp $ */
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
import java.util.LinkedList;
import java.util.List;

import games.stendhal.common.Rand;
import games.stendhal.common.parser.Sentence;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.entity.item.StackableItem;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.DropItemAction;
import games.stendhal.server.entity.npc.action.EquipItemAction;
import games.stendhal.server.entity.npc.action.IncreaseXPAction;
import games.stendhal.server.entity.npc.action.MultipleActions;
import games.stendhal.server.entity.npc.action.SayTimeRemainingAction;
import games.stendhal.server.entity.npc.action.SetQuestAndModifyKarmaAction;
import games.stendhal.server.entity.npc.action.SetQuestToTimeStampAction;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.GreetingMatchesNameCondition;
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.PlayerHasItemWithHimCondition;
import games.stendhal.server.entity.npc.condition.QuestInStateCondition;
import games.stendhal.server.entity.npc.condition.QuestNotInStateCondition;
import games.stendhal.server.entity.npc.condition.QuestNotStartedCondition;
import games.stendhal.server.entity.npc.condition.QuestStartedCondition;
import games.stendhal.server.entity.npc.condition.TimePassedCondition;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.Region;

/**
 * QUEST: Campfire
 * 
 * PARTICIPANTS:
 * <ul>
 * <li> Sally, a scout sitting next to a campfire near Or'ril</li>
 * </ul>
 * 
 * STEPS:
 * <ul>
 * <li> Sally asks you for wood for her campfire</li>
 * <li> You collect 10 pieces of wood in the forest</li>
 * <li> You give the wood to Sally.</li>
 * <li> Sally gives you 10 meat or ham in return.<li>
 * </ul>
 * 
 * REWARD:
 * <ul> 
 * <li> 10 meat or ham</li>
 * <li> 50 XP</li>
 * <li> Karma: 10</li>
 * </ul>
 * 
 * REPETITIONS: 
 * <ul>
 * <li> Unlimited, but 5 minutes of waiting are required between repetitions</li>
 * </ul>
 */
public class RedStone extends AbstractQuest {

	private static final int REQUIRED_WOOD = 100;
	
	private static final int REQUIRED_MINUTES = 240;

	private static final String QUEST_SLOT = "redstone";

	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}
	
	@Override
	public boolean isCompleted(final Player player) {
		return player.hasQuest(QUEST_SLOT) && !"start".equals(player.getQuest(QUEST_SLOT)) && !"rejected".equals(player.getQuest(QUEST_SLOT));
	}

	@Override
	public boolean isRepeatable(final Player player) {
		return new AndCondition(new QuestNotInStateCondition(QUEST_SLOT, "start"), new QuestStartedCondition(QUEST_SLOT), new TimePassedCondition(QUEST_SLOT,REQUIRED_MINUTES)).fire(player, null, null);
	}

	@Override
	public List<String> getHistory(final Player player) {
		final List<String> res = new ArrayList<String>();
		if (!player.hasQuest(QUEST_SLOT)) {
			return res;
		}
		res.add("I met Miriri.");
		final String questState = player.getQuest(QUEST_SLOT);
		if ("rejected".equals(questState)) {
			res.add("I did not want a stone from Miriri.");
			return res;
		}
		res.add("I agreed to buy a stone from Miriri.");
		if ((player.isEquipped("money", REQUIRED_WOOD)) || isCompleted(player)) {
			res.add("I need to bring the money.");
		}
		if (isCompleted(player)) {
			res.add("I gave Miriri the money and received a beautiful red rough stone.");
		}
		if (isRepeatable(player)) {
			res.add("I can return to Miriri for another stone.");
		} 
		return res;
	}

	private void prepareRequestingStep() {
		final SpeakerNPC npc = npcs.get("Miriri");

		// player returns with the promised wood
		npc.add(ConversationStates.IDLE, 
			ConversationPhrases.GREETING_MESSAGES,
			new AndCondition(new GreetingMatchesNameCondition(npc.getName()),
					new QuestInStateCondition(QUEST_SLOT, "start"), new PlayerHasItemWithHimCondition("money", REQUIRED_WOOD)),
			ConversationStates.QUEST_ITEM_BROUGHT, 
			"Welcome back! Do you have the 100 money we talked about?",
			null);

		// player returns without promised wood
		npc.add(ConversationStates.IDLE, 
			ConversationPhrases.GREETING_MESSAGES,
		    new	AndCondition(new GreetingMatchesNameCondition(npc.getName()),
					new QuestInStateCondition(QUEST_SLOT, "start"), new NotCondition(new PlayerHasItemWithHimCondition("money", REQUIRED_WOOD))),
			ConversationStates.ATTENDING, 
			"Good to see you. Bring me the 100 money quickly and you'll get the red stone!",
			null);

		// first chat of player with sally
		npc.add(ConversationStates.IDLE, 
			ConversationPhrases.GREETING_MESSAGES,
			new AndCondition(new GreetingMatchesNameCondition(npc.getName()),
					new QuestNotStartedCondition(QUEST_SLOT)),
			ConversationStates.ATTENDING, "Greetings! I have a special offer for you.",
			null);

		// player who is rejected or 'done' but waiting to start again, returns
		npc.add(ConversationStates.IDLE, 
			ConversationPhrases.GREETING_MESSAGES,
			new AndCondition(new GreetingMatchesNameCondition(npc.getName()),
					new QuestNotInStateCondition(QUEST_SLOT, "start"),
					new QuestStartedCondition(QUEST_SLOT)),
			ConversationStates.ATTENDING,
			"Welcome back.", 
			null);
		
		// if they ask for quest while on it, remind them
		npc.add(ConversationStates.ATTENDING,
			ConversationPhrases.QUEST_MESSAGES, 
			new QuestInStateCondition(QUEST_SLOT, "start"),
			ConversationStates.ATTENDING,
			"We already agreed that you would deliver the coins. 100 money, remember?",
			null);

		// first time player asks/ player had rejected
		npc.add(ConversationStates.ATTENDING,
				ConversationPhrases.QUEST_MESSAGES,
				new QuestNotStartedCondition(QUEST_SLOT),
				ConversationStates.QUEST_OFFERED, 
				"I have a gorgeous #red #stone. I can give it to you, but nothing in life is free. Bring me 100 money and it's yours. What do you say?",
				null);
		
		// player returns - enough time has passed
		npc.add(ConversationStates.ATTENDING,
				ConversationPhrases.QUEST_MESSAGES,
				new AndCondition(new QuestNotInStateCondition(QUEST_SLOT, "start"), new QuestStartedCondition(QUEST_SLOT), new TimePassedCondition(QUEST_SLOT,REQUIRED_MINUTES)),
				ConversationStates.QUEST_OFFERED, 
				"I have another red stone ready. I will trade it for another 100 money. Deal?",
				null);

		// player returns - enough time has passed
		npc.add(ConversationStates.ATTENDING,
				ConversationPhrases.QUEST_MESSAGES,
				new AndCondition(new QuestNotInStateCondition(QUEST_SLOT, "start"), new QuestStartedCondition(QUEST_SLOT), new NotCondition(new TimePassedCondition(QUEST_SLOT,REQUIRED_MINUTES))),
				ConversationStates.ATTENDING, 
				null,
				new SayTimeRemainingAction(QUEST_SLOT,REQUIRED_MINUTES,"Thank you for the coins! We can trade for another stone in "));

		// player is willing to help
		npc.add(ConversationStates.QUEST_OFFERED,
			ConversationPhrases.YES_MESSAGES,
			null,
			ConversationStates.ATTENDING,
			"I will wait here. Return promptly!",
			new SetQuestAndModifyKarmaAction(QUEST_SLOT, "start", 5.0));

		// player is not willing to help
		npc.add(ConversationStates.QUEST_OFFERED,
			ConversationPhrases.NO_MESSAGES,
			null,
			ConversationStates.ATTENDING,
			"I could use some money...",
			new SetQuestAndModifyKarmaAction(QUEST_SLOT, "rejected", -5.0));
	}

	private void prepareBringingStep() {
		final SpeakerNPC npc = npcs.get("Miriri");
		
		final List<ChatAction> reward = new LinkedList<ChatAction>();
		reward.add(new DropItemAction("money", REQUIRED_WOOD));
		reward.add(new EquipItemAction("red rough stone", 1));
		reward.add(new IncreaseXPAction(100));
		reward.add(new SetQuestToTimeStampAction(QUEST_SLOT));
		reward.add(new ChatAction() {
			@Override
			public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
				npc.say("Here is your red rough stone!");
			}
		});
		
		npc.add(ConversationStates.QUEST_ITEM_BROUGHT,
			ConversationPhrases.YES_MESSAGES, 
			new PlayerHasItemWithHimCondition("money", REQUIRED_WOOD),
			ConversationStates.ATTENDING, null,
			new MultipleActions(reward));

		// player said the money was for her but has dropped it from his bag or hands
		npc.add(ConversationStates.QUEST_ITEM_BROUGHT,
			ConversationPhrases.YES_MESSAGES, 
			new NotCondition(new PlayerHasItemWithHimCondition("money", REQUIRED_WOOD)),
			ConversationStates.ATTENDING, 
			"Hey! Where are the coins?",
			null);

		// player had wood but said it is not for sally
		npc.add(
			ConversationStates.QUEST_ITEM_BROUGHT,
			ConversationPhrases.NO_MESSAGES,
			null,
			ConversationStates.ATTENDING,
			"I hope you bring me those coins soon!",
			null);
	}

	@Override
	public void addToWorld() {
		fillQuestInfo(
				"Red Stone", 
				"Miriri will give me a red rough stone in exchange for some gold.", 
				true);
		prepareRequestingStep();
		prepareBringingStep();
	}

	@Override
	public String getName() {
		return "Redstone";
	}
	
	@Override
	public int getMinLevel() {
		return 0;
	}

	@Override
	public String getNPCName() {
		return "Miriri";
	}
	
	@Override
	public String getRegion() {
		return Region.ORRIL;
	}
}
