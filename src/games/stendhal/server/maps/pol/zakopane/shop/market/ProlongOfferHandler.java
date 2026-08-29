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

import java.util.Map;

import games.stendhal.common.grammar.Grammar;
import games.stendhal.common.parser.Sentence;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.entity.trade.Market;
import games.stendhal.server.entity.trade.Offer;
import games.stendhal.server.util.TimeUtil;

import java.util.Arrays;

public class ProlongOfferHandler extends OfferHandler {
	@Override
	public void add(SpeakerNPC npc) {
		npc.add(ConversationStates.ATTENDING, Arrays.asList("prolong", "przedłuż"), null, ConversationStates.ATTENDING, null, 
				new ProlongOfferChatAction());
		npc.add(ConversationStates.SERVICE_OFFERED, ConversationPhrases.YES_MESSAGES, 
				ConversationStates.ATTENDING, null, new ConfirmProlongOfferChatAction());
		npc.add(ConversationStates.SERVICE_OFFERED, ConversationPhrases.NO_MESSAGES, null, 
				ConversationStates.ATTENDING, "So, how else may I assist you?", null);
	}

	protected class ProlongOfferChatAction extends KnownOffersChatAction {

		@Override
		public void fire(Player player, Sentence sentence, EventRaiser npc) {
			if (sentence.hasError()) {
				npc.say("I am sorry, but I do not understand that. "
						+ sentence.getErrorString());
			} else if (sentence.getExpressions().iterator().next().toString().equals("prolong")
				|| sentence.getExpressions().iterator().next().toString().equals("przedłuż")){
				handleSentence(player, sentence, npc);
			}
		}

		private void handleSentence(Player player, Sentence sentence, EventRaiser npc) {
			MarketManagerNPC manager = (MarketManagerNPC) npc.getEntity();
			try {
				String offerNumber = getOfferNumberFromSentence(sentence).toString();
				
				Map<String,Offer> offerMap = manager.getOfferMap();
				if (offerMap == null) {
					npc.say("Please review your offers first.");
					return;
				}
				if(offerMap.containsKey(offerNumber)) {
					Offer o = offerMap.get(offerNumber);
					if(o.getOfferer().equals(player.getName())) {
						setOffer(o);
						int quantity = 1;
						if (o.hasItem()) {
							quantity = getQuantity(o.getItem());
						}
						StringBuilder message = new StringBuilder();

						if (TradeCenterZoneConfigurator.getShopFromZone(player.getZone()).contains(o)) {
							message.append("Your listing for ");
							message.append(Grammar.quantityplnoun(quantity, o.getItemName(), "one"));
							message.append(" will expire in ");
							message.append(TimeUtil.approxTimeUntil((int) ((o.getTimestamp() - System.currentTimeMillis() + 1000 * OfferExpirer.TIME_TO_EXPIRING) / 1000)));
							message.append(". Would you like to extend it by ");
							message.append(TimeUtil.timeUntil(OfferExpirer.TIME_TO_EXPIRING));
							message.append(" for ");
							message.append(TradingUtility.calculateFee(player, o.getPrice()).intValue());
							message.append(" money?");
						} else {
							message.append("Would you like to renew your expired listing for ");
							message.append(Grammar.quantityplnoun(quantity, o.getItemName(), "one"));
							message.append(" at a price of ");
							message.append(o.getPrice());
							message.append(" for a renewal fee of ");
							message.append(TradingUtility.calculateFee(player, o.getPrice()).intValue());
							message.append(" money?");
						}
						npc.say(message.toString());
						npc.setCurrentState(ConversationStates.SERVICE_OFFERED);
					} else {
						npc.say("You can only prolong your own offers. Say #'show mine' to see your active listings.");
					}
				} else {
					npc.say("Please pick a valid number from the listings provided to extend an offer.");
					return;
				}
			} catch (NumberFormatException e) {
				npc.say("You must specify #prolong #<id>");
			}
		}
	}

	protected class ConfirmProlongOfferChatAction implements ChatAction {
		@Override
		public void fire (Player player, Sentence sentence, EventRaiser npc) {
			Offer offer = getOffer();
			if (!wouldOverflowMaxOffers(player, offer)) {
				Integer fee = Integer.valueOf(TradingUtility.calculateFee(player, offer.getPrice()).intValue());
				if (player.isEquipped("money", fee)) { 
					if (prolongOffer(player, offer)) {
						TradingUtility.substractTradingFee(player, offer. getPrice());
						npc.say("I have extended your listing and deducted the fee of "+fee.toString()+" money.");
					} else {
						npc.say("Unfortunately, this offer is no longer listed for sale.");
					}
					// Changed the status, or it has been changed by expiration. Obsolete the offers
					((MarketManagerNPC) npc.getEntity()).getOfferMap().clear();
				} else {
					npc.say("You cannot afford the listing renewal fee of "+fee.toString()+" money.");
				}
			} else {
				npc.say("I am sorry, but you can only have up to " + TradingUtility.MAX_NUMBER_OFF_OFFERS
						+ " active offers at a time.");
			}
		}

		/**
		 * Check if prolonging an offer would result the player having too many active offers on market.
		 * 
		 * @param player the player to be checked
		 * @param offer the offer the player wants to prolong
		 * @return true if prolonging the offer should be denied
		 */
		private boolean wouldOverflowMaxOffers(Player player, Offer offer) {
			Market market = TradeCenterZoneConfigurator.getShopFromZone(player.getZone());

			if ((market.countOffersOfPlayer(player) == TradingUtility.MAX_NUMBER_OFF_OFFERS)
					&& market.getExpiredOffers().contains(offer)) {
				return true;
			}

			return false;
		}

		private boolean prolongOffer(Player player, Offer o) {
			Market market = TradeCenterZoneConfigurator.getShopFromZone(player.getZone());
			if (market != null) {
				if (market.prolongOffer(o) != null) {
					String messageNumberOfOffers = "You currently have "+Integer.valueOf(market.countOffersOfPlayer(player)).toString()+" active offers.";
					player.sendPrivateText(messageNumberOfOffers);

					return true;
				}
			}

			return false;
		}
	}
}
