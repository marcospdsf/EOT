/* $Id$ */
/***************************************************************************
 *                  (C) Copyright 2003-2023 - Marauroa                     *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.server.core.rule.defaultruleset;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import games.stendhal.common.parser.ExpressionType;
import games.stendhal.common.parser.WordList;
import games.stendhal.server.core.config.CreatureGroupsXMLLoader;
import games.stendhal.server.core.config.ItemGroupsXMLLoader;
import games.stendhal.server.core.config.ShopGroupsXMLLoader;
import games.stendhal.server.core.config.SpellGroupsXMLLoader;
import games.stendhal.server.core.rule.EntityManager;
import games.stendhal.server.entity.Entity;
import games.stendhal.server.entity.creature.Creature;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.spell.Spell;

/**
 * entity manager for the default ruleset.
 *
 * @author Matthias Totz
 */
public class DefaultEntityManager implements EntityManager {

	/** the logger instance. */
	private static final Logger LOGGER = Logger.getLogger(DefaultEntityManager.class);

	/** maps the tile ids to the classes. */
	private final Map<String, String> idToClass;

	/** maps the item names to the actual item enums. */
	private final Map<String, DefaultItem> classToItem;

	/** lists all creatures that are being used at least once. */
	private final Map<String, Creature> createdCreature;

	/** lists all items that are being used at least once . */
	private final Map<String, Item> createdItem;

	/** lists all spell that are being used at least once . */
	private final Map<String, Spell> createdSpell;

	/**
	 * lists all loaded default spells that are usable
	 */
	private final Map<String, DefaultSpell> nameToSpell;

	private LowerCaseMap<DefaultCreature> classToCreature;
	private final LowerCaseMap<String> creatureAliases;
	private final LowerCaseMap<String> itemAliases;

	/** no public constructor. */
	public DefaultEntityManager() {
		idToClass = new HashMap<String, String>();
		createdCreature = new HashMap<String, Creature>();
		createdItem = new HashMap<String, Item>();
		createdSpell = new HashMap<String, Spell>();
		classToItem = new HashMap<String, DefaultItem>();
		nameToSpell = new HashMap<String, DefaultSpell>();
		creatureAliases = new LowerCaseMap<String>();
		itemAliases = new LowerCaseMap<String>();
		buildItemTables();
		buildSpellTables();
		buildCreatureTables();

		// initialize shops via XML
		new ShopGroupsXMLLoader("/data/conf/shops.xml").load();
	}

	/**
	 * builds the spell tables
	 */
	private void buildSpellTables() {
		try {
			final SpellGroupsXMLLoader loader = new SpellGroupsXMLLoader(new URI("/data/conf/spells.xml"));
			List<DefaultSpell> loadedDefaultSpells = loader.load();
			for (DefaultSpell defaultSpell : loadedDefaultSpells) {
				addSpell(defaultSpell);
			}
		} catch (Exception e) {
			LOGGER.error("spells.xml could not be loaded", e);
		}
	}

	/**
	 * Build the items tables
	 */
	private void buildItemTables() {
		try {
			final ItemGroupsXMLLoader loader = new ItemGroupsXMLLoader(new URI("/data/conf/items.xml"));
			final List<DefaultItem> items = loader.load();

			for (final DefaultItem item : items) {
				final String clazz = item.getItemName();

				if (classToItem.containsKey(clazz)) {
					LOGGER.warn("Repeated item name: " + clazz);
				}

				classToItem.put(clazz, item);

				final String subclazz = item.getItemSubclass();
				if (subclazz != null && !subclazz.isEmpty() && !subclazz.equalsIgnoreCase(clazz)) {
					itemAliases.put(subclazz, clazz);
				}

				String typeString = ExpressionType.OBJECT;

				if (item.getItemClass().equals("food")) {
					typeString += ExpressionType.SUFFIX_FOOD;
				} else if (item.getItemClass().equals("drink")) {
					typeString += ExpressionType.SUFFIX_FOOD;
					typeString += ExpressionType.SUFFIX_FLUID;
				}

				WordList.getInstance().registerName(item.getItemName(), typeString);
			}
		} catch (final Exception e) {
			LOGGER.error("items.xml could not be loaded", e);
		}
	}

