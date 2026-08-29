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
// Based on WizardBank.

package games.stendhal.server.maps.quests;

import games.stendhal.common.Direction;
import games.stendhal.common.parser.Sentence;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.events.LoginListener;
import games.stendhal.server.core.events.TurnListener;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.DropItemAction;
import games.stendhal.server.entity.npc.action.MultipleActions;
import games.stendhal.server.entity.npc.action.SayTextAction;
import games.stendhal.server.entity.npc.action.SetQuestAction;
import games.stendhal.server.entity.npc.action.TeleportAction;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.GreetingMatchesNameCondition;
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.OrCondition;
import games.stendhal.server.entity.npc.condition.PlayerHasItemWithHimCondition;
import games.stendhal.server.entity.npc.condition.QuestActiveCondition;
import games.stendhal.server.entity.npc.condition.QuestCompletedCondition;
import games.stendhal.server.entity.npc.condition.QuestNotActiveCondition;
import games.stendhal.server.entity.npc.condition.QuestNotCompletedCondition;
import games.stendhal.server.entity.npc.condition.QuestNotStartedCondition;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.util.TimeUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import marauroa.common.game.IRPZone;

/**
 * Controls player access to the Wizard's Bank via an NPC. 
 * <p>He takes a fee to enter. Players are allowed only 5 minutes access at once.
 * 
 * @author kymara
 */

public class ZakopaneBank extends AbstractQuest implements LoginListener {

	// constants
	private static final String QUEST_SLOT = "zakopane_bank";

	private static final String GRAFINDLE_QUEST_SLOT = "grafindle_gold";

	private static final String ZARA_QUEST_SLOT = "suntan_cream_zara";

	private static final String ZONE_NAME = "int_zakopane_bank_0";

	private static final String ZONE_NAME_1 = "int_zakopane_bank_1";

	/** Time (in Seconds) allowed in the bank. */
	private static final int TIME = 60 * 5;

	// Cost to access chests
	private static final int COST = 1000;

	// "static" data
	private StendhalRPZone zone = null;

	private StendhalRPZone zone_1 = null;

	private SpeakerNPC npc;

	/**
	 * Tells the player the remaining time and teleports him out when his time
	 * is up.
	 */
	class Timer implements TurnListener {
		private final WeakReference<Player> timerPlayer;
		/**
		 * Using unique playername for hashcode.
		 */
		private final String playername;
		/**
		 * Starts a teleport-out-timer.
		 * 
		 * @param player
		 *            the player who started the timer
		 */
		protected Timer(final Player player) {
			timerPlayer = new WeakReference<Player>(player);
			playername = player.getName();
		}

		private int counter = TIME;

		// override equals

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			
			if (playername == null) {
				return prime * result;
			} else {
				return prime * result + playername.hashCode();
			}

		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			Timer other = (Timer) obj;
			
			if (playername == null) {
				if (other.playername != null) {
					return false;
				}
			} else if (!playername.equals(other.playername)) {
				return false;
			}
			return true;
		}

		// override hash


