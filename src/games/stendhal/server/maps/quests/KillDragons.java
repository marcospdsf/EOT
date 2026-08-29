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
import games.stendhal.server.entity.item.StackableItem;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.DropItemAction;
import games.stendhal.server.entity.npc.action.EquipItemAction;
import games.stendhal.server.entity.npc.action.IncreaseKarmaAction;
import games.stendhal.server.entity.npc.action.IncreaseXPAction;
import games.stendhal.server.entity.npc.action.MultipleActions;
import games.stendhal.server.entity.npc.action.SayTimeRemainingAction;
import games.stendhal.server.entity.npc.action.SetQuestAction;
import games.stendhal.server.entity.npc.action.SetQuestAndModifyKarmaAction;
import games.stendhal.server.entity.npc.action.StartRecordingKillsAction;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.KilledForQuestCondition;
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.OrCondition;
import games.stendhal.server.entity.npc.condition.PlayerHasItemWithHimCondition;
import games.stendhal.server.entity.npc.condition.QuestCompletedCondition;
import games.stendhal.server.entity.npc.condition.QuestInStateCondition;
import games.stendhal.server.entity.npc.condition.QuestNotStartedCondition;
import games.stendhal.server.entity.npc.condition.QuestStateStartsWithCondition;
import games.stendhal.server.entity.npc.condition.TimePassedCondition;
import games.stendhal.server.entity.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import marauroa.common.Pair;
import org.apache.log4j.Logger;

