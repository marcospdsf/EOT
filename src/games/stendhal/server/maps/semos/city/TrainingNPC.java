package games.stendhal.server.maps.semos.city;


import games.stendhal.server.entity.npc.behaviour.adder.HealerAdder;
import games.stendhal.server.entity.npc.shop.ShopType;
import games.stendhal.server.entity.npc.shop.ShopsList;

//import java.util.Arrays;
//import java.util.LinkedList;
//import java.util.List;
import java.util.Map;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
//import games.stendhal.server.core.pathfinder.FixedPath;
//import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.SpeakerNPC;
/**
 * A young lady (original name: Carmen) who heals players without charge.
 */
public class TrainingNPC implements ZoneConfigurator {

	@Override
	public void configureZone(final StendhalRPZone zone,
			final Map<String, String> attributes) {
		buildNPC(zone);
	}

	private void buildNPC(final StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Healer") {
			@Override
			public void createDialog() {
				addGreeting("Hi, I take care of healing for players that are willing to train here");
				addGoodbye();
			}

		//	@Override
//			protected void createPath() {
//				final List<Node> nodes = new LinkedList<Node>();
//				nodes.add(new Node(5, 46));
//				nodes.add(new Node(18, 46));
//				setPath(new FixedPath(nodes, true));
//			}
		};
		//ShopsList.get().configureNPC(npc, "healing", ShopType.ITEM_SELL, true);
		new HealerAdder().addHealer(npc, 0);
		npc.setPosition(46, 38);
		npc.setDescription("You see the Healer. She will help you to heal.");
		npc.setEntityClass("gnomenpc");
		//npc.setSounds(Arrays.asList("giggle-1", "giggle-2"));
		zone.add(npc);
	}

}