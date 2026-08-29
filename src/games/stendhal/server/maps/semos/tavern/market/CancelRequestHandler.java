/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2026 - Stendhal                    *
 ***************************************************************************/
package games.stendhal.server.maps.semos.tavern.market;

import java.util.List;

import games.stendhal.common.parser.Sentence;
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
 * Handles cancelling an active or expired Buy Request and refunding remaining escrow money.
 */
public class CancelRequestHandler {

	private BuyRequest targetRequest;

	public void add(SpeakerNPC npc) {
		npc.add(ConversationStates.ATTENDING, "cancel", null, ConversationStates.ATTENDING, null,
				new CancelRequestChatAction());
		npc.add(ConversationStates.QUESTION_1, ConversationPhrases.YES_MESSAGES,
				ConversationStates.ATTENDING, null, new ConfirmCancelRequestChatAction());
		npc.add(ConversationStates.QUESTION_1, ConversationPhrases.NO_MESSAGES, null,
				ConversationStates.ATTENDING, "Ok, how else may I help you?", null);
	}

	private class CancelRequestChatAction implements ChatAction {
		@Override
		public void fire(Player player, Sentence sentence, EventRaiser npc) {
			if (sentence.hasError()) {
				npc.say("Sorry, I did not understand that cancel command.");
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
				npc.say("Please say \"cancel <requestId>\".");
				return;
			}

			String targetKey = sentence.getExpression(sentence.getExpressions().size() - 1, "").toString().trim();
			BuyRequest match = findUserBuyRequest(shop, player, targetKey);
			if (match == null) {
				npc.say("Sorry, I could not find an active buy request of yours with that ID.");
				return;
			}

			targetRequest = match;
			int refund = match.getEscrowMoney();
			npc.say("Do you want to cancel your buy request for " + match.getItemName()
					+ "? Your remaining escrow of " + refund + " money will be refunded.");
			npc.setCurrentState(ConversationStates.QUESTION_1);
		}

		private BuyRequest findUserBuyRequest(Market shop, Player player, String key) {
			List<BuyRequest> active = shop.getBuyRequestsOlderThan(0);
			try {
				int index = Integer.parseInt(key);
				if (index >= 1 && index <= active.size()) {
					BuyRequest r = active.get(index - 1);
					if (r.getBuyer().equals(player.getName())) {
						return r;
					}
				}
			} catch (NumberFormatException e) {}

			for (BuyRequest req : active) {
				if (req.getBuyer().equals(player.getName()) && req.getID().toString().equals(key)) {
					return req;
				}
			}
			for (BuyRequest req : shop.getExpiredBuyRequests()) {
				if (req.getBuyer().equals(player.getName()) && req.getID().toString().equals(key)) {
					return req;
				}
			}
			return null;
		}
	}

	class ConfirmCancelRequestChatAction implements ChatAction {
		@Override
		public void fire(Player player, Sentence sentence, EventRaiser npc) {
			Market shop = TradeCenterZoneConfigurator.getShopFromZone(player.getZone());
			if (shop != null && targetRequest != null) {
				int refund = shop.cancelBuyRequest(player, targetRequest);
				if (refund >= 0) {
					npc.say("I cancelled your buy request for " + targetRequest.getItemName()
							+ " and refunded " + refund + " money to your earnings. You can fetch your money anytime.");
					player.sendPrivateText("Your buy request was cancelled and " + refund + " money refunded.");
					player.addEvent(new ShowMarketOffersEvent(shop, player));
				} else {
					npc.say("Sorry, could not cancel that request.");
				}
			}
		}
	}
}
