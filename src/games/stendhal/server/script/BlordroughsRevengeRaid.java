/* $Id: BlordroughRevengeRaid.java,v 1.3 2010/09/19 02:36:26 nhnb Exp $ */
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
 * @author gummipferd
 * 
 * Not safe for players below level 150
 */
public class BlordroughsRevengeRaid extends CreateRaid {

	@Override
	protected Map<String, Integer> createArmy() {
		final Map<String, Integer> attackArmy = new HashMap<String, Integer>();
		attackArmy.put("czart", 7);
		attackArmy.put("imperialny sługa demonów", 5);
		attackArmy.put("imperialny lord demonów", 5);
		attackArmy.put("blordrough kwatermistrz", 7);
		attackArmy.put("imperialny lider", 6);
		attackArmy.put("superczłowiek", 8);
		attackArmy.put("upadły anioł", 2);
		return attackArmy;
	}
	@Override
	protected String getInfo() {
		return "Niebezpieczny dla wojowników poniżej poziomu 150.";
	}
}
