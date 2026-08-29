package games.stendhal.server.maps.pol.zakopane.tavern;

import games.stendhal.server.entity.npc.shop.ShopType;
import games.stendhal.server.entity.npc.shop.ShopsList;

import games.stendhal.common.Direction;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.mapstuff.sign.Sign;
import games.stendhal.server.entity.npc.SpeakerNPC;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/*
 * Food and drink seller,  Inside Semos Tavern - Level 0 (ground floor)
 * Sells the flask required for Tad's quest IntroducePlayers
 */
public class JagnaNPC implements ZoneConfigurator {
/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildJagna(zone);
	}

	private void buildJagna(final StendhalRPZone zone) {
		final SpeakerNPC jagna = new SpeakerNPC("Jagna") {

			@Override
			protected void createPath() {
				// NPC does not move
				setPath(null);
			}
			@Override
			protected void createDialog() {
				addGreeting("Welcome, esteemed guest! May I get you something to eat or drink?");
				addJob("I am a waitress at this tavern. We serve fine local and imported drinks as well as hearty meals. We also have delightful desserts.");
				addHelp("Our tavern is renowned throughout Zakopane as a warm place to rest and feast. If you would like to see our #offer, just let me know.");
				ShopsList.get().configureNPC(this, "urodziny", ShopType.ITEM_SELL, true);
				addGoodbye("Enjoy your meal and have a wonderful time into the night!");
			}
		};

		jagna.setEntityClass("hotelreceptionistnpc");
		jagna.setPosition(25, 3);
		jagna.setDirection(Direction.DOWN);
		jagna.initHP(100);
		zone.add(jagna);

		final Sign book = new Sign();
		book.setPosition(28, 1);
		book.setText(" -- Jagna sells -- \n hops juice\t 8\n grape drink\t 10\n olive drink\t 50\n vanilla shake\t 100\n chocolate shake\t 100\n meat\t\t 20\n ham\t\t 30\n hotdog\t\t 120\n"+
					" cheese hotdog\t 140\n tuna sandwich\t 110\n sandwich\t\t 110\n chocolate bar\t 80\n licorice\t\t 80");
		book.setEntityClass("blackboard");
		book.setResistance(10);
		zone.add(book);
	}
}
