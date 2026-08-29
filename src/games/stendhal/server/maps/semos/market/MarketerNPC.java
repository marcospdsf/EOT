package games.stendhal.server.maps.semos.market;

import games.stendhal.server.entity.npc.shop.ShopType;
import games.stendhal.server.entity.npc.shop.ShopsList;

import java.util.Map;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.SpeakerNPC;
/*
 */
public class MarketerNPC implements ZoneConfigurator {
/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildXinBlanca(zone);
	}

	private void buildXinBlanca(final StendhalRPZone zone) {
		final SpeakerNPC xinBlanca = new SpeakerNPC("Kubola") {


			@Override
			protected void createDialog() {
				addGreeting();
				addJob("I manage the Semos food market. We buy fresh vegetables, fruits, baked goods, and fish.");
				addHelp("Our stall is compact because our large warehouse occupies the back. You can help supply us by selling edible goods.");
				ShopsList.get().configureNPC(this, "semosmarket", ShopType.ITEM_BUY, false);
				addOffer("Look at the board on the wall to check our #offer.");
				addQuest("The best way to help is to sell us your gathered food.");
				addGoodbye();
			}
		};

		xinBlanca.setEntityClass("cloakedwomannpc");
		xinBlanca.setDescription("You see a friendly market vendor. You can sell fresh edible goods to her.");
		xinBlanca.setPosition(7, 2);
		xinBlanca.initHP(100);
		zone.add(xinBlanca);
	}
}