	/**
	 * Build the creatures tables
	 */
	private void buildCreatureTables() {
		classToCreature = new LowerCaseMap<DefaultCreature>();

		final CreatureGroupsXMLLoader loader = new CreatureGroupsXMLLoader("/data/conf/creatures.xml");
		final List<DefaultCreature> creatures = loader.load();

		for (final DefaultCreature creature : creatures) {
			final String id = creature.getTileId();
			final String clazz = creature.getCreatureName();

			if (classToCreature.containsKey(clazz)) {
				LOGGER.warn("Repeated creature name: " + clazz);
			}

			if (!creature.verifyItems(this)) {
				LOGGER.warn("Items dropped by creature name: " + clazz + " doesn't exists");
			}

			classToCreature.put(clazz, creature);
			idToClass.put(id, clazz);

			final String subclass = creature.getCreatureSubclass();
			if (subclass != null && !subclass.isEmpty() && !subclass.equalsIgnoreCase(clazz)) {
				creatureAliases.put(subclass, clazz);
			}

			WordList.getInstance().registerName(creature.getCreatureName(), ExpressionType.SUBJECT);
		}
	}

	@Override
	public boolean addItem(final DefaultItem item) {
		final String clazz = item.getItemName();

		if (classToItem.containsKey(clazz)) {
			LOGGER.warn("Repeated item name: " + clazz);
			return false;
		}

		classToItem.put(clazz, item);

		return true;
	}

	@Override
	public boolean addCreature(final DefaultCreature creature) {
		final String id = creature.getTileId();
		final String clazz = creature.getCreatureName();

		if (classToCreature.containsKey(clazz)) {
			LOGGER.warn("Repeated creature name: " + clazz);
		}

		if (!creature.verifyItems(this)) {
			LOGGER.warn("Items dropped by creature name: " + clazz + " doesn't exists");
		}
		classToCreature.put(clazz, creature);
		idToClass.put(id, clazz);

		return true;
	}

	/**
	 * For manually populating the creature list.
	 *
	 * Useful for tests.
	 */
	@Override
	public void populateCreatureList() {
		for (final DefaultCreature cr: getDefaultCreatures()) {
			createdCreature.put(cr.getCreatureName(), cr.getCreature());
		}
	}

	@Override
	public boolean addSpell(DefaultSpell spell) {
		if(nameToSpell.containsKey(spell.getName())) {
			LOGGER.warn("Repeated spell name: "+ spell.getName());
		}
		nameToSpell.put(spell.getName(), spell);
		return true;
	}

	/**
	 * @return a list of all Creatures that are instantiated.
	 */
	@Override
	public Collection<Creature> getCreatures() {
		return createdCreature.values();
	}

	/**
	 * @return a list of all Items that are instantiated.
	 */
	@Override
	public Collection<Item> getItems() {
		return createdItem.values();
	}

	/**
	 * returns the entity or <code>null</code> if the id is unknown.
	 *
	 * @param clazz
	 *            RPClass
	 * @return the new created entity or null if class not found
	 *
	 * @throws NullPointerException
	 *             if clazz is <code>null</code>
	 */
	@Override
	public Entity getEntity(final String clazz) {
		if (clazz == null) {
			throw new IllegalArgumentException("entity class is null");
		}

		Entity entity;
		// Lookup the id in the creature table
		entity = getCreature(clazz);
		if (entity != null) {
			return entity;
		}

		// Lookup the id in the item table
		entity = getItem(clazz);
		if (entity != null) {
			return entity;
		}

		return null;
	}

