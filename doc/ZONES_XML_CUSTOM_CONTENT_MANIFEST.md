# Stendhal Zone XMLs: Custom Content & 1.49 Sync Manifest

**Document Version:** 1.0  
**Purpose:** Exhaustive inventory of all custom zones, portals, entrances, NPCs, and expansion files in `EmpireOfThorns` compared to official `StendhalCurrent (v1.49)`.

---

## 1. Why WinMerge Marks the XML Files as Entirely Different

1. **Line Ending Differences (CRLF vs LF)**:
   - In `EmpireOfThorns`, several files (like `ados.xml`) use Windows `\r\n` (CRLF) line endings, while `StendhalCurrent` uses Unix `\n` (LF). Without *"Ignore Carriage Returns"* enabled in WinMerge, every line is highlighted yellow.
2. **Whitespace / Formatting Adjustments**:
   - Indentation differences and trailing spaces between official releases and local edits.
3. **Official 1.49 Refactorings**:
   - Configurator package paths were cleaned up (e.g. `games.stendhal.server.maps.FertileGrounds` moved out of `semos.city`).
   - Minor official additions: `int_fado_bank_1` in `fado.xml`, `tutorial_island` in `misc.xml`, and `TadpolesNPC` in `ados.xml`.

---

## 2. Dedicated Standalone Zone Files (100% Custom / Expansion)

These 18 XML files in `data/conf/zones/` are completely custom/standalone and do not exist in official Stendhal:

### A. Chinese Zones
* **`zmyzones.xml`** (Contains all Chinese maps and interior zones).

### B. Polish Expansion Zones
* **`krakow.xml`**
* **`warszawa.xml`**
* **`zakopane.xml`**
* **`koscielisko.xml`**
* **`labirynt.xml`**
* **`tatry.xml`**
* **`wieliczka.xml`**

### C. Faumonia & Custom Dungeon Expansions
* **`faumonia.xml`**
* **`wild_island.xml`**
* **`desert.xml`**
* **`dragon.xml`**
* **`dragon_knights.xml`**
* **`ados-0.xml`**, **`fado-0.xml`**, **`kalavan-0.xml`**, **`nalwor-0.xml`**, **`semos-0.xml`**

