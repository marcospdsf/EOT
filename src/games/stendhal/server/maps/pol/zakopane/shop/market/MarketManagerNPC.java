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

import games.stendhal.common.Direction;
import games.stendhal.server.entity.RPEntity;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.TextHasParameterCondition;
import games.stendhal.server.entity.trade.Offer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class MarketManagerNPC extends SpeakerNPC {
	
	private Map<String,Offer> offers = new HashMap<String, Offer>();

	MarketManagerNPC(String name) {
		super(name);
		// Use smaller than normal range to not interfere players trying to talk
		// to the other NPCs in the tavern.
		setPerceptionRange(3);
	}

	@Override
	protected void createPath() {
		// npc is lazy and does not move
	}
	
	@Override
	protected void onGoodbye(RPEntity player) {
		//clean the offer map on leaving of a player
		offers.clear();
		setDirection(Direction.DOWN);
	}

	@Override
	protected void createDialog() {
		addGreeting("Welcome to the Zakopane trade market. How may I #help you?");
		addJob("I am here to #help you sell and buy goods on the open market.");
		addOffer("To list an offer on the market, say #sell #item #price — then anyone will be able to buy it, even while you are away. For more details, ask for #help.");
		addHelp("Would you like guidance with #buying or #selling?");
		addReply(Arrays.asList("buying", "kupowaniu"), "If you wish to purchase something, say #show to see current wares on sale. To pick an offer, say #buy #number. I can also filter listings for you: just say, for instance, #show #meat to see only meat listings.");
		addReply(Arrays.asList("selling", "sprzedaży"), "Say #sell #item #price to put an item on the market. If you want to take an offer down, say #'show mine' to see your active listings, then say #remove #number. If you have expired listings, ask with #'show expired', and renew them by saying #prolong #number. If your items sold, say #fetch to collect your earnings.");
		new PrepareOfferHandler().add(this);
		add(ConversationStates.ATTENDING, Arrays.asList("show", "pokaż"), new NotCondition(new TextHasParameterCondition()),
				ConversationStates.ATTENDING, null, new ShowOfferItemsChatAction());
		add(ConversationStates.ATTENDING, Arrays.asList("show", "pokaż"), new TextHasParameterCondition(), ConversationStates.ATTENDING, null, new ShowOffersChatAction());
		// fetch earnings when starting to talk to the market manager
		add(ConversationStates.ATTENDING, Arrays.asList("fetch", "wypłata", "wypłać"), null, ConversationStates.ATTENDING, null, new FetchEarningsChatAction());
		new AcceptOfferHandler().add(this);
		new RemoveOfferHandler().add(this);
		new ProlongOfferHandler().add(this);
		add(ConversationStates.ATTENDING, Arrays.asList("examine", "sprawdź", "sprawdzam"), null, ConversationStates.ATTENDING, null, new ExamineOfferChatAction());
		addGoodbye("Visit me anytime to browse the latest listings, post new wares, or collect your profits.");
	}

	public Map<String, Offer> getOfferMap() {
		return offers;
	}
}
