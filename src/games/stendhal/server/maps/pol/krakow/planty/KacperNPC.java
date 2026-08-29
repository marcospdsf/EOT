package games.stendhal.server.maps.pol.krakow.planty;

import games.stendhal.server.entity.npc.shop.ShopType;
import games.stendhal.server.entity.npc.shop.ShopsList;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.SpeakerNPC;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Builds an NPC to buy previously un bought armor.
 *
 * @author kymara
 */
public class KacperNPC implements ZoneConfigurator {
/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildNPC(zone);
	}

	private void buildNPC(final StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Kacper") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(10, 2));
				nodes.add(new Node(16, 2));
				nodes.add(new Node(16, 5));
				nodes.add(new Node(10, 5));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				addGreeting("Hello! Lovely day we're having.");
				addJob("I am gathering building materials for an ice castle. You can #offer me something.");
				addHelp("I purchase materials needed to build an ice castle. If you have any, #offer them to me.");
				addOffer("I buy snowball 5, sling stone 6, ice block 8.");
				addQuest("Look for stones, ice blocks, and snowballs—bring them to me and I will reward you well.");
				ShopsList.get().configureNPC(this, "buykacper", ShopType.ITEM_BUY, false);
				addGoodbye("Farewell.");
			}
		};

		npc.setDescription("You see a young boy building an elaborate ice castle.");
		npc.setEntityClass("kid9npc");
		npc.setPosition(10, 2);
		npc.initHP(100);
		zone.add(npc);
	}
}
