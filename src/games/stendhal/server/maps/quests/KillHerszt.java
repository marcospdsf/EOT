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
// Based on KillDhohrNuggetcutter.
package games.stendhal.server.maps.quests;

import games.stendhal.common.parser.Sentence;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.IncreaseKarmaAction;
import games.stendhal.server.entity.npc.action.MultipleActions;
import games.stendhal.server.entity.npc.action.SetQuestAction;
import games.stendhal.server.entity.npc.action.SetQuestAndModifyKarmaAction;
import games.stendhal.server.entity.npc.action.StartRecordingKillsAction;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.GreetingMatchesNameCondition;
import games.stendhal.server.entity.npc.condition.KilledForQuestCondition;
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.QuestInStateCondition;
import games.stendhal.server.entity.npc.condition.QuestStateStartsWithCondition;
import games.stendhal.server.entity.npc.condition.TimePassedCondition;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.util.TimeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import marauroa.common.Pair;

/**
 * QUEST: Kill Herszt
 * <p>
 * PARTICIPANTS:
 * <ul>
 * <li> Gazda Jędrzej
 * </ul>
 * 
 * STEPS:
 * <ul>
 * <li> Gazda Jędrzej asks you to kill remainging zbójniks and banits from area
 * <li> You go kill Herszt and you get the reward from Gazda Jędrzej
 * </ul>
 * <p>
 * REWARD:
 * <ul>
 * <li> mithril nugget
 * <li> 5000 XP
 * <li> Karma: 20
 * <li> Once base HP bonus of 10
 * </ul>
 * 
 * REPETITIONS:
 * <ul>
 * <li> not for players with red skull.
 * <li> from 30 level.
 * <li> after 14 days.
 * </ul>
 */

public class KillHerszt extends AbstractQuest {