public class KillDragons extends AbstractQuest {
	private static final String QUEST_SLOT = "kill_dragons";
	private static final int DELAY_IN_MINUTES = 60*24;
	private static Logger logger = Logger.getLogger(KillDragons.class);

	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}

	private void step1() {
		final SpeakerNPC npc = npcs.get("Alicja");

		npc.add(ConversationStates.ATTENDING,
				ConversationPhrases.QUEST_MESSAGES,
				new QuestCompletedCondition(QUEST_SLOT),
				ConversationStates.ATTENDING,
				"I have no more nightmares! Now my dreams are cheerful and colorful.",
				null);

		npc.add(ConversationStates.ATTENDING,
				ConversationPhrases.QUEST_MESSAGES,
				new QuestNotStartedCondition(QUEST_SLOT),
				ConversationStates.QUEST_OFFERED,
				"Every night terrible nightmares haunt me. Do you think you could help me?",
				null);

		final HashMap<String, Pair<Integer, Integer>> toKill = new HashMap<String, Pair<Integer, Integer>>();
				toKill.put("green dragon", new Pair<Integer, Integer>(1,0));
		final List<ChatAction> actions = new LinkedList<ChatAction>();
				actions.add(new IncreaseKarmaAction(5.0));
				actions.add(new SetQuestAction(QUEST_SLOT, 0, "start"));
				actions.add(new StartRecordingKillsAction(QUEST_SLOT, 1, toKill));

		npc.add(ConversationStates.QUEST_OFFERED,
				ConversationPhrases.YES_MESSAGES, null,
				ConversationStates.ATTENDING,
				"Och, wspaniale! Co noc śni mi się straszny #'/zielony smok/', proszę odszukaj go i zabij. Przynieś mi też 5 skór zielonego smoka."
				+ " Jeśli włożę je pod poduszkę, będą odstraszać koszmary. Pamintj, że musisz zabić go samodzielnie, bo skóra nie będzie działała jak talizman!",
				new MultipleActions(actions));

		// Player says no, they've lost karma.
		npc.add(ConversationStates.QUEST_OFFERED,
				ConversationPhrases.NO_MESSAGES, null, ConversationStates.IDLE,
				"Please, if you change your mind, let me know. I am afraid to fall asleep, and my mother told me that children grow during sleep. I want to grow up!",
				new SetQuestAndModifyKarmaAction(QUEST_SLOT, "rejected", -10.0));
	}

	private void step2() {
		final SpeakerNPC npc = npcs.get("Alicja");

		//player nie zabil smoka
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0, "start"),
								 new NotCondition(new KilledForQuestCondition(QUEST_SLOT, 1))),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("You have not slain the zielonego smoka by yourself yet, why are you deceiving me?");
					}
				});

		//player zabil smoka ale nie ma potrzebnych resurce	
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0,"start"),
								 new KilledForQuestCondition(QUEST_SLOT, 1),
								 new NotCondition(new PlayerHasItemWithHimCondition("green dragon leather", 5))),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("Nawet jeśli zabiłeś zielonego smoka, na pewno odwiedzi mnie ponownie w koszmarze jeśli nie będę miała amuletu. Proszę, przynieś mi te 5 skór zielonego smoka.");
					}
				});

		//player wykonal obydwa zlecenia
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0,"start"),
								 new KilledForQuestCondition(QUEST_SLOT, 1),
								 new PlayerHasItemWithHimCondition("green dragon leather", 5)),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("Oh, you defeated it! Thank you! Please return in 24 hours to see if the talisman worked. Przypomnij mi mówiąc #zielony.");
						player.drop("green dragon leather", 5);
						player.addKarma(10);
						player.setQuest(QUEST_SLOT, "czas_szkielet;"+System.currentTimeMillis());
					}
				});
	}

	private void czas1() {
		final SpeakerNPC npc = npcs.get("Alicja");
		final String extraTrigger = "zielony";
		List<String> questTrigger;
		questTrigger = new LinkedList<String>(ConversationPhrases.QUEST_MESSAGES);
		questTrigger.add(extraTrigger);

		npc.add(ConversationStates.ATTENDING, questTrigger,
				new AndCondition(new QuestStateStartsWithCondition(QUEST_SLOT, "czas_szkielet"),
								 // jeszcze nie minal czas
								 new NotCondition(new TimePassedCondition(QUEST_SLOT,1,DELAY_IN_MINUTES))),
				ConversationStates.ATTENDING, null,
				new SayTimeRemainingAction(QUEST_SLOT, 1, DELAY_IN_MINUTES, " A full day has not passed yet. Please return in "));

		npc.add(ConversationStates.ATTENDING, questTrigger,
				new AndCondition(new QuestStateStartsWithCondition(QUEST_SLOT, "czas_szkielet"),
								 // czas minal, dalszy krok
								 new TimePassedCondition(QUEST_SLOT,1,DELAY_IN_MINUTES)),
				ConversationStates.ATTENDING,
				"The talisman worked! Sadly, a different nightmare appeared tonight. Will you #help me?",
				new SetQuestAction(QUEST_SLOT,"szkielet?"));

		//kogo ma zabic w nastepnym kroku
		final HashMap<String, Pair<Integer, Integer>> toKill = new HashMap<String, Pair<Integer, Integer>>();
							toKill.put("bone dragon", new Pair<Integer, Integer>(1,0));
		final List<ChatAction> actions = new LinkedList<ChatAction>();
							actions.add(new SetQuestAction(QUEST_SLOT, 0, "szkielet"));
							actions.add(new StartRecordingKillsAction(QUEST_SLOT, 1, toKill));

		// gracz zgadza sie wykonac prozbe
		npc.add(ConversationStates.ATTENDING, Arrays.asList("pomożesz", "pomozesz", "pomogę"),
					new QuestInStateCondition(QUEST_SLOT, "szkielet?"),
					ConversationStates.ATTENDING,
					"Oh, wonderful! Tonight a terrifying #'/bone dragon/' appeared in my nightmare. Please slay it and bring 20 dog bones to place under my pillow. Remember, you must defeat it alone!",
				new MultipleActions(actions));
	}

	private void step3() {
		final SpeakerNPC npc = npcs.get("Alicja");
		//player nie zabil smoka
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0, "szkielet"),
								 new NotCondition(new KilledForQuestCondition(QUEST_SLOT, 1))),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("You have not slain the szkielet smoka by yourself yet, why are you deceiving me?");
					}
				});

		//player zabil smoka ale nie ma potrzebnych resurce	
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0,"szkielet"),
								 new KilledForQuestCondition(QUEST_SLOT, 1),
								 new NotCondition(new PlayerHasItemWithHimCondition("dog bone", 20))),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("Nawet jeśli zabiłeś szkielet smoka, na pewno odwiedzi mnie ponownie w koszmarze jeśli nie będę miała amuletu. Proszę, przynieś mi 20 kości dla psa.");
					}
				});
		//player wykonal obydwa zlecenia
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
			new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0,"szkielet"),
							 new KilledForQuestCondition(QUEST_SLOT, 1),
							 new PlayerHasItemWithHimCondition("dog bone", 20)),
			ConversationStates.ATTENDING, null,
			new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					raiser.say("Oh, you defeated it! Thank you! Please return in 24 hours to see if the talisman worked. Przypomnij mi mówiąc #szkielet.");
					player.drop("dog bone", 20);
					player.addKarma(10);
					player.setQuest(QUEST_SLOT, "czas_niebieski;"+System.currentTimeMillis());
				}
			});
	}

	private void czas2() {
		final SpeakerNPC npc = npcs.get("Alicja");
		final String extraTrigger = "szkielet";
		List<String> questTrigger;
		questTrigger = new LinkedList<String>(ConversationPhrases.QUEST_MESSAGES);
		questTrigger.add(extraTrigger);

		npc.add(ConversationStates.ATTENDING, questTrigger,
				new AndCondition(new QuestStateStartsWithCondition(QUEST_SLOT, "czas_niebieski"),
								 // jeszcze nie minal czas
								 new NotCondition(new TimePassedCondition(QUEST_SLOT, 1, DELAY_IN_MINUTES))),
				ConversationStates.ATTENDING, null,
				new SayTimeRemainingAction(QUEST_SLOT, 1, DELAY_IN_MINUTES, " A full day has not passed yet. Please return in "));

		npc.add(ConversationStates.ATTENDING, questTrigger,
				new AndCondition(new QuestStateStartsWithCondition(QUEST_SLOT, "czas_niebieski"),
								 // czas minal, dalszy krok 
								 new TimePassedCondition(QUEST_SLOT, 1, DELAY_IN_MINUTES)),
				ConversationStates.ATTENDING,
				"The talisman worked! Sadly, a different nightmare appeared tonight. Will you #help me?",
				new SetQuestAction(QUEST_SLOT,"niebieski?"));

		//kogo ma zabic w nastepnym kroku
		final HashMap<String, Pair<Integer, Integer>> toKill = new HashMap<String, Pair<Integer, Integer>>();
							toKill.put("blue dragon", new Pair<Integer, Integer>(1,0));
		final List<ChatAction> actions = new LinkedList<ChatAction>();
							actions.add(new SetQuestAction(QUEST_SLOT, 0, "niebieski"));
							actions.add(new StartRecordingKillsAction(QUEST_SLOT, 1, toKill));

		// gracz zgadza sie wykonac prozbe
		npc.add(ConversationStates.ATTENDING, Arrays.asList("help", "yes", "pomożesz", "pomozesz", "pomoge"),
				new QuestInStateCondition(QUEST_SLOT, "niebieski?"),
				ConversationStates.ATTENDING,
				"Och, wspaniale! Tej nocy przyśnił mi się #'/błękitny smok/', proszę odszukaj go i zabij."
				+ " Przynieś mi też jego 5 skór niebieskiego smoka. Jeśli włożę je pod poduszkę, będą odstraszały koszmary."
				+ " Pamintj, że musisz zabić go samodzielnie, bo skóry nie będą działały jak talizman!",
				new MultipleActions(actions));
	}

	private void step4() {
		final SpeakerNPC npc = npcs.get("Alicja");
		//player nie zabil smoka
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0, "niebieski"),
								 new NotCondition(new KilledForQuestCondition(QUEST_SLOT, 1))),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("You have not slain the błękitnego smoka by yourself yet, why are you deceiving me?");
					}
				});

		//player zabil smoka ale nie ma potrzebnych resurce	
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
			new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0,"niebieski"),
							 new KilledForQuestCondition(QUEST_SLOT, 1),
							 new NotCondition(new PlayerHasItemWithHimCondition("blue dragon leather", 5))),
			ConversationStates.ATTENDING, null,
			new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					raiser.say("Nawet jeśli zabiłeś błękitnego smoka, na pewno odwiedzi mnie ponownie w koszmarze jeśli nie będę miała amuletu. Proszę, przynieś mi skóry niebieskiego smoka.");
				}
			});

		//player wykonal obydwa zlecenia
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
			new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0,"niebieski"),
							 new KilledForQuestCondition(QUEST_SLOT, 1),
							 new PlayerHasItemWithHimCondition("blue dragon leather", 5)),
			ConversationStates.ATTENDING, null,
			new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					raiser.say("Oh, you defeated it! Thank you! Please return in 24 hours to see if the talisman worked. Przypomnij mi mówiąc #niebieski.");
					player.drop("blue dragon leather", 5);
					player.addKarma(10);
					player.setQuest(QUEST_SLOT, "czas_zgnily;"+System.currentTimeMillis());
				}
			});
	}

	private void czas3() {
		final SpeakerNPC npc = npcs.get("Alicja");
		final String extraTrigger = "niebieski";
		List<String> questTrigger;
		questTrigger = new LinkedList<String>(ConversationPhrases.QUEST_MESSAGES);
		questTrigger.add(extraTrigger);

		npc.add(ConversationStates.ATTENDING, questTrigger,
				new AndCondition(new QuestStateStartsWithCondition(QUEST_SLOT, "czas_zgnily"),
								 // jeszcze nie minal czas
								 new NotCondition(new TimePassedCondition(QUEST_SLOT, 1, DELAY_IN_MINUTES))),
				ConversationStates.ATTENDING, null,
				new SayTimeRemainingAction(QUEST_SLOT, 1, DELAY_IN_MINUTES, " A full day has not passed yet. Please return in "));

		npc.add(ConversationStates.ATTENDING, questTrigger,
				new AndCondition(new QuestStateStartsWithCondition(QUEST_SLOT, "czas_zgnily"),
								 // czas minal, dalszy krok
								 new TimePassedCondition(QUEST_SLOT, 1,DELAY_IN_MINUTES)),
				ConversationStates.ATTENDING,
				"The talisman worked! Sadly, a different nightmare appeared tonight. Will you #help me?",
				new SetQuestAction(QUEST_SLOT,"zgnily?"));

		//kogo ma zabic w nastepnym kroku
		final HashMap<String, Pair<Integer, Integer>> toKill = new HashMap<String, Pair<Integer, Integer>>();
							toKill.put("rotten dragon skeleton", new Pair<Integer, Integer>(1,0));
		final List<ChatAction> actions = new LinkedList<ChatAction>();
							actions.add(new SetQuestAction(QUEST_SLOT, 0, "zgnily"));
							actions.add(new StartRecordingKillsAction(QUEST_SLOT, 1, toKill));

		// gracz zgadza sie wykonac prozbe
		npc.add(ConversationStates.ATTENDING, Arrays.asList("help", "yes", "pomożesz", "pomozesz", "pomoge"),
				new QuestInStateCondition(QUEST_SLOT, "zgnily?"),
				ConversationStates.ATTENDING,
				"Och, wspaniale! Tej nocy przyśnił mi się #'/zgniły szkielet smoka/', proszę odszukaj go i zabij."
				+ " Przynieś mi też jego 30 kości dla psa. Jeśli włożę je pod poduszkę, będą odstraszały koszmary."
				+ " Pamintj, że musisz zabić go samodzielnie, bo kości nie będą działały jak talizman!",
				new MultipleActions(actions));

	}

	private void step5() {
		final SpeakerNPC npc = npcs.get("Alicja");
		//player nie zabil smoka
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0, "zgnily"),
								 new NotCondition(new KilledForQuestCondition(QUEST_SLOT, 1))),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("You have not slain the zgniły szkielet smoka by yourself yet, why are you deceiving me?");
					}
				});

		//player zabil smoka ale nie ma potrzebnych resurce	
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0,"zgnily"),
								 new KilledForQuestCondition(QUEST_SLOT, 1),
								 new NotCondition(new PlayerHasItemWithHimCondition("dog bone", 30))),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("Nawet jeśli zabiłeś zgniły szkielet smoka, na pewno odwiedzi mnie ponownie w koszmarze jeśli nie będę miała amuletu. Proszę, przynieś mi kości dla psa.");
					}
				});

		//player wykonal obydwa zlecenia
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0,"zgnily"),
								 new KilledForQuestCondition(QUEST_SLOT, 1),
								 new PlayerHasItemWithHimCondition("dog bone", 30)),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("Oh, you defeated it! Thank you! Please return in 24 hours to see if the talisman worked. Przypomnij mi mówiąc #zgnily.");
						player.drop("dog bone", 30);
						player.addKarma(10);
						player.setQuest(QUEST_SLOT, "czas_czerwony;"+System.currentTimeMillis());
					}
				});
	}

	private void czas4() {
		final SpeakerNPC npc = npcs.get("Alicja");
		final String extraTrigger = "zgnily";
		List<String> questTrigger;
		questTrigger = new LinkedList<String>(ConversationPhrases.QUEST_MESSAGES);
		questTrigger.add(extraTrigger);

		npc.add(ConversationStates.ATTENDING, questTrigger,
				new AndCondition(new QuestStateStartsWithCondition(QUEST_SLOT, "czas_czerwony"),
								 // jeszcze nie minal czas
								 new NotCondition(new TimePassedCondition(QUEST_SLOT, 1, DELAY_IN_MINUTES))),
				ConversationStates.ATTENDING, null,
				new SayTimeRemainingAction(QUEST_SLOT, 1, DELAY_IN_MINUTES, " A full day has not passed yet. Please return in "));

		npc.add(ConversationStates.ATTENDING, questTrigger,
				new AndCondition(new QuestStateStartsWithCondition(QUEST_SLOT, "czas_czerwony"),
								 // czas minal, dalszy krok
								 new TimePassedCondition(QUEST_SLOT, 1, DELAY_IN_MINUTES)),
				ConversationStates.ATTENDING,
				"The talisman worked! Sadly, a different nightmare appeared tonight. Will you #help me?",
				new SetQuestAction(QUEST_SLOT,"czerwony?"));

		//kogo ma zabic w nastepnym kroku
		final HashMap<String, Pair<Integer, Integer>> toKill = new HashMap<String, Pair<Integer, Integer>>();
							toKill.put("red dragon", new Pair<Integer, Integer>(1,0));
		final List<ChatAction> actions = new LinkedList<ChatAction>();
							actions.add(new SetQuestAction(QUEST_SLOT, 0, "czerwony"));
							actions.add(new StartRecordingKillsAction(QUEST_SLOT, 1, toKill));

		// gracz zgadza sie wykonac prozbe
		npc.add(ConversationStates.ATTENDING, Arrays.asList("help", "yes", "pomożesz", "pomozesz", "pomoge"),
				new QuestInStateCondition(QUEST_SLOT, "czerwony?"),
				ConversationStates.ATTENDING,
				"Och, wspaniale! Tej nocy przyśnił mi się #'/czerwony smok/', proszę odszukaj go i zabij."
				+ " Przynieś mi też jego 5 skór czerwonego smoka. Jeśli włożę je pod poduszkę, będą odstraszały koszmary."
				+ " Pamintj, że musisz zabić go samodzielnie, bo skóry nie będą działały jak talizman!",
				new MultipleActions(actions));
	}

	private void step6() {
		final SpeakerNPC npc = npcs.get("Alicja");
		//player nie zabil smoka
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0, "czerwony"),
								 new NotCondition(new KilledForQuestCondition(QUEST_SLOT, 1))),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("You have not slain the czerwonego smoka by yourself yet, why are you deceiving me?");
					}
				});

		//player zabil smoka ale nie ma potrzebnych resurce
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0,"czerwony"),
								 new KilledForQuestCondition(QUEST_SLOT, 1),
								 new NotCondition(new PlayerHasItemWithHimCondition("red dragon leather", 5))),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("Even if you slew the czerwonego smoka, it will surely return in my nightmares unless I have the talisman. Please deliver skóry czerwonego smoka.");
					}
		});
		//player wykonal obydwa zlecenia
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0,"czerwony"),
								 new KilledForQuestCondition(QUEST_SLOT, 1),
								 new PlayerHasItemWithHimCondition("red dragon leather", 5)),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("Oh, you defeated it! Thank you! Please return in 24 hours to see if the talisman worked. Przypomnij mi mówiąc #czerwony.");
						player.drop("red dragon leather", 5);
						player.addKarma(10);
						player.setQuest(QUEST_SLOT, "czas_pustynny;"+System.currentTimeMillis());
					}
				});
	}

	private void czas5() {
		final SpeakerNPC npc = npcs.get("Alicja");
		final String extraTrigger = "czerwony";
		List<String> questTrigger;
		questTrigger = new LinkedList<String>(ConversationPhrases.QUEST_MESSAGES);
		questTrigger.add(extraTrigger);

		npc.add(ConversationStates.ATTENDING, questTrigger,
				new AndCondition(new QuestStateStartsWithCondition(QUEST_SLOT, "czas_pustynny"),
								 // jeszcze nie minal czas
								 new NotCondition(new TimePassedCondition(QUEST_SLOT, 1, DELAY_IN_MINUTES))),
				ConversationStates.ATTENDING, null,
				new SayTimeRemainingAction(QUEST_SLOT, 1, DELAY_IN_MINUTES, " A full day has not passed yet. Please return in "));

		npc.add(ConversationStates.ATTENDING, questTrigger,
				new AndCondition(new QuestStateStartsWithCondition(QUEST_SLOT, "czas_pustynny"),
								 // czas minal, dalszy krok
								 new TimePassedCondition(QUEST_SLOT, 1, DELAY_IN_MINUTES)),
				ConversationStates.ATTENDING,
				"The talisman worked! Sadly, a different nightmare appeared tonight. Will you #help me?",
				new SetQuestAction(QUEST_SLOT,"pustynny?"));

		//kogo ma zabic w nastepnym kroku
		final HashMap<String, Pair<Integer, Integer>> toKill = new HashMap<String, Pair<Integer, Integer>>();
							toKill.put("desert dragon", new Pair<Integer, Integer>(1,0));
		final List<ChatAction> actions = new LinkedList<ChatAction>();
							actions.add(new SetQuestAction(QUEST_SLOT, 0, "pustynny"));
							actions.add(new StartRecordingKillsAction(QUEST_SLOT, 1, toKill));

		// gracz zgadza sie wykonac prozbe
		npc.add(ConversationStates.ATTENDING, Arrays.asList("help", "yes", "pomożesz", "pomozesz", "pomoge"),
				new QuestInStateCondition(QUEST_SLOT, "pustynny?"),
				ConversationStates.ATTENDING,
				"Och, wspaniale! Tej nocy przyśnił mi się #'/pustynny smok/', proszę odszukaj go i zabij."
				+ " Przynieś mi też jego 5 skór złotego smoka. Jeśli włożę je pod poduszkę, będą odstraszały koszmary."
				+ " Pamintj, że musisz zabić go samodzielnie, bo skóry nie będą działały jak talizman!",
				new MultipleActions(actions));
	}

	private void step7() {
		final SpeakerNPC npc = npcs.get("Alicja");
		//player nie zabil smoka
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0, "pustynny"),
								 new NotCondition(new KilledForQuestCondition(QUEST_SLOT, 1))),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("You have not slain the pustynnego smoka by yourself yet, why are you deceiving me?");
					}
				});

		//player zabil smoka ale nie ma potrzebnych resurce	
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0,"pustynny"),
								 new KilledForQuestCondition(QUEST_SLOT, 1),
								 new NotCondition(new PlayerHasItemWithHimCondition("golden dragon leather", 5))),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("Nawet jeśli zabiłeś pustynnego smoka, na pewno odwiedzi mnie ponownie w koszmarze jeśli nie będę miała amuletu. Proszę, podrzuć mi skóry złotego smoka.");
					}
				});
		//player wykonal obydwa zlecenia
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0,"pustynny"),
								 new KilledForQuestCondition(QUEST_SLOT, 1),
								 new PlayerHasItemWithHimCondition("golden dragon leather", 5)),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("Oh, you defeated it! Thank you! Please return in 24 hours to see if the talisman worked. Przypomnij mi mówiąc #pustynny.");
						player.drop("golden dragon leather", 5);
						player.addKarma(10);
						player.setQuest(QUEST_SLOT, "czas_zloty;"+System.currentTimeMillis());
					}
				});
	}

	private void czas6() {
		final SpeakerNPC npc = npcs.get("Alicja");
		final String extraTrigger = "pustynny";
		List<String> questTrigger;
		questTrigger = new LinkedList<String>(ConversationPhrases.QUEST_MESSAGES);
		questTrigger.add(extraTrigger);

		npc.add(ConversationStates.ATTENDING, questTrigger,
				new AndCondition(new QuestStateStartsWithCondition(QUEST_SLOT, "czas_zloty"),
								 // jeszcze nie minal czas
								 new NotCondition(new TimePassedCondition(QUEST_SLOT, 1, DELAY_IN_MINUTES))),
				ConversationStates.ATTENDING, null,
				new SayTimeRemainingAction(QUEST_SLOT, 1, DELAY_IN_MINUTES, " A full day has not passed yet. Please return in "));

		npc.add(ConversationStates.ATTENDING, questTrigger,
				new AndCondition(new QuestStateStartsWithCondition(QUEST_SLOT, "czas_zloty"),
								 // czas minal, dalszy krok
								 new TimePassedCondition(QUEST_SLOT, 1, DELAY_IN_MINUTES)),
				ConversationStates.ATTENDING,
				"The talisman worked! Sadly, a different nightmare appeared tonight. Will you #help me?",
				new SetQuestAction(QUEST_SLOT,"zloty?"));

		//kogo ma zabic w nastepnym kroku
		final HashMap<String, Pair<Integer, Integer>> toKill = new HashMap<String, Pair<Integer, Integer>>();
							toKill.put("flying golden dragon", new Pair<Integer, Integer>(1,0));
		final List<ChatAction> actions = new LinkedList<ChatAction>();
							actions.add(new SetQuestAction(QUEST_SLOT, 0, "zloty"));
							actions.add(new StartRecordingKillsAction(QUEST_SLOT, 1, toKill));

		// gracz zgadza sie wykonac prozbe
		npc.add(ConversationStates.ATTENDING, Arrays.asList("help", "yes", "pomożesz", "pomozesz", "pomoge"),
				new QuestInStateCondition(QUEST_SLOT, "zloty?"),
				ConversationStates.ATTENDING,
				"Och, wspaniale! Tej nocy przyśnił mi się #'/złoty smok/', proszę odszukaj go i zabij."
				+ " Przynieś mi też jego 5 skór złotego smoka. Jeśli włożę je pod poduszkę, będą odstraszały koszmary."
				+ " Pamintj, że musisz zabić go samodzielnie, bo skóry nie będą działały jak talizman!",
				new MultipleActions(actions));
	}

	private void step8() {
		final SpeakerNPC npc = npcs.get("Alicja");
		//player nie zabil smoka
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0, "zloty"),
								 new NotCondition(new KilledForQuestCondition(QUEST_SLOT, 1))),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("You have not slain the złotego smoka by yourself yet, why are you deceiving me?");
					}
				});

		//player zabil smoka ale nie ma potrzebnych resurce	
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0,"zloty"),
								 new KilledForQuestCondition(QUEST_SLOT, 1),
								 new NotCondition(new PlayerHasItemWithHimCondition("golden dragon leather", 5))),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("Nawet jeśli zabiłeś złotego smoka, na pewno odwiedzi mnie ponownie w koszmarze jeśli nie będę miała amuletu. Proszę, przynieś mi jeszcze skóry złotego smoka.");
					}
				});

		//player wykonal obydwa zlecenia
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0,"zloty"),
								 new KilledForQuestCondition(QUEST_SLOT, 1),
								 new PlayerHasItemWithHimCondition("golden dragon leather", 5)),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("Oh, you defeated it! Thank you! Please return in 24 hours to see if the talisman worked. Przypomnij mi mówiąc #zloty.");
						player.drop("golden dragon leather", 5);
						player.addKarma(10);
						player.setQuest(QUEST_SLOT, "czas_dwa_niebieski;"+System.currentTimeMillis());
					}
				});
	}

	private void czas7() {
		final SpeakerNPC npc = npcs.get("Alicja");
		final String extraTrigger = "zloty";
		List<String> questTrigger;
		questTrigger = new LinkedList<String>(ConversationPhrases.QUEST_MESSAGES);
		questTrigger.add(extraTrigger);

		npc.add(ConversationStates.ATTENDING, questTrigger,
				new AndCondition(new QuestStateStartsWithCondition(QUEST_SLOT, "czas_dwa_niebieski"),
								 // jeszcze nie minal czas
								 new NotCondition(new TimePassedCondition(QUEST_SLOT, 1, DELAY_IN_MINUTES))),
				ConversationStates.ATTENDING, null,
				new SayTimeRemainingAction(QUEST_SLOT, 1, DELAY_IN_MINUTES, " A full day has not passed yet. Please return in "));

		npc.add(ConversationStates.ATTENDING, questTrigger,
				new AndCondition(new QuestStateStartsWithCondition(QUEST_SLOT, "czas_dwa_niebieski"),
								 // czas minal, dalszy krok
								 new TimePassedCondition(QUEST_SLOT, 1, DELAY_IN_MINUTES)),
				ConversationStates.ATTENDING,
				"The talisman worked! Sadly, a different nightmare appeared tonight. Will you #help me?",
				new SetQuestAction(QUEST_SLOT,"dwa_niebieski?"));

		//kogo ma zabic w nastepnym kroku
		final HashMap<String, Pair<Integer, Integer>> toKill = new HashMap<String, Pair<Integer, Integer>>();
							toKill.put("twin headed dragon", new Pair<Integer, Integer>(1,0));
		final List<ChatAction> actions = new LinkedList<ChatAction>();
							actions.add(new SetQuestAction(QUEST_SLOT, 0, "dwa_niebieski"));
							actions.add(new StartRecordingKillsAction(QUEST_SLOT, 1, toKill));

		// gracz zgadza sie wykonac prozbe
		npc.add(ConversationStates.ATTENDING, Arrays.asList("help", "yes", "pomożesz", "pomozesz", "pomoge"),
				new QuestInStateCondition(QUEST_SLOT, "dwa_niebieski?"),
				ConversationStates.ATTENDING,
				"Och, wspaniale! Tej nocy przyśnił mi się #'/smok dwugłowy niebieski/', proszę odszukaj go i zabij."
				+ " Przynieś mi też jego 10 skór niebieskiego smoka. Jeśli włożę je pod poduszkę, będą odstraszały koszmary."
				+ " Pamintj, że musisz zabić go samodzielnie, bo skóry nie będą działały jak talizman!",
				new MultipleActions(actions));
	}

	private void step9() {
		final SpeakerNPC npc = npcs.get("Alicja");
		//player nie zabil smoka
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0, "dwa_niebieski"),
								 new NotCondition(new KilledForQuestCondition(QUEST_SLOT, 1))),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("You have not slain the dwugłowego niebieskiego smoka by yourself yet, why are you deceiving me?");
					}
				});

		//player zabil smoka ale nie ma potrzebnych resurce	
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0,"dwa_niebieski"),
								 new KilledForQuestCondition(QUEST_SLOT, 1),
								 new NotCondition(new PlayerHasItemWithHimCondition("blue dragon leather", 10))),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("Even if you slew the dwugłowego niebieskiego smoka, it will surely return in my nightmares unless I have the talisman. Please deliver skóry niebieskiego smoka.");
					}
				});

		//player wykonal obydwa zlecenia
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0,"dwa_niebieski"),
								 new KilledForQuestCondition(QUEST_SLOT, 1),
								 new PlayerHasItemWithHimCondition("blue dragon leather", 10)),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("Oh, you defeated it! Thank you! Please return in 24 hours to see if the talisman worked. Przypomnij mi mówiąc #dwuglowy #niebieski.");
						player.drop("blue dragon leather", 10);
						player.addKarma(10);
						player.setQuest(QUEST_SLOT, "czas_dwu_zielony;"+System.currentTimeMillis());
					}
				});
	}

	private void czas8() {
		final SpeakerNPC npc = npcs.get("Alicja");
		final String extraTrigger = "dwuglowy niebieski";
		List<String> questTrigger;
		questTrigger = new LinkedList<String>(ConversationPhrases.QUEST_MESSAGES);
		questTrigger.add(extraTrigger);

		npc.add(ConversationStates.ATTENDING, questTrigger,
				new AndCondition(new QuestStateStartsWithCondition(QUEST_SLOT, "czas_dwu_zielony"),
								 // jeszcze nie minal czas
								 new NotCondition(new TimePassedCondition(QUEST_SLOT, 1,DELAY_IN_MINUTES))),
				ConversationStates.ATTENDING, null,
				new SayTimeRemainingAction(QUEST_SLOT, 1, DELAY_IN_MINUTES, " A full day has not passed yet. Please return in "));

		npc.add(ConversationStates.ATTENDING, questTrigger,
				new AndCondition(new QuestStateStartsWithCondition(QUEST_SLOT, "czas_dwu_zielony"),
								 // czas minal, dalszy krok
								 new TimePassedCondition(QUEST_SLOT, 1, DELAY_IN_MINUTES)),
				ConversationStates.ATTENDING,
				"The talisman worked! Sadly, a different nightmare appeared tonight. Will you #help me?",
				new SetQuestAction(QUEST_SLOT,"dwu_zielony?"));

		//kogo ma zabic w nastepnym kroku
		final HashMap<String, Pair<Integer, Integer>> toKill = new HashMap<String, Pair<Integer, Integer>>();
							toKill.put("two-headed green dragon", new Pair<Integer, Integer>(1,0));
		final List<ChatAction> actions = new LinkedList<ChatAction>();
							actions.add(new SetQuestAction(QUEST_SLOT, 0, "dwu_zielony"));
							actions.add(new StartRecordingKillsAction(QUEST_SLOT, 1, toKill));

		// gracz zgadza sie wykonac prozbe
		npc.add(ConversationStates.ATTENDING, Arrays.asList("help", "yes", "pomożesz", "pomozesz", "pomoge"),
				new QuestInStateCondition(QUEST_SLOT, "dwu_zielony?"),
				ConversationStates.ATTENDING,
				"Och, wspaniale! Tej nocy przyśnił mi się #'/dwugłowy zielony smok/', proszę odszukaj go i zabij."
				+ " Przynieś mi też jego 10 skór zielonego smoka. Jeśli włożę je pod poduszkę, będą odstraszały koszmary."
				+ " Pamintj, że musisz zabić go samodzielnie, bo skóry nie będą działały jak talizman!",
				new MultipleActions(actions));
	}

	private void step10() {
		final SpeakerNPC npc = npcs.get("Alicja");
		//player nie zabil smoka
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0, "dwu_zielony"),
								 new NotCondition(new KilledForQuestCondition(QUEST_SLOT, 1))),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("You have not slain the dwugłowego zielonego smoka by yourself yet, why are you deceiving me?");
					}
				});

		//player zabil smoka ale nie ma potrzebnych resurce	
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0,"dwu_zielony"),
								 new KilledForQuestCondition(QUEST_SLOT, 1),
								 new NotCondition(new PlayerHasItemWithHimCondition("green dragon leather", 10))),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("Even if you slew the smoka, it will surely return in my nightmares unless I have the talisman. Please deliver te skóry zielonego smoka.");
					}
				});

		//player wykonal obydwa zlecenia
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0,"dwu_zielony"),
								 new KilledForQuestCondition(QUEST_SLOT, 1),
								 new PlayerHasItemWithHimCondition("green dragon leather", 10)),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("Oh, you defeated it! Thank you! Please return in 24 hours to see if the talisman worked. Przypomnij mi mówiąc #dwuglowy #zielony.");
						player.drop("green dragon leather", 10);
						player.addKarma(10);
						player.setQuest(QUEST_SLOT, "czas_arktyczny;"+System.currentTimeMillis());
					}
				});
	}

	private void czas9() {
		final SpeakerNPC npc = npcs.get("Alicja");
		final String extraTrigger = "dwuglowy zielony";
		List<String> questTrigger;
		questTrigger = new LinkedList<String>(ConversationPhrases.QUEST_MESSAGES);
		questTrigger.add(extraTrigger);

		npc.add(ConversationStates.ATTENDING, questTrigger,
				new AndCondition(new QuestStateStartsWithCondition(QUEST_SLOT, "czas_arktyczny"),
								 // jeszcze nie minal czas
								 new NotCondition(new TimePassedCondition(QUEST_SLOT, 1, DELAY_IN_MINUTES))),
				ConversationStates.ATTENDING, null,
				new SayTimeRemainingAction(QUEST_SLOT, 1, DELAY_IN_MINUTES, " A full day has not passed yet. Please return in "));

		npc.add(ConversationStates.ATTENDING, questTrigger,
				new AndCondition(new QuestStateStartsWithCondition(QUEST_SLOT, "czas_arktyczny"),
								 // czas minal, dalszy krok
								 new TimePassedCondition(QUEST_SLOT, 1, DELAY_IN_MINUTES)),
				ConversationStates.ATTENDING,
				"The talisman worked! Sadly, a different nightmare appeared tonight. Will you #help me?",
				new SetQuestAction(QUEST_SLOT,"arktyczny?"));

		//kogo ma zabic w nastepnym kroku
		final HashMap<String, Pair<Integer, Integer>> toKill = new HashMap<String, Pair<Integer, Integer>>();
							toKill.put("arctic leviathan dragon", new Pair<Integer, Integer>(1,0));
		final List<ChatAction> actions = new LinkedList<ChatAction>();
							actions.add(new SetQuestAction(QUEST_SLOT, 0, "arktyczny"));
							actions.add(new StartRecordingKillsAction(QUEST_SLOT, 1, toKill));

		// gracz zgadza sie wykonac prozbe
		npc.add(ConversationStates.ATTENDING, Arrays.asList("help", "yes", "pomożesz", "pomozesz", "pomoge"),
				new QuestInStateCondition(QUEST_SLOT, "arktyczny?"),
				ConversationStates.ATTENDING,
				"Och, wspaniale! Tej nocy przyśnił mi się #'/smok arktyczny/', proszę odszukaj go i zabij."
				+ " Przynieś mi też jego 10 kłów smoka. Jeśli włożę je pod poduszkę, będą odstraszały koszmary."
				+ " Pamintj, że musisz zabić go samodzielnie, bo kły nie będą działały jak talizman!",
				new MultipleActions(actions));
	}

	private void step11() {
		final SpeakerNPC npc = npcs.get("Alicja");
		//player nie zabil smoka
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0, "arktyczny"),
								 new NotCondition(new KilledForQuestCondition(QUEST_SLOT, 1))),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("You have not slain the arktycznego smoka by yourself yet, why are you deceiving me?");
					}
				});

		//player zabil smoka ale nie ma potrzebnych resurce
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0,"arktyczny"),
								 new KilledForQuestCondition(QUEST_SLOT, 1),
								 new NotCondition(new PlayerHasItemWithHimCondition("dragon tusk", 10))),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("Nawet jeśli zabiłeś arktycznego smoka, na pewno odwiedzi mnie ponownie w koszmarze jeśli nie będę miała amuletu. Proszę, przynieś kły smoka.");
					}
				});

		//player wykonal obydwa zlecenia
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0,"arktyczny"),
								 new KilledForQuestCondition(QUEST_SLOT, 1),
								 new PlayerHasItemWithHimCondition("dragon tusk", 10)),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("Oh, you defeated it! Thank you! Please return in 24 hours to see if the talisman worked. Przypomnij mi mówiąc #arktyczny.");
						player.drop("dragon tusk", 10);
						player.addKarma(10);
						player.setQuest(QUEST_SLOT, "czas_dru_czerwony;"+System.currentTimeMillis());
					}
				});
	}

	private void czas10() {
		final SpeakerNPC npc = npcs.get("Alicja");
		final String extraTrigger = "arktyczny";
		List<String> questTrigger;
		questTrigger = new LinkedList<String>(ConversationPhrases.QUEST_MESSAGES);
		questTrigger.add(extraTrigger);

		npc.add(ConversationStates.ATTENDING, questTrigger,
				new AndCondition(new QuestStateStartsWithCondition(QUEST_SLOT, "czas_dru_czerwony"),
								 // jeszcze nie minal czas
								 new NotCondition(new TimePassedCondition(QUEST_SLOT, 1, DELAY_IN_MINUTES))),
				ConversationStates.ATTENDING, null,
				new SayTimeRemainingAction(QUEST_SLOT, 1, DELAY_IN_MINUTES, " A full day has not passed yet. Please return in "));

		npc.add(ConversationStates.ATTENDING, questTrigger,
				new AndCondition(new QuestStateStartsWithCondition(QUEST_SLOT, "czas_dru_czerwony"),
								 // czas minal, dalszy krok
								 new TimePassedCondition(QUEST_SLOT, 1, DELAY_IN_MINUTES)),
				ConversationStates.ATTENDING,
				"The talisman worked! Sadly, a different nightmare appeared tonight. Will you #help me?",
				new SetQuestAction(QUEST_SLOT,"dru_czerwony?"));

		//kogo ma zabic w nastepnym kroku
		final HashMap<String, Pair<Integer, Integer>> toKill = new HashMap<String, Pair<Integer, Integer>>();
							toKill.put("two-headed red dragon", new Pair<Integer, Integer>(1,0));
		final List<ChatAction> actions = new LinkedList<ChatAction>();
							actions.add(new SetQuestAction(QUEST_SLOT, 0, "dru_czerwony"));
							actions.add(new StartRecordingKillsAction(QUEST_SLOT, 1, toKill));

		// gracz zgadza sie wykonac prozbe
		npc.add(ConversationStates.ATTENDING, Arrays.asList("help", "yes", "pomożesz", "pomozesz", "pomoge"),
				new QuestInStateCondition(QUEST_SLOT, "dru_czerwony?"),
				ConversationStates.ATTENDING,
				"Och, wspaniale! Tej nocy przyśnił mi się #'/dwugłowy czerwony smok/', proszę odszukaj go i zabij."
				+ " Przynieś mi też jego 10 skór czerwonego smoka. Jeśli włożę je pod poduszkę, będą odstraszały koszmary."
				+ " Pamintj, że musisz zabić go samodzielnie, bo skóry nie będą działały jak talizman!",
				new MultipleActions(actions));
	}

	private void step12() {
		final SpeakerNPC npc = npcs.get("Alicja");
		//player nie zabil smoka
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0, "dru_czerwony"),
								 new NotCondition(new KilledForQuestCondition(QUEST_SLOT, 1))),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("You have not slain the dwugłowego czerwonego smoka by yourself yet, why are you deceiving me?");
					}
				});

		//player zabil smoka ale nie ma potrzebnych resurce
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0,"dru_czerwony"),
								 new KilledForQuestCondition(QUEST_SLOT, 1),
								 new NotCondition(new PlayerHasItemWithHimCondition("red dragon leather", 10))),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("Even if you slew the smoka, it will surely return in my nightmares unless I have the talisman. Please deliver te skóry czerwonego smoka, o które prosiłam.");
					}
				});

		//player wykonal obydwa zlecenia
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0,"dru_czerwony"),
								 new KilledForQuestCondition(QUEST_SLOT, 1),
								 new PlayerHasItemWithHimCondition("red dragon leather", 10)),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("Oh, you defeated it! Thank you! Please return in 24 hours to see if the talisman worked. Przypomnij mi mówiąc #dwuglowy #czerwony.");
						player.drop("red dragon leather", 10);
						player.addKarma(10);
						player.setQuest(QUEST_SLOT, "czas_czarny;"+System.currentTimeMillis());
					}
				});
	}

	private void czas11() {
		final SpeakerNPC npc = npcs.get("Alicja");
		final String extraTrigger = "dwuglowy czerwony";
		List<String> questTrigger;
		questTrigger = new LinkedList<String>(ConversationPhrases.QUEST_MESSAGES);
		questTrigger.add(extraTrigger);

		npc.add(ConversationStates.ATTENDING, questTrigger,
				new AndCondition(new QuestStateStartsWithCondition(QUEST_SLOT, "czas_czarny"),
								 // jeszcze nie minal czas
								 new NotCondition(new TimePassedCondition(QUEST_SLOT, 1, DELAY_IN_MINUTES))),
				ConversationStates.ATTENDING, null,
				new SayTimeRemainingAction(QUEST_SLOT, 1, DELAY_IN_MINUTES, " A full day has not passed yet. Please return in "));

		npc.add(ConversationStates.ATTENDING, questTrigger,
				new AndCondition(new QuestStateStartsWithCondition(QUEST_SLOT, "czas_czarny"),
								 // czas minal, dalszy krok
								 new TimePassedCondition(QUEST_SLOT, 1, DELAY_IN_MINUTES)),
				ConversationStates.ATTENDING,
				"The talisman worked! Sadly, a different nightmare appeared tonight. Will you #help me?",
				new SetQuestAction(QUEST_SLOT,"czarny?"));

		//kogo ma zabic w nastepnym kroku
		final HashMap<String, Pair<Integer, Integer>> toKill = new HashMap<String, Pair<Integer, Integer>>();
							toKill.put("black dragon", new Pair<Integer, Integer>(1,0));
		final List<ChatAction> actions = new LinkedList<ChatAction>();
							actions.add(new SetQuestAction(QUEST_SLOT, 0, "czarny"));
							actions.add(new StartRecordingKillsAction(QUEST_SLOT, 1, toKill));

		// gracz zgadza sie wykonac prozbe
		npc.add(ConversationStates.ATTENDING, Arrays.asList("help", "yes", "pomożesz", "pomozesz", "pomoge"),
				new QuestInStateCondition(QUEST_SLOT, "czarny?"),
				ConversationStates.ATTENDING,
				"Och, wspaniale! Tej nocy przyśnił mi się #'/czarny smok/', proszę odszukaj go i zabij."
				+ " Przynieś mi też jego 15 skór czarnego smoka. Jeśli włożę je pod poduszkę, będą odstraszały koszmary."
				+ " Pamintj, że musisz zabić go samodzielnie, bo skóry nie będą działały jak talizman!",
				new MultipleActions(actions));
	}

	private void step13() {
		final SpeakerNPC npc = npcs.get("Alicja");
		//player nie zabil smoka
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0, "czarny"),
								 new NotCondition(new KilledForQuestCondition(QUEST_SLOT, 1))),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("You have not slain the czarnego smoka by yourself yet, why are you deceiving me?");
					}
				});

		//player zabil smoka ale nie ma potrzebnych resurce	
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0,"czarny"),
								 new KilledForQuestCondition(QUEST_SLOT, 1),
								 new NotCondition(new PlayerHasItemWithHimCondition("black dragon leather", 15))),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("Nawet jeśli zabiłeś smoka, na pewno odwiedzi mnie ponownie w koszmarze jeśli nie będę miała amuletu. Proszę, przynieś mi te skóry czarnego smoka.");
					}
				});

		//player wykonal obydwa zlecenia
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0,"czarny"),
								 new KilledForQuestCondition(QUEST_SLOT, 1),
								 new PlayerHasItemWithHimCondition("black dragon leather", 15)),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("Oh, you defeated it! Thank you! Please return in 24 hours to see if the talisman worked. Przypomnij mi mówiąc #czarny.");
						player.drop("black dragon leather", 15);
						player.addKarma(10);
						player.setQuest(QUEST_SLOT, "czas_dwuglowy_czarny;"+System.currentTimeMillis());
					}
				});
	}

	private void czas12() {
		final SpeakerNPC npc = npcs.get("Alicja");
		final String extraTrigger = "czarny";
		List<String> questTrigger;
		questTrigger = new LinkedList<String>(ConversationPhrases.QUEST_MESSAGES);
		questTrigger.add(extraTrigger);

		npc.add(ConversationStates.ATTENDING, questTrigger,
				new AndCondition(new QuestStateStartsWithCondition(QUEST_SLOT, "czas_dwuglowy_czarny"),
								 // jeszcze nie minal czas
								 new NotCondition(new TimePassedCondition(QUEST_SLOT, 1, DELAY_IN_MINUTES))),
				ConversationStates.ATTENDING, null,
				new SayTimeRemainingAction(QUEST_SLOT, 1, DELAY_IN_MINUTES, " A full day has not passed yet. Please return in "));

		npc.add(ConversationStates.ATTENDING, questTrigger,
				new AndCondition(new QuestStateStartsWithCondition(QUEST_SLOT, "czas_dwuglowy_czarny"),
								 // czas minal, dalszy krok
								 new TimePassedCondition(QUEST_SLOT, 1, DELAY_IN_MINUTES)),
				ConversationStates.ATTENDING,
				"The talisman worked! Sadly, a different nightmare appeared tonight. Will you #help me?",
				new SetQuestAction(QUEST_SLOT,"dwuglowy_czarny?"));

		//kogo ma zabic w nastepnym kroku
		final HashMap<String, Pair<Integer, Integer>> toKill = new HashMap<String, Pair<Integer, Integer>>();
							toKill.put("two-headed black dragon", new Pair<Integer, Integer>(1,0));
		final List<ChatAction> actions = new LinkedList<ChatAction>();
							actions.add(new SetQuestAction(QUEST_SLOT, 0, "dwuglowy_czarny"));
							actions.add(new StartRecordingKillsAction(QUEST_SLOT, 1, toKill));

		// gracz zgadza sie wykonac prozbe
		npc.add(ConversationStates.ATTENDING, Arrays.asList("help", "yes", "pomożesz", "pomozesz", "pomoge"),
				new QuestInStateCondition(QUEST_SLOT, "dwuglowy_czarny?"),
				ConversationStates.ATTENDING,
				"Och, wspaniale! Tej nocy przyśnił mi się #'/dwugłowy czarny smok/', proszę odszukaj go i zabij."
				+ " Przynieś mi też jego 25 skór czarnego smoka. Jeśli włożę je pod poduszkę, będą odstraszały koszmary."
				+ " Pamintj, że musisz zabić go samodzielnie, bo skóry nie będą działały jak talizman!",
				new MultipleActions(actions));
	}

	private void step14() {
		final SpeakerNPC npc = npcs.get("Alicja");
		//player nie zabil smoka
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0, "dwuglowy_czarny"),
								 new NotCondition(new KilledForQuestCondition(QUEST_SLOT, 1))),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("You have not slain the dwugłowego czarnego smoka by yourself yet, why are you deceiving me?");
					}
				});

		//player zabil smoka ale nie ma potrzebnych resurce	
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0,"dwuglowy_czarny"),
								 new KilledForQuestCondition(QUEST_SLOT, 1),
								 new NotCondition(new PlayerHasItemWithHimCondition("black dragon leather", 25))),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("Nawet jeśli zabiłeś smoka, na pewno odwiedzi mnie ponownie w koszmarze jeśli nie będę miała amuletu. Proszę, przynieś dla mnie skóry czarnego smoka.");
					}
				});

		//player wykonal obydwa zlecenia
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0,"dwuglowy_czarny"),
								 new KilledForQuestCondition(QUEST_SLOT, 1),
								 new PlayerHasItemWithHimCondition("black dragon leather", 25)),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("Oh, you defeated it! Thank you! Please return in 24 hours to see if the talisman worked. Przypomnij mi mówiąc #czarny #dwuglowy.");
						player.drop("black dragon leather", 25);
						player.addKarma(10);
						player.setQuest(QUEST_SLOT, "czas_czar_latajacy;"+System.currentTimeMillis());
					}
				});
	}

	private void czas13() {
		final SpeakerNPC npc = npcs.get("Alicja");
		final String extraTrigger = "czarny dwuglowy";
		List<String> questTrigger;
		questTrigger = new LinkedList<String>(ConversationPhrases.QUEST_MESSAGES);
		questTrigger.add(extraTrigger);

		npc.add(ConversationStates.ATTENDING, questTrigger,
				new AndCondition(new QuestStateStartsWithCondition(QUEST_SLOT, "czas_czar_latajacy"),
								 // jeszcze nie minal czas
								 new NotCondition(new TimePassedCondition(QUEST_SLOT, 1, DELAY_IN_MINUTES))),
				ConversationStates.ATTENDING, null,
				new SayTimeRemainingAction(QUEST_SLOT, 1, DELAY_IN_MINUTES, " A full day has not passed yet. Please return in "));

		npc.add(ConversationStates.ATTENDING, questTrigger,
				new AndCondition(new QuestStateStartsWithCondition(QUEST_SLOT, "czas_czar_latajacy"),
								 // czas minal, dalszy krok
								 new TimePassedCondition(QUEST_SLOT, 1, DELAY_IN_MINUTES)),
				ConversationStates.ATTENDING,
				"The talisman worked! Sadly, a different nightmare appeared tonight. Will you #help me?",
				new SetQuestAction(QUEST_SLOT,"czar_latajacy?"));

		//kogo ma zabic w nastepnym kroku
		final HashMap<String, Pair<Integer, Integer>> toKill = new HashMap<String, Pair<Integer, Integer>>();
							toKill.put("flying black dragon", new Pair<Integer, Integer>(1,0));
		final List<ChatAction> actions = new LinkedList<ChatAction>();
							actions.add(new SetQuestAction(QUEST_SLOT, 0, "czar_latajacy"));
							actions.add(new StartRecordingKillsAction(QUEST_SLOT, 1, toKill));

		// gracz zgadza sie wykonac prozbe
		npc.add(ConversationStates.ATTENDING, Arrays.asList("help", "yes", "pomożesz", "pomozesz", "pomoge"),
				new QuestInStateCondition(QUEST_SLOT, "czar_latajacy?"),
				ConversationStates.ATTENDING,
				"Och, wspaniale! Tej nocy przyśnił mi się #'/latający czarny smok/', proszę odszukaj go i zabij."
				+ " Przynieś mi też jego 2 pazury czarnego smoka. Jeśli włożę je pod poduszkę, będą odstraszały koszmary."
				+ " Pamintj, że musisz zabić go samodzielnie, bo pazury nie będą działały jak talizman!",
				new MultipleActions(actions));
	}

	private void step15() {
		final SpeakerNPC npc = npcs.get("Alicja");
		//player nie zabil smoka
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0, "czar_latajacy"),
								 new NotCondition(new KilledForQuestCondition(QUEST_SLOT, 1))),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("You have not slain the latającego czarnego smoka by yourself yet, why are you deceiving me?");
					}
				});

		//player zabil smoka ale nie ma potrzebnych resurce	
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0,"czar_latajacy"),
								 new KilledForQuestCondition(QUEST_SLOT, 1),
								 new NotCondition(new PlayerHasItemWithHimCondition("black dragon claw", 2))),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("Even if you slew the smoka, it will surely return in my nightmares unless I have the talisman. Please deliver pazury czarnego smoka.");
					}
				});

		//player wykonal obydwa zlecenia
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0,"czar_latajacy"),
								 new KilledForQuestCondition(QUEST_SLOT, 1),
								 new PlayerHasItemWithHimCondition("black dragon claw", 2)),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("Oh, you defeated it! Thank you! Please return in 24 hours to see if the talisman worked. Przypomnij mi mówiąc #czarny #latajacy."
						+ " Aha i weź te kilka butelek napoju uzdrawiającego.");
						player.drop("black dragon claw", 2);
						player.addKarma(150);
						final StackableItem potion = (StackableItem) SingletonRepository.getEntityManager().getItem("mega potion");
						final int potionamount = 3;
						potion.setQuantity(potionamount);
						player.equipOrPutOnGround(potion);
						player.setQuest(QUEST_SLOT, "czas_latajacy_zloty;"+System.currentTimeMillis());
					}
				});
	}

	private void czas14() {
		final SpeakerNPC npc = npcs.get("Alicja");
		final String extraTrigger = "czarny latajacy";
		List<String> questTrigger;
		questTrigger = new LinkedList<String>(ConversationPhrases.QUEST_MESSAGES);
		questTrigger.add(extraTrigger);

		npc.add(ConversationStates.ATTENDING, questTrigger,
				new AndCondition(new QuestStateStartsWithCondition(QUEST_SLOT, "czas_latajacy_zloty"),
								 // jeszcze nie minal czas
								 new NotCondition(new TimePassedCondition(QUEST_SLOT, 1, DELAY_IN_MINUTES))),
				ConversationStates.ATTENDING, null,
				new SayTimeRemainingAction(QUEST_SLOT, 1, DELAY_IN_MINUTES, " A full day has not passed yet. Please return in "));

		npc.add(ConversationStates.ATTENDING, questTrigger,
				new AndCondition(new QuestStateStartsWithCondition(QUEST_SLOT, "czas_latajacy_zloty"),
								 // czas minal, dalszy krok
								 new TimePassedCondition(QUEST_SLOT, 1, DELAY_IN_MINUTES)),
				ConversationStates.ATTENDING,
				"The talisman worked! Sadly, a different nightmare appeared tonight. Will you #help me?",
				new SetQuestAction(QUEST_SLOT,"latajacy_zloty?"));

		//kogo ma zabic w nastepnym kroku
		final HashMap<String, Pair<Integer, Integer>> toKill = new HashMap<String, Pair<Integer, Integer>>();
							toKill.put("flying golden dragon", new Pair<Integer, Integer>(1,0));
		final List<ChatAction> actions = new LinkedList<ChatAction>();
							actions.add(new SetQuestAction(QUEST_SLOT, 0, "latajacy_zloty"));
							actions.add(new StartRecordingKillsAction(QUEST_SLOT, 1, toKill));

		// gracz zgadza sie wykonac prozbe
		npc.add(ConversationStates.ATTENDING, Arrays.asList("help", "yes", "pomożesz", "pomozesz", "pomoge"),
				new QuestInStateCondition(QUEST_SLOT, "latajacy_zloty?"),
				ConversationStates.ATTENDING,
				"Och, wspaniale! Tej nocy przyśnił mi się #'/latający złoty smok/', proszę odszukaj go i zabij."
				+ " Przynieś mi też jego 1 gold dragon claw. Jeśli włożę go pod poduszkę, będzie odstraszał koszmary."
				+ " Pamintj, że musisz zabić go samodzielnie, bo pazur nie będzie działał jak talizman!",
				new MultipleActions(actions));
	}

	private void step16() {
		final SpeakerNPC npc = npcs.get("Alicja");
		//player nie zabil smoka
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0, "latajacy_zloty"),
								 new NotCondition(new KilledForQuestCondition(QUEST_SLOT, 1))),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("You have not slain the latającego złotego smoka by yourself yet, why are you deceiving me?");
					}
				});

		//player zabil smoka ale nie ma potrzebnych resurce	
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0,"latajacy_zloty"),
								 new KilledForQuestCondition(QUEST_SLOT, 1),
								 new NotCondition(new PlayerHasItemWithHimCondition("golden dragon claw"))),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("Even if you slew the smoka, it will surely return in my nightmares unless I have the talisman. Please deliver gold dragon claw.");
					}
				});

		//player wykonal obydwa zlecenia
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0,"latajacy_zloty"),
								 new KilledForQuestCondition(QUEST_SLOT, 1),
								 new PlayerHasItemWithHimCondition("golden dragon claw")),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("Oh, you defeated it! Thank you! Please return in 24 hours to see if the talisman worked. Przypomnij mi mówiąc #zloty #latajacy. Aha i weź te kilka butelek napoju uzdrawiającego");
						player.drop("golden dragon claw");
						player.addKarma(150);
						final StackableItem potion1 = (StackableItem) SingletonRepository.getEntityManager().getItem("mega potion");
						final int potion1amount = 5;
						potion1.setQuantity(potion1amount);
						player.equipOrPutOnGround(potion1);
						player.setQuest(QUEST_SLOT, "czas_wawelski;"+System.currentTimeMillis());
					}
				});
	}

	private void czas15() {
		final SpeakerNPC npc = npcs.get("Alicja");
		final String extraTrigger = "zloty latajacy";
		List<String> questTrigger;
		questTrigger = new LinkedList<String>(ConversationPhrases.QUEST_MESSAGES);
		questTrigger.add(extraTrigger);

		npc.add(ConversationStates.ATTENDING, questTrigger,
				new AndCondition(new QuestStateStartsWithCondition(QUEST_SLOT, "czas_wawelski"),
								 // jeszcze nie minal czas
								 new NotCondition(new TimePassedCondition(QUEST_SLOT, 1, DELAY_IN_MINUTES))),
				ConversationStates.ATTENDING, null,
				new SayTimeRemainingAction(QUEST_SLOT, 1, DELAY_IN_MINUTES, " A full day has not passed yet. Please return in "));

		npc.add(ConversationStates.ATTENDING, questTrigger,
				new AndCondition(new QuestStateStartsWithCondition(QUEST_SLOT, "czas_wawelski"),
								 // czas minal, dalszy krok
								 new TimePassedCondition(QUEST_SLOT, 1, DELAY_IN_MINUTES)),
				ConversationStates.ATTENDING,
				"The talisman worked! Sadly, a different nightmare appeared tonight. Will you #help me?",
				new SetQuestAction(QUEST_SLOT,"wawelski?"));

		//kogo ma zabic w nastepnym kroku
		final HashMap<String, Pair<Integer, Integer>> toKill = new HashMap<String, Pair<Integer, Integer>>();
							toKill.put("Wawel Dragon", new Pair<Integer, Integer>(1,0));
		final List<ChatAction> actions = new LinkedList<ChatAction>();
							actions.add(new SetQuestAction(QUEST_SLOT, 0, "wawelski"));
							actions.add(new StartRecordingKillsAction(QUEST_SLOT, 1, toKill));

		// gracz zgadza sie wykonac prozbe
		npc.add(ConversationStates.ATTENDING, Arrays.asList("help", "yes", "pomożesz", "pomozesz", "pomoge"),
				new QuestInStateCondition(QUEST_SLOT, "wawelski?"),
				ConversationStates.ATTENDING,
				"Och, wspaniale! Tej nocy przyśnił mi się #'/smok wawelski/', proszę odszukaj go i zabij."
				+ " Przynieś mi też jego 1 green dragon claw. Jeśli włożę go pod poduszkę, będzie odstraszał koszmary."
				+ " Pamintj, że musisz zabić go samodzielnie, bo pazur nie będzie działał jak talizman!",
				new MultipleActions(actions));

	}

	private void step17() {
		final SpeakerNPC npc = npcs.get("Alicja");
		//player nie zabil smoka
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0, "wawelski"),
								 new NotCondition(new KilledForQuestCondition(QUEST_SLOT, 1))),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("You have not slain the smoka wawelskiego by yourself yet, why are you deceiving me?");
					}
				});

		//player zabil smoka ale nie ma potrzebnych resurce	
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0,"wawelski"),
								 new KilledForQuestCondition(QUEST_SLOT, 1),
								 new NotCondition(new PlayerHasItemWithHimCondition("green dragon claw"))),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("Nawet jeśli zabiłeś smoka, na pewno odwiedzi mnie ponownie w koszmarze jeśli nie będę miała amuletu. Proszę, przynieś mi green dragon claw, o który prosiłam.");
					}
				});

		//player wykonal obydwa zlecenia
		npc.add(ConversationStates.IDLE, ConversationPhrases.GREETING_MESSAGES,
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, 0,"wawelski"),
								 new KilledForQuestCondition(QUEST_SLOT, 1),
								 new PlayerHasItemWithHimCondition("green dragon claw")),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
						raiser.say("Talizman zadziałał! Tej nocy nie miałam więcej koszmarów! Weź proszę tą broń. Należała do mojego dziadka, on był wielkim bohaterem. Myślę, że tobie bardziej się przyda.");
						player.drop("green dragon claw");
						final Item hammer = SingletonRepository.getEntityManager().getItem("vulcano hammer");
						hammer.setBoundTo(player.getName());
						player.equipOrPutOnGround(hammer);
						player.setBaseHP(150 + player.getBaseHP());
						player.heal(150, true);
						player.addKarma(200);
						player.setQuest(QUEST_SLOT, "done");
					}
				});
	}


	@Override
	public void addToWorld() {
		fillQuestInfo(
				"Koszmary Alicji",
				"Alicji śnią się koszmary, w tym celu potrzebuje talizmanu zrobionego ze skór i pazurów smoków.",
				false);

		step1();
		step2();
		czas1();
		step3();
		czas2();
		step4();
		czas3();
		step5();
		czas4();
		step6();
		czas5();
		step7();
		czas6();
		step8();
		czas7();
		step9();
		czas8();
		step10();
		czas9();
		step11();
		czas10();
		step12();
		czas11();
		step13();
		czas12();
		step14();
		czas13();
		step15();
		czas14();
		step16();
		czas15();
		step17();
	}

	@Override
	public List<String> getHistory(final Player player) {
		final List<String> res = new ArrayList<String>();
		if (!player.hasQuest(QUEST_SLOT)) {
			return res;
		}
		final String questState = player.getQuest(QUEST_SLOT);
		res.add("Spotkałem Alicje w domku Zakopane.");
		if ("rejected".equals(questState)) {
			res.add("Nie mam ochoty na walkę ze smokami.");
			return res;
		}
		res.add("Alicja has nightmares. I need to slay the green dragon and bring 5 green dragon leathers."); 
		if ("start".equals(questState)) {
			return res;
		} 
		res.add("I killed the green dragon and delivered the leathers to Alicja!");
		if (questState.startsWith("czas_szkielet")) {
			if (new TimePassedCondition(QUEST_SLOT,1,DELAY_IN_MINUTES).fire(player, null, null)) {
				res.add("I will visit Alicja to check if the talisman worked. Keyword: green / zielony.");
			} else {
				res.add("I must return in " + DELAY_IN_MINUTES + " minutes to see if the talisman worked. Keyword: green / zielony.");
			}
			return res;
		}
		res.add("Powiedz: pomoge.");
		if ("szkielet?".equals(questState)) {
			return res;
		}
		res.add("Alicja still has nightmares. This time I must kill the dragon skeleton and bring 10 dog bones.");
		if ("szkielet".equals(questState)) {
			return res;
		}
		res.add("I killed the dragon skeleton and delivered the dog bones to Alicja!");
		if (questState.startsWith("czas_niebieski")) {
			if (new TimePassedCondition(QUEST_SLOT,1,DELAY_IN_MINUTES).fire(player, null, null)) {
				res.add("I will visit Alicja to check if the talisman worked. Keyword: skeleton / szkielet");
			} else {
				res.add("I must return in " + DELAY_IN_MINUTES + " minutes to see if the talisman worked. Keyword: skeleton / szkielet.");
			}
			return res;
		}
		res.add("Powiedz: pomoge.");
		if ("niebieski?".equals(questState)) {
			return res;
		} 
		res.add("Alicja still has nightmares. This time I must kill the blue dragon and bring 5 blue dragon leathers.");
		if ("niebieski".equals(questState)) {
			return res;
		}
		res.add("I killed the blue dragon and delivered the blue dragon leathers to Alicja!");
		if (questState.startsWith("czas_zgnily")) {
			if (new TimePassedCondition(QUEST_SLOT,1,DELAY_IN_MINUTES).fire(player, null, null)) {
				res.add("I will visit Alicja to check if the talisman worked. Keyword: blue / niebieski.");
			} else {
				res.add("I must return in " + DELAY_IN_MINUTES + " minutes to see if the talisman worked. Keyword: blue / niebieski");
			}
			return res;
		}
		res.add("Powiedz: pomoge.");
		if ("zgnily?".equals(questState)) {
			return res;
		}
		res.add("Alicja still has nightmares. This time I must kill the rotten dragon skeleton and bring 30 dog bones.");
		if ("zgnily".equals(questState)) {
			return res;
		}
		res.add("I killed the rotten dragon skeleton and delivered the bones to Alicja!");
		if (questState.startsWith("czas_czerwony")) {
			if (new TimePassedCondition(QUEST_SLOT,1,DELAY_IN_MINUTES).fire(player, null, null)) {
				res.add("I will visit Alicja to check if the talisman worked. Keyword: rotten / zgnily.");
			} else {
				res.add("I must return in " + DELAY_IN_MINUTES + " minutes to see if the talisman worked. Keyword: rotten / zgnily");
			}
			return res;
		}
		res.add("Powiedz: pomoge.");
		if ("czerwony?".equals(questState)) {
			return res;
		}
		res.add("Alicja still has nightmares. This time I must kill the red dragon and bring 5 red dragon leathers.");
		if ("czerwony".equals(questState)) {
			return res;
		}
		res.add("I killed the red dragon and delivered the red dragon leathers to Alicja!");
		if (questState.startsWith("czas_pustynny")) {
			if (new TimePassedCondition(QUEST_SLOT,1,DELAY_IN_MINUTES).fire(player, null, null)) {
				res.add("I will visit Alicja to check if the talisman worked. Keyword: red / czerwony.");
			} else {
				res.add("I must return in " + DELAY_IN_MINUTES + " minutes to see if the talisman worked. Keyword: red / czerwony");
			}
			return res;
		}
		res.add("Powiedz: pomoge.");
		if ("pustynny?".equals(questState)) {
			return res;
		}
		res.add("Alicja still has nightmares. This time I must kill the desert dragon and bring 5 golden dragon leathers.");
		if ("pustynny".equals(questState)) {
			return res;
		}
		res.add("I killed the desert dragon and delivered the golden dragon leathers to Alicja!");
		if (questState.startsWith("czas_zloty")) {
			if (new TimePassedCondition(QUEST_SLOT,1,DELAY_IN_MINUTES).fire(player, null, null)) {
				res.add("I will visit Alicja to check if the talisman worked. Keyword: desert / pustynny.");
			} else {
				res.add("I must return in " + DELAY_IN_MINUTES + " minutes to see if the talisman worked. Keyword: desert / pustynny");
			}
			return res;
		}
		res.add("Powiedz: pomoge.");
		if ("zloty?".equals(questState)) {
			return res;
		}
		res.add("Alicja still has nightmares. This time I must kill the golden dragon and bring 5 golden dragon leathers.");
		if ("zloty".equals(questState)) {
			return res;
		}
		res.add("I killed the golden dragon and delivered the golden dragon leathers to Alicja!");
		if (questState.startsWith("czas_dwa_niebieski")) {
			if (new TimePassedCondition(QUEST_SLOT,1,DELAY_IN_MINUTES).fire(player, null, null)) {
				res.add("I will visit Alicja to check if the talisman worked. Keyword: golden / zloty.");
			} else {
				res.add("I must return in " + DELAY_IN_MINUTES + " minutes to see if the talisman worked. Keyword: golden / zloty");
			}
			return res;
		}
		res.add("Powiedz: pomoge.");
		if ("dwa_niebieski?".equals(questState)) {
			return res;
		}
		res.add("Alicja still has nightmares. This time I must kill the two-headed blue dragon and bring 10 blue dragon leathers.");
		if ("dwa_niebieski".equals(questState)) {
			return res;
		} 
		res.add("I killed the two-headed blue dragon and delivered the leathers to Alicja!");
		if (questState.startsWith("czas_dwu_zielony")) {
			if (new TimePassedCondition(QUEST_SLOT,1,DELAY_IN_MINUTES).fire(player, null, null)) {
				res.add("I will visit Alicja to check if the talisman worked. Keyword: two-headed blue / dwuglowy niebieski.");
			} else {
				res.add("I must return in " + DELAY_IN_MINUTES + " minutes to see if the talisman worked. Keyword: two-headed blue / dwuglowy niebieski");
			}
			return res;
		}
		res.add("Powiedz: pomoge.");
		if ("dwu_zielony?".equals(questState)) {
			return res;
		}
		res.add("Alicja still has nightmares. This time I must kill the two-headed green dragon and bring 10 green dragon leathers.");
		if ("dwu_zielony".equals(questState)) {
			return res;
		} 
		res.add("I killed the two-headed green dragon and delivered the leathers to Alicja!");
		if (questState.startsWith("czas_arktyczny")) {
			if (new TimePassedCondition(QUEST_SLOT,1,DELAY_IN_MINUTES).fire(player, null, null)) {
				res.add("I will visit Alicja to check if the talisman worked. Keyword: two-headed green / dwuglowy zielony.");
			} else {
				res.add("I must return in " + DELAY_IN_MINUTES + " minutes to see if the talisman worked. Keyword: two-headed green / dwuglowy zielony");
			}
			return res;
		}
		res.add("Powiedz: pomoge.");
		if ("arktyczny?".equals(questState)) {
			return res;
		}
		res.add("Alicja still has nightmares. This time I must kill the arctic dragon and bring 10 dragon teeth.");
		if ("arktyczny".equals(questState)) {
			return res;
		} 
		res.add("I killed the arctic dragon and delivered the dragon teeth to Alicja!");
		if (questState.startsWith("czas_dru_czerwony")) {
			if (new TimePassedCondition(QUEST_SLOT,1,DELAY_IN_MINUTES).fire(player, null, null)) {
				res.add("I will visit Alicja to check if the talisman worked. Keyword: arctic / arktyczny.");
			} else {
				res.add("I must return in " + DELAY_IN_MINUTES + " minutes to see if the talisman worked. Keyword: arctic / arktyczny");
			}
			return res;
		}
		res.add("Powiedz: pomoge.");
		if ("dru_czerwony?".equals(questState)) {
			return res;
		}
		res.add("Alicja still has nightmares. This time I must kill the two-headed red dragon and bring 10 red dragon leathers.");
		if ("dru_czerwony".equals(questState)) {
			return res;
		}
		res.add("I killed the two-headed red dragon and delivered the leathers to Alicja!");
		if (questState.startsWith("czas_czarny")) {
			if (new TimePassedCondition(QUEST_SLOT,1,DELAY_IN_MINUTES).fire(player, null, null)) {
				res.add("I will visit Alicja to check if the talisman worked. Keyword: two-headed red / dwuglowy czerwony.");
			} else {
				res.add("I must return in " + DELAY_IN_MINUTES + " minutes to see if the talisman worked. Keyword: two-headed red / dwuglowy czerwony");
			}
			return res;
		}
		res.add("Powiedz: pomoge.");
		if ("czarny?".equals(questState)) {
			return res;
		}
		res.add("Alicja still has nightmares. This time I must kill the black dragon and bring 15 black dragon leathers.");
		if ("czarny".equals(questState)) {
			return res;
		}
		res.add("I killed the black dragon and delivered the black dragon leathers to Alicja!");
		if (questState.startsWith("czas_dwuglowy_czarny")) {
			if (new TimePassedCondition(QUEST_SLOT,1,DELAY_IN_MINUTES).fire(player, null, null)) {
				res.add("I will visit Alicja to check if the talisman worked. Keyword: black / czarny.");
			} else {
				res.add("I must return in " + DELAY_IN_MINUTES + " minutes to see if the talisman worked. Keyword: black / czarny");
			}
			return res;
		}
		res.add("Powiedz: pomoge.");
		if ("dwuglowy_czarny?".equals(questState)) {
			return res;
		}
		res.add("Alicja still has nightmares. This time I must kill the two-headed black dragon and bring 25 black dragon leathers.");
		if ("dwuglowy_czarny".equals(questState)) {
			return res;
		}
		res.add("I killed the two-headed black dragon and delivered the black dragon leathers to Alicja!");
		if (questState.startsWith("czas_czar_latajacy")) {
			if (new TimePassedCondition(QUEST_SLOT,1,DELAY_IN_MINUTES).fire(player, null, null)) {
				res.add("I will visit Alicja to check if the talisman worked. Keyword: two-headed black / czarny dwuglowy.");
			} else {
				res.add("I must return in " + DELAY_IN_MINUTES + " minutes to see if the talisman worked. Keyword: black / czarny dwuglowy");
			}
			return res;
		}
		res.add("Powiedz: pomoge.");
		if ("czar_latajacy?".equals(questState)) {
			return res;
		}
		res.add("Alicja still has nightmares. This time I must kill the flying black dragon and bring 2 black dragon claws.");
		if ("czar_latajacy".equals(questState)) {
			return res;
		}
		res.add("I killed the flying black dragon and delivered the claws to Alicja!");
		if (questState.startsWith("czas_latajacy_zloty")) {
			if (new TimePassedCondition(QUEST_SLOT,1,DELAY_IN_MINUTES).fire(player, null, null)) {
				res.add("I will visit Alicja to check if the talisman worked. Keyword: flying black / czarny latajacy.");
			} else {
				res.add("I must return in " + DELAY_IN_MINUTES + " minutes to see if the talisman worked. Keyword: black / czarny latajacy");
			}
			return res;
		}
		res.add("Powiedz: pomoge.");
		if ("latajacy_zloty?".equals(questState)) {
			return res;
		}
		res.add("Alicja still has nightmares. This time I must kill the flying golden dragon and bring its golden dragon claw.");
		if ("latajacy_zloty".equals(questState)) {
			return res;
		}
		res.add("I killed the flying golden dragon and delivered its claw to Alicja!");
		if (questState.startsWith("czas_wawelski")) {
			if (new TimePassedCondition(QUEST_SLOT,1,DELAY_IN_MINUTES).fire(player, null, null)) {
				res.add("I will visit Alicja to check if the talisman worked. Keyword: flying golden / zloty latajacy.");
			} else {
				res.add("I must return in " + DELAY_IN_MINUTES + " minutes to see if the talisman worked. Keyword: golden / zloty latajacy");
			}
			return res;
		}
		res.add("Powiedz: pomoge.");
		if ("wawelski?".equals(questState)) {
			return res;
		}
		res.add("Alicja still has nightmares. This time I must kill the Wawel dragon myself and bring its green dragon claw.");
		if ("wawelski".equals(questState)) {
			return res;
		}
		res.add("Alice can finally sleep peacefully without nightmares. For my help, I received the vulcano hammer that belonged to her grandfather.");
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
	public String getName() {
		return "KillDragons";
	}

	@Override
	public String getNPCName() {
		return "Alicja";
	}
}