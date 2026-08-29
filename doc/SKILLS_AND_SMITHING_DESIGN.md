# Stendhal Skill Concepts & Crafting / Smithing System Design

This design document outlines RuneScape-inspired skills adapted to Stendhal's 2D engine and economy, with detailed design specifications for **Smithing**, **Tempering**, and **Gem Dust Lifesteal Crafting**.

---

## 1. RuneScape-Inspired Skills for Stendhal

| Skill | Category | Mechanics in Stendhal | Existing Assets & Synergy |
| :--- | :--- | :--- | :--- |
| **Herblore / Alchemy** | Artisan | Brew health/mana restoration, speed potions (1.2x/1.5x), antipoison, and weapon oils from collected herbs, creature glands, blood vials, and water vials. | Apothecary labs (Semos), plant/flower nodes, monster organs. |
| **Fishing** | Gathering | Click water nodes with rods and nets to catch raw trout, salmon, perch, and sea treasures. Directly supplies Cooking. | Water bodies (Semos River, Athor, Ados), existing fish items. |
| **Crafting** | Artisan | **Leatherworking:** Turn animal and dragon hides into ranger/mage armor and bags.<br>**Jewelry & Gemology:** Cut raw gems, forge rings/amulets, and crush gems into dust for infusions. | Animal pelts, dragon leathers, raw gems, gold/silver bars. |
| **Fletching** | Artisan | Cut logs from Woodcutting into shafts, carve bows/crossbows, and fletch specialized arrows (fire, poisoned, armor-piercing) using feathers and metals. | Wood source trees, small/large feathers, bows/quivers. |
| **Slayer** | Combat/Support | Guildmasters (e.g. Guardhouses) assign creature kill quotas. Grants Slayer XP, specialized monster drop access, and entry to restricted Slayer dungeons. | Extensive bestiary, boss encounters, guardhouse NPCs. |
| **Thieving / Agility** | Gathering/Utility | **Thieving:** Pickpocket bandits/rogues and lockpick dungeon treasure chests.<br>**Agility:** Unlock shortcuts across rivers, fences, and cliffs to bypass long detours. | City houses, locked chests, bandit camps, terrain obstacles. |
| **Farming** | Gathering | Plant seeds in farm allotments, water and tend to crops over growth ticks to harvest fresh ingredients for Cooking and Herblore. | Farm areas (Semos/Ados), crops (grain, sugar cane, carrots). |
| **Prayer / Faith** | Support | Offer or bury monster bones at temple shrines and altars for temporary blessings (damage mitigation, stat padding, auto-healing) and karma. | Temples/churches, karma system, monster bones/relics. |

---

## 2. Smithing Skill & Economy Design

### Economic Reality in Stendhal
Unlike massive MMO economies, Stendhal operates with a smaller, dedicated player base where **scarcity drives value**. 
* Allowing players to forge rare items (like a *Golden Dagger*) from raw ores alone would instantly devalue rare monster drops.
* Upgrades must have controlled caps to prevent game-breaking power creep in Stendhal’s tight combat math.

---

### A. Incremental Equipment Tempering (% Upgrade System)
Tempering acts as a continuous ore/bar sink for Miners and Smiths without inflating base drop stats overnight.

* **The Process:**
  * Take a weapon or armor piece to an anvil along with refined metal bars (Iron, Silver, Gold, Mithril, Shadow Iron) + Coal.
  * Each tempering attempt adds **1% to 5% Tempering Progress** (scaled by Smithing Level vs Item Tier).
  * Reaching **100% Progress** permanently upgrades the item to the next tier:
    * **Tier 1:** 100% progress $\rightarrow$ Milestone Perk 1
    * **Tier 2:** 200% progress $\rightarrow$ Milestone Perk 2
    * **Tier 3:** 300% progress $\rightarrow$ Milestone Perk 3 (Maximum Hard Cap)
* **Safe Failure Mechanic:**
  * Failed high-tier attempts never destroy the underlying item. They only consume the ore/fuel or reset partial progress within that tier.

---

### B. Per-Weapon Custom Refining Milestones
Rather than generic flat stats, milestones are tailored to individual weapon archetypes to give every weapon unique identity and build variety:

#### 1. Heavy Weapons (e.g. Chaos Hammer, Warhammers, Great Axes)
* **100% Upgrade:** `+1 ATK`
* **200% Upgrade:** `+2 ATK`
* **300% Upgrade:** **-1 Attack Delay (Swings 1 tick faster)** $\rightarrow$ Game-changing milestone for heavy weapon builds.