All of these are included via [`data/conf/zones.xml`](file:///c:/Projetos/Stendhal/EmpireOfThorns/data/conf/zones.xml).

---

## 3. Custom Zones Embedded Inside Standard Files

| File | Custom `<zone>` Entries in `EmpireOfThorns` |
| :--- | :--- |
| **`semos.xml`** | `<zone name="int_semos_real_guard_house" file="interiors/semos/guard_house.tmx">`<br>`<zone name="int_semos_skiller_room" file="interiors/semos/int_semos_skiller_room.tmx">`<br>`<zone name="int_semos_skiller_room_-1" file="interiors/semos/int_semos_skiller_room_-1.tmx">` |
| **`ados.xml`** | `<zone name="int_ados_bank_1" file="interiors/ados/bank_1.tmx">`<br>`<zone name="int_ados_stronghold" file="interiors/ados/stronghold.tmx">` |
| **`orril.xml`** | `<zone name="dead_hell" level="-3" file="Level -3/orril/dead_hell.tmx">`<br>`<zone name="dead_hell-2" level="-4" file="Level -4/orril/dead_hell.tmx">`<br>`<zone name="int_bandit_waterfall" file="interiors/zakopane/zion_ogrod.tmx">` |
| **`kikareukin.xml`** | `<zone name="5_kikareukin_cave_w" level="5" file="Level 5/kikareukin/cave_w.tmx">`<br>`<zone name="6_kikareukin_islands_e" level="6" file="Level 6/kikareukin/islands_e.tmx">` |
| **`misc.xml`** | `<zone name="int_universe" file="interiors/universe/universe.tmx">` |

---

## 4. Custom Portals & Entrances in Standard Zones

These are custom portals placed in standard game zones connecting to new dungeons, caves, and areas:

### `semos.xml` (11 Custom Entrances)
* **`0_semos_plains_n`**: Portal `to_semos_sky_farm` (x=70, y=120) $\rightarrow$ `1_semos_sky_farm_n:semos_sky_farm` *(Sky Farms)*.
* **`0_semos_city`**: Portal `sc_down01` (x=8, y=28) $\rightarrow$ `int_semos_bighole:bh_up2` *(Semos Big Hole / Great Cave)*.
* **`0_semos_plains_ne`**: Portal `sp_ne_down01` (x=31, y=51) $\rightarrow$ `int_semos_bighole:bh_up1`.
* **`0_semos_plains_n_e2`**: Portal `sp_ne2_mt_hole` (x=22, y=16) $\rightarrow$ `int_semos_mountain_hole:mt_hole_out`.
* **`0_semos_mountain_n_w2`**: Portal `downstairs` (x=90, y=22) $\rightarrow$ `-1_caves_castle:upstairs`.
* **`-2_semos_mine_e`**: Portal `to_mine_bighole01` (x=7, y=47) $\rightarrow$ `int_semos_mine_bighole:to_mine_e01`.
* **`-2_semos_mine_w2`**: Portal `stairstocaves` (x=88, y=75) $\rightarrow$ `int_semos_balrog_caves:stairstominew2`.
* **`-2_semos_catacombs`**: Portal `dragon_portal_1` (x=9, y=6).
* **`0_semos_village_w`**: Portal `guard_house_entrance` (x=16, y=34) $\rightarrow$ `int_semos_real_guard_house:entrance`.
* **`int_semos_guard_house`**: Portal `universe_entrance` (x=13, y=16) $\rightarrow$ `int_universe:universe_place`.
* **`int_semos_library`**: Portal `downstairs` (x=19, y=2) $\rightarrow$ `int_semos_library_-1:upstairs`.

### `fado.xml` (15 Sedah Underworld Entrances)
* **`-1_fado_great_cave_w`**: Portals to `int_sedah_house5`, `int_sedah_house6`, `int_sedah_storage`.
* **`-1_fado_great_cave_w2`**: Portals to `int_sedah_house0`, `int_sedah_house1`, `int_sedah_house2`, `int_sedah_barracks_0`, `int_sedah_house4_0`, `int_sedah_mausoleum`.
* **`-1_fado_great_cave_n_w2`**: Portal `downstairs` $\rightarrow$ `-2_fado_great_cave_n_w2:upstairs`.
* **Sedah Interiors**: Portals linking barracks and houses (`int_sedah_barracks_0/1/2`, `int_sedah_house4_0/1`).

### `ados.xml` (3 Custom Entrances)
* **`0_ados_city`**: Portal `adosCity_pirateDown1` (x=73, y=59) $\rightarrow$ `int_ados_pirate_caves:pirateUp1`.
* **`int_ados_bank`**: Portal `bank0Up1` (x=14, y=3) $\rightarrow$ `int_ados_bank_1:bank1Down0`.
* **`-1_ados_outside_w`**: Portal `downdragoncaves` (x=44, y=6) $\rightarrow$ `int_ados_dragon_caves:upoutsidew`.

### `orril.xml` (1 Custom Entrance)
* **`-2_orril_lich_palace`**: Portal `downstairs` (x=110, y=98) $\rightarrow$ `dead_hell:upstairs`.

---

## 5. Custom NPC / Zone Configurators in Standard Zones

| Zone | Custom Configurator in `EmpireOfThorns` |
| :--- | :--- |
| **`0_semos_city`** | `games.stendhal.server.maps.semos.city.PkRemoverNPC`<br>`games.stendhal.server.maps.semos.city.TrainingNPC` |
| **`int_semos_townhall`** | `games.stendhal.server.maps.semos.townhall.UrzednikSemosNPC` |
| **`int_semos_tavern_1`** | `games.stendhal.server.maps.pol.zakopane.home.JuhasNPC` |
| **`0_ados_city_n2`** | `games.stendhal.server.maps.ados.city.ElementalNPC2` |
| **`int_ados_town_hall_1`** | `games.stendhal.server.maps.ados.townhall.UrzednikAdosNPC` |
| **`-1_ados_caves`** | `games.stendhal.server.maps.ados.caves.PodroznikNPC` |
| **`0_fado_city`** | `games.stendhal.server.maps.fado.city.ElementalNPC` |
| **`int_fado_tavern`** | `games.stendhal.server.maps.fado.tavern.UrzednikFadoNPC` |
| **`0_kirdneh_city`** | `games.stendhal.server.maps.kirdneh.city.AdalbertNPC` |
| **`int_kirdneh_townhall`** | `games.stendhal.server.maps.kirdneh.townhall.UrzednikKirdnehNPC` |
| **`int_nalwor_royal_hall`** | `games.stendhal.server.maps.nalwor.royal.UrzednikNalworNPC` |
| **`int_constantines_villa_library`** | `games.stendhal.server.maps.orril.constantines_villa.LibrarianNPC` |

---

## 6. How to Safely Update Standard XML Files to Stendhal 1.49

Whenever updating from `StendhalCurrent`:
1. Copy the 14 standard files from `StendhalCurrent/data/conf/zones/` into `EmpireOfThorns/data/conf/zones/`.
2. Do **not** touch or delete the 18 standalone custom XML files (Chinese, Polish, Faumonia).
3. Ensure [`data/conf/zones.xml`](file:///c:/Projetos/Stendhal/EmpireOfThorns/data/conf/zones.xml) retains all `<group uri="..."/>` entries.
4. Using this manifest, re-insert:
   - The **11 custom `<zone>` blocks** into `semos.xml`, `ados.xml`, `orril.xml`, `kikareukin.xml`, and `misc.xml`.
   - The **custom portals** (Sky Farm, Semos Big Hole, Sedah caves, Pirate caves, Dead Hell).
   - The **custom NPC configurators** (`PkRemoverNPC`, `TrainingNPC`, Polish officials `Urzednik*`, `JuhasNPC`, etc.).
