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
public class RybakNPC implements ZoneConfigurator {
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
		final SpeakerNPC npc = new SpeakerNPC("Fisherman") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(17, 94));
				nodes.add(new Node(19, 94));
				nodes.add(new Node(19, 96));
				nodes.add(new Node(19, 92));
				nodes.add(new Node(19, 94));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				addGreeting("Shhh, keep quiet or you'll scare the fish away.");
				addJob("I'll gladly buy fish from you—I cannot return home empty-handed. You can #offer me your catch.");
				addHelp("I buy all kinds of fish. If you caught some, #offer them to me.");
				addOffer("I buy cod 5, char 7, roach 7, mackerel 8, perch 8, surgeonfish 9, trout 9, clownfish 10.");
				addQuest("I cannot seem to catch anything today and I cannot go home empty-handed. Please #help me.");
				ShopsList.get().configureNPC(this, "buyrybak", ShopType.ITEM_BUY, false);
				addGoodbye("Farewell.");
			}
		};

		npc.setDescription("You see a local fisherman waiting patiently for a bite.");
		npc.setEntityClass("fishermannpc");
		npc.setPosition(17, 94);
		npc.initHP(100);
		zone.add(npc);
	}
}
