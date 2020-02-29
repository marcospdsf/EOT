/* $Id$ */
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
package games.stendhal.client.gui;


import javax.swing.SwingUtilities;

import games.stendhal.client.listener.FeatureChangeListener;

/**
 * A weaponbag.
 */
@SuppressWarnings("serial")
class WeaponBag extends SlotWindow implements FeatureChangeListener {
	/**
	 * Create a weapon bag.
	 * 
	 */
	public WeaponBag() {
		// Remember if you change these numbers change also a number in
		// src/games/stendhal/server/entity/RPEntity.java
		super("weaponbag", 3, 1);
		// A panel window; forbid closing
		setCloseable(false);
	}

	//
	// KeyRing
	//

	/**
	 * Disable the scrollbag.
	 */
	private void disableScrollBag() {
		/*
		 * You can not really lose a keyring for now, but
		 * a disable message is received at every map change.
		 * Just ignore it. (And after keyrings are made to
		 * real items, this whole file will be obsolete anyway).
		 */
	}

	//
	// FeatureChangeListener
	//

	/**
	 * A feature was disabled.
	 *
	 * @param name
	 *            The name of the feature.
	 */
	@Override
	public void featureDisabled(final String name) {
		if (name.equals("weaponbag")) {
			disableScrollBag();
		}
	}

	/**
	 * A feature was enabled.
	 *
	 * @param name
	 *            The name of the feature.
	 * @param value
	 *            Optional feature specific data.
	 */
	@Override
	public void featureEnabled(final String name, final String value) {
		if (name.equals("weaponbag")) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					if(!isVisible()) {
						setVisible(true);
					}
				}
			});
		}
	}
}
