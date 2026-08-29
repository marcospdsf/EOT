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
package games.stendhal.server.maps.pol.zakopane.shop.market;

import marauroa.server.db.command.DBCommandQueue;
import games.stendhal.common.grammar.Grammar;
import games.stendhal.common.parser.Expression;
import games.stendhal.common.parser.Sentence;
import games.stendhal.server.core.engine.dbcommand.LogTradeEventCommand;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.item.StackableItem;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.condition.LevelGreaterThanCondition;
import games.stendhal.server.entity.npc.condition.LevelLessThanCondition;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.entity.trade.Market;
import games.stendhal.server.entity.trade.Offer;
import games.stendhal.server.util.AsynchronousProgramExecutor;

import java.util.Arrays;

public class PrepareOfferHandler {
	private Item item;
	private int price;
	private int quantity;
	
	public void add(SpeakerNPC npc) {
		npc.add(ConversationStates.ATTENDING, Arrays.asList("sell", "sprzedam"),
				new LevelLessThanCondition(6), 
				ConversationStates.ATTENDING, 
				"I am sorry, but I only accept market listings from reputable adventurers with at least level 6 experience. Gain some experience by helping locals or defending our borders, then come back.", null);
		npc.add(ConversationStates.ATTENDING, Arrays.asList("sell", "sprzedam"), 
				new LevelGreaterThanCondition(5), 
				ConversationStates.ATTENDING, null, 
				new PrepareOfferChatAction());
		npc.add(ConversationStates.ATTENDING, Arrays.asList("sell", "sprzedam"), null, ConversationStates.ATTENDING, null, 
				new PrepareOfferChatAction());
		npc.add(ConversationStates.SELL_PRICE_OFFERED, ConversationPhrases.YES_MESSAGES, 
				ConversationStates.ATTENDING, null, new ConfirmPrepareOfferChatAction());
		npc.add(ConversationStates.SELL_PRICE_OFFERED, ConversationPhrases.NO_MESSAGES, null, 
				ConversationStates.ATTENDING, "Very well. How else may I assist you?", null);
	}
	
	private void setData(Item item, int price, int quantity) {
		this.item = item;
		this.price = price;
		this.quantity = quantity;
	}
	
	/**
	 * Builds the message for the tweet to be posted
	 * @param i the offered item
	 * @param q the quantity of the offered item
	 * @param p the price for the item
	 * @return the message to be posted in the tweet
	 */
	public String buildTweetMessage(Item i, int q, int p) {
		StringBuilder message = new StringBuilder();
		message.append("New market listing for ");
		message.append(Grammar.quantityplnoun(q, i.getName(), "a"));
		message.append(" for ");
		message.append(p);
		message.append(" money. ");
		String stats = "";
		String description = i.describe();
		int start = description.indexOf("Stats are (");
		if(start > -1) {
			stats = description.substring(start);
		}
		message.append(stats);
		return message.toString();
	}
	
	private class PrepareOfferChatAction implements ChatAction {
		public void fire(Player player, Sentence sentence, EventRaiser npc) {
			if (sentence.hasError()) {
				npc.say("I could not understand the details of your offer.");
				npc.setCurrentState(ConversationStates.ATTENDING);
			} else if ((sentence.getExpressions().iterator().next().toString().equals("sell"))
					|| (sentence.getExpressions().iterator().next().toString().equals("sprzedam"))) {
				handleSentence(player, sentence, npc);
			}
		}

