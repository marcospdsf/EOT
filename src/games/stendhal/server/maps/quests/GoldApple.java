/* $Id: GoldApple.java,v 1.58 2012/04/24 17:01:18 kymara/edited by szyg Exp $ */
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
public class GoldApple extends AbstractQuest {
	private static final int REQUIRED_IRON = 10;

	private static final int REQUIRED_GOLD_BAR = 10;

	private static final int REQUIRED_WOOD = 10;

	private static final int REQUIRED_GIANT_HEART = 1;

	private static final int REQUIRED_MINUTES = 300;

	private static final String QUEST_SLOT = "goldapple_quest";

	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}

	private void step_1() {
		final SpeakerNPC npc = npcs.get("Tajemniczy Wędrowiec");

		npc.add(ConversationStates.ATTENDING,
			ConversationPhrases.QUEST_MESSAGES, null,
			ConversationStates.QUEST_OFFERED, null,
			new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					if (!player.hasQuest(QUEST_SLOT) || "rejected".equals(player.getQuest(QUEST_SLOT))) {
						raiser.say("Jeśli zabijesz zielonego smoka i przyniesiesz mi odpowiednie przedmioty, mogę dla Ciebie przygotować złote jabłko.");
					} else if (player.isQuestCompleted(QUEST_SLOT)) {
						raiser.say("Przecież już dostałeś złote jabłko...");
						raiser.setCurrentState(ConversationStates.ATTENDING);
					} else {
						raiser.say("Dlaczego zawracasz mi głowę skoro nie ukończyłeś zadania?");
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
						+ " pazurów niedźwiedzia, "
						+ REQUIRED_WOOD
						+ " pazurów tygrysa, "
						+ REQUIRED_GOLD_BAR
						+ " pazurów wilka i "
						+ REQUIRED_GIANT_HEART
						+ " jablko. Wróć, gdy będziesz je miał #dokładnie w tej kolejności! Jeżeli zapomnisz to powiedz #przypomnij");
					player.setQuest(QUEST_SLOT, "start;0;0;0;0");
					player.addKarma(10);

				}
			});

		npc.add(
			ConversationStates.QUEST_OFFERED,
			ConversationPhrases.NO_MESSAGES,
			null,
			ConversationStates.IDLE,
			"Och, naprawdę nie chcesz złotego jabłka?",
			new SetQuestAndModifyKarmaAction(QUEST_SLOT, "rejected", -10.0));

		npc.addReply(Arrays.asList("exact", "dokładnie"),
			"Nie lubię, gdy coś jest nieuporządkowane...");
		npc.addReply(Arrays.asList("kokosów", "kokosy"),
			"");
	}

	private void step_2() {
		/* Get the stuff. */
	}

	private void step_3() {

		final SpeakerNPC npc = npcs.get("Tajemniczy Wędrowiec");

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
						if (player.isEquipped("bear claws", neededIron)) {
							player.drop("bear claws", neededIron);
							neededIron = 0;
						} else {
							final int amount = player.getNumberOfEquipped("bear claws");
							if (amount > 0) {
								player.drop("bear claws", amount);
								neededIron -= amount;
							}

							raiser.say("Nie mogę przygotować jabłka bez "
								+ Grammar.quantityplnoun(
										neededIron, "bear claws", "")
								+ ".");
							missingSomething = true;
						}
					}

					if (!missingSomething && (neededWoodLogs > 0)) {
						if (player.isEquipped("pazury tygrysie", neededWoodLogs)) {
							player.drop("pazury tygrysie", neededWoodLogs);
							neededWoodLogs = 0;
						} else {
							final int amount = player.getNumberOfEquipped("pazury tygrysie");
							if (amount > 0) {
								player.drop("pazury tygrysie", amount);
								neededWoodLogs -= amount;
							}

							raiser.say("Jak możesz wymagać jabłka skoro nie masz "
								+ Grammar.quantityplnoun(neededWoodLogs, "pazury tygrysie","")
								+ " dla mnie?");
							missingSomething = true;
						}
					}

					if (!missingSomething && (neededGoldBars > 0)) {
						if (player.isEquipped("wolf claws", neededGoldBars)) {
							player.drop("wolf claws", neededGoldBars);
							neededGoldBars = 0;
						} else {
							final int amount = player.getNumberOfEquipped("wolf claws");
							if (amount > 0) {
								player.drop("wolf claws", amount);
								neededGoldBars -= amount;
							}
							raiser.say("Bez pazurów wilka nie zasługujesz na to jabłko. Potrzebuję  "
									+ Grammar.quantityplnoun(neededGoldBars, "wolf claws","one") + " więcej.");
							missingSomething = true;
						}
					}

					if (!missingSomething && (neededGiantHearts > 0)) {
						if (player.isEquipped("jabłko", neededGiantHearts)) {
							player.drop("jabłko", neededGiantHearts);
							neededGiantHearts = 0;
						} else {
							final int amount = player.getNumberOfEquipped("jabłko");
							if (amount > 0) {
								player.drop("jabłko", amount);
								neededGiantHearts -= amount;
							}
							raiser.say("Bez jabłka nie zrobisz złotego jabłka, przynieś mi"
								+ Grammar.quantityplnoun(neededGiantHearts, "jabłko","one") + " więcej");
							missingSomething = true;
						}
					}

					if (player.hasKilled("zielony smok") && !missingSomething) {
						raiser.say("Przyniosłeś wszystko. Biorę się do transmutacji tego jabłka. Wróć za "
							+ REQUIRED_MINUTES
							+ " minutę" + ", a będzie gotowy.");
						player.setQuest(QUEST_SLOT, "forging;" + System.currentTimeMillis());
					} else {
						if (!player.hasKilled("zielony smok") && !missingSomething) {
							raiser.say("Najpierw musisz zabić zielonego smoka!");
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
						raiser.say("Jeszcze nie skończyłem przygotowywania jabłka. Wróć za "
							+ TimeUtil.approxTimeUntil((int) (timeRemaining / 1000L))
							+ ".");
						return;
					}

					raiser.say("Skończyłem! Oto jabłko!");
					player.addXP(5000);
					
					player.setAtkXP(1000 + player.getAtkXP());
					player.setDefXP(1000 + player.getDefXP());
					

					player.addKarma(15);
					player.incAtkXP();
					player.incDefXP();
					final Item magicSword = SingletonRepository.getEntityManager().getItem("złote jabłko");
					magicSword.setBoundTo(player.getName());
					player.equipOrPutOnGround(magicSword);
					player.notifyWorldAboutChanges();
					player.setQuest(QUEST_SLOT, "done");
				}
			});

		npc.add(ConversationStates.ATTENDING,
			Arrays.asList("forge", "missing", "wykuj", "brakuje", "lista", "przypomnij"), 
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

					raiser.say("Będę potrzebował " + neededIron + " #iron, "
							+ neededWoodLogs + " #wood, "
							+ neededGoldBars + " #gold bar i "
							+ neededGiantHearts + " #kokos");
				}
			});

		npc.add(
			ConversationStates.ANY,
			"iron",
			null,
			ConversationStates.ATTENDING,
			"Zbierz kilka rud żelaza, które są bogate w minerały.",
			null);

		npc.add(ConversationStates.ANY, "wood", null,
				ConversationStates.ATTENDING,
				"W lesie jest pełno drewna.", null);
		npc.add(ConversationStates.ANY, "złoto", null,
				ConversationStates.ATTENDING,
				"Kowal w Ados może dla Ciebie odlać bryłki złoto w sztabki złota.",
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
				"Rzeczy dla Vulcanusa",
				"Vulcanus syn Zeusa wykuje dla Ciebie boski miecz.",
				false);
		step_1();
		step_2();
		step_3();
	}

	@Override
	public String getName() {
		return "ElementalDagger1";
	}
	
	@Override
	public List<String> getHistory(final Player player) {
			final List<String> res = new ArrayList<String>();
			if (!player.hasQuest(QUEST_SLOT)) {
				return res;
			}
			final String questState = player.getQuest(QUEST_SLOT);
			res.add("Spotkałem Ognistiak w Kotoch.");
			if (questState.equals("rejected")) {
				res.add("Nie potrzebny mi immortal sword.");
				return res;
			} 
			res.add("Aby wykuć miecz żywiołów Ognistiak potrzebuje: " + REQUIRED_IRON
					+ " iron, "
					+ REQUIRED_WOOD
					+ " polana, "
					+ REQUIRED_GOLD_BAR
					+ " sztabki zlota i "
					+ REQUIRED_GIANT_HEART
					+ " kokosów, dokładnie w tej kolejności.");
			// yes, yes. this is the most horrible quest code and so you get a horrible quest history. 
			if(questState.startsWith("start") && !"start;15;26;12;6".equals(questState)){
				res.add("Nie dostarczyłem wszystkiego. Vulkanus powie mi co jeszcze potrzebuje.");
			} else if ("start;15;26;12;6".equals(questState) || !questState.startsWith("start")) {
				res.add("Dostarczyłem wszystko co potrzebne dla Vulcanus.");
			}
			if("start;15;26;12;6".equals(questState) && !player.hasKilled("szczur")){
				res.add("Aby zasłużyć na miecz muszę przynieść kokosy.");
			} 
			if (questState.startsWith("forging")) {
				res.add("Kowal wykuwa mi miecz.");
			} 
			if (isCompleted(player)) {
				res.add("Za sztabki złota, kokosy i pare innach drobiazgów zostałem nagrodzony mieczem nieśmiertelnych.");
			}
			return res;	
	}

 	// match to the min level of the immortal sword
	@Override
	public int getMinLevel() {
		return 20;
	}

	@Override
	public String getNPCName() {
		return "Tajemniczy Wędrowiec";
	}
	
	@Override
	public String getRegion() {
		return Region.ADOS_CITY;
	}
}
