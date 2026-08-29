package games.stendhal.server.maps.pol.zakopane.hostel;

import games.stendhal.server.entity.npc.shop.ShopType;
import games.stendhal.server.entity.npc.shop.ShopsList;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.mapstuff.sign.Sign;
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
public class BronekNPC implements ZoneConfigurator {
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
		final SpeakerNPC npc = new SpeakerNPC("Bronek") {

			@Override
			protected void createPath() {
				setPath(null);
			}

			@Override
			protected void createDialog() {
				addGreeting("Greetings, traveler.");
				addJob("I purchase beast claws and dragon claws.");
				addHelp("I will gladly buy dragon claws from you, for I hear that across the far hills they can be forged into potent talismans! If you have any, make me an #offer.");
				addOffer("The notice board lists all the items I purchase.");
				addQuest("Thank you, but I have no quests at present.");
				ShopsList.get().configureNPC(this, "buydragonitems", ShopType.ITEM_BUY, false);
				addGoodbye("Farewell, friend.");
			}
		};

		npc.setDescription("You see Bronek, a sturdy and respectable highlander.");
		npc.setEntityClass("npcbronek");
		npc.setPosition(16, 18);
		npc.initHP(100);
		zone.add(npc);

		final Sign tablica = new Sign();
		tablica.setPosition(16, 15);
		tablica.setText(" -- Items you can sell to Bronek -- \n wolf claws\t 5\n bear claws\t 8\n tiger claws\t 100\n green dragon claw\t 5000\n"+
						" blue dragon claw\t 5000\n red dragon claw\t 5000\n black dragon's claw\t 10000\n gold dragon claw\t 15000");
		tablica.setEntityClass("blackboard");
		tablica.setResistance(10);
		zone.add(tablica);

	}
}
