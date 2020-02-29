/* $Id: DumpSpeakerNPCtoDB.java,v 1.7 2010/11/27 10:55:24 nhnb Exp $ */
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
package games.stendhal.server.core.rp;

import games.stendhal.server.core.engine.dbcommand.DumpSpeakerNPCsCommand;
import games.stendhal.server.core.events.TurnListener;
import marauroa.server.db.command.DBCommandQueue;

/**
 * Dumps information of all SpeakerNPCs to the database
 * 
 * @author hendrik
 */
public class DumpSpeakerNPCtoDB implements TurnListener {

	public void onTurnReached(int currentTurn) {
		DBCommandQueue.get().enqueue(new DumpSpeakerNPCsCommand());
	}
}