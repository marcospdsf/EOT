package games.stendhal.server.maps.ados.market;

import games.stendhal.server.entity.npc.shop.ShopType;
import games.stendhal.server.entity.npc.shop.ShopsList;

import java.util.Map;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.SpeakerNPC;
/*
 */
public class DeeanyNPC implements ZoneConfigurator {
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
		final SpeakerNPC xinBlanca = new SpeakerNPC("Deeany") {


			@Override
			protected void createDialog() {
				addGreeting();
				addJob("Nie mam dla ciebie żadnego zadania, ale chętnie kupie troche żelastwa..");
				addHelp("Aktualnie nie potrzebuje pomocy.");
				ShopsList.get().configureNPC(this, "blooditems", ShopType.ITEM_BUY, false);
				addOffer("Spójrz na tablicę koło mnie, aby zobaczyć czego dokładnie potrzebuje.");
				addQuest("Nie mam dla ciebie żadnego zadania.");
				addGoodbye();
			}
		};

		xinBlanca.setEntityClass("deeanynpc");
		xinBlanca.setDescription("You see Deeany, a sharp young gentleman.");
		xinBlanca.setPosition(55, 7);
		xinBlanca.initHP(100);
		zone.add(xinBlanca);
	}
}
