/* $Id: BlueStone.java,v 1.65 2012/11/18 03:53:53 tigertoes Exp $ */
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
public class BlueStone extends AbstractQuest {

	private static final int REQUIRED_WOOD = 500;
	
	private static final int REQUIRED_MINUTES = 480;

	private static final String QUEST_SLOT = "bluestone";

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
		res.add("Spotkałem Pariri");
		final String questState = player.getQuest(QUEST_SLOT);
		if ("rejected".equals(questState)) {
			res.add("Nie chcę kamienia od Pariri");
			return res;
		}
		res.add("Chcę kamień od Pariri");
		if ((player.isEquipped("money", REQUIRED_WOOD)) || isCompleted(player)) {
			res.add("Musze przynieść pieniadze.");
		}
		if (isCompleted(player)) {
			res.add("Dałem Pariri pieniądze. Dostałem przepiękny niebieski kamień");
		}
		if(isRepeatable(player)){
			res.add("Mogę iść po nowy kamień do Pariri.");
		} 
		return res;
	}



	private void prepareRequestingStep() {
		final SpeakerNPC npc = npcs.get("Pariri");

		// player returns with the promised wood
		npc.add(ConversationStates.IDLE, 
			ConversationPhrases.GREETING_MESSAGES,
			new AndCondition(new GreetingMatchesNameCondition(npc.getName()),
					new QuestInStateCondition(QUEST_SLOT, "start"), new PlayerHasItemWithHimCondition("money", REQUIRED_WOOD)),
			ConversationStates.QUEST_ITEM_BROUGHT, 
			"Witaj znowu! Masz te 500 money o których wcześniej rozmawialiśmy?",
			null);

		//player returns without promised wood
		npc.add(ConversationStates.IDLE, 
			ConversationPhrases.GREETING_MESSAGES,
		    new	AndCondition(new GreetingMatchesNameCondition(npc.getName()),
					new QuestInStateCondition(QUEST_SLOT, "start"), new NotCondition(new PlayerHasItemWithHimCondition("money", REQUIRED_WOOD))),
			ConversationStates.ATTENDING, 
			"Dobrze, że jesteś. Przynieś szybko te 500 money a dostaniesz niebieski kamień!",
			null);

		// first chat of player with sally
		npc.add(ConversationStates.IDLE, 
			ConversationPhrases.GREETING_MESSAGES,
			new AndCondition(new GreetingMatchesNameCondition(npc.getName()),
					new QuestNotStartedCondition(QUEST_SLOT)),
			ConversationStates.ATTENDING, "Cześć! Mam dla ciebie specjalne #zadanie",
			null);

		// player who is rejected or 'done' but waiting to start again, returns
		npc.add(ConversationStates.IDLE, 
			ConversationPhrases.GREETING_MESSAGES,
			new AndCondition(new GreetingMatchesNameCondition(npc.getName()),
					new QuestNotInStateCondition(QUEST_SLOT, "start"),
					new QuestStartedCondition(QUEST_SLOT)),
			ConversationStates.ATTENDING,
			"Witaj ponownie.", 
			null);
		
		// if they ask for quest while on it, remind them
		npc.add(ConversationStates.ATTENDING,
			ConversationPhrases.QUEST_MESSAGES, 
			new QuestInStateCondition(QUEST_SLOT, "start"),
			ConversationStates.ATTENDING,
			"Już się umówiliśmy, że dotarczysz pieniądze. 500 money, pamintsz?",
			null);

		// first time player asks/ player had rejected
		npc.add(ConversationStates.ATTENDING,
				ConversationPhrases.QUEST_MESSAGES,
				new QuestNotStartedCondition(QUEST_SLOT),
				ConversationStates.QUEST_OFFERED, 
				"Mam przepiękny #niebieski #kamień. Moge ci go dać ale nie ma nic za darmo. Przynieś mi 500 money a dam ci kamień. Co ty na to ?",
				null);
		
		// player returns - enough time has passed
		npc.add(ConversationStates.ATTENDING,
				ConversationPhrases.QUEST_MESSAGES,
				new AndCondition(new QuestNotInStateCondition(QUEST_SLOT, "start"), new QuestStartedCondition(QUEST_SLOT), new TimePassedCondition(QUEST_SLOT,REQUIRED_MINUTES)),
				ConversationStates.QUEST_OFFERED, 
				"Mam kolejny niebieski kamień. Dam ci go za kolejne 500 money. Pasuje?",
				null);

		// player returns - enough time has passed
		npc.add(ConversationStates.ATTENDING,
				ConversationPhrases.QUEST_MESSAGES,
				new AndCondition(new QuestNotInStateCondition(QUEST_SLOT, "start"), new QuestStartedCondition(QUEST_SLOT), new NotCondition(new TimePassedCondition(QUEST_SLOT,REQUIRED_MINUTES))),
				ConversationStates.ATTENDING, 
				null,
				new SayTimeRemainingAction(QUEST_SLOT,REQUIRED_MINUTES,"Dzięki za pieniądze. W sprawie kolejnego kamienia porozmawiamy za"));

		// player is willing to help
		npc.add(ConversationStates.QUEST_OFFERED,
			ConversationPhrases.YES_MESSAGES,
			null,
			ConversationStates.ATTENDING,
			"Będe czekać. Postaraj się szybko wrócić!",
			new SetQuestAndModifyKarmaAction(QUEST_SLOT, "start", 5.0));

		// player is not willing to help
		npc.add(ConversationStates.QUEST_OFFERED,
			ConversationPhrases.NO_MESSAGES,
			null,
			ConversationStates.ATTENDING,
			"Potrzebuje trochę pieniędzy..",
			new SetQuestAndModifyKarmaAction(QUEST_SLOT, "rejected", -5.0));
	}

	private void prepareBringingStep() {
		final SpeakerNPC npc = npcs.get("Pariri");
		// player has wood and tells sally, yes, it is for her
		
		final List<ChatAction> reward = new LinkedList<ChatAction>();
		reward.add(new DropItemAction("money", REQUIRED_WOOD));
		reward.add(new EquipItemAction("niebieski kamień", 1));
		reward.add(new IncreaseXPAction(500));
		reward.add(new SetQuestToTimeStampAction(QUEST_SLOT));
		reward.add(new ChatAction() {
			@Override
			public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
				String rewardClass;
				if (Rand.throwCoin() == 1) {
					rewardClass = "niebieski kamień";
				} else {
					rewardClass = "niebieski kamień";
				}
				npc.say("Oto twój " + rewardClass + "!");
				final StackableItem reward = (StackableItem) SingletonRepository.getEntityManager().getItem(rewardClass);
				reward.setQuantity(REQUIRED_WOOD);
				player.equipOrPutOnGround(reward);
				player.notifyWorldAboutChanges();
			}
		});
		
		npc.add(ConversationStates.QUEST_ITEM_BROUGHT,
			ConversationPhrases.YES_MESSAGES, 
			new PlayerHasItemWithHimCondition("money", REQUIRED_WOOD),
			ConversationStates.ATTENDING, null,
			new MultipleActions(reward));

		//player said the money was for her but has dropped it from his bag or hands
		npc.add(ConversationStates.QUEST_ITEM_BROUGHT,
			ConversationPhrases.YES_MESSAGES, 
			new NotCondition(new PlayerHasItemWithHimCondition("money", REQUIRED_WOOD)),
			ConversationStates.ATTENDING, 
			"Hej! Gdzie są pieniądze?",
			null);

		// player had wood but said it is not for sally
		npc.add(
			ConversationStates.QUEST_ITEM_BROUGHT,
			ConversationPhrases.NO_MESSAGES,
			null,
			ConversationStates.ATTENDING,
			"Mam nadzieje, że zaraz przyniesiesz mi te monety!",
			null);
	}

	@Override
	public void addToWorld() {
		//super.addToWorld();
		fillQuestInfo(
				"Niebieski kamień", 
				"Pariri da mi niebieski kamień za trochę złota.", 
				true);
		prepareRequestingStep();
		prepareBringingStep();
	}

	@Override
	public String getName() {
		return "Bluestone";
	}
	
	@Override
	public int getMinLevel() {
		return 0;
	}

	@Override
	public String getNPCName() {
		return "Pariri";
	}
	
	@Override
	public String getRegion() {
		return Region.ORRIL;
	}
}
