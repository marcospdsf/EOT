package games.stendhal.server.maps.ados.market;

import games.stendhal.server.entity.npc.shop.ShopType;
import games.stendhal.server.entity.npc.shop.ShopsList;

import java.util.Map;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.SpeakerNPC;
/*
 */
public class KolekcjonerNPC implements ZoneConfigurator {
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
		final SpeakerNPC xinBlanca = new SpeakerNPC("Kolekcjoner Cole") {


			@Override
			protected void createDialog() {
				addGreeting();
				addJob("Nie mam dla ciebie żadnego zadania.");
				addHelp("Aktualnie nie potrzebuje pomocy.");
				ShopsList.get().configureNPC(this, "kolekcja333", ShopType.ITEM_BUY, false);
				addOffer("Spójrz na tablicę koło mnie, aby zobaczyć co kupuję.");
				addQuest("Nie mam dla ciebie żadnego zadania.");
				addGoodbye();
			}
		};

		xinBlanca.setEntityClass("colenpc");
		xinBlanca.setDescription("You see Cole, an avid collector of curious artifacts.");
		xinBlanca.setPosition(21, 47);
		xinBlanca.initHP(100);
		zone.add(xinBlanca);
	}
}