	/**
	 * @param tileset
	 * @param id
	 * @return the creature or <code>null</code> if the id is unknown.
	 */
	private String normalizeTileKey(final String tileset, final int id) {
		String base = tileset;
		if (base.contains("logic/creature/")) {
			base = base.substring(base.indexOf("logic/creature/") + "logic/creature/".length());
		}
		base = base.replace(".png", "");

		// 1. Direct standard check
		String key = tileset + ":" + id;
		if (idToClass.containsKey(key)) {
			return key;
		}
		String relKey = "../../tileset/logic/creature/" + base + ".png:" + id;
		if (idToClass.containsKey(relKey)) {
			return relKey;
		}

		// 2. Legacy 0.60 / Polish / Custom sheet aliases
		if ("small_animal".equals(base)) {
			switch (id) {
				case 0: return "../../tileset/logic/creature/rodent.png:5"; // mouse
				case 1: return "../../tileset/logic/creature/rodent.png:1"; // rat
				case 2: return "../../tileset/logic/creature/rodent.png:2"; // giant rat
				case 3: return "../../tileset/logic/creature/rodent.png:3"; // cave rat
				case 4: return "../../tileset/logic/creature/rodent.png:4"; // sewer rat
				default: return "../../tileset/logic/creature/rodent.png:1";
			}
		} else if ("farm_animal".equals(base)) {
			switch (id) {
				case 0: return "../../tileset/logic/creature/fowl.png:1"; // chicken
				case 2: return "../../tileset/logic/creature/farm_animal.png:10"; // ram / sheep
				case 5: return "../../tileset/logic/creature/farm_animal.png:9"; // piglet
				case 6: return "../../tileset/logic/creature/farm_animal.png:3"; // cow
				case 8: return "../../tileset/logic/creature/farm_animal.png:4"; // goat
				default: return "../../tileset/logic/creature/farm_animal.png:3";
			}
		} else if ("animal".equals(base)) {
			switch (id) {
				case 17: return "../../tileset/logic/creature/reptile.png:0"; // grass snake
				case 18: return "../../tileset/logic/creature/reptile.png:1"; // snake
				case 23: return "../../tileset/logic/creature/animal.png:24"; // fox
				case 25: return "../../tileset/logic/creature/animal.png:20"; // wolf
				default: return relKey;
			}
		} else if ("gnome_patrol".equals(base)) {
			switch (id) {
				case 0: return "../../tileset/logic/creature/gnome.png:0"; // gnome
				case 1: return "../../tileset/logic/creature/gnome.png:1"; // mage gnome
				default: return "../../tileset/logic/creature/gnome.png:0";
			}
		} else if ("giant_human".equals(base)) {
			switch (id) {
				case 5: return "../../tileset/logic/creature/giant_human.png:0"; // giant elder
				case 6: return "../../tileset/logic/creature/giant_human.png:1"; // giant
				case 7: return "../../tileset/logic/creature/giant_human.png:3"; // black giant
				case 11: return "../../tileset/logic/creature/giant_human.png:8"; // imperial general giant
				case 12: return "../../tileset/logic/creature/giant_human.png:9"; // master giant
				default: return "../../tileset/logic/creature/giant_human.png:1";
			}
		} else if ("huge_animal".equals(base) || "huger_animal".equals(base)) {
			switch (id) {
				case 0: return "../../tileset/logic/creature/dragon.png:0"; // green dragon
				case 1: return "../../tileset/logic/creature/dragon.png:1"; // blue dragon
				case 2: return "../../tileset/logic/creature/dragon.png:2"; // red dragon
				case 3: return "../../tileset/logic/creature/huge_animal.png:3"; // chaos green dragonrider
				case 5: return "../../tileset/logic/creature/dragon.png:4"; // black dragon
				case 6: return "../../tileset/logic/creature/dragon.png:5"; // flying golden dragon
				case 7: return "../../tileset/logic/creature/dragon.png:6"; // twin head dragon
				case 9: return "../../tileset/logic/creature/dragon.png:7"; // rysin dragon
				case 10: return "../../tileset/logic/creature/huge_animal.png:14"; // black twin head dragon
				case 11: return "../../tileset/logic/creature/huge_animal.png:11"; // chaos red dragonrider
				case 12: return "../../tileset/logic/creature/huge_animal.png:14"; // black twin head dragon
				default: return "../../tileset/logic/creature/dragon.png:0";
			}
		} else if ("hybrid".equals(base)) {
			return "../../tileset/logic/creature/animal.png:20"; // wolf
		} else if ("mythical_animal".equals(base)) {
			return "../../tileset/logic/creature/animal.png:9"; // deer
		} else if ("red_oni".equals(base)) {
			return "../../tileset/logic/creature/goblin.png:0";
		} else if ("undead".equals(base)) {
			switch (id) {
				case 2: return "../../tileset/logic/creature/undead_specter.png:3"; // death
				case 3: return "../../tileset/logic/creature/undead_skeleton.png:0"; // skeleton
				case 5: return "../../tileset/logic/creature/undead_zombie.png:1"; // zombie
				case 6: return "../../tileset/logic/creature/undead.png:1"; // death knight
				case 7: return "../../tileset/logic/creature/mummy.png:0"; // mummy
				case 9: return "../../tileset/logic/creature/mummy.png:0"; // mummy
				case 10: return "../../tileset/logic/creature/mummy.png:1"; // royal mummy
				default: return "../../tileset/logic/creature/undead_skeleton.png:0";
			}
		} else if ("undead_fallen".equals(base)) {
			switch (id) {
				case 1: return "../../tileset/logic/creature/undead_fallen.png:1";
				case 2: return "../../tileset/logic/creature/undead_fallen.png:2";
				case 3: return "../../tileset/logic/creature/undead_fallen.png:3";
				case 7: return "../../tileset/logic/creature/undead_fallen.png:7";
				case 8: return "../../tileset/logic/creature/undead_fallen.png:8";
				default: return "../../tileset/logic/creature/undead_fallen.png:3"; // fallen warrior
			}
		} else if ("my_monsters_01".equals(base)) {
			return "../../tileset/logic/creature/my_monsters_01.png:0";
		} else if ("giant_animal".equals(base)) {
			return "../../tileset/logic/creature/rodent.png:2";
		} else if ("fairy".equals(base)) {
			return "../../tileset/logic/creature/pol_zjawy.png:1";
		} else if ("faumonia_alp".equals(base) || "faumonia_alpy".equals(base)) {
			switch (id) {
				case 0: return "../../tileset/logic/creature/pol_zbojnicy_gorscy.png:0";
				case 1: return "../../tileset/logic/creature/pol_zbojnicy_gorscy.png:1";
				case 2: return "../../tileset/logic/creature/pol_zbojnicy_gorscy.png:4";
				case 3: return "../../tileset/logic/creature/pol_zbojnicy_gorscy.png:5";
				default: return "../../tileset/logic/creature/pol_zbojnicy_gorscy.png:0";
			}
		} else if ("faumonia_arena".equals(base)) {
			switch (id) {
				case 0: return "../../tileset/logic/creature/kobold.png:0";
				case 1: return "../../tileset/logic/creature/human_evil.png:2";
				case 2: return "../../tileset/logic/creature/elf.png:1";
				case 3: return "../../tileset/logic/creature/faumonia_duchy.png:3";
				case 4: return "../../tileset/logic/creature/faumonia_duchy.png:4";
				case 5: return "../../tileset/logic/creature/madaram.png:1";
				case 6: return "../../tileset/logic/creature/human_evil.png:2";
				case 7: return "../../tileset/logic/creature/human_imperial.png:2";
				case 8: return "../../tileset/logic/creature/elf.png:1";
				case 9: return "../../tileset/logic/creature/human_imperial.png:1";
				case 10: return "../../tileset/logic/creature/human_imperial.png:6";
				case 11: return "../../tileset/logic/creature/oni.png:1";
				case 12: return "../../tileset/logic/creature/dark_elf.png:1";
				case 13: return "../../tileset/logic/creature/dark_elf.png:2";
				case 14: return "../../tileset/logic/creature/pol_magowie.png:3";
				case 15: return "../../tileset/logic/creature/pol_magowie.png:4";
				case 16: return "../../tileset/logic/creature/pol_magowie.png:5";
				case 17: return "../../tileset/logic/creature/pol_magowie.png:6";
				case 18: return "../../tileset/logic/creature/pol_magowie.png:7";
				case 19: return "../../tileset/logic/creature/amazon.png:1";
				case 20: return "../../tileset/logic/creature/human_soldier.png:3";
				case 21: return "../../tileset/logic/creature/amazon.png:4";
				case 22: return "../../tileset/logic/creature/human_soldier.png:0";
				case 23: return "../../tileset/logic/creature/pol_magowie.png:8";
				default: return "../../tileset/logic/creature/human_soldier.png:0";
			}
		} else if ("faumonia_mgly".equals(base)) {
			return "../../tileset/logic/creature/faumonia_duchy.png:0";
		} else if ("faumonia_huge_animal".equals(base)) {
			return "../../tileset/logic/creature/huge_animal.png:8";
		} else if ("faumonia_smierci".equals(base)) {
			return "../../tileset/logic/creature/undead_specter.png:3";
		}

		return key;
	}

