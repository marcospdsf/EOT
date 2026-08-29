package games.stendhal.server.maps.pol.koscielisko.plains;

import games.stendhal.server.entity.npc.shop.ShopType;
import games.stendhal.server.entity.npc.shop.ShopsList;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.SpeakerNPC;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Builds an NPC to buy previously un bought armor.
 *
 * @author kymara
 */
public class WielkoludNPC implements ZoneConfigurator {
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
		final SpeakerNPC npc = new SpeakerNPC("Wielkolud") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(1, 75));
				nodes.add(new Node(1, 85));
				nodes.add(new Node(15, 85));
				nodes.add(new Node(15, 90));
				nodes.add(new Node(23, 90));
				nodes.add(new Node(23, 100));
				nodes.add(new Node(46, 100));
				nodes.add(new Node(46, 90));
				nodes.add(new Node(36, 90));
				nodes.add(new Node(36, 81));
				nodes.add(new Node(15, 81));
				nodes.add(new Node(15, 76));
				nodes.add(new Node(8, 76));
				nodes.add(new Node(8, 73));
				nodes.add(new Node(1, 73));
				nodes.add(new Node(1, 75));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				addGreeting("Greetings, traveler.");
				addJob("I'm looking for someone brave enough to deal with the local #monsters.");
				addReply(Arrays.asList("monsters", "potworami"), "Crowds of mountain wraiths roam these crags, and falling rock slides often crush my toes! If you wish to help me, accept my #quest.");
				addHelp("I purchase rare armor, weapons, and accessories. If you have any to part with, make me an #offer. In addition, I have a small #quest for you.");
				addOffer("I buy: highlander shoes 100, highlander's shawl 200, highlander chain 200, highlanders skirt 200, highlanders corset 300, highlander helmet 300, beech pants 300, polish light shield 500, polish wooden shield 500, highlander axe 1000, chaplet 1000, highlander belt 1000, polish chainmail 1000, polish plate shield 2000, highlander buckle 2000, polish heavy shield 1500, golden highlander axe 5000, and ceremonial sword 1000000.");
				ShopsList.get().configureNPC(this, "buyrareitems", ShopType.ITEM_BUY, false);
				addGoodbye("Farewell, friend.");
			}
		};

		npc.setDescription("You see the Mountain Giant, guardian of the wild crags of Kościelisko.");
		npc.setEntityClass("npcwielkolud");
		npc.setPosition(1, 75);
		npc.initHP(1000);
		zone.add(npc);
	}
}
