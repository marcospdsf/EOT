package games.stendhal.server.maps.pol.krakow.wawel;

import games.stendhal.server.entity.npc.shop.ShopType;
import games.stendhal.server.entity.npc.shop.ShopsList;

import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.SpeakerNPCFactory;
/**
 * A young lady (original name: Straznik) who heals players without charge. 
 */
public class StraznikNPC extends SpeakerNPCFactory {

	@Override
	public void createDialog(final SpeakerNPC npc) {
		npc.addGreeting("Greetings, traveler.");
		npc.addJob("I lead a peaceful life guarding the entrance to Wawel Castle.");
		npc.addHelp("I can sell you a Kraków scroll or a Tatra scroll.");
		ShopsList.get().configureNPC(npc, "wawel", ShopType.ITEM_SELL, true);
		npc.addGoodbye();
	}
}
