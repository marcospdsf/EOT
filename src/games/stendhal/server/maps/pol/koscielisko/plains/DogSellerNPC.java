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
// Based on ../games/stendhal/server/maps/ados/felinashouse/CatSellerNPC.java
package games.stendhal.server.maps.pol.koscielisko.plains;

import games.stendhal.common.grammar.ItemParserResult;
import games.stendhal.server.entity.Entity;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.creature.BrownDog;
import games.stendhal.server.entity.creature.TatraDog;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.behaviour.adder.SellerAdder;
import games.stendhal.server.entity.npc.behaviour.impl.SellerBehaviour;
import games.stendhal.server.entity.player.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DogSellerNPC implements ZoneConfigurator {

	public static final int BUYING_PRICE_BROWNDOG = 300;
	public static final int BUYING_PRICE_OWCZAREK_PODHALANSKI = 1000;

	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildZakopaneSouthArea(zone);
	}

	private void buildZakopaneSouthArea(final StendhalRPZone zone) {

		final SpeakerNPC npc = new SpeakerNPC("Stary Baca") {
			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(100, 120));
				nodes.add(new Node(115, 120));
				nodes.add(new Node(115, 110));
				nodes.add(new Node(115, 120));
				nodes.add(new Node(100, 120));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				class DogSellerBehaviour extends SellerBehaviour {
					DogSellerBehaviour(final Map<String, Integer> items) {
						super(items);
					}

					@Override
					public boolean transactAgreedDeal(ItemParserResult res, final EventRaiser seller, final Player player) {
						if (res.getAmount() > 1) {
							seller.say("Hmm ... I don't think you can take care of more than one pet at a time.");
							return false;
						} else if (player.hasPet()) {
							say("Why don't you make sure and look for the pet you already have?");
							return false;
						} else {
							if (!player.drop("money", getCharge(res,player))) {
								seller.say("You don't have that much money.");
								return false;
							}
							if (res.getChosenItemName().equals("tatra dog")) {

								seller.say("Here you go, little Tatra Dog! Your sheepdog feeds on thigh, meat, ham, home-made sausage, steak, goat cheese or rye. Be happy! ");

								final TatraDog tatraDog = new TatraDog(player);
								Entity sellerEntity = seller.getEntity();
								tatraDog.setPosition(sellerEntity.getX(), sellerEntity.getY() + 1);

								player.setPet(tatraDog);
								player.notifyWorldAboutChanges();

								return true;
							} else {
								seller.say("Here you go, here's a little brown dog! Your sheepdog feeds on thigh, meat, ham, home-made sausage, steak, goat cheese or rye. Be happy!");

								final BrownDog brownDog = new BrownDog(player);
								Entity sellerEntity = seller.getEntity();
								brownDog.setPosition(sellerEntity.getX(), sellerEntity.getY() + 1);

								//player.setPet(owczarek);
								player.setPet(brownDog);
								player.notifyWorldAboutChanges();

								return true;
							}
						}
					}
				}

				final Map<String, Integer> items = new HashMap<String, Integer>();
				items.put("brownDog", BUYING_PRICE_BROWNDOG);
				items.put("tatraDog", BUYING_PRICE_OWCZAREK_PODHALANSKI);

				addGreeting();
				addJob("I sell sheepdogs. When I sell them, they are small sheepdogs, but when you take care of such a sheepdog, it grows into a beautiful sheepdog.");
				addHelp("I sell sheepdogs, to buy one you just need to say #I'll buy a #hepherd dog. If you are new to this business, I can tell you how to #travel and #care for sheepdogs. If you find a wild sheepdog, you can #take it.");
				addGoodbye();
				new SellerAdder().addSeller(this, new DogSellerBehaviour(items));
				addReply(Arrays.asList("care", "zaopiekujesz", "opiekować"),
						"Sheepdogs love meat, ham, home-made sausage, steak and goat cheese. Just put a piece on the ground and the sheepdog will come up and eat it. You can check its weight by right-clicking on it and selecting 'Look'. His weight will increase after eating each ham.");
				addReply(Arrays.asList("travel", "podróżować"),
						"When you move, your sheepdog should be close to you so that it does not die. If he doesn't pay attention to you, just say #pet to call him. If you decide to abandon him, right-click on yourself and select 'Leave pet', but to be honest I think this behavior is disgusting .");
				addReply("sell",
						"Sell??? What kind of monster are you? Why would you even want to sell your beautiful sheepdog?");
				addReply(Arrays.asList("take", "przygarnąć"),
						"If you find a wild or abandoned sheepdog, to take it in, press the right mouse button on it and select 'Take pet' and it will start following you. Sheepdogs get a little wild without an owner!");
			}
		};

		npc.setEntityClass("npcstarybaca");
		npc.setPosition(100, 120);
		npc.initHP(100);
		npc.setDescription("Old Baca is taking care of the sheepdogs.");
		zone.add(npc);
	}
}
