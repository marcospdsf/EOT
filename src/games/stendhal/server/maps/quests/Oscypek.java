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
// Based on few tasks. Zwracac uwage na zmiany w plikach o sprzedazy owcy, mrs.yeti(biega o czas)     

package games.stendhal.server.maps.quests;

import games.stendhal.common.parser.Sentence;
import games.stendhal.server.core.engine.SingletonRepository; 
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.rp.StendhalRPAction;
import games.stendhal.server.entity.RPEntity;
import games.stendhal.server.entity.item.StackableItem;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.creature.Pet;
import games.stendhal.server.entity.creature.Sheep;
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
import games.stendhal.server.entity.npc.action.SetQuestAction;
import games.stendhal.server.entity.npc.action.SetQuestAndModifyKarmaAction;
import games.stendhal.server.entity.npc.action.SayTimeRemainingAction;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.OrCondition;
import games.stendhal.server.entity.npc.condition.PlayerHasItemWithHimCondition;
import games.stendhal.server.entity.npc.condition.PlayerHasPetOrSheepCondition;
import games.stendhal.server.entity.npc.condition.QuestCompletedCondition;
import games.stendhal.server.entity.npc.condition.QuestInStateCondition;
import games.stendhal.server.entity.npc.condition.QuestNotStartedCondition;
import games.stendhal.server.entity.npc.condition.QuestStateStartsWithCondition;
import games.stendhal.server.entity.npc.condition.TimePassedCondition;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.pol.zakopane.city.BacaZbyszekNPC;
import games.stendhal.server.util.Area;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

public class Oscypek extends AbstractQuest {

