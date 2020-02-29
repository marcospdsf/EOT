/***************************************************************************
 *                   (C) Copyright 2003-2010 - Stendhal                    *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
 //Zrobiony na podstawie blacksmithNPC
package games.stendhal.server.maps.pol.zakopane.blacksmith;

import games.stendhal.common.Direction;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.behaviour.adder.ProducerAdder;
import games.stendhal.server.entity.npc.behaviour.impl.ProducerBehaviour;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class KowalAndrzejNPC implements ZoneConfigurator {
	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildKuznia(zone);
	}

	private void buildKuznia(final StendhalRPZone zone) {
		final SpeakerNPC kuznia = new SpeakerNPC("Kowal Andrzej") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(23, 12));
				nodes.add(new Node(29,12));
				nodes.add(new Node(29,5));
				nodes.add(new Node(17,5));
				nodes.add(new Node(17,9));
				nodes.add(new Node(28,9));
				nodes.add(new Node(28,12));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				addGreeting("Witaj!");
				addJob("Witaj. Jestem tutejszym kowalem. Jako specjalne #zadanie mogę zrobić dla Ciebie #złotą #ciupagę lub odlać iron.");
				addHelp("Odlewam iron oraz w ramach specjalnego zadania wykonuje #złotą #ciupagę.");
				addOffer("Mogę odlać iron, jeżeli przyniesiesz mi #rudę #żelaza i #wood. Powiedz tylko #odlej. Mogę także w ramach specjalnego #zadania wykonać #złotą #ciupagę o ile przyniesiesz mi #ciupagę, #sztabki #złota, #polana i trochę #money oraz pod warunkiem, że udowodnisz swoją odwagę! W tym celu udaj się do Gazdy Jędrzeja i poproś o #misyjkę #dla #prawdziwego #rycerza! Tylko kiedy je wykonasz, a on da mi znać to zrobię dla Ciebie #złotą #ciupagę.");
				addGoodbye("Dowidzenia.");
				addReply(Arrays.asList("złota ciupaga", "złotą ciupagę"),"Podejmę się #zadania wykonania złotej ciupagi o ile posiadasz #ciupagę, #polana, #sztabki #złota i #money. Jestem trochę zapracowany i gdy przyjdziesz to przypomnij mi mówiąc 'przypomnij'.");
				addReply(Arrays.asList("ciupaga", "ciupagę"),"Ciupaga to ulubiona broń zbójców w Zakopanem.");
				addReply("wood","wood znajdziesz na obrzeżach lasów. Potrzebne mi są do podtrzymywania ognia czy wykonania rękojeści");
				addReply("gold bar","Gold bar wytapia Joshua w Ados. Potrzebuję ich do zrobienia ostrza oraz detali.");
				addReply("money","Każdy ich potrzebuje. Po za tym potrzebne są do zakupienia specjalnych składników bez których złota ciupaga będzie nie wiele warta. Nie zdradzę Tobie o jakie chodzi. To tajemnica rodzinna przekazywana z pokolenia na pokolenie.");

				// Joshua makes gold if you bring him gold nugget and wood
				final Map<String, Integer> requiredResources = new TreeMap<String, Integer>();	
				requiredResources.put("wood", 1);
				requiredResources.put("ruda żelaza", 1);

				final ProducerBehaviour behaviour = new ProducerBehaviour("andrzej_cast_iron",
						"cast", "iron", requiredResources, 3 * 60);

				new ProducerAdder().addProducer(this, behaviour,
				        "Cześć! Jestem tutejszym kowalem. Jeżeli będziesz chciał, abym odlał dla Ciebie #iron to daj znać!");
				addReply(Arrays.asList("ruda", "iron", "ruda żelaza"),
								"Rudę żelaza znajdziesz w górach Zakopanego i Kościeliska. Uważaj tam na siebie!");
			}
		};

		kuznia.setDescription("Oto zapracowany Kowal Andrzej. Czasami trzeba mu przypomnieć co powinien zrobić!");
		kuznia.setEntityClass("goldsmithnpc");
		kuznia.setDirection(Direction.DOWN);
		kuznia.setPosition(23, 12);
		kuznia.initHP(100);
		zone.add(kuznia);
	}
}
