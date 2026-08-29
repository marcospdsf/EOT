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
// Based on UltimateCollector
package games.stendhal.server.maps.quests;

import games.stendhal.common.parser.Sentence;
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
import games.stendhal.server.entity.npc.action.SayTimeRemainingAction;
import games.stendhal.server.entity.npc.action.SetQuestAction;
import games.stendhal.server.entity.npc.action.StartRecordingRandomItemCollectionAction;
import games.stendhal.server.entity.npc.action.SetQuestAndModifyKarmaAction;
import games.stendhal.server.entity.npc.action.SayRequiredItemAction;
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
import games.stendhal.server.entity.player.Player;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

	public class ClearQuestPierscien extends AbstractQuest {

	private static final String QUEST_SLOT = "clear_questy_pierscieni";

	private static final String PIERSCIEN_MIESZCZANINA_QUEST_SLOT = "pierscien_mieszczanina"; 

	private static final String PIERSCIEN_RYCERZA_QUEST_SLOT = "pierscien_rycerza";

	private static final String PIERSCIEN_BARONA_QUEST_SLOT = "pierscien_barona";

	private static final String PIERSCIEN_MAGNATA_QUEST_SLOT = "pierscien_magnata";

	private static final String MITHRIL_CLOAK_QUEST_SLOT = "mithril_cloak";

	private static final String MITHRILSHIELD_QUEST_SLOT = "mithrilshield_quest";

	private static Logger logger = Logger.getLogger(PierscienBarona.class);

	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}

	private void checkLevelHelm() {
		final SpeakerNPC npc = npcs.get("eFuR");

		npc.add(ConversationStates.ATTENDING,
			ConversationPhrases.QUEST_MESSAGES, null,
			ConversationStates.QUEST_OFFERED, null,
			new ChatAction() {
				public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
					if (player.isBadBoy()){ 
						raiser.say("A noble knight died by your hand! You have no business here until you rid yourself of the skull mark. Out of my sight!");
					} else {
						if (player.isQuestCompleted(PIERSCIEN_MIESZCZANINA_QUEST_SLOT)) {
							if (player.getLevel() >= 150) {
								if (!player.hasQuest(QUEST_SLOT) || "rejected".equals(player.getQuest(QUEST_SLOT))) {
									raiser.say("I can reset your quest progress for the #burgher #knight #baron #magnate ring, or for the mithril #cloak and #shield. Which quest progress would you like to reset?");
								}
							} else {
								npc.say("I do not speak to anyone who is not at least a burgher. Return once you reach level 150.");
								raiser.setCurrentState(ConversationStates.ATTENDING);
							}
						} else {
							npc.say("You do not possess any of the noble rings, so we have nothing to discuss.");
							raiser.setCurrentState(ConversationStates.ATTENDING);
						}
					}
				}
			});
	} 

	private void checkCollectingQuests() {
		final SpeakerNPC npc = npcs.get("eFuR");

		npc.add(
			ConversationStates.IDLE,
			ConversationPhrases.GREETING_MESSAGES,
			new AndCondition(new QuestCompletedCondition(PIERSCIEN_MIESZCZANINA_QUEST_SLOT),
					 new QuestNotStartedCondition(QUEST_SLOT)),
			ConversationStates.ATTENDING,
			"Welcome, friend. I can reset quests for the noble rings: #burgher, #knight, #baron, #magnate, or for the mithril #cloak and #shield. Which quest should I reset?",
			null);

		npc.add(ConversationStates.ATTENDING,
			Arrays.asList("knight", "rycerza"),
			new AndCondition(new QuestCompletedCondition(PIERSCIEN_MIESZCZANINA_QUEST_SLOT),
					 new QuestNotStartedCondition(QUEST_SLOT),
					 new QuestNotCompletedCondition(PIERSCIEN_RYCERZA_QUEST_SLOT)),
			ConversationStates.ATTENDING, 
			"How can I reset the Knight Ring quest if you haven't completed it yet?!",
			null);

		npc.add(ConversationStates.ATTENDING,
			Arrays.asList("baron", "barona"),
			new AndCondition(new QuestCompletedCondition(PIERSCIEN_MIESZCZANINA_QUEST_SLOT),
					 new QuestNotStartedCondition(QUEST_SLOT),
					 new QuestNotCompletedCondition(PIERSCIEN_BARONA_QUEST_SLOT)),
			ConversationStates.ATTENDING, 
			"How can I reset the Baron Ring quest if you haven't completed it yet?!",
			null);

		npc.add(ConversationStates.ATTENDING,
			Arrays.asList("magnate", "magnata"),
			new AndCondition(new QuestCompletedCondition(PIERSCIEN_MIESZCZANINA_QUEST_SLOT),
					 new QuestNotStartedCondition(QUEST_SLOT),
					 new QuestNotCompletedCondition(PIERSCIEN_MAGNATA_QUEST_SLOT)),
			ConversationStates.ATTENDING, 
			"How can I reset the Magnate Ring quest if you haven't completed it yet?!",
			null);

		npc.add(ConversationStates.ATTENDING,
			Arrays.asList("cloak", "płaszcz", "plaszcz"),
			new AndCondition(new QuestCompletedCondition(PIERSCIEN_MIESZCZANINA_QUEST_SLOT),
					 new QuestNotStartedCondition(QUEST_SLOT),
					 new QuestNotCompletedCondition(MITHRIL_CLOAK_QUEST_SLOT)),
			ConversationStates.ATTENDING, 
			"How can I reset the Mithril Cloak quest if you haven't completed it yet?!",
			null);

		npc.add(ConversationStates.ATTENDING,
			Arrays.asList("shield", "tarcze", "tarcza", "tarczę"),
			new AndCondition(new QuestCompletedCondition(PIERSCIEN_MIESZCZANINA_QUEST_SLOT),
					 new QuestNotStartedCondition(QUEST_SLOT),
					 new QuestNotCompletedCondition(MITHRILSHIELD_QUEST_SLOT)),
			ConversationStates.ATTENDING, 
			"How can I reset the Mithril Shield quest if you haven't completed it yet?!",
			null);
	}

	private void requestItem() {
		final SpeakerNPC npc = npcs.get("eFuR");

				npc.add(ConversationStates.ATTENDING,
				Arrays.asList("cloak", "płaszcz", "plaszcz"),
				new AndCondition( 
						new QuestCompletedCondition(PIERSCIEN_MIESZCZANINA_QUEST_SLOT),
						new QuestNotStartedCondition(QUEST_SLOT)),
				ConversationStates.ATTENDING, "To reset the Mithril Cloak quest, I require a specific #fee.",
				new SetQuestAction(QUEST_SLOT, "plaszcz" ));

		final List<ChatAction> plaszczactions = new LinkedList<ChatAction>();
		plaszczactions.add(new DropItemAction("money",2000000));
		plaszczactions.add(new IncreaseXPAction(1000));
		plaszczactions.add(new SetQuestAction(MITHRIL_CLOAK_QUEST_SLOT, "rejected"));
		plaszczactions.add(new SetQuestAction(QUEST_SLOT, "rejected"));

		npc.add(ConversationStates.ATTENDING, Arrays.asList("cloak", "reset", "plaszcz", "cofnij", "płaszcz"),
				new AndCondition(new QuestInStateCondition(QUEST_SLOT,"plaszcz"),
				new PlayerHasItemWithHimCondition("money",2000000)),
				ConversationStates.ATTENDING, "I see we have an agreement! The Mithril Cloak quest progress has been reset.",
				new MultipleActions(plaszczactions));

		npc.add(ConversationStates.ATTENDING, Arrays.asList("fee", "payment", "pay", "zapłaty", "zaplaty"),
				new AndCondition(new QuestInStateCondition(QUEST_SLOT,"plaszcz"),
								 new NotCondition(
								 new AndCondition(new PlayerHasItemWithHimCondition("money",2000000)))),
				ConversationStates.ATTENDING, "Musisz mi zapłacić:\n"
									+"#'2000000 money'\n"
									+"Przyjmuje zapłatę w całości, nie na raty!!!\n"
									+"When you have the funds, say #'/cloak/'. Thank you!", null);

				npc.add(ConversationStates.ATTENDING,
				Arrays.asList("shield", "tarcza", "tarcze"),
				new AndCondition( 
						new QuestCompletedCondition(PIERSCIEN_MIESZCZANINA_QUEST_SLOT),
						new QuestNotStartedCondition(QUEST_SLOT)),
				ConversationStates.ATTENDING, "To reset the Mithril Shield quest, I require a specific #fee.",
				new SetQuestAction(QUEST_SLOT, "tarcza" ));

		final List<ChatAction> tarczaactions = new LinkedList<ChatAction>();
		tarczaactions.add(new DropItemAction("money",1500000));
		tarczaactions.add(new IncreaseXPAction(1000));
		tarczaactions.add(new SetQuestAction(MITHRILSHIELD_QUEST_SLOT, "rejected"));
		tarczaactions.add(new SetQuestAction(QUEST_SLOT, "rejected"));

		npc.add(ConversationStates.ATTENDING, Arrays.asList("shield", "reset", "tarcza", "tarcze", "cofnij"),
				new AndCondition(new QuestInStateCondition(QUEST_SLOT,"tarcza"),
				new PlayerHasItemWithHimCondition("money",1500000)),
				ConversationStates.ATTENDING, "I see we have an agreement! The Mithril Shield quest progress has been reset.",
				new MultipleActions(tarczaactions));

		npc.add(ConversationStates.ATTENDING, Arrays.asList("fee", "payment", "pay", "zapłaty", "zaplaty"),
				new AndCondition(new QuestInStateCondition(QUEST_SLOT,"tarcza"),
								 new NotCondition(
								 new AndCondition(new PlayerHasItemWithHimCondition("money",1500000)))),
				ConversationStates.ATTENDING, "Musisz mi zapłacić:\n"
									+"#'1500000 money'\n"
									+"Przyjmuje zapłatę w całości, nie na raty!!!\n"
									+"When you have the funds, say #'/shield/'. Thank you!", null);

		npc.add(ConversationStates.ATTENDING,
				Arrays.asList("burgher", "mieszczanin", "mieszczanina"),
				new AndCondition( 
						new QuestCompletedCondition(PIERSCIEN_MIESZCZANINA_QUEST_SLOT),
						new QuestNotStartedCondition(QUEST_SLOT)),
				ConversationStates.ATTENDING, "To reset the Burgher Ring quest, I #require a specific payment.",
				new SetQuestAction(QUEST_SLOT, "mieszczanin" ));

		final List<ChatAction> mieszczaninactions = new LinkedList<ChatAction>();
		mieszczaninactions.add(new DropItemAction("money",250000));
		mieszczaninactions.add(new DropItemAction("gold bar",30));
		mieszczaninactions.add(new IncreaseXPAction(1000));
		mieszczaninactions.add(new SetQuestAction(PIERSCIEN_MIESZCZANINA_QUEST_SLOT, "rejected"));
		mieszczaninactions.add(new SetQuestAction(QUEST_SLOT, "rejected"));

		npc.add(ConversationStates.ATTENDING, Arrays.asList("burgher", "reset", "mieszczanin", "cofnij"),
				new AndCondition(new QuestInStateCondition(QUEST_SLOT,"mieszczanin"),
				new PlayerHasItemWithHimCondition("money",250000),
				new PlayerHasItemWithHimCondition("gold bar",30)),
				ConversationStates.ATTENDING, "I see we have an agreement! The Burgher Ring quest progress has been reset.",
				new MultipleActions(mieszczaninactions));

		npc.add(ConversationStates.ATTENDING, Arrays.asList("require", "fee", "demand", "żądam", "zadam"),
				new AndCondition(new QuestInStateCondition(QUEST_SLOT,"mieszczanin"),
								 new NotCondition(
								 new AndCondition(new PlayerHasItemWithHimCondition("money",250000),
												  new PlayerHasItemWithHimCondition("gold bar",30)))),
				ConversationStates.ATTENDING, "Musisz mi zapłacić:\n"
									+"#'250k money'\n"
									+"#'30 sztabek złota'\n"
									+"Przyjmuje zapłatę w całości, nie na raty!!!\n"
									+"When you are ready, say #'/burgher/'. Thank you!", null);

		npc.add(ConversationStates.ATTENDING,
				Arrays.asList("knight", "rycerza"),
				new AndCondition( 
						new QuestCompletedCondition(PIERSCIEN_MIESZCZANINA_QUEST_SLOT),
						new QuestNotStartedCondition(QUEST_SLOT)),
				ConversationStates.ATTENDING, "To reset the Knight Ring quest, I require a #fee.",
				new SetQuestAction(QUEST_SLOT, "rycerz" ));

		final List<ChatAction> rycerzactions = new LinkedList<ChatAction>();
		rycerzactions.add(new DropItemAction("money",350000));
		rycerzactions.add(new DropItemAction("gold bar",50));
		rycerzactions.add(new IncreaseXPAction(1000));
		rycerzactions.add(new SetQuestAction(PIERSCIEN_RYCERZA_QUEST_SLOT, "rejected"));
		rycerzactions.add(new SetQuestAction(QUEST_SLOT, "rejected"));

		npc.add(ConversationStates.ATTENDING, Arrays.asList("knight", "reset", "rycerz", "cofnij"),
				new AndCondition(new QuestInStateCondition(QUEST_SLOT,"rycerz"),
				new PlayerHasItemWithHimCondition("money",350000),
				new PlayerHasItemWithHimCondition("gold bar",50)),
				ConversationStates.ATTENDING, "I see we have an agreement! The Knight Ring quest progress has been reset.",
				new MultipleActions(rycerzactions));

		npc.add(ConversationStates.ATTENDING, Arrays.asList("fee", "payment", "pay", "zapłaty", "zaplaty"),
				new AndCondition(new QuestInStateCondition(QUEST_SLOT,"rycerz"),
								 new NotCondition(
								 new AndCondition(new PlayerHasItemWithHimCondition("money",350000),
												  new PlayerHasItemWithHimCondition("gold bar",50)))),
				ConversationStates.ATTENDING, "Musisz mi zapłacić:\n"
									+"#'350k money'\n"
									+"#'50 sztabek złota'\n"
									+"Przyjmuje zapłatę w całości, nie na raty!!!\n"
									+"When you are ready, say #'/knight/'. Thank you!", null);

		npc.add(ConversationStates.ATTENDING,
				Arrays.asList("baron", "barona"),
				new AndCondition( 
						new QuestCompletedCondition(PIERSCIEN_MIESZCZANINA_QUEST_SLOT),
						new QuestCompletedCondition(PIERSCIEN_BARONA_QUEST_SLOT),
						new QuestNotStartedCondition(QUEST_SLOT)),
				ConversationStates.ATTENDING, "To reset the Baron Ring quest, the #fee is quite high.",
				new SetQuestAction(QUEST_SLOT, "baron" ));

		final List<ChatAction> baronactions = new LinkedList<ChatAction>();
		baronactions.add(new DropItemAction("money",450000));
		baronactions.add(new DropItemAction("gold bar",70));
		baronactions.add(new IncreaseXPAction(1000));
		baronactions.add(new SetQuestAction(PIERSCIEN_BARONA_QUEST_SLOT, "rejected"));
		baronactions.add(new SetQuestAction(QUEST_SLOT, "rejected"));

		npc.add(ConversationStates.ATTENDING, Arrays.asList("baron", "reset", "cofnij"),
				new AndCondition(new QuestInStateCondition(QUEST_SLOT,"baron"),
				new PlayerHasItemWithHimCondition("money",450000),
				new PlayerHasItemWithHimCondition("gold bar",70)),
				ConversationStates.ATTENDING, "I see we have an agreement! The Baron Ring quest progress has been reset.",
				new MultipleActions(baronactions));

		npc.add(ConversationStates.ATTENDING, Arrays.asList("fee", "cost", "opłata", "oplata"),
				new AndCondition(new QuestInStateCondition(QUEST_SLOT,"baron"),
								 new NotCondition(
								 new AndCondition(new PlayerHasItemWithHimCondition("money",450000),
												  new PlayerHasItemWithHimCondition("gold bar",70)))),
				ConversationStates.ATTENDING, "Musisz mi zapłacić:\n"
									+"#'450k money'\n"
									+"#'70 sztabek złota'\n"
									+"Przyjmuje zapłatę w całości, nie na raty!!!\n"
									+"When you are ready, say #'/baron/'. Thank you!", null);

		npc.add(ConversationStates.ATTENDING,
				Arrays.asList("magnate", "magnata"),
				new AndCondition( 
						new QuestCompletedCondition(PIERSCIEN_MIESZCZANINA_QUEST_SLOT),
						new QuestCompletedCondition(PIERSCIEN_MAGNATA_QUEST_SLOT),
						new QuestNotStartedCondition(QUEST_SLOT)),
				ConversationStates.ATTENDING, "To reset the Magnate Ring quest, my #services come at a high price.",
				new SetQuestAction(QUEST_SLOT, "magnat" ));

		final List<ChatAction> magnatactions = new LinkedList<ChatAction>();
		magnatactions.add(new DropItemAction("money",600000));
		magnatactions.add(new DropItemAction("gold bar",100));
		magnatactions.add(new IncreaseXPAction(1000));
		magnatactions.add(new SetQuestAction(PIERSCIEN_MAGNATA_QUEST_SLOT, "rejected"));
		magnatactions.add(new SetQuestAction(QUEST_SLOT, "rejected"));

		npc.add(ConversationStates.ATTENDING, Arrays.asList("magnate", "magnat", "reset", "cofnij"),
				new AndCondition(new QuestInStateCondition(QUEST_SLOT,"magnat"),
				new PlayerHasItemWithHimCondition("money",600000),
				new PlayerHasItemWithHimCondition("gold bar",100)),
				ConversationStates.ATTENDING, "I see we have an agreement! The Magnate Ring quest progress has been canceled.",
				new MultipleActions(magnatactions));

		npc.add(ConversationStates.ATTENDING, Arrays.asList("price", "services", "cenię", "cenie"),
				new AndCondition(new QuestInStateCondition(QUEST_SLOT,"magnat"),
								 new NotCondition(
								 new AndCondition(new PlayerHasItemWithHimCondition("money",600000),
												  new PlayerHasItemWithHimCondition("gold bar",100)))),
				ConversationStates.ATTENDING, "Musisz mi zapłacić:\n"
									+"#'600k money'\n"
									+"#'100 sztabek złota'\n"
									+"Przyjmuje zapłatę w całości, nie na raty!!!\n"
									+"When you are ready, say #'/magnate/'. Thank you!", null);
	}

	@Override
	public void addToWorld() {
		fillQuestInfo(
				"Anulowanie zadani",
				"eFuR resets completed noble quests in exchange for a fee.",
				true);

		checkLevelHelm(); 
		checkCollectingQuests();
		requestItem();
	}

	@Override
	public List<String> getHistory(final Player player) {
			final List<String> res = new ArrayList<String>();
			if (!player.hasQuest(QUEST_SLOT)) {
				return res;
			}
			final String questState = player.getQuest(QUEST_SLOT);
			res.add("I met the dragon eFuR.");
			res.add("He offered to reset one of my completed quests for the Burgher, Knight, Baron, or Magnate ring, or the Mithril Cloak and Shield.");
			if ("rejected".equals(questState)) {
				res.add("For now, my quest progress remains as it is.");
				return res;
			}
			if ("start".equals(questState)) {
				return res;
			}
			res.add("eFuR asked for 250k money and 30 gold bars to reset the Burgher Ring quest. Keyword: burgher / mieszczanin.");
			if ("mieszczanin".equals(questState)) {
				return res;
			}

			res.add("eFuR asked for 350k money and 50 gold bars to reset the Knight Ring quest. Keyword: knight / rycerz.");
			if ("rycerz".equals(questState)) {
				return res;
			}

			res.add("eFuR asked for 450k money and 70 gold bars to reset the Baron Ring quest. Keyword: baron.");
			if ("baron".equals(questState)) {
				return res;
			}

			res.add("eFuR asked for 600k money and 100 gold bars to reset the Magnate Ring quest. Keyword: magnate / magnat.");
			if ("magnat".equals(questState)) {
				return res;
			}
			res.add("eFuR asked for 2,000,000 money to reset the Mithril Cloak quest. Keyword: cloak / plaszcz.");
			if ("magnat".equals(questState)) {
				return res;
			}
			res.add("eFuR asked for 1,500,000 money to reset the Mithril Shield quest. Keyword: shield / tarcza.");
			if ("magnat".equals(questState)) {
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
		return "EfurNPC";
	}

	@Override
	public String getNPCName() {
		return "eFuR";
	}
}
