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

import games.stendhal.common.grammar.Grammar;
import games.stendhal.common.parser.Sentence;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.entity.item.Item;
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
import games.stendhal.server.entity.npc.action.SetQuestAction;
import games.stendhal.server.entity.npc.action.SetQuestAndModifyKarmaAction;
import games.stendhal.server.entity.npc.action.StartRecordingRandomItemCollectionAction;
import games.stendhal.server.entity.npc.action.SayRequiredItemAction;
import games.stendhal.server.entity.npc.action.SayTimeRemainingAction;
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
import games.stendhal.server.entity.npc.condition.QuestStartedCondition;
import games.stendhal.server.entity.npc.condition.QuestStateStartsWithCondition;
import games.stendhal.server.entity.npc.condition.GreetingMatchesNameCondition;
import games.stendhal.server.entity.npc.condition.TimePassedCondition;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.util.TimeUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * QUEST: The złota ciupaga upgrading.
 * 
 * PARTICIPANTS:
 * <ul>
 * <li> Józek will upgrade for you the złota ciupaga.
 * </ul>
 * 
 * STEPS:
 * <ul>
 * <li> Józek tells you about the złota ciupaga z wąsem.
 * <li> He offers to upgrade a złota ciupaga for you if you bring him what it
 * needs.
 * <li> You give him all what he ask you.
 * <li> He tells you you must have killed a złota śmierć
 * <li> Józek upgrade the złota ciupaga to złota ciupaga z wąsem for you
 * </ul>
 * 
 * REWARD:
 * <ul>
 * <li>złota ciupaga z wąsem
 * <li>20000 XP
 * <li>100 Karma
 * </ul>
 * 
 * 
 * REPETITIONS:
 * <ul>
 * <li> None.
 * </ul>
 */
public class StuffForAndrzej extends AbstractQuest {

	private static final int REQUIRED_WAIT_DAYS = 1;

	private static final int REQUIRED_MINUTES = 360;

	private static final String QUEST_SLOT = "andrzej_make_zlota_ciupaga";

	private static final String GAZDA_JEDRZEJ_NAGRODA_QUEST_SLOT = "gazda_jedrzej_nagroda";

