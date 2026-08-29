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

import games.stendhal.common.parser.Sentence;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.DropItemAction;
import games.stendhal.server.entity.npc.action.MultipleActions;
import games.stendhal.server.entity.npc.action.SetQuestAction;
import games.stendhal.server.entity.npc.action.SetQuestAndModifyKarmaAction;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.QuestInStateCondition;
import games.stendhal.server.entity.npc.condition.PlayerHasItemWithHimCondition;
import games.stendhal.server.entity.npc.condition.QuestStateStartsWithCondition;
import games.stendhal.server.entity.npc.condition.GreetingMatchesNameCondition;
import games.stendhal.server.entity.npc.condition.TimePassedCondition;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.util.TimeUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

public class ZlotyRog extends AbstractQuest {

	private static final int REQUIRED_WAIT_DAYS = 4;

	private static final int REQUIRED_MINUTES = 60;

	private static final String QUEST_SLOT = "zloty_rog";

	private static final String WEAPONS_COLLECTOR_QUEST_SLOT = "weapons_collector";

	private static Logger logger = Logger.getLogger(ZlotyRog.class);

	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}

	private void step_1() {
		final SpeakerNPC npc = npcs.get("Bartłomiej");

		npc.add(ConversationStates.ATTENDING,
			ConversationPhrases.QUEST_MESSAGES, null,
			ConversationStates.QUEST_OFFERED, null,
			new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					if (player.isQuestCompleted(WEAPONS_COLLECTOR_QUEST_SLOT)) {
						if(player.getLevel() >= 200) {
							if(player.getKarma()>=500) {
								if(player.hasKilledSolo("archangel") || player.hasKilledSolo("archanioł")) {
									if (!player.hasQuest(QUEST_SLOT) || "rejected".equals(player.getQuest(QUEST_SLOT))) {
										raiser.say("Did you come seeking a legendary #'golden horn'? My brother knows the secret of their craftsmanship. Are you interested?");
									} else if (player.getQuest(QUEST_SLOT).startsWith("done;")) {
										final String[] waittokens = player.getQuest(QUEST_SLOT).split(";");
										final long waitdelay = REQUIRED_WAIT_DAYS * TimeUtil.MILLISECONDS_IN_DAY;
										final long waittimeRemaining = (Long.parseLong(waittokens[1]) + waitdelay) - System.currentTimeMillis();
										if (waittimeRemaining > 0L) {
											raiser.say("My brother must rest after crafting such an artifact. Return in " + TimeUtil.approxTimeUntil((int) (waittimeRemaining / 1000L)) + ".");
										} else {
											raiser.say("Have you returned for another #'golden horn'?");
											raiser.setCurrentState(ConversationStates.QUEST_OFFERED);
										}
									} else if (player.getQuest(QUEST_SLOT).startsWith("make;")) {
										raiser.say("Why do you disturb me? My brother has not finished your horn yet.");
										raiser.setCurrentState(ConversationStates.ATTENDING);
									}
								} else {
									npc.say("I only speak with those who proved their valor by slaying an archangel alone.");
									raiser.setCurrentState(ConversationStates.ATTENDING);
								}
							} else {
								npc.say("You are not yet worthy of such an instrument. Your karma must be at least 500.");
								raiser.setCurrentState(ConversationStates.ATTENDING);
							}
						} else {
							npc.say("Your standing is too low for this task. Return when you reach level 200.");
							raiser.setCurrentState(ConversationStates.ATTENDING);
						}
					} else {
						npc.say("I see you have not helped my cousin Balduin! Assist him first, then we will speak of the golden horn.");
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
					raiser.say("My brother requires several #items to craft the horn.");
					player.setQuest(QUEST_SLOT, "start");
					player.addKarma(10);

				}
			});

		npc.add(
			ConversationStates.QUEST_OFFERED,
			ConversationPhrases.NO_MESSAGES,
			null,
			ConversationStates.IDLE,
			"Twoja strata.",
			new SetQuestAndModifyKarmaAction(QUEST_SLOT, "rejected", -10.0));

	}

	private void step_2() {
		final SpeakerNPC npc = npcs.get("Bartłomiej");

		final List<ChatAction> ciupagaactions = new LinkedList<ChatAction>();
		ciupagaactions.add(new DropItemAction("small feather",100));
		ciupagaactions.add(new DropItemAction("angel feather",20));
		ciupagaactions.add(new DropItemAction("archangel feather",10));
		ciupagaactions.add(new DropItemAction("dark angel feather",8));
		ciupagaactions.add(new DropItemAction("fallen angel feather",20));
		ciupagaactions.add(new DropItemAction("seraph feather",2));
		ciupagaactions.add(new SetQuestAction(QUEST_SLOT, "make;" + System.currentTimeMillis()));

		npc.add(ConversationStates.ATTENDING, Arrays.asList("items", "horn", "remind", "rzeczy", "przedmioty", "róg", "przypomnij"),
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, "start"),
								 new PlayerHasItemWithHimCondition("small feather",100),
								 new PlayerHasItemWithHimCondition("angel feather",20),
								 new PlayerHasItemWithHimCondition("archangel feather",10),
								 new PlayerHasItemWithHimCondition("dark angel feather",8),
								 new PlayerHasItemWithHimCondition("fallen angel feather",20),
								 new PlayerHasItemWithHimCondition("seraph feather",2)),
				ConversationStates.ATTENDING, "I see you have brought everything requested. Return in one hour and the golden horn will be ready. Say #/reward/.",
				new MultipleActions(ciupagaactions));

		npc.add(ConversationStates.ATTENDING, Arrays.asList("items", "remind", "przedmioty", "przypomnij", "rzeczy"),
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, "start"),
								 new NotCondition(
								 new AndCondition(new PlayerHasItemWithHimCondition("small feather",100),
												  new PlayerHasItemWithHimCondition("angel feather",20),
												  new PlayerHasItemWithHimCondition("archangel feather",10),
												  new PlayerHasItemWithHimCondition("dark angel feather",8),
												  new PlayerHasItemWithHimCondition("fallen angel feather",20),
												  new PlayerHasItemWithHimCondition("seraph feather",2)))),
				ConversationStates.ATTENDING, "Potrzebuję:\n"
									+"#'100 piórek gołębich'\n"
									+"#'20 piór anioła'\n"
									+"#'10 piór archanioła'\n"
									+"#'8 piór mrocznego anioła'\n"
									+"#'20 piór upadłego anioła'\n"
									+"#'2 pióra serafina'\n"
									+"Proszę przynieś mi to wszystko naraz. Jeżeli zapomnisz co masz przynieść to powiedz #przypomnij. Dziękuję!", null);

	}

	private void step_3() { 
		final SpeakerNPC npc = npcs.get("Bartłomiej");

		npc.add(ConversationStates.ATTENDING,
			Arrays.asList("reward", "nagroda"),
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
						raiser.say("My brother is still crafting your horn. Return in "
							+ TimeUtil.approxTimeUntil((int) (timeRemaining / 1000L))
							+ " to receive it.");
						return;
					}

					raiser.say("It was worth the wait! Here is the golden horn. Farewell!");
					player.addXP(20000);
					player.addKarma(100);
					final Item zlotyRog = SingletonRepository.getEntityManager().getItem("golden horn");
					zlotyRog.setBoundTo(player.getName());
					player.equipOrPutOnGround(zlotyRog);
					player.notifyWorldAboutChanges();
					player.setQuest(QUEST_SLOT, "done" + ";" + System.currentTimeMillis());
				}
			});

		npc.add(
			ConversationStates.ANY,
			"small feather", 
			null,
			ConversationStates.ATTENDING,
			"W Zakopanem jest dużo gołębi.",
			null);

		npc.add(
			ConversationStates.ANY,
			"angel feather",
			null,
			ConversationStates.ATTENDING,
			"Anioł jest dość silny więc uważaj.",
			null);

		npc.add(
			ConversationStates.ANY,
			"archangel feather",
			null,
			ConversationStates.ATTENDING,
			"Archanioł już z daleka może ciebie zobaczyć, miej się na baczności.",
			null);

		npc.add(
			ConversationStates.ANY,
			"dark angel feather",
			null,
			ConversationStates.ATTENDING,
			"Zdobędziesz je gdy spotkasz się oko w oko z tą mroczną bestią.",
			null);

		npc.add(
			ConversationStates.ANY,
			"fallen angel feather",
			null,
			ConversationStates.ATTENDING,
			"Błąka się gdzieś po kikareukin.",
			null);

		npc.add(
			ConversationStates.ANY,
			"seraph feather",
			null,
			ConversationStates.ATTENDING,
			"Serafin jest najmocniejszym aniołem sam go nie zabijesz.",
			null);
	}

	@Override
	public void addToWorld() {
		fillQuestInfo(
			"Golden Horn",
			"Bartłomiej's brother can craft a legendary golden horn for you.",
			true);
		step_1();
		step_2();
		step_3();
	}

	@Override
	public String getName() {
		return "ZlotyRog";
	}

	@Override
	public List<String> getHistory(final Player player) {
		final List<String> res = new ArrayList<String>();
		if (!player.hasQuest(QUEST_SLOT)) {
			return res;
		}
		final String questState = player.getQuest(QUEST_SLOT);
		res.add("Spotkałem się z Bartłomiejem w jaskiniach Zakopanego. Zaproponował mi złoty róg, który może zrobić jego brat.");
		if (questState.equals("rejected")) {
			res.add("Nie jestem muzykiem aby grać na tym rogu.");
			return res;
		}
		if (questState.startsWith("start")) {
			return res;
		}
		res.add("Bartłomiej kazał mi przynnieść kilka przedmiotów, które są potrzebne do tej pracy. Gdybym zapomniał co mam przynieść mam mu powiedzieć: przypomnij.");
		if ("przedmioty".equals(questState)) {
			return res;
		}
		res.add("Dostarczyłem potrzebne przedmioty! Bartłomiej zaniósł je dla brata.");
		if (questState.startsWith("forging")) {
			if (new TimePassedCondition(QUEST_SLOT,1,REQUIRED_MINUTES).fire(player, null, null)) {
				res.add("Złoty róg chyba jest już gotowy. Hasło: róg.");
			} else {
				res.add("Po złoty róg mam zgłosić się za godzine. Hasło: róg.");
			}
			return res;
		} 
		res.add("It was well worth waiting for the Golden Horn. Its resonance is truly magnificent!");
		if (isCompleted(player)) {
			return res;
		} 
		// if things have gone wrong and the quest state didn't match any of the above, debug a bit:
		final List<String> debug = new ArrayList<String>();
		debug.add("Stan zadania to: " + questState);
		logger.error("Historia nie pasuje do stanu poszukiwania " + questState);
		return debug;
	}

	@Override
	public String getNPCName() {
		return "Bartłomiej";
	}
}