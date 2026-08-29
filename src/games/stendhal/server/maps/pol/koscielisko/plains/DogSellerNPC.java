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




import games.stendhal.server.entity.npc.behaviour.impl.SellerBehaviour;
import games.stendhal.server.entity.npc.behaviour.adder.SellerAdder;
import games.stendhal.server.core.rp.StendhalRPAction;
import games.stendhal.common.grammar.ItemParserResult;
import games.stendhal.server.entity.Entity;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.creature.Owczarek;
import games.stendhal.server.entity.creature.OwczarekPodhalanski;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.player.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DogSellerNPC implements ZoneConfigurator {

	public static final int BUYING_PRICE_OWCZAREK = 300;
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
							seller.say("Hmm... I don't believe you can take care of more than one sheepdog at a time.");
							return false;
						} else if (player.hasPet()) {
							say("Why don't you check on the companion you already have first?");
							return false;
						} else {
							if (!player.drop("money", getCharge(res,player))) {
								seller.say("You do not have enough money.");
								return false;
							}
							if (res.getChosenItemName().equals("owczarek podhalański") || res.getChosenItemName().equals("tatra sheepdog")) {

								seller.say("Here you are, a lovely Tatra sheepdog pup! Your sheepdog feeds on chicken legs, meat, ham, home sausage, steak, goat cheese, or whey. Enjoy your loyal companion!");

								final OwczarekPodhalanski owczarek_podhalanski = new OwczarekPodhalanski(player);
								Entity sellerEntity = seller.getEntity();
								owczarek_podhalanski.setPosition(sellerEntity.getX(), sellerEntity.getY() + 1);

								player.setPet(owczarek_podhalanski);
								player.notifyWorldAboutChanges();

								return true;
							} else {
								seller.say("Here you are, a little sheepdog pup! Your sheepdog feeds on chicken legs, meat, ham, home sausage, steak, goat cheese, or whey. Enjoy your loyal companion!");

								final Owczarek owczarek = new Owczarek(player);
								Entity sellerEntity = seller.getEntity();
								owczarek.setPosition(sellerEntity.getX(), sellerEntity.getY() + 1);

								player.setPet(owczarek);
								player.notifyWorldAboutChanges();

								return true;
							}
						}
					}
				}

				final Map<String, Integer> items = new HashMap<String, Integer>();
				items.put("sheepdog", BUYING_PRICE_OWCZAREK);
				items.put("tatra sheepdog", BUYING_PRICE_OWCZAREK_PODHALANSKI);
				items.put("owczarek", BUYING_PRICE_OWCZAREK);
				items.put("owczarek podhalański", BUYING_PRICE_OWCZAREK_PODHALANSKI);

				addGreeting();
				addJob("I breed and sell loyal mountain sheepdogs. When they leave my care they are small pups, but when you #care for them they grow into magnificent guardian dogs.");
				addHelp("I sell shepherd dogs. To purchase one, simply say #buy #sheepdog or #buy #'tatra sheepdog'. If you're new to caring for pets, I can explain how to #travel and #care for them. If you come across a stray dog in the wild, you can #adopt it.");
				addGoodbye();
				new SellerAdder().addSeller(this, new DogSellerBehaviour(items));
				addReply(Arrays.asList("care", "zaopiekujesz", "opiekować"),
						"Sheepdogs love meat, ham, home sausage, steak, and goat cheese. Simply place food on the ground nearby and the dog will eat it. You can check its weight by right-clicking it and selecting 'Look'. Its weight and strength will grow as it eats.");
				addReply(Arrays.asList("travel", "podróżować"),
						"When exploring the wilderness, keep your sheepdog close so it doesn't get left behind or lost. If it wanders off, say #sheepdog to call it back. If you ever choose to abandon it, right-click on yourself and choose 'Abandon Pet', though frankly that breaks a good dog's heart.");
				addReply("sell",
						"Sell??? What kind of heartless rogue are you? Why would you ever want to sell your loyal companion?");
				addReply(Arrays.asList("own", "adopt", "przygarnąć"),
						"If you find a wild or lost sheepdog, right-click it and choose 'Adopt' so it begins following you. Mountain dogs quickly grow feral without a loving master!");
			}
		};

		npc.setEntityClass("npcstarybaca");
		npc.setPosition(100, 120);
		npc.initHP(100);
		npc.setDescription("You see the Old Baca, who breeds and tends to faithful Tatra sheepdogs.");
		zone.add(npc);
	}
}
