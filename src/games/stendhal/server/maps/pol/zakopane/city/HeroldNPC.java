/***************************************************************************
 *                   (C) Copyright 2003-2011 - Stendhal                    *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
  //Na podstawie pliku SignLessorNPC z Semos/city
package games.stendhal.server.maps.pol.zakopane.city;

import games.stendhal.common.parser.Sentence;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.GameEvent;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.Entity;
import games.stendhal.server.entity.mapstuff.office.RentedSign;
import games.stendhal.server.entity.mapstuff.office.RentedSignList;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ChatCondition;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.RemoveStorableEntityAction;
import games.stendhal.server.entity.npc.condition.AdminCondition;
import games.stendhal.server.entity.npc.condition.AndCondition;
import games.stendhal.server.entity.npc.condition.LevelGreaterThanCondition;
import games.stendhal.server.entity.npc.condition.LevelLessThanCondition;
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.PlayerHasItemWithHimCondition;
import games.stendhal.server.entity.npc.condition.PlayerHasStorableEntityCondition;
import games.stendhal.server.entity.npc.condition.TextHasParameterCondition;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.util.StringUtils;

import java.util.Arrays;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * A merchant (original name: Gordon) who rents signs to players.
 *
 * The player has to have at least level 5 to prevent abuse by newly created characters.
 */
public class HeroldNPC implements ZoneConfigurator {
	protected String text;

	// 1.5 mins at 300 ms/turn
	private static final int CHAT_TIMEOUT = 300;
	private static final int MONEY = 100; 
	protected RentedSignList rentedSignList;

	@Override
	public void configureZone(StendhalRPZone zone,
			Map<String, String> attributes) {
		final Shape shape = new Rectangle(56, 37, 11, 1);
		rentedSignList = new RentedSignList(zone, shape);
		buildNPC(zone);
	}

	private void buildNPC(final StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Herold") {

			@Override
			public void createDialog() {
				addGreeting("Greetings! I rent billboard signs and remove expired ones.");
				addJob("I rent notice boards for one full day. Just say #rent followed by your message.");
				addHelp("If you wish to rent a sign, say #rent followed by the text you want inscribed on it. If you want to take it down early, say #remove.");
				setPlayerChatTimeout(CHAT_TIMEOUT);
		
				add(ConversationStates.ATTENDING, "",
					new AndCondition(getRentMatchCond(), new LevelLessThanCondition(6)),
					ConversationStates.ATTENDING, 
					"I am sorry, but I only rent notice boards to seasoned adventurers with at least level 6 experience.",
					null);

				add(ConversationStates.ATTENDING, "", 
					new AndCondition(getRentMatchCond(), new LevelGreaterThanCondition(5), new NotCondition(new TextHasParameterCondition())), 
					ConversationStates.ATTENDING, 
					"Say #rent followed by the text message you would like inscribed on the board.",
					null);

				add(ConversationStates.ATTENDING, "", 
					new AndCondition(getRentMatchCond(), new LevelGreaterThanCondition(5), new TextHasParameterCondition()), 
					ConversationStates.BUY_PRICE_OFFERED, 
					null,
					new ChatAction() {
						@Override
						public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
							text = sentence.getOriginalText().substring(9).trim();

							String reply = "Renting a notice board for 24 hours costs " + MONEY + " gold. Would you like to proceed?";

							if (rentedSignList.getByName(player.getName()) != null) {
								reply = reply + " Remember that this will replace any sign you currently have posted.";
							}

							npc.say(reply);
						}

						@Override
						public String toString() {
							return "remember text";
						}
				});

				add(ConversationStates.ATTENDING, "wynajmij", 
					new AndCondition(getRentMatchCond(), new LevelGreaterThanCondition(5), new TextHasParameterCondition()), 
					ConversationStates.BUY_PRICE_OFFERED, 
					null,
					new ChatAction() {
						@Override
						public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
							text = sentence.getOriginalText().substring(9).trim();

							String reply = "Renting a notice board for 24 hours costs " + MONEY + " gold. Would you like to proceed?";

							if (rentedSignList.getByName(player.getName()) != null) {
								reply = reply + " Remember that this will replace any sign you currently have posted.";
							}

							npc.say(reply);
						}

						@Override
						public String toString() {
							return "remember text";
						}
				});

