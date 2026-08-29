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

import java.util.Arrays;

public class RemoveOfferHandler extends OfferHandler {
	@Override
	public void add(SpeakerNPC npc) {
		npc.add(ConversationStates.ATTENDING, Arrays.asList("remove", "usuń"), null, ConversationStates.ATTENDING, null, 
				new RemoveOfferChatAction());
		npc.add(ConversationStates.QUESTION_1, ConversationPhrases.YES_MESSAGES, 
				ConversationStates.ATTENDING, null, new ConfirmRemoveOfferChatAction());
		npc.add(ConversationStates.QUESTION_1, ConversationPhrases.NO_MESSAGES, null, 
				ConversationStates.ATTENDING, "How else may I assist you?", null);
	}

	protected class RemoveOfferChatAction extends KnownOffersChatAction {
		@Override
		public void fire(Player player, Sentence sentence, EventRaiser npc) {
			if (sentence.hasError()) {
				npc.say("I am sorry, but I do not understand that. "
						+ sentence.getErrorString());
			} else if (sentence.getExpressions().iterator().next().toString().equals("remove")
				|| sentence.getExpressions().iterator().next().toString().equals("usuń")){
				handleSentence(player, sentence, npc);
			}
		}

		private void handleSentence(Player player, Sentence sentence, EventRaiser npc) {
			MarketManagerNPC manager = (MarketManagerNPC) npc.getEntity();
			try {
				String offerNumber = getOfferNumberFromSentence(sentence).toString();
				Map<String,Offer> offerMap = manager.getOfferMap();
				if (offerMap.isEmpty()) {
					npc.say("Please check your offers first.");
					return;
				}
				if(offerMap.containsKey(offerNumber)) {
					Offer o = offerMap.get(offerNumber);
					if(o.getOfferer().equals(player.getName())) {
						setOffer(o);
						// Ask for confirmation only if the offer is still active
						if (TradeCenterZoneConfigurator.getShopFromZone(player.getZone()).contains(o)) {
							int quantity = 1;
							if (o.hasItem()) {
								quantity = getQuantity(o.getItem());
							}
							npc.say("Would you like to cancel and remove your listing for " + Grammar.quantityplnoun(quantity, o.getItem().getName()) + "?");
							npc.setCurrentState(ConversationStates.QUESTION_1);
						} else {
							removeOffer(player, npc);
							// Changed the status, or it has been changed by expiration. Obsolete the offers
							((MarketManagerNPC) npc.getEntity()).getOfferMap().clear();
						}
						return;
					}
					npc.say("You can only remove your own offers. Say #'show mine' to see your active listings.");
					return;
				}
				npc.say("Please provide a valid number from the listings provided to remove your offer.");
			} catch (NumberFormatException e) {
				npc.say("Please say: #remove #number");
			}
		}
	}

	protected class ConfirmRemoveOfferChatAction implements ChatAction {
		@Override
		public void fire(Player player, Sentence sentence, EventRaiser npc) {
			removeOffer(player, npc);
			// Changed the status, or it has been changed by expiration. Obsolete the offers
			((MarketManagerNPC) npc.getEntity()).getOfferMap().clear();
		}
	}

	private void removeOffer(Player player, EventRaiser npc) {
		Offer offer = getOffer();
		Market m = TradeCenterZoneConfigurator.getShopFromZone(player.getZone());
		m.removeOffer(offer,player);
		npc.say("Very well, the offer has been removed.");
	}
}