	private static Logger logger = Logger.getLogger(StuffForAndrzej.class);

	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}

	private void step_1() {
		final SpeakerNPC npc = npcs.get("Blacksmith Andrzej");

		npc.add(ConversationStates.ATTENDING,
			ConversationPhrases.QUEST_MESSAGES, null,
			ConversationStates.QUEST_OFFERED, null,
			new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					if (player.isQuestCompleted(GAZDA_JEDRZEJ_NAGRODA_QUEST_SLOT)) {
						if(player.getLevel() >= 120) {
							if(player.getKarma() >=100) {
								if (!player.hasQuest(QUEST_SLOT) || "rejected".equals(player.getQuest(QUEST_SLOT))) {
									raiser.say("Some time ago I crafted a golden highlander axe. I can forge one for you as well. Are you interested?");
								} else if (player.getQuest(QUEST_SLOT).startsWith("done;")) {
									final String[] waittokens = player.getQuest(QUEST_SLOT).split(";");
									final long waitdelay = REQUIRED_WAIT_DAYS * TimeUtil.MILLISECONDS_IN_DAY;
									final long waittimeRemaining = (Long.parseLong(waittokens[1]) + waitdelay) - System.currentTimeMillis();
									if (waittimeRemaining > 0) {
										raiser.say("I am exhausted from smithing. Return in " + TimeUtil.approxTimeUntil((int) (waittimeRemaining / 1000L)) + ".");
									} else {
										raiser.say("Do you require another golden highlander axe?");
										raiser.setCurrentState(ConversationStates.QUEST_OFFERED);
									}
								} else if (player.getQuest(QUEST_SLOT).startsWith("make;")) {
									raiser.say("Why do you disturb me? I am still working on your axe." );
									raiser.setCurrentState(ConversationStates.ATTENDING);
								}
							} else {
								npc.say("You are not yet worthy to wield such a fine weapon. Perform more noble deeds; your karma must be at least 100.");
								raiser.setCurrentState(ConversationStates.ATTENDING);
							}
						} else {
							npc.say("Your standing is too low for this task. Return when you reach level 120.");
							raiser.setCurrentState(ConversationStates.ATTENDING);
						}
					} else {
						npc.say("You have not yet earned the respect of Gazda Jędrzej.");
						raiser.setCurrentState(ConversationStates.ATTENDING);
					}
				}
			});

		npc.add(ConversationStates.QUEST_OFFERED,
			ConversationPhrases.YES_MESSAGES, null,
			ConversationStates.ATTENDING, null,
			new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {

					raiser.say("I will require several #items to forge the axe.");
					player.setQuest(QUEST_SLOT, "start");
					player.addKarma(10);

				}
			});

		npc.add(ConversationStates.QUEST_OFFERED,
			ConversationPhrases.NO_MESSAGES, null,
			ConversationStates.IDLE,
			"Your loss.",
			new SetQuestAndModifyKarmaAction(QUEST_SLOT, "rejected", -10.0));

	}

	private void step_2() {
		final SpeakerNPC npc = npcs.get("Blacksmith Andrzej");


		final List<ChatAction> ciupagaactions = new LinkedList<ChatAction>();
		ciupagaactions.add(new DropItemAction("highlander walking axe",1));
		ciupagaactions.add(new DropItemAction("gold bar",25));
		ciupagaactions.add(new DropItemAction("wood",5));
		ciupagaactions.add(new DropItemAction("money",50000));
		ciupagaactions.add(new SetQuestAction(QUEST_SLOT, "make;" + System.currentTimeMillis()));

		npc.add(ConversationStates.ATTENDING, Arrays.asList("rzeczy", "przedmiot", "nagroda", "highlander walking axe", "przypomnij"),
				new AndCondition(new QuestInStateCondition(QUEST_SLOT,"start"),
								 new PlayerHasItemWithHimCondition("highlander walking axe",1),
								 new PlayerHasItemWithHimCondition("gold bar",25),
								 new PlayerHasItemWithHimCondition("wood",5),
								 new PlayerHasItemWithHimCondition("money",50000)),
				ConversationStates.ATTENDING, "I see you have brought everything I requested. Return in 6 hours and the golden axe will be ready.",
				new MultipleActions(ciupagaactions));

		npc.add(ConversationStates.ATTENDING, Arrays.asList("rzeczy", "przedmiot", "nagroda", "highlander walking axe", "przypomnij"),
				new AndCondition(new QuestInStateCondition(QUEST_SLOT,"start"),
								 new NotCondition(
								 new AndCondition(new PlayerHasItemWithHimCondition("highlander walking axe",1),
												  new PlayerHasItemWithHimCondition("gold bar",25),
												  new PlayerHasItemWithHimCondition("wood",5),
												  new PlayerHasItemWithHimCondition("money",50000)))),
				ConversationStates.ATTENDING, "I need:\n"
									+"#'1 highlander walking axe'\n"
									+"#'25 gold bars'\n"
									+"#'5 wood'\n"
									+"#'50000 money'\n"
									+"Please bring everything at once."
									+" If you forget what is needed, say #remind. Thank you!", null);

	}

	private void step_3() { 
		final SpeakerNPC npc = npcs.get("Blacksmith Andrzej");

		npc.add(ConversationStates.ATTENDING,
			Arrays.asList("highlander walking axe", "złota", "nagroda"),
			new AndCondition(new GreetingMatchesNameCondition(npc.getName()),
			new QuestStateStartsWithCondition(QUEST_SLOT, "make;")),
			ConversationStates.IDLE, null, new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					final String[] tokens = player.getQuest(QUEST_SLOT).split(";");
					final long delay = REQUIRED_MINUTES * TimeUtil.MILLISECONDS_IN_MINUTE; 
					final long timeRemaining = (Long.parseLong(tokens[1]) + delay)
							- System.currentTimeMillis();

					if (timeRemaining > 0L) {
						raiser.say("I am still working on your commission. Return in "
							+ TimeUtil.approxTimeUntil((int) (timeRemaining / 1000L))
							+ " to receive your axe.");
						return;
					}

					raiser.say("It was worth the wait! Here is your golden highlander axe. Farewell!");
					player.addXP(15000);
					player.addKarma(25);
					final Item zlotaCiupaga = SingletonRepository.getEntityManager().getItem("golden highlander axe");
					zlotaCiupaga.setBoundTo(player.getName());
					player.equipOrPutOnGround(zlotaCiupaga);
					player.notifyWorldAboutChanges();
					player.setQuest(GAZDA_JEDRZEJ_NAGRODA_QUEST_SLOT, "rejected");
					player.setQuest(QUEST_SLOT, "done" + ";" + System.currentTimeMillis());

				}
			});
	}


	@Override
	public void addToWorld() {
		fillQuestInfo(
			"Golden Highlander Axe",
			"Blacksmith Andrzej can forge a golden highlander axe if you prove worthy.",
			false);

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
		res.add("I met Blacksmith Andrzej in the Zakopane forge.");
		res.add("Blacksmith Andrzej offered to forge a golden highlander axe for me in exchange for: 1 highlander walking axe, 25 gold bars, 5 wood, and 50000 money.");
		if ("rejected".equals(questState)) {
			res.add("I have no need for an axe.");
			return res;
		} 
		if ("start".equals(questState)) {
			return res;
		}
		res.add("I delivered all required items to Andrzej.");
		if (questState.startsWith("make")) {
			if (new TimePassedCondition(QUEST_SLOT,1,REQUIRED_MINUTES).fire(player, null, null)) {
				res.add("I will visit Andrzej to collect my axe. Keyword: axe.");
			} else {
				res.add("I must wait 6 hours for Andrzej to finish forging. Keyword: axe.");
			}
			return res;
		}
		if (isCompleted(player)) {
			res.add("Blacksmith Andrzej presented me with the golden highlander axe.");
		} else if(isRepeatable(player)) {
			res.add("Blacksmith Andrzej is ready to forge another golden axe for me!");
		} else {
			res.add("Blacksmith Andrzej is busy now; I should return later.");
		}
		return res;
	}

	@Override
	public String getName() {
		return "StuffForAndrzej";
	}
	@Override
	public String getNPCName() {
		return "Blacksmith Andrzej";
	}
}