	@Override
	public Creature getCreature(final String tileset, final int id) {
		final String clazz = idToClass.get(normalizeTileKey(tileset, id));
		if (clazz == null) {
			return null;
		}

		return getCreature(clazz);
	}

	/**
	 * @param clazz
	 * @return the creature or <code>null</code> if the clazz is unknown.
	 *
	 * @throws NullPointerException
	 *             if clazz is <code>null</code>
	 */
	@Override
	public Creature getCreature(final String clazz) {
		if (clazz == null) {
			throw new IllegalArgumentException("entity class is null");
		}

		// Lookup the clazz in the creature table
		DefaultCreature creature = classToCreature.get(clazz);
		if (creature == null) {
			final String alias = creatureAliases.get(clazz);
			if (alias != null) {
				creature = classToCreature.get(alias);
			}
		}
		if (creature != null) {
			final String realName = creature.getCreatureName();
			if (createdCreature.get(realName) == null) {
				createdCreature.put(realName, creature.getCreature());
			}
			return creature.getCreature();
		}

		return null;
	}

	/**
	 * @param clazz
	 * @return the DefaultCreature or <code>null</code> if the clazz is
	 *         unknown.
	 *
	 * @throws NullPointerException
	 *             if clazz is <code>null</code>
	 */
	@Override
	public DefaultCreature getDefaultCreature(final String clazz) {
		if (clazz == null) {
			throw new IllegalArgumentException("entity class is null");
		}

		// Lookup the clazz in the creature table
		DefaultCreature creature = classToCreature.get(clazz);
		if (creature == null) {
			final String alias = creatureAliases.get(clazz);
			if (alias != null) {
				creature = classToCreature.get(alias);
			}
		}
		return creature;
	}

