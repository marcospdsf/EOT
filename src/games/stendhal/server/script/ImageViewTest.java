/* $Id: ImageViewTest.java,v 1.8 2010/09/19 02:36:26 nhnb Exp $ */
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

import games.stendhal.server.core.scripting.ScriptImpl;
import games.stendhal.server.entity.player.Player;

import java.util.List;

import marauroa.common.game.RPEvent;

/**
 * A simple test for the client ImageViewer.
 * 
 * @author timothyb89
 */
public class ImageViewTest extends ScriptImpl {

	@Override
	public void execute(final Player admin, final List<String> args) {

		final RPEvent event = new RPEvent("examine");
		event.put("path", "/data/sprites/examine/map-semos-city.png");
		event.put("alt", "Mapa Miasta Semos");
		event.put("title", "Miasto Semos");
		event.put(
				"text",
				"The city of Semos is where your adventure begins and where you will often return.<br>"
						+ "1&nbsp;Ratusz,&nbsp;tutaj&nbsp;mieszka&nbsp;Tad, "
						+ "2&nbsp;Biblioteka, 3&nbsp;Bank, 4&nbsp;Magazyn, 5&nbsp;Piekarnia, "
						+ "6&nbsp;Kowal,&nbsp;Carmen, 7&nbsp;Hotel,&nbsp;Margaret, "
						+ "8&nbsp;Temple,&nbsp;ilisa, 9&nbsp;Dangerous&nbsp;Dungeon<br>"
						+ "A&nbsp;Semos&nbsp;Village, B&nbsp;North Plains&nbsp;&amp;&nbsp;Mines, "
						+ "C&nbsp;Long&nbsp;Road&nbsp;to&nbsp;Ados, "
						+ "D&nbsp;South&nbsp;Plains&nbsp;&amp;&nbsp;Nalwor&nbsp;Forest");
		admin.addEvent(event);
	}
}