				add(ConversationStates.ATTENDING, "rent", 
					new AndCondition(getRentMatchCond(), new LevelGreaterThanCondition(5), new TextHasParameterCondition()), 
					ConversationStates.BUY_PRICE_OFFERED, 
					null,
					new ChatAction() {
						@Override
						public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
							text = sentence.getOriginalText().substring(5).trim();

							String reply = "Renting a notice board for 24 hours costs " + MONEY + " gold. Would you like to proceed?";

							if (rentedSignList.getByName(player.getName()) != null) {
								reply = reply + " Remember that this will replace any sign you currently have posted.";
							}

							npc.say(reply);
						}

						@Override
						public String toString() {
							return "remember text";
						}
				});
		
				add(ConversationStates.BUY_PRICE_OFFERED,
					ConversationPhrases.YES_MESSAGES,
					new NotCondition(new PlayerHasItemWithHimCondition("money", MONEY)),
					ConversationStates.ATTENDING,
					"I am sorry, but you do not have enough money.", null);

				add(ConversationStates.BUY_PRICE_OFFERED,
					ConversationPhrases.YES_MESSAGES,
					new PlayerHasItemWithHimCondition("money", MONEY),
					ConversationStates.IDLE, null,
					new RentSignChatAction());

				add(ConversationStates.BUY_PRICE_OFFERED, 
					ConversationPhrases.NO_MESSAGES, null,
					ConversationStates.ATTENDING,
					"If you change your mind, come talk to me again.", null);

				add(ConversationStates.ATTENDING, Arrays.asList("remove", "usuń"), 
					new PlayerHasStorableEntityCondition(rentedSignList),
					ConversationStates.ATTENDING,
					"Very well, I will take down your notice board.",
					new RemoveStorableEntityAction(rentedSignList));

				add(ConversationStates.ATTENDING, Arrays.asList("remove", "usuń"), 
					new NotCondition(new PlayerHasStorableEntityCondition(rentedSignList)),
					ConversationStates.ATTENDING,
					"You do not have an active sign rented, so there is nothing to remove.", null);

				// admins may remove signs (even low level admins)
				add(ConversationStates.ATTENDING, Arrays.asList("delete", "usuń"), 
					new AdminCondition(100),
					ConversationStates.ATTENDING, null,
					new ChatAction() {
						@Override
						public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
							if (sentence.getExpressions().size() < 2) {
								npc.say("Syntax: delete <player name>");
								return;
							}
							final String playerName = sentence.getOriginalText().substring("delete ".length()).trim();
							if (rentedSignList.removeByName(playerName)) {
								final String message = player.getName() + " deleted sign belonging to " + playerName;
								SingletonRepository.getRuleProcessor().sendMessageToSupporters("SignLessorNPC", message);
								new GameEvent(player.getName(), "sign", "deleted", playerName).raise();
							} else {
								player.sendPrivateText("Could not find a sign posted by " + playerName);
							}
						}

						@Override
						public String toString() {
							return "administrator deleted sign";
						}
				});

				addGoodbye();
			}

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(62,32));
				nodes.add(new Node(66, 32));
				setPath(new FixedPath(nodes, true));
			}

		};
		npc.setPosition(62, 32);
		npc.setEntityClass("signguynpc");
		zone.add(npc);
		npc.setDescription("You see the Herald, who places notice boards and player signs on the town billboard.");
	}

	private static ChatCondition getRentMatchCond() {
		return new ChatCondition() {
			@Override
			public boolean fire(Player player, Sentence sentence, Entity npc) {
				String txt = sentence.getOriginalText();

				//TODO replaced by using sentence matching "[you] rent"
				if (txt.startsWith("rent") || txt.startsWith("you rent")  || txt.startsWith("wynajmij")  || txt.startsWith("wynająłeś")) {
					return true;
				} else {
					return false;
				}
			}
		};
	}

	class RentSignChatAction implements ChatAction {

		private final Logger logger = Logger.getLogger(RentSignChatAction.class);

		@Override
		public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
			if (text.length() > 1000) {
				text = text.substring(0, 1000) + "...";
			}

			// do not accept all upper case
			if (StringUtils.countLowerCase(text) < StringUtils.countUpperCase(text) * 2) {
				text = text.toLowerCase();
			}

			// put the sign up
			rentedSignList.removeByName(player.getName());
			final RentedSign sign = new RentedSign(player, text);
			final boolean success = rentedSignList.add(sign);

			// confirm, log, tell postman
			if (success) {
				player.drop("money", MONEY);
				npc.say("All set! I have posted your message on the notice board.");

				// inform IRC using postman
				final Player postman = SingletonRepository.getRuleProcessor().getPlayer("postman");
				String message = player.getName() + " rented sign says \"" + text + "\"";
				if (postman != null) {
					postman.sendPrivateText(message);
				}
				logger.log(Level.toLevel(System.getProperty("stendhal.support.loglevel"), Level.DEBUG), message);
				new GameEvent(player.getName(), "sign", "rent", text).raise();
			} else {
				npc.say("I am sorry, but there are already too many signs posted right now. There is no available space for another.");
			}
		}

		@Override
		public String toString() {
			return "put up sign";
		}
	}
}