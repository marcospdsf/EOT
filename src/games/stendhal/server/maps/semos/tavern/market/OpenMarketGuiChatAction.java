/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2026 - Stendhal                    *
 ***************************************************************************/
package games.stendhal.server.maps.semos.tavern.market;

import games.stendhal.common.parser.Sentence;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.entity.trade.Market;
import games.stendhal.server.events.OpenMarketGuiEvent;
import games.stendhal.server.events.ShowMarketOffersEvent;

/**
 * ChatAction triggered when a player asks Harold for the market GUI (#catalog, #gui, etc.).
 */
public class OpenMarketGuiChatAction implements ChatAction {

	@Override
	public void fire(Player player, Sentence sentence, EventRaiser npc) {
		Market market = TradeCenterZoneConfigurator.getShopFromZone(player.getZone());
		player.addEvent(new OpenMarketGuiEvent());
		player.addEvent(new ShowMarketOffersEvent(market, player));
		npc.say("Here is the market catalog GUI.");
	}
}
