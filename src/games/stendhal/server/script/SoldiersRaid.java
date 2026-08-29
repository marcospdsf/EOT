/* $Id: SoldiersRaid.java,v 1.2 2010/10/07 17:10:26 nhnb Exp $ */
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
 * Atak zbrojnego rycertswa!
 */
public class SoldiersRaid extends CreateRaid {

	@Override
	protected Map<String, Integer> createArmy() {
		final Map<String, Integer> attackArmy = new HashMap<String, Integer>();
		attackArmy.put("gatekeeper guard", 5);
		attackArmy.put("black knight", 2);
		attackArmy.put("emerald knight", 3);
		attackArmy.put("sapphire knight", 3);
		attackArmy.put("crimson knight", 3);
		attackArmy.put("golden armor knight", 2);
		attackArmy.put("death knight", 4);
		attackArmy.put("fortress guard", 1);
		attackArmy.put("imperial knight", 1);
		attackArmy.put("mountain elf warrior", 1);
		attackArmy.put("dark elf knight", 1);
		attackArmy.put("causeway guard", 1);
		return attackArmy;
	}
	@Override
	protected String getInfo() {
		return "Attack of the armed knights!";
	}
}
