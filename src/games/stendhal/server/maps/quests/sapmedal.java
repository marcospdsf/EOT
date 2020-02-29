/* $Id: ElementalDagger1.java,v 1.58 2012/04/24 17:01:18 kymara/ edited by szyg Exp $ */
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

import games.stendhal.common.MathHelper;
import games.stendhal.common.grammar.Grammar;
import games.stendhal.common.parser.Sentence;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.SetQuestAndModifyKarmaAction;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.QuestStartedCondition;
import games.stendhal.server.entity.npc.condition.QuestStateStartsWithCondition;
import games.stendhal.server.entity.npc.condition.GreetingMatchesNameCondition;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.Region;
import games.stendhal.server.util.TimeUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * QUEST: The immortal sword forging.
 * 
 * PARTICIPANTS:
 * <ul>
 * <li> Vulcanus, son of Zeus itself, will forge for you the god's sword.
 * </ul>
 * 
 * STEPS:
 * <ul>
 * <li> Vulcanus tells you about the sword.
 * <li> He offers to forge a immortal sword for you if you bring him what it
 * needs.
 * <li> You give him all what he ask you.
 * <li> He tells you you must have killed a giant to get the shield
 * <li> Vulcanus forges the immortal sword for you
 * </ul>
 * 
 * REWARD:
 * <ul>
 * <li> immortal sword
 * <li>15000 XP
 * </ul>
 * 
 * 
 * REPETITIONS:
 * <ul>
 * <li> None.
 * </ul>
 */
public class sapmedal extends AbstractQuest {
	private static final int REQUIRED_IRON = 1;

	private static final int REQUIRED_GOLD_BAR = 40;

	private static final int REQUIRED_WOOD = 1;

	private static final int REQUIRED_GIANT_HEART = 60;

	private static final int REQUIRED_MINUTES = 720;

