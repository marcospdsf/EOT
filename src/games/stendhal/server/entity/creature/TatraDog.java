/* $Id: OwczarekPodhalanski.java,v 1.30 2011/02/13 15:08:40 edi18028 Exp $ */
/***************************************************************************
 *                      (C) Copyright 2003 - Marauroa                      *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.server.entity.creature;

import games.stendhal.server.entity.player.Player;

import java.util.Arrays;
import java.util.List;

import marauroa.common.game.RPClass;
import marauroa.common.game.RPObject;
import marauroa.common.game.SyntaxException;

import org.apache.log4j.Logger;

/**
 * A owczarek podhalanski is a domestic animal that can be owned by a player.
 * <p>
 * It eats meat from the ground.
 * <p>
 * They move much faster than sheep
 * <p>
 * 
 * @author edi18028 (based on cat by kymara and on sheep by Daniel Herding)
 * 
 */
public class TatraDog extends Pet {

	/** the logger instance. */
	private static final Logger logger = Logger.getLogger(TatraDog.class);

	@Override
	void setUp() {
		HP = 1000;
		// each chicken or fish would give +5 HP
		incHP = 6; 

		ATK = 20;

		DEF = 60;

		XP = 100;

		baseSpeed = 0.9;

		setAtk(ATK);
		setDef(DEF);
		setXP(XP);
		setBaseHP(HP);
		setHP(HP);
	}

	public static void generateRPClass() {
		try {
			final RPClass tatraDog = new RPClass("tatra_sheepdog");
			tatraDog.isA("pet");
			// owczarek.add("weight", Type.BYTE);
			// owczarek.add("eat", Type.FLAG);
		} catch (final SyntaxException e) {
			logger.error("cannot generate RPClass", e);
		}
	}

	/**
	 * Creates a new Owczarek Podhalanski.
	 */
	public TatraDog() {
		this(null);
	}

	/**
	 * Creates a new Owczarek Podhalanski that may be owned by a player.
	 * @param owner 
	 */
	public TatraDog(final Player owner) {
		// call set up before parent constructor is called as it needs those
		// values
		super();
		setOwner(owner);
		setUp();
		setRPClass("tatra_sheepdog");
		put("type", "tatra_sheepdog");

		if (owner != null) {
			// add pet to zone and create RPObject.ID to be used in setPet()
			owner.getZone().add(this);
			owner.setPet(this);
		}

		update();
	}

	/**
	 * Creates a Owczarek based on an existing owczarek podhalanski RPObject, and assigns it to a
	 * player.
	 * 
	 * @param object
	 * @param owner
	 *            The player who should own the owczarek podhalanski
	 */
	public TatraDog(final RPObject object, final Player owner) {
		super(object, owner);
		int storedHP = getInt("hp");
		// fetch the speed etc values...
		setUp();
		// ...but don't heal the owczarek podhalanski
		setHP(storedHP);
		setRPClass("tatra_sheepdog");
		put("type", "tatra_sheepdog");
		update();
	}

	@Override
	protected List<String> getFoodNames() {
		return Arrays.asList("chicken", "meat", "ham", "sausage", "dog bone", "steak", "goat cheese",
				"sheep milk");
	}

	/**
	 * Does this domestic animal take part in combat?
	 *
	 * @return true, if it can be attacked by creatures, false otherwise
	 */
	@Override
	protected boolean takesPartInCombat() {
		return false;
	}


}
