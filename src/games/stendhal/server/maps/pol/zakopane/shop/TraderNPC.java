package games.stendhal.server.maps.pol.zakopane.shop;

import games.stendhal.server.entity.npc.shop.ShopType;
import games.stendhal.server.entity.npc.shop.ShopsList;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.SpeakerNPC;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/*
 * Inside Zakopane Tavern - Level 0 (ground floor)
 */
public class TraderNPC implements ZoneConfigurator {
/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildMichal(zone);
	}

	private void buildMichal(final StendhalRPZone zone) {
		final SpeakerNPC michal = new SpeakerNPC("Michał") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(11, 8));
				nodes.add(new Node(11, 17));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				addGreeting();
				addJob("I sell supplies and starter gear to novice adventurers.");
				addHelp("I buy and sell all sorts of items. Ask me for my #offer.");
				ShopsList.get().configureNPC(this, "sellstuff", ShopType.ITEM_SELL, false);
				ShopsList.get().configureNPC(this, "buystuff", ShopType.ITEM_BUY, false);
				addOffer("Check the board on the wall to review my wares and prices.");
				addQuest("I have no quests for you. Talk to other townsfolk—perhaps they need assistance.");
				addGoodbye();
			}
		};

		michal.setEntityClass("weaponsellernpc");
		michal.setDescription("You see Michał. He is well known among novice travelers for his wide assortment of starter gear.");
		michal.setPosition(11, 8);
		michal.initHP(100);
		zone.add(michal);
	}
}