	private static final String QUEST_SLOT = "sapphiremedal_quest";

	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}

	private void step_1() {
		final SpeakerNPC npc = npcs.get("Trener Łuczników");

		npc.add(ConversationStates.ATTENDING,
			ConversationPhrases.QUEST_MESSAGES, null,
			ConversationStates.QUEST_OFFERED, null,
			new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					if (!player.hasQuest(QUEST_SLOT) || "rejected".equals(player.getQuest(QUEST_SLOT))) {
						raiser.say("Nasze wojsko potrzebuję zapasów. Chcesz nam pomóc?");
					} else if (player.isQuestCompleted(QUEST_SLOT)) {
						raiser.say("Już przyniosłeś nam to co potrzebowaliśmy.");
						raiser.setCurrentState(ConversationStates.ATTENDING);
					} else {
						raiser.say("Dlaczego zawracasz mi głowę?");
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
					raiser.say("Będę potrzebował kilku rzeczy: "
						+ REQUIRED_IRON
						+ " pazura czarnego smoka "
						+ REQUIRED_WOOD
						+ " emeraldowego medalu "
						+ REQUIRED_GOLD_BAR
						+ " sztabek złota"
						+ REQUIRED_GIANT_HEART
						+ " serc giganta. Wróć, gdy będziesz je miał w tej kolejności! Jeżeli zapomnisz to powiedz #przypomnij");
					player.setQuest(QUEST_SLOT, "start;0;0;0;0");
					player.addKarma(10);

				}
			});

		npc.add(
			ConversationStates.QUEST_OFFERED,
			ConversationPhrases.NO_MESSAGES,
			null,
			ConversationStates.IDLE,
			"Och, naprawdę nie chcesz nam pomóc?",
			new SetQuestAndModifyKarmaAction(QUEST_SLOT, "rejected", -10.0));
	}

	private void step_2() {
		/* Get the stuff. */
	}

	private void step_3() {

		final SpeakerNPC npc = npcs.get("Trener Łuczników");

		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
			new AndCondition(new GreetingMatchesNameCondition(npc.getName()),
						new QuestStateStartsWithCondition(QUEST_SLOT, "start")),
			ConversationStates.ATTENDING, null,
			new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					final String[] tokens = player.getQuest(QUEST_SLOT).split(";");

					int neededIron = REQUIRED_IRON
							- Integer.parseInt(tokens[1]);
					int neededWoodLogs = REQUIRED_WOOD
							- Integer.parseInt(tokens[2]);
					int neededGoldBars = REQUIRED_GOLD_BAR
							- Integer.parseInt(tokens[3]);
					int neededGiantHearts = REQUIRED_GIANT_HEART
							- Integer.parseInt(tokens[4]);
					boolean missingSomething = false;

					if (!missingSomething && (neededIron > 0)) {
						if (player.isEquipped("black dragon's claw", neededIron)) {
							player.drop("black dragon's claw", neededIron);
							neededIron = 0;
						} else {
							final int amount = player.getNumberOfEquipped("black dragon's claw");
							if (amount > 0) {
								player.drop("black dragon's claw", amount);
								neededIron -= amount;
							}

							raiser.say("Potrzebujemy "
								+ Grammar.quantityplnoun(
										neededIron, "black dragon's claw", "")
								+ ".");
							missingSomething = true;
						}
					}

					if (!missingSomething && (neededWoodLogs > 0)) {
						if (player.isEquipped("emeraldowy medal", neededWoodLogs)) {
							player.drop("emeraldowy medal", neededWoodLogs);
							neededWoodLogs = 0;
						} else {
							final int amount = player.getNumberOfEquipped("emeraldowy medal");
							if (amount > 0) {
								player.drop("emeraldowy medal", amount);
								neededWoodLogs -= amount;
							}

							raiser.say("Nie mam jak cię awansować bez "
								+ Grammar.quantityplnoun(neededWoodLogs, "emeraldowy medal","")
								+ ".");
							missingSomething = true;
						}
					}

					if (!missingSomething && (neededGoldBars > 0)) {
						if (player.isEquipped("gold bar", neededGoldBars)) {
							player.drop("gold bar", neededGoldBars);
							neededGoldBars = 0;
						} else {
							final int amount = player.getNumberOfEquipped("gold bar");
							if (amount > 0) {
								player.drop("gold bar", amount);
								neededGoldBars -= amount;
							}
							raiser.say("Potrzebujemy materiały do zbroji. Przynieś "
									+ Grammar.quantityplnoun(neededGoldBars, "gold bar","one") + " więcej.");
							missingSomething = true;
						}
					}

					if (!missingSomething && (neededGiantHearts > 0)) {
						if (player.isEquipped("giant heart", neededGiantHearts)) {
							player.drop("giant heart", neededGiantHearts);
							neededGiantHearts = 0;
						} else {
							final int amount = player.getNumberOfEquipped("giant heart");
							if (amount > 0) {
								player.drop("giant heart", amount);
								neededGiantHearts -= amount;
							}
							raiser.say("Potrzebujemy serc olbrzyma do wytwarzania mikstur. Nadal potrzebujemy  "
								+ Grammar.quantityplnoun(neededGiantHearts, "giant heart","one") + " wciąż.");
							missingSomething = true;
						}
					}

					if (player.hasKilled("czarne smoczysko") && !missingSomething) {
						raiser.say("Przyniosłeś wszystko. Za niedługo zostaniesz odznaczony. Wróć za  "
							+ REQUIRED_MINUTES
							+ " minutę" + ", a będzie gotowy.");
						player.setQuest(QUEST_SLOT, "forging;" + System.currentTimeMillis());
					} else {
						if (!player.hasKilled("czarny smok") && !missingSomething) {
							raiser.say("Zanim dostanisz medal zabij czarne smoczysko");
						}

						player.setQuest(QUEST_SLOT,
							"start;"
							+ (REQUIRED_IRON - neededIron)
							+ ";"
							+ (REQUIRED_WOOD - neededWoodLogs)
							+ ";"
							+ (REQUIRED_GOLD_BAR - neededGoldBars)
							+ ";"
							+ (REQUIRED_GIANT_HEART - neededGiantHearts));
					}
				}
			});

		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
			new AndCondition(new GreetingMatchesNameCondition(npc.getName()),
						new QuestStateStartsWithCondition(QUEST_SLOT, "forging;")),
			ConversationStates.IDLE, null, new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {

					final String[] tokens = player.getQuest(QUEST_SLOT).split(";");
					
					final long delay = REQUIRED_MINUTES * MathHelper.MILLISECONDS_IN_ONE_MINUTE; 
					final long timeRemaining = (Long.parseLong(tokens[1]) + delay)
							- System.currentTimeMillis();

					if (timeRemaining > 0L) {
						raiser.say("Musisz jeszcze poczekać. Wróć za "
							+ TimeUtil.approxTimeUntil((int) (timeRemaining / 1000L))
							+ ".");
						return;
					}

					raiser.say("Proszę bardzo, oto Twoje odznaczenie.");
					player.addXP(2500000);
					player.setAtkXP(30000 + player.getAtkXP());
					player.setDefXP(40000 + player.getDefXP());
					
					player.incAtkXP();
					player.incDefXP();
					player.addKarma(200);
					final Item magicSword = SingletonRepository.getEntityManager().getItem("obsidianowy medal");
					magicSword.setBoundTo(player.getName());
					player.equipOrPutOnGround(magicSword);
					player.notifyWorldAboutChanges();
					player.setQuest(QUEST_SLOT, "done");
				}
			});

		npc.add(ConversationStates.ATTENDING,
			Arrays.asList("odznaczenie", "missing", "medal", "brakuje", "lista", "przypomnij"), 
			new QuestStartedCondition(QUEST_SLOT),
			ConversationStates.ATTENDING,
			null,
			new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					final String[] tokens = player.getQuest(QUEST_SLOT).split(";");

					final int neededIron = REQUIRED_IRON
							- Integer.parseInt(tokens[1]);
					final int neededWoodLogs = REQUIRED_WOOD
							- Integer.parseInt(tokens[2]);
					final int neededGoldBars = REQUIRED_GOLD_BAR
							- Integer.parseInt(tokens[3]);
					final int neededGiantHearts = REQUIRED_GIANT_HEART
							- Integer.parseInt(tokens[4]);

					raiser.say("Będę potrzebował " + neededIron + " pazura czarnego smoka, "
							+ neededWoodLogs + " emeraldowego medalu "
							+ neededGoldBars + " sztabek złota i "
							+ neededGiantHearts + "  serc olbrzyma.");
				}
			});

		npc.add(
			ConversationStates.ANY,
			"emeraldowy medal",
			null,
			ConversationStates.ATTENDING,
			"Dostaniesz go za pomoc Trenerowi Rycerzy.",
			null);

		npc.add(ConversationStates.ANY, "black dragon's claw", null,
				ConversationStates.ATTENDING,
				"Wyrwij go z stopy czarnego smoka.", null);
		npc.add(ConversationStates.ANY, "złoto", null,
				ConversationStates.ATTENDING,
				"Kowal w Ados może dla Ciebie odlać bryłki złota w sztabki złota.",
				null);
		npc.add(
			ConversationStates.ANY,
			Arrays.asList("giant","olbrzyma", "olbrzym"),
			null,
			ConversationStates.ATTENDING,
			"Są starodawne legendy o olbrzymach żyjących w górach na północ od Semos i Ados.",
			null);
	}

	@Override
	public void addToWorld() {
		//super.addToWorld();
		fillQuestInfo(
				"Rzeczy dla Trenera Łuczników",
				"Trener Łuczników ma dla mnie zadanie.",
				false);
		step_1();
		step_2();
		step_3();
	}

	@Override
	public String getName() {
		return "sapmedal";
	}
	
	@Override
	public List<String> getHistory(final Player player) {
			final List<String> res = new ArrayList<String>();
			if (!player.hasQuest(QUEST_SLOT)) {
				return res;
			}
			final String questState = player.getQuest(QUEST_SLOT);
			res.add("Spotkałem Trenera Łuczników w twierdzy Ados.");
			if (questState.equals("rejected")) {
				res.add("Nie chce pomóc Trenerowi Łuczników.");
				return res;
			} 
			res.add("Trener Łuczników poprosił mnie abym przyniósł: " + REQUIRED_IRON
					+ " black dragon's claw, "
					+ REQUIRED_WOOD
					+ " szmargdowy medal, "
					+ REQUIRED_GOLD_BAR
					+ " sztabki zlota i "
					+ REQUIRED_GIANT_HEART
					+ " serc olbrzyma, dokładnie w tej kolejności.");
			// yes, yes. this is the most horrible quest code and so you get a horrible quest history. 
			if(questState.startsWith("start") && !"start;15;26;12;6".equals(questState)){
				res.add("Nie dostarczyłem wszystkiego. Trener Łuczników powie mi czego jeszcze potrzebuje.");
			} else if ("start;15;26;12;6".equals(questState) || !questState.startsWith("start")) {
				res.add("Dostarczyłem wszystko co potrzebne dla Trenera Łuczników.");
			}
			if("start;15;26;12;6".equals(questState) && !player.hasKilled("czarne smoczysko")){
				res.add("Muszę udowodnić swe męstwo zabijając czarnego smoka.");
			} 
			if (questState.startsWith("forging")) {
				res.add("Muszę poczekać na swoją odznakę.");
			} 
			if (isCompleted(player)) {
				res.add("Pomogłem Trenerowi Łuczników i dostałem obsidianową odznakę.");
			}
			return res;	
	}

 	// match to the min level of the immortal sword
	@Override
	public int getMinLevel() {
		return 150;
	}

	@Override
	public String getNPCName() {
		return "Trener Łuczników";
	}
	
	@Override
	public String getRegion() {
		return Region.ADOS_CITY;
	}
}
