package games.stendhal.server.maps.pol.zakopane.stadium;

import games.stendhal.server.entity.npc.shop.ShopType;
import games.stendhal.server.entity.npc.shop.ShopsList;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.SpeakerNPC;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Builds an NPC to buy previously un bought armor.
 *
 * @author kymara
 */
public class KibicNPC implements ZoneConfigurator {
/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildKibic(zone);
	}

	private void buildKibic(final StendhalRPZone zone) {
		final SpeakerNPC kibic = new SpeakerNPC("Kibic") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(26, 5));
				nodes.add(new Node(37, 5));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				addGreeting("Welcome to the stadium!");
				addJob("I'm a loyal fan cheering on our top team!");
				addHelp("In the future, refreshments and snacks will be sold right at the stadium. For now, bring your own provisions.");
				ShopsList.get().configureNPC(this, "mecz", ShopType.ITEM_SELL, true);
				addGoodbye("Farewell and visit our matches often. Our team needs true supporters!");
			}
		};

		kibic.setDescription("You see a passionate sports fan cheering passionately for the home team.");
		kibic.setEntityClass("npckibic");
		kibic.setPosition(26, 5);
		kibic.initHP(1000);
		kibic.setSounds(Arrays.asList("pol-boisko-tlo", "pol-chcemy-gola"));

		zone.add(kibic);
	}
}
