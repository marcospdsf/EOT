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
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.SetQuestAndModifyKarmaAction;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.OrCondition;
import games.stendhal.server.entity.npc.condition.PlayerHasItemWithHimCondition;
import games.stendhal.server.entity.npc.condition.QuestCompletedCondition;
import games.stendhal.server.entity.npc.condition.QuestInStateCondition;
import games.stendhal.server.entity.npc.condition.QuestNotStartedCondition;
import games.stendhal.server.entity.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * QUEST: The Jailed Barbarian
 *
 * PARTICIPANTS:
 * <ul>
 * <li>Lorenz, the jailed barbarian in a hut on Amazon Island</li>
 * <li>Esclara the Amazon Princess</li>
 * <li>Ylflia the Princess of Kalavan</li>
 * </ul>
 *
 * STEPS:
 * <ul>
 * <li>1. Lorenz ask you for a scythe to bring him</li>
 * <li>2. You have to ask Princess Esclara for a 'reason'</li>
 * <li>3. You have to bring him an egg</li>
 * <li>4. You have to inform Princess Ylflia</li>
 * <li>5. You have to bring him a barbarian armor</li>
 * <li>6. You get a reward.</li>
 * </ul>
 *
 * REWARD:
 * <ul>
 * <li>You get 20 gold bars</li>
 * <li>Karma: 15</li>
 * <li>You get 52,000 experience points in all</li>
 * </ul>
 *
 * REPETITIONS:
 * <ul>
 * <li>Not repeatable.</li>
 * </ul>
 */

 public class ZagadkiBrzezdoma extends AbstractQuest {

	private static final String QUEST_SLOT = "zagadki";
	private static Logger logger = Logger.getLogger(ZagadkiBrzezdoma.class);

	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}
	private void step1() {
		final SpeakerNPC npc = npcs.get("Brzezdom");

		npc.add(ConversationStates.ATTENDING,
				ConversationPhrases.QUEST_MESSAGES, 
				new QuestNotStartedCondition(QUEST_SLOT),
				ConversationStates.QUEST_OFFERED, 
				"I seek answers to many ancient folklore riddles. Will you assist my research?",
				null);

		npc.add(ConversationStates.ATTENDING,
				ConversationPhrases.QUEST_MESSAGES, 
				new QuestCompletedCondition(QUEST_SLOT),
				ConversationStates.ATTENDING,
				"Thank you for your assistance! My knowledge of Tatra folklore is complete.",
				null);

		npc.add(ConversationStates.QUEST_OFFERED,
				ConversationPhrases.YES_MESSAGES, null,
				ConversationStates.ATTENDING,
				"Splendid! The first riddle: who gave the enchanted belt and highlander shoes to legendary outlaw Janosik? Bring me a pair of #highlander #shoes as well.",
				new SetQuestAndModifyKarmaAction(QUEST_SLOT, "start", 20.0));

		// Player says no, they've lost karma.
		npc.add(ConversationStates.QUEST_OFFERED,
				ConversationPhrases.NO_MESSAGES, null, ConversationStates.IDLE,
				"Leave then, another scholar will assist me!",
				new SetQuestAndModifyKarmaAction(QUEST_SLOT, "rejected", -20.0));
	}

	private void step2() {
	final SpeakerNPC npc = npcs.get("Brzezdom");

		npc.add(ConversationStates.ATTENDING, Arrays.asList("maryna", "witch", "czarownica"),
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, "start"),
				new PlayerHasItemWithHimCondition("highlander shoes")),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
					player.drop("highlander shoes");
					player.addKarma(10);
					player.addXP(1000);
					npc.say("Aha! So that witch gave him those enchanted items! That solves the first riddle!"
					+ " Old Shepherd wants to know who shepherd Kuba encountered while grazing his sheep."
					+ " Go give him the answer.");
					player.setQuest(QUEST_SLOT, "baca");
				};
		});

		npc.add(
			ConversationStates.ATTENDING, Arrays.asList("witch", "shoes", "belt", "czarownica", "kierpce", "pas"),
			new AndCondition(new QuestInStateCondition(QUEST_SLOT, "start"), new NotCondition(new PlayerHasItemWithHimCondition("highlander shoes"))),
			ConversationStates.ATTENDING,
			"You must bring me the highlander shoes Janosik received from that mysterious person,"
			+ " and tell me who gave them to him.",
			null);

		npc.add(ConversationStates.ATTENDING,
				ConversationPhrases.QUEST_MESSAGES, 
				new QuestInStateCondition(QUEST_SLOT, "start"),
				ConversationStates.ATTENDING,
				"I already asked you for the name of that person. Please also bring highlander shoes.",
				null);
	}

	private void step3() {
	final SpeakerNPC npc = npcs.get("Stary Baca");

		npc.add(ConversationStates.ATTENDING, Arrays.asList("king", "the king", "król", "króla"),
				new QuestInStateCondition(QUEST_SLOT, "baca"),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
					npc.say("Thank you very much! That scoundrel could have enlisted the king's army, why didn't he say anything..."
					+ " You seem quite resourceful at gathering folklore. I have a small favor to ask."
					+ " Blacksmith Andrzej argues with me about what is the highest #peak in the Tatras. Go tell him the name so we can settle this dispute.");
					player.setQuest(QUEST_SLOT, "gerlach");
				};
		});

		npc.add(ConversationStates.ATTENDING, Arrays.asList("peak", "tatras", "szczyt", "tatr"),
				new QuestInStateCondition(QUEST_SLOT, "gerlach"),
				ConversationStates.ATTENDING, "He has argued with me about this for ages. I want an end to this debate."
				+ " As an impartial traveler, tell him the true name of that peak.",
				null);	
	}

	private void step4() {
		final SpeakerNPC npc = npcs.get("Blacksmith Andrzej");

		npc.add(ConversationStates.ATTENDING, Arrays.asList("gerlach", "gierlach", "garłuch"),
				new QuestInStateCondition(QUEST_SLOT, "gerlach"),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
					npc.say("Hm... Well, it seems Brzezdom was right after all. I am glad this debate is finally settled."
					 + " I have a task for you. Retrieve a highlanders corset and bring it to Maryna."
					 + " While you're at it, find out the name of Podhale's greatest legendary storyteller. Tell Maryna his name; she will certainly appreciate it.");
					player.setQuest(QUEST_SLOT, "gorset");
				};
		});
	}

	private void step5() {
	final SpeakerNPC npc = npcs.get("Maryna");	

		npc.add(ConversationStates.ATTENDING, Arrays.asList("sabala", "sabała", "sabalik", "jan krzeptowski"),
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, "gorset"),
				new PlayerHasItemWithHimCondition("highlanders corset")),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
					player.drop("highlanders corset");
					player.addKarma(30);
					player.addXP(10000);
					npc.say("So the blacksmith sent you! Sabała, also known as Jan Krzeptowski—the legendary storyteller, who would have thought? And the corset is lovely too!"
					+ " Do something for me: hurry to Boguś and tell him who is to #blame for why cabbage won't grow in Zakopane.");
					player.setQuest(QUEST_SLOT, "kapusta");
				};
		});

		npc.add(
			ConversationStates.ATTENDING, Arrays.asList("sabala", "sabała", "sabalik", "jan krzeptowski"),
			new AndCondition(new QuestInStateCondition(QUEST_SLOT, "gorset"), new NotCondition(new PlayerHasItemWithHimCondition("highlanders corset"))),
			ConversationStates.ATTENDING,
			"The blacksmith said you'd deliver a corset. Where is it?",
			null);

		npc.add(ConversationStates.ATTENDING,
				ConversationPhrases.QUEST_MESSAGES, 
				new QuestInStateCondition(QUEST_SLOT, "gorset"),
				ConversationStates.ATTENDING,
				"Please bring me the highlanders corset.",
				null);

		npc.add(ConversationStates.ATTENDING, Arrays.asList("blame", "winien", "winna"),
				new QuestInStateCondition(QUEST_SLOT, "kapusta"),
				ConversationStates.ATTENDING, "Rumor has it cabbage won't grow in Zakopane. Find out what is the cause and tell Boguś. You can find him in the Zakopane hospital.",
				null);

		npc.add(ConversationStates.ATTENDING,
				ConversationPhrases.QUEST_MESSAGES, 
				new QuestInStateCondition(QUEST_SLOT, "kapusta"),
				ConversationStates.ATTENDING,
				"I need you to find out who or what is causing this situation.",
				null);
	}

	private void step6() {
	final SpeakerNPC npc = npcs.get("Boguś");

		npc.add(ConversationStates.ATTENDING, Arrays.asList("caterpillar", "caterpillars", "gasienica", "gąsienica", "gąsienice"),
				new QuestInStateCondition(QUEST_SLOT, "kapusta"),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
					npc.say("Ha ha ha! Maryna pulled your leg! That's her classic joke about cabbage and caterpillars."
					+ " But joking aside, since you are here, do something for me. Go to Edgard in the lands of the Order of the Shadow Knights."
					+ " He walks near the castle; deliver my #greetings and tell him who was the equivalent of Zeus in Slavic mythology.");
					player.setQuest(QUEST_SLOT, "bogowie");
				};
		});

		npc.add(ConversationStates.ATTENDING, Arrays.asList("greetings", "pozdrawiam", "pozdrowienia"),
				new QuestInStateCondition(QUEST_SLOT, "bogowie"),
				ConversationStates.ATTENDING, "Go and give Edgard the answer to his mythological question.",
				null);
	}

	private void step7() {
	final SpeakerNPC npc = npcs.get("Edgard");

		npc.add(ConversationStates.ATTENDING, Arrays.asList("perun", "perun god"),
				new QuestInStateCondition(QUEST_SLOT, "bogowie"),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
					npc.say("Thank you, my friend! My knowledge grows. You are truly a wise and capable warrior."
					+ " I have a quest worthy of you: obtain a black armor and deliver it to Bercik. Good luck!");
					player.setQuest(QUEST_SLOT, "armor");
				};
		});
	}

	private void step8() {
	final SpeakerNPC npc = npcs.get("Bercik");
	
		npc.add(ConversationStates.ATTENDING, Arrays.asList("armor", "black armor", "zbroja", "zbroję"),
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, "armor"),
				new PlayerHasItemWithHimCondition("black armor")),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
					player.drop("black armor");
					final StackableItem gold = (StackableItem) SingletonRepository.getEntityManager().getItem("gold bar");
					final int goldamount = 30;
					gold.setQuantity(goldamount);
					player.equipOrPutOnGround(gold);
					player.addKarma(35);
					player.addXP(50000);
					npc.say("Magnificent armor! Thank you for delivering it. Please accept these gold bars as a reward."
					+ " I discovered a new vein in the mine beneath Kościelisko, though rumor says there is even more beneath Zakopane.");
					player.setQuest(QUEST_SLOT, "done");
				};
		});

		npc.add(
			ConversationStates.ATTENDING, Arrays.asList("armor", "black armor", "zbroja", "zbroję"),
			new AndCondition(new QuestInStateCondition(QUEST_SLOT, "armor"), new NotCondition(new PlayerHasItemWithHimCondition("black armor"))),
			ConversationStates.ATTENDING,
			"You do not have the black armor with you! Edgard said I could rely on you. Bring it to me!",
			null);

		npc.add(ConversationStates.ATTENDING,
				ConversationPhrases.QUEST_MESSAGES, 
				new QuestInStateCondition(QUEST_SLOT, "armor"),
				ConversationStates.ATTENDING,
				"I am awaiting the black #armor from you.",
				null);
	}

	@Override
	public void addToWorld() {
		fillQuestInfo(
			"Riddles of Brzezdom",
			"Find answers to the folklore riddles posed by Brzezdom and the highlanders.",
			false);

		step1();
		step2();
		step3();
		step4();
		step5();
		step6();
		step7();
		step8();
	}

	@Override
	public String getName() {
		return "ZagadkiBrzezdoma";
	}

	@Override
	public List<String> getHistory(final Player player) {
		final List<String> res = new ArrayList<String>();
		if (!player.hasQuest(QUEST_SLOT)) {
			return res;
		}
		final String questState = player.getQuest(QUEST_SLOT);
		res.add("I met Brzezdom in the meadows of Kościelisko.");
		res.add("He wondered who gave the belt and highlander shoes to Janosik, and asked for a pair of highlander shoes.");
		if ("rejected".equals(questState)) {
			res.add("I was not in the mood for riddles.");
			return res;
		} 
		if ("start".equals(questState)) {
			return res;
		} 
		res.add("I answered Brzezdom and delivered the highlander shoes. Next, Old Shepherd wondered who shepherd Kuba met.");
		if ("baca".equals(questState)) {
			return res;
		} 
		res.add("Old Shepherd asked me to tell Blacksmith Andrzej the name of the highest peak in the Tatras.");
		if ("gerlach".equals(questState)) {
			return res;
		}
		res.add("Blacksmith Andrzej asked me to deliver a corset to Maryna and name Podhale's legendary storyteller.");
		if ("gorset".equals(questState)) {
			return res;
		}
		res.add("Maryna asked me to find out why cabbage won't grow in Zakopane and tell Boguś.");
		if ("kapusta".equals(questState)) {
			return res;
		}
		res.add("Boguś sent me to Edgard in the Order lands to answer who was the Slavic equivalent of Zeus.");
		if ("bogowie".equals(questState)) {
			return res;
		}
		res.add("Edgard asked me to obtain black armor and deliver it to Bercik.");
		if ("armor".equals(questState)) {
			return res;
		}
		res.add("Bercik rewarded me with 30 gold bars for the black armor.");
		if (isCompleted(player)) {
			return res;
		}
		final List<String> debug = new ArrayList<String>();
		debug.add("Quest state is: " + questState);
		logger.error("History does not match quest state " + questState);
		return debug;
	}

	@Override
	public int getMinLevel() {
		return 60;
	}
	@Override
	public String getNPCName() {
		return "Brzezdom";
	}
}
