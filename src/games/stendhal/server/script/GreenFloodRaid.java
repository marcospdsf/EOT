/* $Id: GreenFloodRaid.java,v 1.2 2010/09/19 02:36:26 nhnb Exp $ */
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
 */
public class GreenFloodRaid extends CreateRaid {

	@Override
	protected Map<String, Integer> createArmy() {
		final Map<String, Integer> attackArmy = new HashMap<String, Integer>();
		attackArmy.put("ork wojownik", 7);
		attackArmy.put("ork łowca", 5);
		attackArmy.put("szef orków", 3);
		attackArmy.put("górski ork", 6);
		attackArmy.put("szef górskich orków", 3);
		attackArmy.put("superogr", 4);
		attackArmy.put("goblin żołnierz", 7);
		attackArmy.put("trol jaskiniowy", 2);
		attackArmy.put("zielony smok", 3);  

		return attackArmy;
	}
	@Override
	protected String getInfo() {
		return "Bezpieczny dla wojowników poniżej poziomu 60.";
	}
}
