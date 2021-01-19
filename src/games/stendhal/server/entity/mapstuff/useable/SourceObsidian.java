/* $Id: SourceObsidian.java,v 1.4 2011/02/13 15:28:45 edi18028 Exp $ */
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
package games.stendhal.server.entity.mapstuff.useable;

import games.stendhal.common.Rand;
import games.stendhal.common.constants.SoundLayer;
import games.stendhal.common.grammar.Grammar;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.item.StackableItem;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.events.ImageEffectEvent;
import games.stendhal.server.events.SoundEvent;
import marauroa.common.game.RPClass;

import org.apache.log4j.Logger;

/**
 * A obsidian source is a spot where a player can prospect for obsidian. He
 * needs a kilof and lina, time, and luck.
 * 
 * Prospecting takes 7-11 seconds; during this time, the player keep standing
 * next to the obsidian source. In fact, the player only has to be there when the
 * prospecting action has finished. Therefore, make sure that two obsidian sources
 * are always at least 5 sec of walking away from each other, so that the player
 * can't prospect for obsidian at several sites simultaneously.
 * 
 * @author daniel
 * @changes artur
 */
public class SourceObsidian extends PlayerActivityEntity {
	private static final Logger logger = Logger.getLogger(SourceObsidian.class);

	private final String startSound = "pickaxe_02";
	private final int SOUND_RADIUS = 20;
	
	private final int miningLevelRequired = 300;

	/**
	 * The equipment needed.
	 */
	private static final String NEEDED_EQUIPMENT_3 = "steel pick";
	private static final String NEEDED_EQUIPMENT_4 = "silver pick";
	private static final String NEEDED_EQUIPMENT_5 = "gold pick";
	private static final String NEEDED_EQUIPMENT_6 = "obsidian pick";
	private static final String NEEDED_EQUIPMENT_7 = "dragon bone pick";


	/**
	 * The name of the item to be found.
	 */
	private final String itemName;

	/**
	 * Create a obsidian source.
	 */
	public SourceObsidian() {
		this("obsidian crystal");
	}

	/**
	 * source name.
	 */
	@Override
	public String getName() {
		return("obsidian source");
	}

	/**
	 * Create a obsidian source.
	 * 
	 * @param itemName
	 *            The name of the item to be prospected.
	 */
	public SourceObsidian(final String itemName) {
		this.itemName = itemName;
		put("class", "source");
		put("name", "source_obsidian");
		setMenu("Mine");
		setDescription("Everything indicates that there is something here.");
		setResistance(100);
	}

	//
	// SourceObsidian
	//

	public static void generateRPClass() {
		final RPClass rpclass = new RPClass("source_obsidian");
		rpclass.isA("entity");
	}

	/**
	 * Calculates the probability that the given player finds stone. This is
	 * based on the player's mining skills, however even players with no skills
	 * at all have a 5% probability of success.
	 * 
	 * @param player
	 *            The player,
	 * 
	 * @return The probability of success.
	 */
	private double getSuccessProbability(final Player player) {
		double probability = 0.02;

		final long skill = player.getMiningLevel();
		
		if (skill != 0) {
			probability = Math.max(probability, skill); 
		}

		return probability + player.useKarma(0.02);
	}

	//
	// PlayerActivityEntity
	//

	/**
	 * Get the time it takes to perform this activity.
	 * 
	 * @return The time to perform the activity (in seconds).
	 */
	@Override
	protected int getDuration(final Player player) {
		if (player.isEquipped(NEEDED_EQUIPMENT_3)) {
			return 22 + Rand.rand(4);
		} else if (player.isEquipped(NEEDED_EQUIPMENT_4)) {
			return 20 + Rand.rand(4);
		} else if (player.isEquipped(NEEDED_EQUIPMENT_5)) {
			return 18 + Rand.rand(4);
		} else if (player.isEquipped(NEEDED_EQUIPMENT_6)) {
			return 16 + Rand.rand(4);
		} else if (player.isEquipped(NEEDED_EQUIPMENT_7)) {
			return 14 + Rand.rand(4);
		}
		
		return 24 + Rand.rand(4);
		
	}

	/**
	 * Decides if the activity can be done.
	 * 
	 * @return <code>true</code> if successful.
	 */
	@Override
	protected boolean isPrepared(final Player player) {
		if (
			player.isEquipped(NEEDED_EQUIPMENT_3) ||
			player.isEquipped(NEEDED_EQUIPMENT_4) || 
			player.isEquipped(NEEDED_EQUIPMENT_5) ||
			player.isEquipped(NEEDED_EQUIPMENT_6) ||
			player.isEquipped(NEEDED_EQUIPMENT_7) && 
			player.getMiningLevel() >= miningLevelRequired) {
			return true;
		} else if (
			player.isEquipped(NEEDED_EQUIPMENT_3) ||
			player.isEquipped(NEEDED_EQUIPMENT_4) || 
			player.isEquipped(NEEDED_EQUIPMENT_5) ||
			player.isEquipped(NEEDED_EQUIPMENT_6) ||
			player.isEquipped(NEEDED_EQUIPMENT_7) &&
			player.getMiningLevel() < miningLevelRequired) {
			player.sendPrivateText("You need level " + 
			miningLevelRequired +  "in mining to be able to mine this.");
			return false;
		}

		player.sendPrivateText("You need at least a steel pick to mine this.");
		return false;
	}

	/**
	 * Decides if the activity was successful.
	 * 
	 * @return <code>true</code> if successful.
	 */
	@Override
	protected boolean isSuccessful(final Player player) {
		final int random = Rand.roll1D100();
		return (random <= (getSuccessProbability(player) * 100));
	}

	/**
	 * Called when the activity has finished.
	 * 
	 * @param player
	 *            The player that did the activity.
	 * @param successful
	 *            If the activity was successful.
	 */
	@Override
	protected void onFinished(final Player player, final boolean successful) {
		if (successful) {
			final Item item = SingletonRepository.getEntityManager().getItem(itemName);
			int amount = 1;

			if (item != null) {
				if(player.isEquipped(NEEDED_EQUIPMENT_7)) {
					amount = Rand.throwCoin();
					((StackableItem) item).setQuantity(amount);
				}
				player.equipOrPutOnGround(item);
				player.incMinedForItem(item.getName(), item.getQuantity());
				SingletonRepository.getAchievementNotifier().onObtain(player);
				player.incMiningXP(750);
				player.sendPrivateText("You mined "
						+ Grammar.quantityplnoun(amount, itemName, "a")+ ".");
			} else {
				logger.error("could not find item: " + itemName);
			}
		} else {
			player.incMiningXP(75);
			player.sendPrivateText("You have not extracted anything.");
		}
	}

	/**
	 * Called when the activity has started.
	 * 
	 * @param player
	 *            The player starting the activity.
	 */
	@Override
	protected void onStarted(final Player player) {
		addEvent(new SoundEvent(startSound, SOUND_RADIUS, 100, SoundLayer.AMBIENT_SOUND));
		player.sendPrivateText("You started mining.");
        notifyWorldAboutChanges();
        addEvent(new ImageEffectEvent("mining", true));
        notifyWorldAboutChanges();
	}
}
