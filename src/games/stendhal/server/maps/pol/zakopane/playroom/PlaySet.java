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
package games.stendhal.server.maps.pol.zakopane.playroom;

import games.stendhal.common.Direction;
import games.stendhal.common.grammar.Grammar;
import games.stendhal.common.parser.Sentence;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.Spot;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.PlayerInAreaCondition;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.util.Area;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Creates the Ados Deathmatch Game.
 */
public class PlaySet {
	/** the logger instance. */
	private static final Logger logger = Logger.getLogger(PlaySet.class);

	private StendhalRPZone zone;

	private static Area arena;

	public PlaySet(final StendhalRPZone zone, final Area area) {
		this.zone = zone;
		arena = area;
		// do not let players scroll out of deathmatch
	}

	private Item getItem(final String polishName, final String englishName) {
		Item item = SingletonRepository.getEntityManager().getItem(polishName);
		if (item == null && englishName != null) {
			item = SingletonRepository.getEntityManager().getItem(englishName);
		}
		return item;
	}

	private void addItem(final String polishName, final String englishName, final int x, final int y) {
		final Item item = getItem(polishName, englishName);
		if (item != null) {
			item.setPosition(x, y);
			zone.add(item, false);
		} else {
			logger.warn("PlaySet could not find item: " + polishName + " / " + englishName);
		}
	}

	public void createFiguraFioletowa(final int x, final int y) {
		addItem("figura fioletowa", "purple figure", x, y);
	}

	public void createFiguraZielona(final int x, final int y) {
		addItem("figura zielona", "green figure", x, y);
	}

	public void createDamkaFioletowa(final int x, final int y) {
		addItem("damka fioletowa", "purple lady", x, y);
	}

	public void createDamkaZielona(final int x, final int y) {
		addItem("damka zielona", "green lady", x, y);
	}

	public void createPionekCzerwony(final int x, final int y) {
		addItem("pionek czerwony", "red pawn", x, y);
	}

	public void createPionekNiebieski(final int x, final int y) {
		addItem("pionek niebieski", "blue pawn", x, y);
	}

	public void createPionekZielony(final int x, final int y) {
		addItem("pionek zielony", "green pawn", x, y);
	}

	public void createPionekZolty(final int x, final int y) {
		addItem("pionek żółty", "yellow pawn", x, y);
	}

	public void createKostka(final int x, final int y) {
		addItem("kostka", "dice", x, y);
	}

	public void createKolko(final int x, final int y) {
		addItem("kółko", "circle", x, y);
	}

	public void createKrzyzyk(final int x, final int y) {
		addItem("krzyżyk", "cross", x, y);
	}

	public void createCzarnyPionek(final int x, final int y) {
		addItem("czarny pionek", "black pawn", x, y);
	}

	public void createCzarnaWieza(final int x, final int y) {
		addItem("czarna wieża", "black tower", x, y);
	}

	public void createCzarnySkoczek(final int x, final int y) {
		addItem("czarny skoczek", "black horse", x, y);
	}

	public void createCzarnyGoniec(final int x, final int y) {
		addItem("czarny goniec", "black bishop", x, y);
	}

	public void createCzarnyHetman(final int x, final int y) {
		addItem("czarny hetman", "black hetman", x, y);
	}

	public void createCzarnyKrol(final int x, final int y) {
		addItem("czarny król", "black king", x, y);
	}

	public void createBialyPionek(final int x, final int y) {
		addItem("biały pionek", "white pawn", x, y);
	}

	public void createBialaWieza(final int x, final int y) {
		addItem("biała wieża", "white tower", x, y);
	}

	public void createBialySkoczek(final int x, final int y) {
		addItem("biały skoczek", "white horse", x, y);
	}

	public void createBialyGoniec(final int x, final int y) {
		addItem("biały goniec", "white bishop", x, y);
	}

	public void createBialyHetman(final int x, final int y) {
		addItem("biały hetman", "white hetman", x, y);
	}

	public void createBialyKrol(final int x, final int y) {
		addItem("biały król", "white king", x, y);
	}
}
