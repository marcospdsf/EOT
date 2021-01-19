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
// Based on FishermansLicenseQuiz.

package games.stendhal.server.maps.quests;

import games.stendhal.common.parser.ConversationParser;
import games.stendhal.common.parser.Expression;
import games.stendhal.common.parser.JokerExprMatcher;
import games.stendhal.common.parser.Sentence;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.TriggerInListCondition;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.Region;
import games.stendhal.server.util.TimeUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import marauroa.common.game.RPObjectNotFoundException;

/**
 * QUEST: Staz na gornika.
 * 
 * PARTICIPANTS: 
 * <ul>
 * <li>Bercik the miner</li>
 * </ul>
 * 
 * STEPS:
 * <ul>
 * <li> The miner puts all stones onto the table and the player must
 *  	identify the names of the stone in the correct order.</li>
 * <li> The player has one try per day.</li>
 * </ul>
 * 
 * REWARD:
 * <ul>
 * <li> 500 XP</li>
 * <li> Karma: 15</li>
 * <li> Skill: mining</li>
 * <li> Kilof</li>
 * <li> Lina</li>
 * </ul>
 * 
 * REPETITIONS:
 * <ul>
 * <li> If the player has failed the quiz, he can retry after 24 hours.</li>
 * <li> After passing the quiz, no more repetitions are possible.</li>
 * </ul>
 * 
 * @author dine
 */

public class StazNaGornika extends AbstractQuest {
	static final String QUEST_SLOT = "cech_gornika";

	// TODO: use standard conditions and actions

	private final List<String> speciesList = Arrays.asList("emerald", "sapphire",
			"ruby", "obsidian", "diamond", "bursztyn", "ruda żelaza", "silver ore", "gold nugget", "bryłka mithrilu", "silver bar", "mithril bar", "gold bar");

	private int currentSpeciesNo;

	private static StendhalRPZone zone = SingletonRepository.getRPWorld().getZone(
			"int_koscielisko_stones_room");