		@Override
		public void onTurnReached(final int currentTurn) {
			// check that the player is still in game and stop the timer
			// in case the player is not playing anymore.
			// Note that "player" always refers to the current player
			// in order not to teleport the next player out too early,
			// we have to compare it to the player who started this timer

			final Player playerTemp = timerPlayer.get();

			if (playerTemp != null) {
				final IRPZone playerZone = playerTemp.getZone();

				if ((playerZone.equals(zone)) || (playerZone.equals(zone_1))) {
					if (counter > 0) {
						npc.say(playerTemp.getTitle() + ", you have "
								+ TimeUtil.timeUntil(counter) + ".");
						counter = counter - 10 * 6;
						SingletonRepository.getTurnNotifier().notifyInTurns(10 * 3 * 6, this);
					} else {
						// teleport the player out
						npc.say("Przepraszam " + playerTemp.getTitle()
								+ ", but your time in the bank vault has expired.");
						teleportAway(playerTemp);
					}
				}
			}
		}
	}

	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}

	private void createNPC() {

		npc = new SpeakerNPC("Bankier Kazimierz") {
			@Override
			protected void createPath() {
				// NPC doesn't move
				setPath(null);
			}

			@Override
			protected void createDialog() {

				// has been here before
				add(ConversationStates.IDLE,
						ConversationPhrases.GREETING_MESSAGES,
						new AndCondition(new GreetingMatchesNameCondition(super.getName()),
								new QuestCompletedCondition(GRAFINDLE_QUEST_SLOT), 
								new QuestCompletedCondition(ZARA_QUEST_SLOT),
								new QuestCompletedCondition(QUEST_SLOT)),
						ConversationStates.ATTENDING,
						null,
						new SayTextAction("Welcome to the Zakopane Bank, [name]. Would you like to #pay for access to the upper vault chests?"));

				// never started quest
				add(ConversationStates.IDLE,
						ConversationPhrases.GREETING_MESSAGES,
						new AndCondition(new GreetingMatchesNameCondition(super.getName()),
								new QuestCompletedCondition(GRAFINDLE_QUEST_SLOT), 
								new QuestCompletedCondition(ZARA_QUEST_SLOT),
								new QuestNotStartedCondition(QUEST_SLOT)),
						ConversationStates.ATTENDING,
						null,
						new SayTextAction("Witaj w Banku w Zakopanem, [name]."));

				// currently in bank
				add(ConversationStates.IDLE,
						ConversationPhrases.GREETING_MESSAGES,
						new AndCondition(new GreetingMatchesNameCondition(super.getName()),
								new QuestCompletedCondition(GRAFINDLE_QUEST_SLOT), 
								new QuestCompletedCondition(ZARA_QUEST_SLOT),
								new QuestActiveCondition(QUEST_SLOT)),
						ConversationStates.ATTENDING,
						null,
						new SayTextAction("Welcome to the Zakopane Bank, [name]. You may leave early. If you wish to depart, simply say #leave."));

				// hasn't got access to all banks yet
				add(ConversationStates.IDLE,
						ConversationPhrases.GREETING_MESSAGES,
						new AndCondition(new GreetingMatchesNameCondition(super.getName()),
						new OrCondition(
								new QuestNotCompletedCondition(GRAFINDLE_QUEST_SLOT), 
								new QuestNotCompletedCondition(ZARA_QUEST_SLOT))),
						ConversationStates.IDLE,
						"You cannot access the upper vault chests unless you have earned chest rights in Nalwor and from Zara in Ados. Farewell!",
						null);

				add(ConversationStates.ATTENDING,
						Arrays.asList("fee", "enter", "opłaty", "opłata", "opłatę"),
						new QuestNotActiveCondition(QUEST_SLOT),
						ConversationStates.ATTENDING,
						"Cena wynosi " + COST
						+ " money. Would you like to pay?",
						null);

				add(ConversationStates.ATTENDING,
						Arrays.asList("fee", "enter", "opłaty", "opłata", "opłatę"),
						new QuestActiveCondition(QUEST_SLOT),
						ConversationStates.ATTENDING,
						"As you know, the price is "
						+ COST + " money.",
						null);

				add(ConversationStates.ATTENDING,
						ConversationPhrases.YES_MESSAGES,
						new AndCondition(
								new PlayerHasItemWithHimCondition("money", COST), 
								new QuestNotActiveCondition(QUEST_SLOT)),
								ConversationStates.ATTENDING,
								"The bank chests for Semos, Nalwor, Ados, and Fado are located to my right.",
								new MultipleActions(
										new DropItemAction("money", COST),
										new TeleportAction(ZONE_NAME, 41, 15, Direction.DOWN),
										new SetQuestAction(QUEST_SLOT, "start"),
										new ChatAction() {
											@Override
											public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
												SingletonRepository.getTurnNotifier().notifyInTurns(0, new Timer(player));
											}}));

				add(ConversationStates.ATTENDING,
						ConversationPhrases.YES_MESSAGES,
						new AndCondition(
								new NotCondition(new PlayerHasItemWithHimCondition("money", COST)), 
								new QuestNotActiveCondition(QUEST_SLOT)),
						ConversationStates.ATTENDING,
						"You do not have enough money!",
						null);

				add(ConversationStates.ATTENDING,
						ConversationPhrases.YES_MESSAGES,
						new QuestActiveCondition(QUEST_SLOT),
						ConversationStates.ATTENDING,
						"I do not understand you. If you wish to leave, simply say #leave.",
						null);

				add(ConversationStates.ATTENDING,
						ConversationPhrases.NO_MESSAGES,
						new QuestNotActiveCondition(QUEST_SLOT),
						ConversationStates.ATTENDING,
						"Bardzo dobrze.",
						null);

				add(ConversationStates.ATTENDING,
						ConversationPhrases.NO_MESSAGES,
						new QuestActiveCondition(QUEST_SLOT),
						ConversationStates.ATTENDING,
						"I do not understand you. If you wish to leave, simply say #leave.",
						null);

				add(ConversationStates.ATTENDING,
						Arrays.asList("leave", "wyjście", "wyjdź"),
						new QuestNotActiveCondition(QUEST_SLOT),
						ConversationStates.ATTENDING,
						"Leave to where?",
						null);


				add(ConversationStates.ATTENDING,
						Arrays.asList("leave", "wyjście", "wyjdź"),
						new QuestActiveCondition(QUEST_SLOT),
						ConversationStates.ATTENDING,
						"Thank you for using the Zakopane Bank.",
						// we used to use teleportAway() here 
						new MultipleActions(
								new TeleportAction(ZONE_NAME, 34, 15, Direction.DOWN),
								new SetQuestAction(QUEST_SLOT, "done"),
								new ChatAction() {
									@Override
									public void fire(final Player player, final Sentence sentence, final EventRaiser raiser) {
										SingletonRepository.getTurnNotifier().dontNotify(new Timer(player));
									}}));


				addJob("I control access to this section of the bank. Without the entrance #fee, you cannot proceed.");
				addOffer("I believe the vault access fee is well worth the convenience.");

				addReply(Arrays.asList("fiscal", "podatkowa"),
						"Do you not know the meaning of this word? You should spend more time in the library. I hear the one in Ados is superb.");

				addHelp("This vault has access to all regional banks. You must pay a #fee for this privilege. We are not a charity. Say #pay #1000 when you are ready.");

				addQuest("You may use this bank only if you have earned the right to use the Nalwor bank chest.");

				addGoodbye("Dowidzenia.");
			}
		};

		npc.setDescription("You see the Vault Guard. You should not provoke him.");
		npc.setEntityClass("youngnpc");
		npc.setPosition(35, 14);
		npc.initHP(100);
		zone.add(npc);
	}

	@Override
	public void onLoggedIn(final Player player) {
		/*
		 *  Stop any possible running notifiers that might be left after the player
		 *  logged out while in the bank. Otherwise the player could be thrown out 
		 *  too early if he goes back.
		 */
		SingletonRepository.getTurnNotifier().dontNotify(new Timer(player));
		teleportAway(player);
	}

	/**
	 * Finishes the time and teleports the player out.
	 * 
	 * @param player
	 *            the player to teleport out
	 */
	private void teleportAway(final Player player) {
		if (player != null) {
			final IRPZone playerZone = player.getZone();
			if (playerZone.equals(zone)) {
				player.teleport(zone, 32, 14, Direction.DOWN, player);

				// complete the quest if it already started
				if (player.hasQuest(QUEST_SLOT)) {
					player.setQuest(QUEST_SLOT, "done");
				}
			} else if (playerZone.equals(zone_1)) {
				player.teleport(zone_1, 28, 3, Direction.DOWN, player);

				// complete the quest if it already started
				if (player.hasQuest(QUEST_SLOT)) {
					player.setQuest(QUEST_SLOT, "done");
				}
			}
		}
	}

	@Override
	public void addToWorld() {
		fillQuestInfo(
			"Bank Zakopane",
			"Want access to all regional bank chests in one place? Visit the upper floor of the Zakopane bank.",
			false);

		SingletonRepository.getLoginNotifier().addListener(this);

		zone = SingletonRepository.getRPWorld().getZone(ZONE_NAME);
		zone_1 = SingletonRepository.getRPWorld().getZone(ZONE_NAME_1);
		createNPC();
	}

	@Override
	public String getName() {
		return "ZakopaneBank";
	}

	@Override
	public boolean isVisibleOnQuestStatus() {
		return false;
	}

	@Override
	public List<String> getHistory(final Player player) {
		return new ArrayList<String>();
	}
	@Override
	public String getNPCName() {
		return "Bankier Kazimierz";
	}
}
