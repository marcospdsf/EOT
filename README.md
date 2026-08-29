# ⚔️ Empire of Thorns (EOT)

**Empire of Thorns** is an enhanced, feature-rich MMORPG built upon the open-source **Stendhal** engine. It unites and modernizes content from three distinct branches:
1. **Official Stendhal** (Upstream core engine & world)
2. **Chinese Edition (YRPG v0.80)** (Custom quests, items, and mechanics)
3. **Polish Edition (Polska Online v0.60)** (Expanded maps, dungeons, and regional content)

---

## 🌟 Key Features & Custom Additions (Not in Vanilla Stendhal)

### 🏪 1. Grand Exchange & Harold's Trading Center
* **In-Game Trading Hub (`HaroldMarketWindow`)**:
  * **Market (Sell Offers)**: Browse and purchase player-listed items.
  * **My Offers**: Create, manage, and cancel sell orders.
  * **My Requests**: Place custom purchase requests with automated escrow and partial fulfillments.
  * **Order Book**: View global purchase demands and fulfill them directly.
  * **Collection Box / Deliveries**: Claim sold goods, purchased orders, and refunded gold asynchronously.
* **Web Market Browser**: Integrated website trade portal with live search, spotlight cards, and paginated/scrollable market tables.

### 🛍️ 2. NPC Merchant Shop GUI System
* **Modern Shop Interface (`NpcShopWindow`)**:
  * Dedicated split-panel window with dynamic **Buy** and **Sell** tabs.
  * Instant search filter for fast item discovery.
  * Numeric amount input + synchronized **Slider (1–1000)** and quick quantity buttons (`1`, `5`, `10`, `50`, `100`, `Max`).
* **Server-Side Safety & Capacity Verification**:
  * Proximity enforcement (must stay within $\le 4$ tiles).
  * Strict inventory capacity verification: non-stackable purchases recursively verify available slots across all bags/pouches and abort with clear feedback before taking coins.
* **Smart Shopper Session & NPC Pause**:
  * Right-clicking any merchant NPC in the 2D world opens the shop directly via the context menu.
  * Multi-player tracking: Wandering/patrolling NPCs stop and face the shopper while the interface is open, and automatically resume their patrol route once all shoppers close their windows.

### 🗺️ 3. Merged World & Regional Expansions
* **Multi-Version Content Integration**:
  * Combined maps, interiors, and dungeons from the Chinese and Polish server forks into a single cohesive world.
  * Additional monster spawners, crazy dungeons, underground labyrinths, and secret passages.
  * Translations and localized dialogue integration.

### 🛡️ 4. Expanded Equipment & Slots
* Extended slot configurations across client and server (support for secondary rings `finger2`, specialized pouches, quiver, and utility containers).

---

## 🚀 Getting Started & Building

### Requirements
* **Java Development Kit (JDK 8 or 17)**
* **Apache Ant**

### Compiling Server and Client
To check XML data definitions and compile both server and client JARs:
```bash
ant checkxml compile
```
This builds:
* `build/lib/stendhal-server-1.49.5.jar` (Server)
* `build/lib/stendhal-1.49.5.jar` (Client)

### Running the Server
```bash
ant run-server
```

### Running the Client
```bash
ant run-client
```
Alternatively, launch `stendhal-starter.jar` or run from the build directory.

---

## 📜 License
Empire of Thorns is open-source software licensed under the **GNU General Public License v2 (GPLv2)**.












