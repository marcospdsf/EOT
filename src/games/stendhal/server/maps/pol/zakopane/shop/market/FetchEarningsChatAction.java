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

import games.stendhal.common.parser.Sentence;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.entity.trade.Earning;
import games.stendhal.server.entity.trade.Market;

import java.util.Set;
/**
 * chat action to let a player fetch his earnings from the market
 * 
 * @author madmetzger
 *
 */
public class FetchEarningsChatAction implements ChatAction {

	@Override
	public void fire(Player player, Sentence sentence, EventRaiser npc) {
		if (sentence.hasError()) {
			npc.say("I am sorry, but I do not understand that. "
					+ sentence.getErrorString());
			npc.setCurrentState(ConversationStates.ATTENDING);
		} else if ((sentence.getExpressions().iterator().next().toString().equals("fetch"))
				|| (sentence.getExpressions().iterator().next().toString().equals("wypłata"))
				|| (sentence.getExpressions().iterator().next().toString().equals("wypłać"))){
			handleSentence(player, npc);
		}
	}

	private void handleSentence(Player player, EventRaiser npc) {
		Market market = TradeCenterZoneConfigurator.getShopFromZone(player.getZone());
		Set<Earning> earnings = market.fetchEarnings(player);
		int collectedSum = 0;
		for (Earning earning : earnings) {
			collectedSum += earning.getValue().intValue();
		}
		if (collectedSum > 0) {
			player.sendPrivateText("You collected " + Integer.valueOf(collectedSum).toString() + " money from your sales.");
		    npc.say("I have paid out your earnings from market sales. How else may I assist you?");
		} else {
			//either you have no space in your bag or there isn't anything to collect
           	npc.say("Welcome to the trade market. How may I #help you?");
		}
		npc.setCurrentState(ConversationStates.ATTENDING);
	}
}
