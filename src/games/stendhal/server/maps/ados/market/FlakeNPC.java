package games.stendhal.server.maps.ados.market;

import games.stendhal.server.entity.npc.shop.ShopType;
import games.stendhal.server.entity.npc.shop.ShopsList;

import java.util.Map;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.SpeakerNPC;
/*
 */
public class FlakeNPC implements ZoneConfigurator {
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
		final SpeakerNPC xinBlanca = new SpeakerNPC("Flake") {


			@Override
			protected void createDialog() {
				addGreeting();
				addJob("Jestem starym mędrcem. Potrzebuje dla swoich wnuków trochę ekwipunku do treningu.");
				addHelp("Aktualnie nie potrzebuje pomocy.");
				ShopsList.get().configureNPC(this, "orcitems", ShopType.ITEM_BUY, false);
				addOffer("Spójrz na tablicę koło mnie, aby zobaczyć czego dokładnie potrzebuje.");
				addQuest("Nie mam dla ciebie żadnego zadania.");
				addGoodbye();
			}
		};

		xinBlanca.setEntityClass("flakenpc");
		xinBlanca.setDescription("You see Flake, a battle-hardened veteran.");
		xinBlanca.setPosition(48, 17);
		xinBlanca.initHP(100);
		zone.add(xinBlanca);
	}
}