	private Item miningOnTable;

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
		res.add("I met Bercik. After passing the miner's exam, my chances of mining stones will increase.");
		if (!player.isQuestCompleted(QUEST_SLOT)) {
			if (remainingTimeToWait(player)>0) {
				res.add("It is too early to try again and take the exam.");
			} else {
				res.add("It's been a long time since I failed the last exam, I can now try again.");
			}
		} else {
			res.add("I passed the exam with a positive result. Now my chances of finding precious stones are much higher.");
		}
		return res;
	}

	public void cleanUpTable() {
		if (miningOnTable != null) {
			try {
				zone.remove(miningOnTable);
			} catch (final RPObjectNotFoundException e) {
				// The item timed out, or an admin destroyed it.
				// So no need to clean up the table.
			}
			miningOnTable = null;
		}
	}

	private void startQuiz() {
		Collections.shuffle(speciesList);
		currentSpeciesNo = -1;

		putNextMiningOnTable();
	}

	private String getCurrentSpecies() {
		return speciesList.get(currentSpeciesNo);
	}

	private void putNextMiningOnTable() {
		currentSpeciesNo++;
		cleanUpTable();
		miningOnTable = SingletonRepository.getEntityManager()
				.getItem(getCurrentSpecies());
		miningOnTable.setDescription("What is this stone?");

		miningOnTable.setPosition(19, 4);
		zone.add(miningOnTable);
	}

	private long remainingTimeToWait(final Player player) {
		if (!player.hasQuest(QUEST_SLOT)) {
			// The player has never tried the quiz before.
			return 0L;
		}
		final long timeLastFailed = Long.parseLong(player.getQuest(QUEST_SLOT));
		//final long onedayInMilliseconds = 60 * 60 * 24 * 1000;
		final long onedayInMilliseconds = 1 * 1 * 1 * 1;
		final long timeRemaining = timeLastFailed + onedayInMilliseconds
				- System.currentTimeMillis();

		return timeRemaining;
	}

	private void createQuizStep() {
		final SpeakerNPC minerman = npcs.get("Bercik");

		// Don't Use condition here, because of FishermansLicenseCollector
		minerman.add(ConversationStates.ATTENDING,
				ConversationPhrases.QUEST_MESSAGES, null,
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
						if (player.isQuestCompleted(StazNaGornika.QUEST_SLOT)) {
							npc.say("You already have mining rights and I have no task for you.");
						} else {
							npc.say("I don't want anything, but you can pass the mining #exam.");
						}
					}
				});

		minerman.add(ConversationStates.ATTENDING, Arrays.asList("exam", "miner", "examming", "exame", "teste"), null,
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
						if (player.isQuestCompleted(StazNaGornika.QUEST_SLOT)) {
							npc.say("You already have mining rights.");
						} else {
							final long timeRemaining = remainingTimeToWait(player);
							if (timeRemaining > 0L) {
								npc.say("You can only take the quiz once a day. Come back in "
									+ TimeUtil.approxTimeUntil((int) (timeRemaining / 1L))
									+ ".");
							} else {
								npc.say("Are you ready for the exam?");
								npc.setCurrentState(ConversationStates.QUEST_OFFERED);
							}
						}
					}
				});

		minerman.add(ConversationStates.QUEST_OFFERED,
				ConversationPhrases.NO_MESSAGES, null,
				ConversationStates.ATTENDING, "Come back when you're ready.",
				null);

		minerman.add(ConversationStates.QUEST_OFFERED,
				ConversationPhrases.YES_MESSAGES, null,
				ConversationStates.QUESTION_1,
				"Well. The first question is: What is this?",
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
						startQuiz();
						player.setQuest(QUEST_SLOT, "" + System.currentTimeMillis());
					}
				});

		minerman.addMatching(ConversationStates.QUESTION_1, Expression.JOKER, new JokerExprMatcher(),
				new NotCondition(new TriggerInListCondition(ConversationPhrases.GOODBYE_MESSAGES)),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
						if (sentence.getTriggerExpression().matches(ConversationParser.createTriggerExpression(getCurrentSpecies()))) {
							if (currentSpeciesNo == speciesList.size() - 1) {
								npc.say("That's right! Congratulations, you passed the exam."
								+ "As a reward, take a rope and a pickaxe. These tools will make your work easier underground");
								cleanUpTable();
								player.setQuest(QUEST_SLOT, "done");
								player.addKarma(15);
								player.addXP(500);
								final Item kilof  = SingletonRepository.getEntityManager().getItem("pick");
								kilof.setBoundTo(player.getName());
								player.equipOrPutOnGround(kilof);
								//final Item lina = SingletonRepository.getEntityManager().getItem("lina");
								//lina.setBoundTo(player.getName());
								//player.equipOrPutOnGround(lina);
								player.setSkill("mining", Double.toString(0.2));
								player.notifyWorldAboutChanges();
							} else {
								npc.say("That's right! How is it called?");
								putNextMiningOnTable();
								npc.setCurrentState(ConversationStates.QUESTION_1);
							}
						} else {
							npc.say("You made a mistake. Unfortunately you failed, but you can try again in 24 hours.");
							cleanUpTable();
							// remember the current time, as you can't do the
							// quiz twice a day.
							player.setQuest(QUEST_SLOT, "" + System.currentTimeMillis());
						}
					}
				});

		minerman.add(ConversationStates.ANY, ConversationPhrases.GOODBYE_MESSAGES,
				ConversationStates.IDLE, "Goodbye.", new ChatAction() {

			// this should be put into a custom ChatAction for this quest when the quest is refactored
			@Override
			public void fire(final Player player, final Sentence sentence,
					final EventRaiser npc) {
				cleanUpTable();
			}
		});
	}

	@Override
	public void addToWorld() {
		fillQuestInfo(
			"Egzamin na Górnika",
			"Bercik wants to test my knowledge of precious stones.",
			false);
		createQuizStep();
	}

	@Override
	public String getName() {
		return "StazNaGornika";
	}
	@Override
	public String getNPCName() {
		return "Bercik";
	}
}
