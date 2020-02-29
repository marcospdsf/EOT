/* $Id: HousesFromPostman.java,v 1.5 2010/09/19 02:36:26 nhnb Exp $ */
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
package games.stendhal.server.script;

import games.stendhal.common.filter.FilterCriteria;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.scripting.ScriptImpl;
import games.stendhal.server.entity.Entity;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.item.StackableItem;
import games.stendhal.server.entity.mapstuff.chest.StoredChest;
import games.stendhal.server.entity.mapstuff.portal.HousePortal;
import games.stendhal.server.entity.mapstuff.portal.Portal;
import games.stendhal.server.entity.player.Player;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * an one-off script for marking the houses at postmans slots as owned by someone.
 */
public class HousesFromPostman extends ScriptImpl {
	private static final Logger logger = Logger.getLogger(HousesFromPostman.class);
	
	private static final String[] SLOTS = { "house", "ados_house", "kirdneh_house" };
	private static final String[] ZONE_NAMES = { "0_kalavan_city",
		"0_kirdneh_city",
		"0_ados_city_n",
		"0_ados_city",
		"0_zakopane_se",
	};
	
	private List<HousePortal> portals = null;
	
	private void initPortalList() {
		portals = new LinkedList<HousePortal>();
		
		for (final String zoneName : ZONE_NAMES) {
			final StendhalRPZone zone = SingletonRepository.getRPWorld().getZone(zoneName);
			
			for (final Portal portal : zone.getPortals()) {
				if (portal instanceof HousePortal) {
					portals.add((HousePortal) portal);
				}
			}
		}
	}
	
	private void updateHouse(final int number) {
		for (final HousePortal portal : portals) {
			if (portal.getPortalNumber() == number) {
				portal.setOwner("nieznany właściciel");
							
				final long time = System.currentTimeMillis();
			
				portal.setExpireTime(time);
				
				fillChest(findChest(portal));
				
				logger.debug("Updated house " + number);
				
				return;
			}
		}
		logger.error("Failed to find house " + number);
	}
	
	private StoredChest findChest(final HousePortal portal) {
		final String zoneName = portal.getDestinationZone();
		final StendhalRPZone zone = SingletonRepository.getRPWorld().getZone(zoneName);
		
		final List<Entity> chests = zone.getFilteredEntities(new FilterCriteria<Entity>() {
			public boolean passes(final Entity object) {
				return (object instanceof StoredChest);
			}
		});
		
		if (chests.size() != 1) {
			logger.error(chests.size() + " chests in " + portal.getDoorId());
			return null;
		}
		
		return (StoredChest) chests.get(0);
	}
	
	private void fillChest(final StoredChest chest) {
		Item item = SingletonRepository.getEntityManager().getItem("karteczka");
		item.setDescription("INFORMACJA DLA WŁAŚCICIELA DOMU\n"
				+ "1. Jeżeli nie zapłacisz podatku za dom to wszystkie przedmioty znajdujące się w skrzyni będą skonfiskowane.\n"
				+ "2. Wszystkie osoby, które dostaną się do domu mogą korzystać ze skrzyni.\n"
				+ "3. Pamintj, aby zmienić zamki jako zabezpieczenie twojego domu.\n"
				+ "4. Możesz odsprzedać dom o ile chcesz (nie zostawiaj mnie)\n");
		chest.add(item);
		
		item = SingletonRepository.getEntityManager().getItem("grape drink");
		((StackableItem) item).setQuantity(2);
		chest.add(item);
		
		item = SingletonRepository.getEntityManager().getItem("tabliczka czekolady");
		((StackableItem) item).setQuantity(2);
		chest.add(item);
	}
	
	@Override
	public void execute(final Player admin, final List<String> args) {
		super.execute(admin, args);

		final Player postman = SingletonRepository.getRuleProcessor().getPlayer("postman");
		
		if (postman == null) {
			logger.error("postman is not available");
			return;
		}
		
		initPortalList();
		
		for (final String slotName : SLOTS) {
			final String slotContents = postman.getQuest(slotName);
			final String[] ownedHouses = slotContents.split(";");
			
			for (final String house : ownedHouses) {
				if (house.length() > 0) {
					updateHouse(Integer.parseInt(house));
				}
			}
		}
	}
}
