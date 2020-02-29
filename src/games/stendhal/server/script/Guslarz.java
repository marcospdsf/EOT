/* $Id: Guslarz.java,v 1.2 2012/12/21 00:46:08 edi18028 Exp $ */
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

import games.stendhal.server.core.rp.StendhalQuestSystem;
import games.stendhal.server.core.scripting.ScriptImpl;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.quests.MeetGuslarz;

import java.util.List;

/**
 * Starts or stops Guslarz.
 * 
 * @author kymara
 */
public class Guslarz extends ScriptImpl {

	@Override
	public void execute(final Player admin, final List<String> args) {
		if (args.size() != 1) {
			admin.sendPrivateText("/script Guslarz.class {true|false}");
			return;
		}

		boolean enable = Boolean.parseBoolean(args.get(0));
		if (enable) {
			startGusla(admin);
		} else {
			stopGusla(admin);
		}
	}

	/**
	 * starts Gusla.
	 */
	private void startGusla(Player admin) {
		if (System.getProperty("stendhal.guslarz") != null) {
			admin.sendPrivateText("Guślarz jest już aktywowany.");
			return;
		}
		System.setProperty("stendhal.guslarz", "true");
		StendhalQuestSystem.get().loadQuest(new MeetGuslarz());
	}

	/**
	 * ends Gusla
	 */
	private void stopGusla(Player admin) {
		if (System.getProperty("stendhal.guslarz") == null) {
			admin.sendPrivateText("Guślarz nie został aktywowany.");
			return;
		}
		System.getProperties().remove("stendhal.guslarz");
		StendhalQuestSystem.get().unloadQuest(MeetGuslarz.QUEST_NAME);
	}

}