	private static final String QUEST_SLOT = "Oscypek";
	private static final int DELAY_IN_MINUTES = 60*3;
	private static Logger logger = Logger.getLogger(Oscypek.class);
  
	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}

	private void step1() {
		final SpeakerNPC npc = npcs.get("Baca Zbyszek");

		npc.add(ConversationStates.ATTENDING,
				ConversationPhrases.QUEST_MESSAGES,
				new QuestNotStartedCondition(QUEST_SLOT),
				ConversationStates.QUEST_OFFERED,
				"There is always hard work on the mountain farmstead! Would you like to become my apprentice shepherd and learn the craft of making Tatra smoked cheese?",
				null);

		npc.add(ConversationStates.ATTENDING,
				ConversationPhrases.QUEST_MESSAGES,
				new QuestCompletedCondition(QUEST_SLOT),
				ConversationStates.ATTENDING,
				"Thank you for your assistance! You proved to be a fine shepherd.",
				null);

		npc.add(ConversationStates.QUEST_OFFERED,
				ConversationPhrases.YES_MESSAGES, null,
				ConversationStates.ATTENDING,
				"First we must test your endurance! Say #buy #sheep to purchase a young sheep. Lead it to green pastures and return once it has grown nice and heavy.",
				new SetQuestAndModifyKarmaAction(QUEST_SLOT, "start", 10.0));

		// Player says no, they've lost karma.
		npc.add(ConversationStates.QUEST_OFFERED,
				ConversationPhrases.NO_MESSAGES, null, ConversationStates.IDLE,
				"Then stop wasting my time and go idle elsewhere!",
				new SetQuestAndModifyKarmaAction(QUEST_SLOT, "rejected", -20.0));
	}

	private void step2() {
		final SpeakerNPC npc = npcs.get("Baca Zbyszek");
		npc.add(ConversationStates.ATTENDING, Arrays.asList("sheep", "owca"),
				new QuestInStateCondition(QUEST_SLOT, "start"),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
						final Sheep sheep = player.getSheep();
						if (sheep != null) {
							if (npc.getEntity().squaredDistance(sheep) > 5 * 5) {
								npc.say("I cannot see the sheep from here! Lead it closer so I can inspect its weight.");
							} else if (sheep.getWeight() < 100) {
								// prevent newbies from selling their sheep too early
								npc.say("You are a poor shepherd! The sheep is not yet well-fed enough (reach weight 100).");
								player.addKarma(-10);
							} else {
								npc.say("Splendid! That is a properly fattened sheep. Now run over to Kościelisko where Blacksmith #Jacek is repairing my large brewing #pot!");				
								//sheep.getZone().remove(sheep);
								player.removeSheep(sheep);
								sheep.getZone().remove(sheep);
								player.notifyWorldAboutChanges();
								player.addKarma(10);
								player.addXP(100);
								player.setQuest(QUEST_SLOT, "inter1");
							}
						} else {
							npc.say("" + player.getTitle() + ", you do not have a sheep with you! What trickery is this?");
						}
					};
				});
	}


	private void interpretacion1() {
		final SpeakerNPC npc = npcs.get("Baca Zbyszek");

		npc.add(ConversationStates.ATTENDING, Arrays.asList("kowal jacek", "kowal Jacek"),
				new QuestInStateCondition(QUEST_SLOT, "inter1"),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
					npc.say("Blacksmith Jacek has my cheese #pot. Hurry and retrieve it!");
					player.setQuest(QUEST_SLOT, "pot_make");
				};
		});

		npc.add(ConversationStates.ATTENDING, "pot",
				new QuestInStateCondition(QUEST_SLOT, "pot_make"),
				ConversationStates.ATTENDING, "A cheese pot is the large kettle used to curdle fresh milk into sheep cheese. Blacksmith Jacek in Kościelisko was repairing it for me.",
				null);
	}


	private void odbior1() {
		final SpeakerNPC npc = npcs.get("Kowal Jacek");	

		npc.add(ConversationStates.ATTENDING, "pot",
				new QuestInStateCondition(QUEST_SLOT, "pot_make"),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
						final Item pot = SingletonRepository.getEntityManager().getItem("pot");
						pot.setBoundTo(player.getName());
						player.equipOrPutOnGround(pot);
						player.addKarma(10);
						player.addXP(100);
						npc.say("Greetings! The repairs are complete—take the pot back to Baca Zbyszek quickly.");
						player.setQuest(QUEST_SLOT, "pot_done");
					};
				});
	}


	private void step3() {
		final SpeakerNPC npc = npcs.get("Baca Zbyszek");

		npc.add(ConversationStates.ATTENDING, "pot",
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, "pot_done"),
				new PlayerHasItemWithHimCondition("pot")),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
					player.drop("pot");
					player.addKarma(10);
					player.addXP(100);
					npc.say("You took your sweet time! Now run back towards Kościelisko where Gaździna #Maryśka has washed my #cheesecloth. Bring some wood for the campfire on your way back!");
					player.setQuest(QUEST_SLOT, "inter2");
				};
		});

		npc.add(
			ConversationStates.ATTENDING, "pot",
			new AndCondition(new QuestInStateCondition(QUEST_SLOT, "pot_done"), new NotCondition(new PlayerHasItemWithHimCondition("pot"))),
			ConversationStates.ATTENDING,
			"You are a clumsy helper! Where is the #pot? Blacksmith #Jacek in Kościelisko was supposed to repair it! Don't show up without it, or the milk will spoil!",
			null);

		npc.add(
				ConversationStates.ATTENDING,
				ConversationPhrases.QUEST_MESSAGES,
				new QuestInStateCondition(QUEST_SLOT, "pot_done"),
				ConversationStates.ATTENDING,
				"I specifically asked you to bring the brewing pot!",
				null);
	}


	private void interpretacion2() {
		final SpeakerNPC npc = npcs.get("Baca Zbyszek");

		npc.add(ConversationStates.ATTENDING, Arrays.asList("cheesecloth", "linen cheesecloth", "grudziarka"),
				new QuestInStateCondition(QUEST_SLOT, "inter2"),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
					npc.say("The cheesecloth is the clean linen cloth used to strain fresh curds. Gaździna Maryśka washed it for me—hurry and fetch it!");
					player.setQuest(QUEST_SLOT, "grudziarka_make");
				};
		});

		npc.add(ConversationStates.ATTENDING, Arrays.asList("maryska", "maryśka", "marysia", "Maryśka", "Gaździna Maryśka"),
				new QuestInStateCondition(QUEST_SLOT, "inter2"),
				ConversationStates.ATTENDING,
				"Maryśka is my beloved wife! Just retrieve the #cheesecloth and return!",
				null);
	}


	private void odbior2() {
		final SpeakerNPC npc = npcs.get("Gaździna Maryśka");

		npc.add(ConversationStates.ATTENDING, Arrays.asList("cheesecloth", "linen cheesecloth", "grudziarka"),
				new QuestInStateCondition(QUEST_SLOT, "grudziarka_make"),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
						final Item grudziarka = SingletonRepository.getEntityManager().getItem("linen cheesecloth");
						grudziarka.setBoundTo(player.getName());
						player.equipOrPutOnGround(grudziarka);
						player.addKarma(10);
						player.addXP(100);
						npc.say("Here it is! It's freshly washed, so don't get it dirty!");
						player.setQuest(QUEST_SLOT, "grudziarka_done");
					};
				});
	}


	private void step4() {
		final SpeakerNPC npc = npcs.get("Baca Zbyszek");

		npc.add(ConversationStates.ATTENDING, Arrays.asList("cheesecloth", "linen cheesecloth", "grudziarka"),
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, "grudziarka_done"),
				new PlayerHasItemWithHimCondition("linen cheesecloth")),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
						player.drop("linen cheesecloth");
						player.addKarma(10);
						player.addXP(100);
						npc.say("There you are! Thank you for the cheesecloth. Now I must strain the curdled milk and shape the cheese. Then we smoke it over the fire. Do you have #wood?");
						player.setQuest(QUEST_SLOT, "drewno");
					};
				});

		npc.add(
			ConversationStates.ATTENDING, Arrays.asList("cheesecloth", "linen cheesecloth", "grudziarka"),
			new AndCondition(new QuestInStateCondition(QUEST_SLOT, "grudziarka_done"), new NotCondition(new PlayerHasItemWithHimCondition("linen cheesecloth"))),
			ConversationStates.ATTENDING,
			"Where is the #cheesecloth? Don't tell me you forgot what to bring!",
			null);

		npc.add(ConversationStates.ATTENDING,
				ConversationPhrases.QUEST_MESSAGES,
				new QuestInStateCondition(QUEST_SLOT, "grudziarka_done"),
				ConversationStates.ATTENDING,
				"I asked you to bring the #cheesecloth!",
				null);
	}


	private void step5() {
		final SpeakerNPC npc = npcs.get("Baca Zbyszek");

		npc.add(ConversationStates.ATTENDING, Arrays.asList("wood", "drewno"),
				new AndCondition(new QuestInStateCondition(QUEST_SLOT, "drewno"),
				new PlayerHasItemWithHimCondition("wood")),
				ConversationStates.ATTENDING, null,
				new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
					player.drop("wood");
					player.addKarma(10);
					player.addXP(100);
					npc.say("Thank you! Return to me in a few hours when the #oscypek cheese is ready."); 
					player.setQuest(QUEST_SLOT, "Oscypek;"+System.currentTimeMillis());
				};
		});

		npc.add(
			ConversationStates.ATTENDING, Arrays.asList("wood", "drewno"),
			new AndCondition(new QuestInStateCondition(QUEST_SLOT, "drewno"), new NotCondition(new PlayerHasItemWithHimCondition("wood"))),
			ConversationStates.ATTENDING,
			"How am I supposed to light the smoking fire without wood? Bring wood quickly!",
			null);

		npc.add(ConversationStates.ATTENDING,
				ConversationPhrases.QUEST_MESSAGES,
				new QuestInStateCondition(QUEST_SLOT, "drewno"),
				ConversationStates.ATTENDING,
				"I asked you to bring #wood!",
				null);
		}


	private void step6() {
		final SpeakerNPC npc = npcs.get("Baca Zbyszek");

		final String extraTrigger = "oscypek";
		List<String> questTrigger;
		questTrigger = new LinkedList<String>(ConversationPhrases.QUEST_MESSAGES);
		questTrigger.add(extraTrigger);

		npc.add(
			ConversationStates.ATTENDING, questTrigger,
			new AndCondition(new QuestStateStartsWithCondition(QUEST_SLOT, "Oscypek"),
			// delay is in minutes, last parameter is argument of timestamp
			new NotCondition(new TimePassedCondition(QUEST_SLOT,1,DELAY_IN_MINUTES))),
			ConversationStates.ATTENDING,
			null,
			new SayTimeRemainingAction(QUEST_SLOT,1,DELAY_IN_MINUTES, "I have not finished smoking the oscypek yet. Return in "));

		npc.add(
			ConversationStates.ATTENDING, questTrigger,
			new AndCondition(new QuestStateStartsWithCondition(QUEST_SLOT, "Oscypek"),
			// delay is in minutes, last parameter is argument of timestamp
			new TimePassedCondition(QUEST_SLOT,1,DELAY_IN_MINUTES)),
			ConversationStates.ATTENDING,
			"Thank you! You make a fine shepherd! Here is your reward: delicious smoked oscypek and fresh sheep milk left over from the curds! Enjoy!",
			new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
					final StackableItem Oscypek = (StackableItem) SingletonRepository.getEntityManager().getItem("oscypek");
					final StackableItem zyntyca = (StackableItem) SingletonRepository.getEntityManager().getItem("sheep milk");
					final int Oscypekamount = 20;
					final int zyntycaamount = 10;
					Oscypek.setQuantity(Oscypekamount);
					zyntyca.setQuantity(zyntycaamount);
					player.equipOrPutOnGround(Oscypek);
					player.equipOrPutOnGround(zyntyca);
					player.addXP(5000);
					player.addKarma(50);
					player.setQuest(QUEST_SLOT, "done");
				};
			});
		}


	@Override
	public void addToWorld() {
		fillQuestInfo(
			"Oscypek",
			"Help Senior Shepherd Zbyszek make traditional smoked cheese.",
			false);

		step1();
		step2();
		interpretacion1();
		odbior1();
		step3();
		interpretacion2();
		odbior2();
		step4();
		step5();
		step6();
	}

	@Override
	public List<String> getHistory(final Player player) {
		final List<String> res = new ArrayList<String>();
		if (!player.hasQuest(QUEST_SLOT)) {
			return res;
		}
		final String questState = player.getQuest(QUEST_SLOT);
		res.add("I met Senior Shepherd Zbyszek in Zakopane.");
		res.add("He offered to make me his apprentice shepherd. I must raise a sheep to weight 100.");
		if ("rejected".equals(questState)) {
			res.add("I did not feel like tending sheep.");
			return res;
		}
		if ("start".equals(questState)) {
			return res;
		}
		res.add("Zbyszek needs his cheese pot repaired by Blacksmith Jacek in Kościelisko.");
		if ("inter1".equals(questState)) {
			return res;
		}
		if ("pot_make".equals(questState)) {
			res.add("I am heading to Blacksmith Jacek to retrieve the pot.");
			return res;
		}
		res.add("Blacksmith Jacek gave me the repaired pot.");
		if ("pot_done".equals(questState)) {
			return res;
		}
		res.add("Zbyszek thanked me for delivering the pot.");
		if ("inter2".equals(questState)) {
			return res;
		}
		res.add("Zbyszek asked me to fetch a clean cheesecloth from his wife Maryśka.");
		if ("grudziarka_make".equals(questState)) {
			return res;
		}
		res.add("Maryśka gave me the clean cheesecloth.");
		if ("grudziarka_done".equals(questState)) {
			return res;
		}
		res.add("I brought the cheesecloth to Zbyszek; now he needs wood for smoking.");
		if ("drewno".equals(questState)) {
			return res;
		}
		res.add("I brought the wood. Zbyszek began smoking the cheese.");
		if (questState.startsWith("Oscypek")) {
			if (new TimePassedCondition(QUEST_SLOT,1,DELAY_IN_MINUTES).fire(player, null, null)) {
				res.add("I should return to Zbyszek for my reward.");
			} else {
				res.add("The cheese is still smoking; I must wait a few hours.");
			}
			return res;
		}
		res.add("Zbyszek praised my hard work and rewarded me with 20 oscypek and 10 sheep milk.");
		if (isCompleted(player)) {
			return res;
		}

		// if things have gone wrong and the quest state didn't match any of the above, debug a bit:
		final List<String> debug = new ArrayList<String>();
		debug.add("Quest state is: " + questState);
		logger.error("History does not match quest state " + questState);
		return debug;
	}

	@Override
	public String getName() {
		return "Oscypek";
	}
	@Override
	public String getNPCName() {
		return "Baca Zbyszek";
	}
}