		private void handleSentence(Player player, Sentence sentence, EventRaiser npc) {
			if(TradingUtility.isPlayerWithinOfferLimit(player)) {
				if (sentence.getExpressions().size() < 3 || sentence.getNumeralCount() != 1) {
					npc.say("I didn't quite catch that. Please use the format: \"sell <item> <price>\".");
					npc.setCurrentState(ConversationStates.ATTENDING);
					return;
				}
				String itemName = determineItemName(sentence);
				int number = determineNumber(sentence);
				int price = determinePrice(sentence);
				Integer fee = Integer.valueOf(TradingUtility.calculateFee(player, price).intValue());
				if(TradingUtility.canPlayerAffordTradingFee(player, price)) {
					Item item = player.getFirstEquipped(itemName);
					if (item == null) {
						// Some items are in plural. look for those
						item = player.getFirstEquipped(Grammar.plural(itemName));
					}

					if (item == null) {
						npc.say("It seems you do not have any " 
								+ Grammar.plural(itemName)+ " on you.");
						return;
					}
					// The item name might not be what was used for looking it up (plurals)
					itemName = item.getName();
					
					if ((number > 1) && !(item instanceof StackableItem)) {
						npc.say("I am sorry, but non-stackable items can only be listed one at a time.");
						return;
					} else if (item.isBound()) {
						npc.say("This " + itemName + " is bound to you. It cannot be sold on the market.");
						return;
					} else if (item.getDeterioration() > 0) {
						npc.say("This " + itemName + " is damaged or degraded. I cannot list damaged items.");
						return;
					} else if (number > 1000) {
						npc.say("I do not have enough storage space for such a huge batch of " + Grammar.plural(itemName) + ".");
						return;
					} else if (price > 1000000) {
						npc.say("That is an astronomical asking price for " + Grammar.plural(itemName) + ". I cannot list such an exorbitant offer.");
						return;
					}

					// All looks ok so far. Ask confirmation from the player.
					setData(item, price, number);
					StringBuilder msg = new StringBuilder();
					msg.append("Would you like to list ");
					msg.append(Grammar.quantityplnoun(number, itemName, "a"));
					msg.append(" for ");
					if (number != 1) {
						msg.append("a total of ");
					}
					msg.append(price);
					msg.append(" money? The market listing fee will be ");
					msg.append(fee);
					msg.append(" money.");
					npc.say(msg.toString());
					
					npc.setCurrentState(ConversationStates.SELL_PRICE_OFFERED);
					return;
				}
				npc.say("You cannot afford the market listing fee of " + fee.toString() + " money.");
				return;
			}
			npc.say("You cannot place more than " + TradingUtility.MAX_NUMBER_OFF_OFFERS + " active market offers at one time.");
		}

		private int determineNumber(Sentence sentence) {
			Expression expression = sentence.getExpression(1,"");
			return expression.getAmount();
		}

		private String determineItemName(Sentence sentence) {
			Expression expression = sentence.getExpression(1,"");
			return expression.getNormalized();
		}

		private int determinePrice(Sentence sentence) {
			return sentence.getNumeral().getAmount();
		}
	}
	
	private class ConfirmPrepareOfferChatAction implements ChatAction {
		public void fire(Player player, Sentence sentence, EventRaiser npc) {
			int fee = TradingUtility.calculateFee(player, price).intValue();
			if (TradingUtility.canPlayerAffordTradingFee(player, price)) {
				if (createOffer(player, item, price, quantity)) {
					TradingUtility.substractTradingFee(player, price);
					new AsynchronousProgramExecutor("trade", buildTweetMessage(item, quantity, price)).start();
					DBCommandQueue.get().enqueue(new LogTradeEventCommand(player, item, quantity, price));
					npc.say("I have posted your listing on the market and deducted the " + fee + " money listing fee.");
					npc.setCurrentState(ConversationStates.ATTENDING);
				} else {
					npc.say("You do not have " + Grammar.quantityplnoun(quantity, item.getName(), "a") + ".");
				}
				return;
			}
			npc.say("You cannot afford the listing fee of " + fee + " money.");
		}

		/**
		 * Try creating an offer.
		 * 
		 * @param player the player who makes the offer
		 * @param item item for sale
		 * @param price price for the item
		 * @param number number of items to sell
		 * @return true if making the offer was successful, false otherwise
		 */
		private boolean createOffer(Player player, Item item, int price, int number) {
			Market shop = TradeCenterZoneConfigurator.getShopFromZone(player.getZone());
			if(shop != null) {
				Offer o = shop.createOffer(player, item, Integer.valueOf(price), Integer.valueOf(number));
				if (o == null) {
					return false;
				}

				StringBuilder message = new StringBuilder("Created market listing for ");
				message.append(item.getName());
				message.append(" for ");
				message.append(price);
				message.append(" money. ");
				String messageNumberOfOffers = "You currently have "
					+ Grammar.quantityplnoun(Integer.valueOf(shop.countOffersOfPlayer(player)), "active offer", "one") + ".";
				player.sendPrivateText(message.toString() + messageNumberOfOffers);
				return true;
			}
			return false;
		}
	}
}
