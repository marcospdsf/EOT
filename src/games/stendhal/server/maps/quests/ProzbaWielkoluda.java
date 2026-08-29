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
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.entity.item.Item;
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
 * QUEST: Kill pokutniki
 * <p>
 * PARTICIPANTS:
 * <ul>
 * <li> Wielkolud
 * </ul>
 * 
 * STEPS:
 * <ul>
 * <li> Wielkolud asks you to kill remainging pokutniki from area
 * <li> You go kill lawina kamienna and you get the reward from wielkolud
 * </ul>
 * <p>
 * REWARD:
 * <ul>
 * <li> mithril nugget
 * <li> 10000 XP
 * <li>25 karma in total
 * </ul>
 * 
 * REPETITIONS:
 * <ul>
 * <li> after 14 days.
 * </ul>
 */

public class ProzbaWielkoluda extends AbstractQuest {

	private static final String QUEST_SLOT = "prozba_wielkoluda";

	private static final String POMOC_DLA_WIELKOLUDA_QUEST_SLOT = "pomoc_dla_wielkoluda";
	private static final String NAGRODA_WIELKOLUDA_QUEST_SLOT = "nagroda_wielkoluda";

	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}

	private void step_1() {
		final SpeakerNPC npc = npcs.get("Wielkolud");

		npc.add(ConversationStates.ATTENDING,
				ConversationPhrases.QUEST_MESSAGES, 
				null,
				ConversationStates.QUEST_OFFERED, 
				null,
				new ChatAction() {
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						if (!player.hasQuest(QUEST_SLOT) || player.getQuest(QUEST_SLOT).equals("rejected")) {
							raiser.say("Far too many aggressive spirits roam these mountain meadows, disrupting my trade. Will you help eliminate them?");
						}  else if (player.getQuest(QUEST_SLOT, 0).equals("start")) {
							raiser.say("I already asked you to defeat the penitents and the stone avalanche!");
							raiser.setCurrentState(ConversationStates.ATTENDING);
						}  else if (player.getQuest(QUEST_SLOT).startsWith("killed;")) {
							final String[] tokens = player.getQuest(QUEST_SLOT).split(";");
							final long delay = 2 * TimeUtil.MILLISECONDS_IN_WEEK;
							final long timeRemaining = (Long.parseLong(tokens[1]) + delay) - System.currentTimeMillis();
							if (timeRemaining > 0) {
								raiser.say("Thank you deeply. For now they steer clear of my path. Return in " + TimeUtil.approxTimeUntil((int) (timeRemaining / 1000L)) + ".");
								raiser.setCurrentState(ConversationStates.ATTENDING);
								return;
							}
							raiser.say("Could you help me clear the mountain paths once more?");
						} else {
							raiser.say("Thank you for your help. Customers can reach my mountain stall safely now.");
							raiser.setCurrentState(ConversationStates.ATTENDING);
						}
					}
				});

		final HashMap<String, Pair<Integer, Integer>> toKill = new HashMap<String, Pair<Integer, Integer>>();
		toKill.put("stone avalanche", new Pair<Integer, Integer>(0,1));
		toKill.put("swamp penitent", new Pair<Integer, Integer>(1,0));
		toKill.put("moor penitent", new Pair<Integer, Integer>(1,0)); 
		toKill.put("night penitent", new Pair<Integer, Integer>(1,0));
		toKill.put("evening penitent", new Pair<Integer, Integer>(1,0));
		toKill.put("meadow penitent", new Pair<Integer, Integer>(1,0));

		final List<ChatAction> actions = new LinkedList<ChatAction>();
		actions.add(new IncreaseKarmaAction(5.0));
		actions.add(new SetQuestAction(QUEST_SLOT, 0, "start"));
		actions.add(new StartRecordingKillsAction(QUEST_SLOT, 1, toKill));

		npc.add(ConversationStates.QUEST_OFFERED,
				ConversationPhrases.YES_MESSAGES,
				null,
				ConversationStates.ATTENDING,
				"Splendid! Please eliminate those pesky #penitents around Kościelisko. The #'stone avalanche' in particular is a menace!",
				new MultipleActions(actions));

		npc.add(ConversationStates.QUEST_OFFERED, 
				ConversationPhrases.NO_MESSAGES,
				null,
				ConversationStates.ATTENDING,
				"I understand. Not everyone has the courage to face restless phantoms.",
				new SetQuestAndModifyKarmaAction(QUEST_SLOT, "rejected", -5.0));
	}

	private void step_2() {
		/* Player has to kill the pokutniki*/
	}

	private void step_3() {
		final SpeakerNPC npc = npcs.get("Wielkolud");

		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new GreetingMatchesNameCondition(npc.getName()),
						new QuestInStateCondition(QUEST_SLOT, 0, "start"),
						new NotCondition(new KilledForQuestCondition(QUEST_SLOT, 1))),
				ConversationStates.ATTENDING, 
				null,
				new ChatAction() {
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("You must defeat the stone avalanche and the penitents with your own blade! Go forth!");
				}
		});

		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new GreetingMatchesNameCondition(npc.getName()),
						new QuestInStateCondition(QUEST_SLOT, 0, "start"),
						new KilledForQuestCondition(QUEST_SLOT, 1)),
				ConversationStates.ATTENDING, 
				null,
				new ChatAction() {
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("You performed magnificently! Your valor and courage will be remembered for generations!");
						final Item pokutniki = SingletonRepository.getEntityManager()
								.getItem("four-leaf clover");
						player.equipOrPutOnGround(pokutniki);
						player.addKarma(25.0);
						player.addXP(10000);
						final List<ChatAction> actions = new LinkedList<ChatAction>();
						if (!player.hasQuest(POMOC_DLA_WIELKOLUDA_QUEST_SLOT) || player.getQuest(POMOC_DLA_WIELKOLUDA_QUEST_SLOT).equals("rejected") || !player.getQuest(POMOC_DLA_WIELKOLUDA_QUEST_SLOT).equals("done")) {
							actions.add(new SetQuestAction(POMOC_DLA_WIELKOLUDA_QUEST_SLOT, "start"));
						}
						if (!"done".equals(player.getQuest(NAGRODA_WIELKOLUDA_QUEST_SLOT))) {
							player.setBaseHP(10 + player.getBaseHP());
							player.heal(10, true);
							player.setQuest(NAGRODA_WIELKOLUDA_QUEST_SLOT, "done");
						}
						if (!"done".equals(player.getQuest(POMOC_DLA_WIELKOLUDA_QUEST_SLOT))) {
							player.setQuest(POMOC_DLA_WIELKOLUDA_QUEST_SLOT, "done");
						}
						player.setQuest(QUEST_SLOT, "killed;" + System.currentTimeMillis());
		 			}
				});

		npc.add(
			ConversationStates.ANY,
			"pokutników",
			null,
			ConversationStates.ATTENDING,
			"You must defeat alone: the marsh penitent, heath penitent, night penitent, evening penitent, and meadow penitent.",
			null);
	}

	@Override
	public void addToWorld() {
		fillQuestInfo(
			"The Giant's Request",
			"The Giant wants you to defeat the stone avalanche and the penitents of the swamp, moor, night, evening, and meadow single-handedly.",
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
			res.add("I must defeat the stone avalanche and all penitents at the Giant's request.");
		} else if(isRepeatable(player)){
			res.add("Wielkolud potrzebuje jeszcze raz pomocy i nagrodzi mnie, czy mam mu pomóc?");
		} else {
			res.add("Moja wyprawa na pokutniki i lawine uspokoiła na jakiś czas nerwy Wielkoluda.");
		}
		return res;	
	}

	@Override
	public String getName() {
		return "ProzbaWielkoluda";
	}

	// The kill requirements and surviving in the zone requires at least this level
	@Override
	public int getMinLevel() {
		return 100;
	}

	@Override
	public boolean isRepeatable(final Player player) {
		return new AndCondition(new QuestStateStartsWithCondition(QUEST_SLOT, "killed"),
				new TimePassedCondition(QUEST_SLOT, 1, 2*TimeUtil.MINUTES_IN_WEEK)).fire(player,null, null);
	}

	@Override
	public boolean isCompleted(final Player player) {
		return new QuestStateStartsWithCondition(QUEST_SLOT,"killed").fire(player, null, null);
	}

	@Override
	public String getNPCName() {
		return "Wielkolud";
	}
}