	/** @param tileset
	 * @param id
	 * @return true if the Entity is a creature. */
	@Override
	public boolean isCreature(final String tileset, final int id) {
		final String clazz = idToClass.get(normalizeTileKey(tileset, id));
		if (clazz == null) {
			return false;
		}

		return isCreature(clazz);
	}

	/** @param clazz
	 * @return true if the Entity is a creature . */
	@Override
	public boolean isCreature(final String clazz) {
		if (clazz == null) {
			throw new IllegalArgumentException("entity class is null");
		}

		return classToCreature.containsKey(clazz) || creatureAliases.containsKey(clazz);
	}

	/** @param clazz
	 * @return true if the Entity is a creature. */
	@Override
	public boolean isItem(final String clazz) {
		if (clazz == null) {
			throw new IllegalArgumentException("entity class is null");
		}

		return classToItem.containsKey(clazz) || itemAliases.containsKey(clazz);
	}

	/**
	 * @param clazz
	 * @return the item or <code>null</code> if the clazz is unknown.
	 *
	 * @throws NullPointerException
	 *             if clazz is <code>null</code>
	 */
	@Override
	public Item getItem(final String clazz) {
		if (clazz == null) {
			throw new IllegalArgumentException("entity class is null");
		}

		// Lookup the clazz in the item table
		DefaultItem item = classToItem.get(clazz);
		if (item == null) {
			final String alias = itemAliases.get(clazz);
			if (alias != null) {
				item = classToItem.get(alias);
			}
		}
		if (item != null) {
			final String realName = item.getItemName();
			if (createdItem.get(realName) == null) {
				createdItem.put(realName, item.getItem());
			}
			return item.getItem();
		}

		return null;
	}

	@Override
	public Spell getSpell(String spell) {
		if(spell == null) {
			throw new IllegalArgumentException("spell name is null");
		}
		DefaultSpell defaultSpell = nameToSpell.get(spell);
		if (defaultSpell != null) {
			Spell spellEntity = defaultSpell.getSpell();
			if(!createdSpell.containsKey(spell)) {
				createdSpell.put(spell, spellEntity);
			}
			return spellEntity;
		}
		return null;
	}

	@Override
	public boolean isSpell(String spellName) {
		return nameToSpell.containsKey(spellName);
	}

	@Override
	public Collection<Spell> getSpells() {
		return createdSpell.values();
	}

	@Override
	public Collection<DefaultCreature> getDefaultCreatures() {
		return classToCreature.values();
	}

	@Override
	public Collection<DefaultItem> getDefaultItems() {
		return classToItem.values();
	}

	public Collection<String> getConfiguredItems() {
		return classToItem.keySet();
	}

	@Override
	public Collection<String> getConfiguredSpells() {
		return nameToSpell.keySet();
	}
}
