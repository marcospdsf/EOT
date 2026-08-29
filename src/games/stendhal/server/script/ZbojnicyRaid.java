/* $Id: OrcRaid.java,v 1.2 2010/10/07 17:10:26 nhnb Exp $ */
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

import java.util.HashMap;
import java.util.Map;

/**
 * @author kymara
 * 
 * Atak hordy zbójeckiej
 */
public class ZbojnicyRaid extends CreateRaid {

	@Override
	protected Map<String, Integer> createArmy() {
		final Map<String, Integer> attackArmy = new HashMap<String, Integer>();
		attackArmy.put("deceitful forest bandit", 4);
		attackArmy.put("forest bandit", 5);
		attackArmy.put("craven forest bandit", 4);
		attackArmy.put("senior forest bandit", 3);
		attackArmy.put("forest bandit scout", 3);
		attackArmy.put("female outlaw", 2);
		attackArmy.put("forest outlaw", 3);
		attackArmy.put("poacher outlaw", 2);
		return attackArmy;
	}
	@Override
	protected String getInfo() {
		return "Attack of the bandit horde.";
	}
}
