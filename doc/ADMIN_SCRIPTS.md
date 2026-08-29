# Stendhal & Empire of Thorns: Admin Script Commands Reference

This document lists custom and standard in-game administrator scripts.
Scripts can be executed in-game by server administrators using `/script <ScriptName>.class [parameters]`.

---

## 1. Rate & Multiplier Management Scripts

### `/script SetXpMultiplier.class <multiplier>`
* **Description**: Changes the monster experience points reward multiplier globally and immediately at runtime without restarting or rebuilding the server.
* **Parameters**: `<multiplier>` (float, e.g. `1.0`, `2.0`, `3.0`, `5.0`).
* **Example**:
  ```text
  /script SetXpMultiplier.class 2.0
  ```
* **Effect**: Doubles all combat XP awarded from killing creatures and broadcasts a server announcement to all online players.
* **Server Boot Configuration (`server.ini`)**:
  ```ini
  xp_multiplier = 2.0
  ```

---

### `/script SetDropMultiplier.class <multiplier>`
* **Description**: Changes the creature item drop rate (chance) multiplier globally and immediately at runtime without restarting or rebuilding the server.
* **Parameters**: `<multiplier>` (float/double, e.g. `1.0`, `2.0`, `5.0`, `10.0`).
* **Example**:
  ```text
  /script SetDropMultiplier.class 2.0
  ```
* **Effect**: Doubles the drop chance for all creature loot items and broadcasts a server announcement.
* **Server Boot Configuration (`server.ini`)**:
  ```ini
  drop_multiplier = 2.0
  ```

---

### `/script SetDropQuantityMultiplier.class <multiplier>`
* **Description**: Changes the stack drop quantity multiplier for stackable items (money, arrows, potions, reagents, ores, etc.) dropped by creatures.
* **Parameters**: `<multiplier>` (float/double $\ge 1.0$, e.g. `2.0`, `3.0`).
* **Example**:
  ```text
  /script SetDropQuantityMultiplier.class 2.0
  ```
* **Effect**: If a creature drops 10 Money, it will now drop 20 Money.
* **Server Boot Configuration (`server.ini`)**:
  ```ini
  drop_quantity_multiplier = 2.0
  ```

---

## 2. Player Movement & Speed Scripts

### `/script SetPlayerSpeed.class [player_name] <speed>`
* **Description**: Sets the movement speed of yourself or another online player. Supports multi-tile step movement per turn.
* **Parameters**:
  * `<speed>`: Movement speed (double, e.g. `1.0` = normal, `1.2` = fast, `1.5` = very fast, `2.0` = 2 tiles per turn, `3.0` = 3 tiles per turn).
  * `[player_name]`: (Optional) Name of target player. If omitted, applies to the admin executing the command.
* **Examples**:
  ```text
  /script SetPlayerSpeed.class 2.0
  /script SetPlayerSpeed.class marcospdsf 1.5
  ```

---

## 3. Speed Consumable Items

### `lesser speed potion`
* **Item Name**: `lesser speed potion`
* **Item Type**: `drink` (Consumable)
* **Effect**: Grants `1.2` movement speed for 60 seconds (1 minute).
* **Command to Summon**:
  ```text
  /create "lesser speed potion" 5
  ```

### `greater speed potion`
* **Item Name**: `greater speed potion`
* **Item Type**: `drink` (Consumable)
* **Effect**: Grants `1.5` movement speed for 60 seconds (1 minute).
* **Command to Summon**:
  ```text
  /create "greater speed potion" 5
  ```

---

## 4. General Useful Admin Scripts & Commands

| Command | Usage | Description |
| :--- | :--- | :--- |
| `/create "<item_name>" [amount]` | `/create "money" 1000` | Spawns an item directly into your inventory. |
| `/teleport <zone> [x] [y]` | `/teleport 0_semos_city 30 40` | Teleports you to specified zone coordinates. |
| `/script SpawnCreature.class <x> <y>` | `/script SpawnCreature.class 25 30` | Forces a creature spawn point to spawn. |
| `/script TeleportAllPlayers.class <zone> <x> <y>` | `/script TeleportAllPlayers.class 0_semos_city 30 40` | Teleports all active players to a location. |
| `/script UpdateHallOfFame.class` | `/script UpdateHallOfFame.class` | Immediately recalculates and populates Hall of Fame & Best Player archive tables for today. |
| `/script SaveAllPlayers.class` | `/script SaveAllPlayers.class` | Immediately forces database persistence of all players. |
| `/script LogoutPlayer.class <player_name>` | `/script LogoutPlayer.class John` | Disconnects and logs out a specific player. |
| `/script SetWelcomeText.class "<message>"` | `/script SetWelcomeText.class "Welcome to Empire of Thorns!"` | Sets global MOTD/welcome announcement. |

---

## 5. Summary of `server.ini` Settings
Add these keys to your `server.ini` file for automatic persistent configuration:
```ini
# Multipliers
xp_multiplier = 2.0
drop_multiplier = 2.0
drop_quantity_multiplier = 2.0
```