#### 2. Fast Weapons & Daggers (e.g. Golden Dagger, Stiletto, Short Sword)
* **100% Upgrade:** `+1 ATK`
* **200% Upgrade:** `+1 DEF` (Parrying)
* **300% Upgrade:** `+5% Critical Strike Chance` or `Armor Penetration`

#### 3. Ranged Weapons (e.g. Elven Bow, Heavy Crossbow)
* **100% Upgrade:** `+1 RATK`
* **200% Upgrade:** `+2 RATK`
* **300% Upgrade:** `+1 Max Firing Range`

#### 4. Defensive Armor & Shields
* **100% Upgrade:** `+1 DEF`
* **200% Upgrade:** `+2 DEF`
* **300% Upgrade:** `-10% Weight` or `+5% Elemental Damage Resistance`

---

### C. Base Forging (Relic/Core-Gated Scarcity)
To ensure bosses and monster hunting remain lucrative:
* **Common Gear (Iron/Steel tiers):** Can be forged freely from standard bars $\rightarrow$ used for leveling Smithing and providing starter kits.
* **Rare/Endgame Gear (Golden/Mithril/Shadow tiers):** Cannot be forged from raw bars alone. Requires a **Broken Core / Relic Component** that only drops from bosses + a large quantity of refined bars.
  * *Example:* Forging a *Golden Dagger* requires **1x Dull Golden Blade** (rare boss drop) + **15x Gold Bars** + **Smithing Level 60**.

---

### D. Master-Craft Exclusive Equipment (Smithing-Only Gear)
To give dedicated blacksmiths true prestige and high economic demand, certain powerful weapons and armor sets **cannot be dropped by any monster or bought from any NPC shop**:
* **Exclusivity:** Obtainable exclusively through high-level Smithing (e.g. Levels 70, 80, 90+).
* **Crafting Requirements:** Require complex combinations of high-tier metals (Mithril, Shadow Iron, Gold) combined with rare beast components (Dragon Claws, Unicorn Horns, Demon Horns) and significant quantities of Coal.
* **Examples:**
  * **`Dreadforged Claymore`** (Smithing Lv 75): 10x Shadow Iron Bars + 5x Mithril Bars + 2x Black Dragon Claws. High base damage and unique innate armor penetration.
  * **`Aegis of the Mountain Core`** (Smithing Lv 80): 15x Mithril Bars + 10x Gold Bars + 2x Obsidian Bars + 5x Sulfur. Heavy shield with high block and passive fire defense.
  * **`Masterwork Adamantine Plate`** (Smithing Lv 90): End-game full armor set offering balanced defensive stats and reduced movement weight penalty.
* **Economic Value:** Creates an exclusive service market where combat players commission master blacksmiths to forge top-tier custom gear.

---

### E. Consumable Blacksmithing (Whetstones & Polishes)
Provides Blacksmiths with everyday high-turnover consumable goods:
* **Whetstones / Honing Oils:** Temporary **+1 or +2 ATK** buff for 30–60 minutes.
* **Armor Polishes:** Temporary **+1 DEF** or elemental resistance buff for dungeon raids.
* **Master Lockpicks & Heavy Tools:** Tools required for dungeon chests and high-tier gathering.

---

## 3. Crafting Skill: Gem Crushing & Lifesteal Infusion

Stendhal natively supports the `lifesteal` attribute on items in `Item.java`. Creating a **Gem Dust** mechanic provides an essential, high-demand sink for surplus mined gems.

---

### A. Gem Crushing Mechanics
Using a *Mortar & Pestle*, Crafters crush cut or uncut gems into **Gem Dust** of that tier.

| Gem Tier | Gem Dust Item | Infusion Progress / Use |
| :--- | :--- | :--- |
| **Common** | `Emerald Dust` | +0.01% Infusion |
| **Uncommon** | `Sapphire Dust` / `Ruby Dust` | +0.02% Infusion |
| **Rare** | `Carbuncle Dust` | +0.035% Infusion |
| **Very Rare** | `Obsidian Dust` | +0.05% Infusion |
| **Ultra Rare** | `Diamond Dust` | +0.10% Infusion |

---

### B. Infusion Progression & Caps
* Applying Gem Dust to a weapon increases its **Lifesteal Infusion Meter**.
* Reaching **100% Infusion** permanently adds **+0.1% Lifesteal** to the weapon.
* **Hard Cap:** Capped at **+0.5% or +1.0% Max Total Lifesteal** per weapon.
* **Balance & Persistence:**
  * Small percentage sustain (+0.1% to +0.5%) rewards dedicated end-game grinders without invalidating healers, potions, or food.
  * Infused items utilize Marauroa's `persistent = 1` attribute so all custom lifesteal and tempering bonuses persist permanently across server restarts and player logins.
