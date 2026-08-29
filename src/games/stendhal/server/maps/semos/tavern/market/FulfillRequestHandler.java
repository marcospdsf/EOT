/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2026 - Stendhal                    *
 ***************************************************************************/
package games.stendhal.server.maps.semos.tavern.market;

import java.util.List;

import games.stendhal.common.grammar.Grammar;
import games.stendhal.common.parser.Expression;
import games.stendhal.common.parser.Sentence;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.item.StackableItem;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.entity.trade.BuyRequest;
import games.stendhal.server.entity.trade.Market;
import games.stendhal.server.events.ShowMarketOffersEvent;

/**
 * Handles fulfilling another player's Buy Request (partially or fully).
 */
public class FulfillRequestHandler {

	private BuyRequest targetRequest;
	private int fulfillQuantity;

	public void add(SpeakerNPC npc) {
		npc.add(ConversationStates.ATTENDING, "fulfill", null, ConversationStates.ATTENDING, null,
				new FulfillRequestChatAction());
		npc.add(ConversationStates.SELL_PRICE_OFFERED, ConversationPhrases.YES_MESSAGES,
				ConversationStates.ATTENDING, null, new ConfirmFulfillRequestChatAction());
		npc.add(ConversationStates.SELL_PRICE_OFFERED, ConversationPhrases.NO_MESSAGES, null,
				ConversationStates.ATTENDING, "Ok, how else may I help you?", null);
	}

	private class FulfillRequestChatAction implements ChatAction {
		@Override
		public void fire(Player player, Sentence sentence, EventRaiser npc) {
			if (sentence.hasError()) {
				npc.say("Sorry, I did not understand that fulfillment command.");
				return;
			}
			handleSentence(player, sentence, npc);
		}

		private void handleSentence(Player player, Sentence sentence, EventRaiser npc) {
			Market shop = TradeCenterZoneConfigurator.getShopFromZone(player.getZone());
			if (shop == null) {
				return;
			}

			if (sentence.getExpressions().size() < 2) {
				npc.say("Please say \"fulfill <requestId> <quantity>\".");
				return;
			}

			String targetKey = sentence.getExpression(1, "").toString().trim();
			int qty = 1;
			if (sentence.getExpressions().size() >= 3) {
				try {
					qty = sentence.getExpression(2, "").getAmount();
					if (qty <= 0) {
						qty = 1;
					}
				} catch (Exception e) {}
			}

			BuyRequest match = findBuyRequest(shop, targetKey);
			if (match == null) {
				npc.say("Sorry, I could not find a buy request with that ID.");
				return;
			}

			if (match.getBuyer().equals(player.getName())) {
				npc.say("You cannot fulfill your own buy request. You can cancel it if you wish.");
				return;
			}

			int remaining = match.getRemainingQuantity();
			int toFulfill = Math.min(qty, remaining);

			Item sellerItem = player.getFirstEquipped(match.getItemName());
			if (sellerItem == null) {
				sellerItem = player.getFirstEquipped(Grammar.plural(match.getItemName()));
			}
			if (sellerItem == null) {
				npc.say("You do not have any " + Grammar.plural(match.getItemName()) + " to sell.");
				return;
			}

			int ownedCount = 1;
			if (sellerItem instanceof StackableItem) {
				ownedCount = ((StackableItem) sellerItem).getQuantity();
			}
			if (ownedCount < toFulfill) {
				toFulfill = ownedCount;
			}

			if (toFulfill <= 0) {
				npc.say("You do not have enough " + match.getItemName() + " to fulfill that.");
				return;
			}

			targetRequest = match;
			fulfillQuantity = toFulfill;
			int totalPayment = toFulfill * match.getPricePerPiece();

			npc.say("Do you want to sell " + Grammar.quantityplnoun(toFulfill, match.getItemName(), "a")
					+ " to " + match.getBuyer() + " for " + totalPayment + " money?");
			npc.setCurrentState(ConversationStates.SELL_PRICE_OFFERED);
		}

		private BuyRequest findBuyRequest(Market shop, String key) {
			List<BuyRequest> active = shop.getBuyRequestsOlderThan(0);
			try {
				int index = Integer.parseInt(key);
				if (index >= 1 && index <= active.size()) {
					return active.get(index - 1);
				}
			} catch (NumberFormatException e) {}

			for (BuyRequest req : active) {
				if (req.getID().toString().equals(key)) {
					return req;
				}
			}
			return null;
		}
	}

	class ConfirmFulfillRequestChatAction implements ChatAction {
		@Override
		public void fire(Player player, Sentence sentence, EventRaiser npc) {
			Market shop = TradeCenterZoneConfigurator.getShopFromZone(player.getZone());
			if (shop != null && targetRequest != null) {
				int fulfilled = shop.fulfillBuyRequest(player, targetRequest, fulfillQuantity);
				if (fulfilled > 0) {
					int totalPayment = fulfilled * targetRequest.getPricePerPiece();
					npc.say("I completed your sale of " + Grammar.quantityplnoun(fulfilled, targetRequest.getItemName(), "a")
							+ " to " + targetRequest.getBuyer() + " for " + totalPayment + " money! You can collect your earnings anytime.");
					player.sendPrivateText("You earned " + totalPayment + " money for fulfilling a buy request.");
					player.addEvent(new ShowMarketOffersEvent(shop, player));
				} else {
					npc.say("Sorry, fulfillment could not be completed. Please check your inventory items.");
				}
			}
		}
	}
}