	private static final String QUEST_SLOT = "kill_herszt";
	private static final String GAZDA_JEDRZEJ_BASEHP_QUEST_SLOT = "gazda_jedrzej_basehp";
	private static final String GAZDA_JEDRZEJ_NAGRODA_QUEST_SLOT = "gazda_jedrzej_nagroda";

	
	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}

	private void step_1() {
		final SpeakerNPC npc = npcs.get("Gazda Jędrzej");

		npc.add(ConversationStates.ATTENDING,
				ConversationPhrases.QUEST_MESSAGES,
				null,
				ConversationStates.QUEST_OFFERED,
				null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						if (!player.hasQuest(QUEST_SLOT) || player.getQuest(QUEST_SLOT).equals("rejected")) {
							raiser.say("We cannot rid our lands of the mountain brigands, especially their fierce Leader. Could you venture into their cave hideout and eliminate them?");
						} else if (player.getQuest(QUEST_SLOT, 0).equals("start")) {
							raiser.say("I already asked you to defeat the mountain brigands!");
							raiser.setCurrentState(ConversationStates.ATTENDING);
						} else if (player.getQuest(QUEST_SLOT).startsWith("killed;")) {
							final String[] tokens = player.getQuest(QUEST_SLOT).split(";");
							final long delay = 2 * TimeUtil.MILLISECONDS_IN_WEEK;
							final long timeRemaining = (Long.parseLong(tokens[1]) + delay) - System.currentTimeMillis();
							if (timeRemaining > 0) {
								raiser.say("Thank you deeply for your valor. The brigands might return later. Please check back in " + TimeUtil.approxTimeUntil((int) (timeRemaining / 1000L)) + ".");
								raiser.setCurrentState(ConversationStates.ATTENDING);
								return;
							}
							raiser.say("We need your heroic blade once more. Will you help protect our highlands?");
						} else {
							raiser.say("Thank you for your assistance. Our mountain trails feel safe again.");
							raiser.setCurrentState(ConversationStates.ATTENDING);
						}
					}
				});

		final HashMap<String, Pair<Integer, Integer>> toKill = new HashMap<String, Pair<Integer, Integer>>();
		toKill.put("mountain brigand leader", new Pair<Integer, Integer>(0,1));
		toKill.put("mountain brigand", new Pair<Integer, Integer>(0,4));
		toKill.put("mountain brigand courier", new Pair<Integer, Integer>(0,4));
		toKill.put("malicious mountain brigand", new Pair<Integer, Integer>(0,3));
		toKill.put("mountain brigand scout", new Pair<Integer, Integer>(0,3));
		toKill.put("elder mountain brigand", new Pair<Integer, Integer>(0,1));

		final List<ChatAction> actions = new LinkedList<ChatAction>();
		actions.add(new IncreaseKarmaAction(5.0));
		actions.add(new SetQuestAction(QUEST_SLOT, 0, "start"));
		actions.add(new StartRecordingKillsAction(QUEST_SLOT, 1, toKill));

		npc.add(ConversationStates.QUEST_OFFERED,
				ConversationPhrases.YES_MESSAGES,
				null,
				ConversationStates.ATTENDING,
				"Splendid! Find their lair in the mountain caves and make them pay for their crimes!",
				new MultipleActions(actions));

		npc.add(ConversationStates.QUEST_OFFERED,
				ConversationPhrases.NO_MESSAGES,
				null,
				ConversationStates.ATTENDING,
				"I understand. They are dangerous outlaws. I shall await a bolder warrior.",
				new SetQuestAndModifyKarmaAction(QUEST_SLOT, "rejected", -5.0));
	}

	private void step_2() {
		/* Player has to kill the zbojnik*/
	}

	private void step_3() {
		final SpeakerNPC npc = npcs.get("Gazda Jędrzej");

		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new GreetingMatchesNameCondition(npc.getName()),
						new QuestInStateCondition(QUEST_SLOT, 0, "start"),
						new NotCondition(new KilledForQuestCondition(QUEST_SLOT, 1))),
				ConversationStates.ATTENDING,
				null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("Go eliminate the Mountain Brigand Leader and his crew in the cave.");
				}
		});

		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new GreetingMatchesNameCondition(npc.getName()),
						new QuestInStateCondition(QUEST_SLOT, 0, "start"),
						new KilledForQuestCondition(QUEST_SLOT, 1)),
				ConversationStates.ATTENDING,
				null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("News of your glorious triumph preceded you! You are worthy of highest honor (+10 Max HP)! Remember, the path of heroism demands steadfast courage.");
							player.setQuest(GAZDA_JEDRZEJ_NAGRODA_QUEST_SLOT, "start");
							player.drop("brigand leader feather");
							player.addKarma(20.0);
							player.addXP(5000);
							final List<ChatAction> actions = new LinkedList<ChatAction>();
							if (!player.hasQuest(GAZDA_JEDRZEJ_BASEHP_QUEST_SLOT) || player.getQuest(GAZDA_JEDRZEJ_BASEHP_QUEST_SLOT).equals("rejected") || !player.getQuest(GAZDA_JEDRZEJ_BASEHP_QUEST_SLOT).equals("done")) {
								actions.add(new SetQuestAction(GAZDA_JEDRZEJ_BASEHP_QUEST_SLOT, "start"));
							}
							if (!"done".equals(player.getQuest(GAZDA_JEDRZEJ_BASEHP_QUEST_SLOT))) {
								player.setBaseHP(10 + player.getBaseHP());
								player.heal(10, true);
								player.setQuest(GAZDA_JEDRZEJ_BASEHP_QUEST_SLOT, "done");
							}
							player.setQuest(QUEST_SLOT, "killed;" + System.currentTimeMillis());
							player.setQuest(GAZDA_JEDRZEJ_NAGRODA_QUEST_SLOT, "done");
		 			}
				});
	}

	@Override
	public void addToWorld() {
		fillQuestInfo(
				"Zabij Herszta",
				"Gazda Jędrzej chce abym zajął się rozbójnikami, którzy swoją siedzibę mają w jaskini nie daleko miasta.",
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
			if (!isCompleted(player)) {
				res.add("Muszę zabić herszta i wszystkich jego kumpli na prośbę Gazdy Jęrzeja.");
			} else if(isRepeatable(player)){
				res.add("Gazda Jędrzej potrzebuje jeszcze raz pomocy i nagrodzi mnie, czy mam mu pomóc?");
			} else {
				res.add("Moja wyprawa na zbójników uspokoiła na jakiś czas nerwy Gazdy Jędrzeja.");
			}
			return res;
	}

	@Override
	public String getName() {
		return "KillHerszt";
	}

	// The kill requirements and surviving in the zone requires at least this level
	@Override
	public int getMinLevel() {
		return 30;
		}

	@Override
	public boolean isRepeatable(final Player player) {
		return new AndCondition(new QuestStateStartsWithCondition(QUEST_SLOT,"killed"),
				new TimePassedCondition(QUEST_SLOT, 1, 2*TimeUtil.MINUTES_IN_WEEK)).fire(player,null, null);
	}

	@Override
	public boolean isCompleted(final Player player) {
		return new QuestStateStartsWithCondition(QUEST_SLOT,"killed").fire(player, null, null);
	}

	@Override
	public String getNPCName() {
		return "Gazda Jędrzej";
	}
}
