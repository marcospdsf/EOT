/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2026 - Stendhal                    *
 ***************************************************************************/
package games.stendhal.server.maps.semos.tavern.market;

import java.util.Arrays;
import java.util.List;

import games.stendhal.common.grammar.Grammar;
import games.stendhal.common.parser.Expression;
import games.stendhal.common.parser.Sentence;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.condition.LevelGreaterThanCondition;
import games.stendhal.server.entity.npc.condition.LevelLessThanCondition;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.entity.trade.BuyRequest;
import games.stendhal.server.entity.trade.Market;
import games.stendhal.server.events.ShowMarketOffersEvent;

/**
 * Handles placing a Buy Request / Buy Order with Harold.
 */
public class RequestOfferHandler {

	private String itemName;
	private String itemClass;
	private int quantity;
	private int pricePerPiece;

	private static final List<String> items_blacklist = Arrays.asList("bulb", "seed", "money");

	public void add(SpeakerNPC npc) {
		npc.add(ConversationStates.ATTENDING, "request",
				new LevelLessThanCondition(6),
				ConversationStates.ATTENDING,
				"I am sorry, I only accept buy requests from people who have a good reputation. You can gain experience by helping people with their tasks or defending the city from evil creatures.", null);
		npc.add(ConversationStates.ATTENDING, "request",
				new LevelGreaterThanCondition(5),
				ConversationStates.ATTENDING, null,
				new RequestOfferChatAction());
		npc.add(ConversationStates.ATTENDING, "request", null, ConversationStates.ATTENDING, null,
				new RequestOfferChatAction());
		npc.add(ConversationStates.BUY_PRICE_OFFERED, ConversationPhrases.YES_MESSAGES,
				ConversationStates.ATTENDING, null, new ConfirmRequestOfferChatAction());
		npc.add(ConversationStates.BUY_PRICE_OFFERED, ConversationPhrases.NO_MESSAGES, null,
				ConversationStates.ATTENDING, "Ok, how else may I help you?", null);
	}

	private void setData(String itemName, String itemClass, int quantity, int pricePerPiece) {
		this.itemName = itemName;
		this.itemClass = itemClass;
		this.quantity = quantity;
		this.pricePerPiece = pricePerPiece;
	}

	private class RequestOfferChatAction implements ChatAction {
		@Override
		public void fire(Player player, Sentence sentence, EventRaiser npc) {
			if (sentence.hasError()) {
				npc.say("Sorry, I did not understand that buy request.");
				npc.setCurrentState(ConversationStates.ATTENDING);
			} else {
				handleSentence(player, sentence, npc);
			}
		}

		private void handleSentence(Player player, Sentence sentence, EventRaiser npc) {
			Market shop = TradeCenterZoneConfigurator.getShopFromZone(player.getZone());
			if (shop != null && shop.countBuyRequestsOfPlayer(player) >= TradingUtility.MAX_NUMBER_OFF_OFFERS) {
				npc.say("You may not place more than " + TradingUtility.MAX_NUMBER_OFF_OFFERS + " active buy requests.");
				return;
			}

			if (sentence.getExpressions().size() < 3) {
				npc.say("I did not understand you. Please say \"request <quantity> <item> <price_per_piece>\".");
				return;
			}

			int qty = determineQuantity(sentence);
			int price = determinePrice(sentence);
			String item = determineItemName(sentence);

			if (qty <= 0) {
				qty = 1;
			}
			if (price <= 0) {
				npc.say("Price must be at least 1 money.");
				return;
			}

			if (items_blacklist.contains(item.toLowerCase())) {
				npc.say("Sorry, that item cannot be requested on the market.");
				return;
			}

			Item proto = SingletonRepository.getEntityManager().getItem(item);
			if (proto == null) {
				proto = SingletonRepository.getEntityManager().getItem(Grammar.singular(item));
			}
			if (proto == null) {
				npc.say("Sorry, I don't know any item called \"" + item + "\".");
				return;
			}

			String resolvedName = proto.getName();
			String resolvedClass = "";
			if (proto.has("class")) {
				resolvedClass = proto.get("class");
				if (proto.has("subclass")) {
					resolvedClass += "/" + proto.get("subclass");
				}
			}

			int totalEscrow = qty * price;
			int fee = TradingUtility.calculateFee(player, totalEscrow).intValue();
			int totalCost = totalEscrow + fee;

			if (!player.isEquipped("money", totalCost)) {
				npc.say("You cannot afford the total cost of " + totalCost + " money (Escrow: " + totalEscrow + ", Fee: " + fee + ").");
				return;
			}

			setData(resolvedName, resolvedClass, qty, price);
			npc.say("Do you want to request " + Grammar.quantityplnoun(qty, resolvedName, "a")
					+ " for " + price + " money each? Escrow: " + totalEscrow + " money, Fee: " + fee + " money.");
			npc.setCurrentState(ConversationStates.BUY_PRICE_OFFERED);
		}

		private int determineQuantity(Sentence sentence) {
			try {
				Expression expr = sentence.getExpression(1, "");
				if (expr.getAmount() > 0) {
					return expr.getAmount();
				}
			} catch (Exception e) {}
			return 1;
		}

		private int determinePrice(Sentence sentence) {
			try {
				Expression expr = sentence.getExpression(sentence.getExpressions().size() - 1, "");
				return expr.getAmount();
			} catch (Exception e) {}
			return 0;
		}

		private String determineItemName(Sentence sentence) {
			if (sentence.getExpressions().size() >= 4) {
				// e.g. "request 5 meat 10" -> item is at index 2
				return sentence.getExpression(2, "").getNormalized();
			}
			// e.g. "request sword 100" -> item is at index 1
			return sentence.getExpression(1, "").getNormalized();
		}
	}

	class ConfirmRequestOfferChatAction implements ChatAction {
		@Override
		public void fire(Player player, Sentence sentence, EventRaiser npc) {
			Market shop = TradeCenterZoneConfigurator.getShopFromZone(player.getZone());
			if (shop != null) {
				BuyRequest req = shop.createBuyRequest(player, itemName, itemClass, quantity, pricePerPiece);
				if (req != null) {
					int totalEscrow = quantity * pricePerPiece;
					int fee = TradingUtility.calculateFee(player, totalEscrow).intValue();
					npc.say("I created your buy request for " + Grammar.quantityplnoun(quantity, itemName, "a")
							+ " and held " + totalEscrow + " money in escrow with a fee of " + fee + ".");
					player.sendPrivateText("You have placed a buy request for " + req.describe() + ".");
					player.addEvent(new ShowMarketOffersEvent(shop, player));
				} else {
					npc.say("Sorry, could not create the buy request. Please check your money.");
				}
			}
		}
	}
}
