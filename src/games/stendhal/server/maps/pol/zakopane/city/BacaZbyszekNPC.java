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
 // Based on /games/stendhal/server/maps/semos/village/SheepSellerNPC.java
 
package games.stendhal.server.maps.pol.zakopane.city;



import games.stendhal.server.entity.npc.behaviour.impl.SellerBehaviour;
import games.stendhal.server.entity.npc.behaviour.adder.SellerAdder;
import games.stendhal.common.grammar.ItemParserResult;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.core.rp.StendhalRPAction;
import games.stendhal.server.entity.creature.Sheep;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.player.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BacaZbyszekNPC implements ZoneConfigurator {

	public static final int BUYING_PRICE = 30;

	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildZakopaneCenterArea(zone);
	}

	private void buildZakopaneCenterArea(final StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Baca Zbyszek") {
			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(42, 110));
				nodes.add(new Node(42, 115));
				nodes.add(new Node(49, 115));
				nodes.add(new Node(42, 115));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				class SheepSellerBehaviour extends SellerBehaviour {
					SheepSellerBehaviour(final Map<String, Integer> items) {
						super(items);
					}

					@Override
					public boolean transactAgreedDeal(ItemParserResult res, final EventRaiser seller, final Player player) {
						if (res.getAmount() > 1) {
							seller.say("Hmm... I don't think you could manage more than one sheep at a time.");
							return false;
						} else if (!player.hasSheep()) {
							if (!player.drop("money", getCharge(res,player))) {
								seller.say("You don't have enough money.");
								return false;
							}
							seller.say("Here you go, a fine, fluffy little sheep! Take good care of her...");

							final Sheep sheep = new Sheep(player);
							StendhalRPAction.placeat(seller.getZone(), sheep, seller.getX(), seller.getY() + 1);

							player.notifyWorldAboutChanges();

							return true;
						} else {
							say("Why don't you check on the sheep you already have?");
							return false;
						}
					}
				}

				final Map<String, Integer> items = new HashMap<String, Integer>();
				items.put("sheep", BUYING_PRICE);

				addGreeting();
				addJob("I work as a sheep shepherd and trader.");
				addHelp("I sell sheep. To purchase one, simply say #buy #sheep. If you are new to herding, I can teach you how to #travel with your sheep, how to #care for it, and where you can #sell it. If you happen upon a stray sheep in the wild, you can #own it.");
				addGoodbye();
				new SellerAdder().addSeller(this, new SheepSellerBehaviour(items));
				addReply(Arrays.asList("care", "opieka", "opiekować"),
						"My sheep love grazing on fresh green grass.");
				addReply(Arrays.asList("travel", "podróż", "podróżować"),
						"When you travel, keep your sheep close so she doesn't get lost. If she wanders off, call out #sheep to summon her. If you ever decide to let her go, right-click yourself and choose 'Abandon sheep'—though I personally find abandoning animals reprehensible.");
				addReply(Arrays.asList("sell", "sprzedać"),
						"Once your sheep reaches 100 weight, you can lead her to Sato in Semos, and he will buy her from you.");
				addReply(Arrays.asList("own", "adopt", "przygarnąć"),
						"If you find a wild or abandoned sheep, right-click on it and choose 'Adopt'. Then take a good look at your new woolly companion!");
			}
		};

		npc.setEntityClass("npcbaca");
		npc.setPosition(42, 110);
		npc.initHP(100);
		zone.add(npc);
	}
}
