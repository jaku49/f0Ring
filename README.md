<h1 align="center">✨ f0Ring ✨</h1>

<p align="center">
  Premium aesthetic particle rings for modern Minecraft servers
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Minecraft-1.21-blue.svg" alt="Minecraft 1.21">
  <img src="https://img.shields.io/badge/Java-21-blue.svg" alt="Java 21">
  <img src="https://img.shields.io/badge/Platform-Spigot%20%7C%20Paper-blue" alt="Spigot | Paper">
  <img src="https://img.shields.io/badge/Architecture-Modular-blue" alt="Modular Architecture">
  <img src="https://img.shields.io/badge/Status-Active%20Development-blue" alt="Active Development">
</p>

<h4 align="center">
  The most advanced and optimized particle ring system for your Minecraft server.
</h4>

---

## 🌟 Overview

**f0Ring** is a premium cosmetic plugin designed for modern Minecraft servers. It allows players to equip dynamic particle rings rendered in real time with optimized asynchronous calculations.

Built with **Java 21** for **Spigot** and **Paper**, the plugin focuses on smooth visuals, scalability, and low performance impact, making it ideal for hubs, lobbies, and competitive servers.

Player data is stored using **MySQL** or **SQLite**, so saved effects and preferences can persist across sessions.

---

## ✨ Key Features

- 🎨 In-game ring creator with HEX color selection.
- ⚡ Optimized asynchronous particle rendering.
- 💾 Persistent player settings and saved effects.
- 🧩 Fully configurable GUI system.
- ⚙️ Visibility and preference settings for players.
- 🔌 PlaceholderAPI support.
- 🌈 RGB / HEX color support.
- 🛠 Built-in update checker.
- 🗄 MySQL / SQLite data storage.

---

## 🔌 PlaceholderAPI Support

f0Ring includes built-in **PlaceholderAPI** support, which makes it easy to display ring data inside scoreboards, menus, tab lists, chat formats, and other plugin integrations.

### Available Placeholders

- `%f0ring_status%`  
  Shows whether the player's ring is enabled or disabled.

- `%f0ring_name%`  
  Shows the name of the currently selected ring.

- `%f0ring_shape%`  
  Shows the name of the equipped shape.

- `%f0ring_total%`  
  Shows the total number of available rings on the server.

- `%f0ring_unlocked%`  
  Shows how many rings the player has access to based on permissions.

This makes it easy to build dynamic GUI items and player-facing messages based on ring progress and access.

---

## 📸 Gallery

### Main Menu

<p align="center">
  <img src="https://raw.githubusercontent.com/jaku49/f0Ring/refs/heads/main/img/f0ring_mainmenu.png" width="700" alt="Main Menu">
</p>

### Ring Creator

<p align="center">
  <img src="https://raw.githubusercontent.com/jaku49/f0Ring/refs/heads/main/img/f0ring_creator.png" width="700" alt="Ring Creator">
</p>

### Visual Effects

<p align="center">
  <img src="https://raw.githubusercontent.com/jaku49/f0Ring/refs/heads/main/img/f0ring_effects.png" width="700" alt="Visual Effects">
</p>

### Preferences Menu

<p align="center">
  <img src="https://raw.githubusercontent.com/jaku49/f0Ring/refs/heads/main/img/f0ring_preferences.png" width="700" alt="Preferences Menu">
</p>

---

## 🌀 Particle Styles & Shapes

The plugin supports multiple animated particle styles rendered using optimized geometry calculations.

Available styles:

- `CIRCLE`
- `DOUBLE_RING`
- `STAR`
- `HELIX`
- `WAVE`
- `HEART`
- `DNA`
- `ORBITALS`

---

## 🌈 Advanced Color System

f0Ring supports full HEX formatting (`&#RRGGBB`) for both messages and particle effects.

### Available Color Modes

- **Solid** — single constant color.
- **Dual** — alternating dual-color pattern.
- **Rainbow** — animated RGB transition.

---

## 🧠 Performance

f0Ring was designed for large multiplayer servers.

### Optimization Features

- Asynchronous particle calculations.
- Smart render-distance filtering.
- Reduced main-thread workload.
- Efficient mathematical rendering engine.
- Optimized for hubs and cosmetic systems.

---

## 📚 Commands & Permissions

| Command | Description | Permission |
| :--- | :--- | :--- |
| `/f0ring` or `/ring` | Opens the main GUI | `f0ring.use` |
| `/f0ring reload` | Reloads the configuration | `f0ring.admin` |
| `/f0ring alloff` | Disables effects globally | `f0ring.admin` |
| *(GUI option)* | Access to ring creator | `f0ring.creator` |

---

## ⚙ Configuration Example

```yml
rings:
  premium_star:
    display-name: "&#ff5555&lStar Ring"
    style: STAR
    color-type: RAINBOW
    permissions:
      - "f0ring.star"
      - "f0ring.premium"
    gui-slot: 13
```

---

## 🚀 Installation

1. Download the latest release.
2. Put `f0Ring.jar` into the `/plugins/` folder.
3. Restart the server.
4. Configure `config.yml`.
5. Run `/ring reload`.

---

## 🚀 Roadmap

### Completed

- [x] Core particle engine
- [x] Async rendering
- [x] Java 21 support
- [x] HEX / RGB support
- [x] Dynamic GUI system
- [x] Visibility settings
- [x] PlaceholderAPI support
- [x] MySQL / SQLite storage

### Planned

- [ ] WorldEdit support
- [ ] CombatLog support
- [ ] Multi-ring support
- [ ] Multi-language support
- [ ] Density settings
- [ ] Developer API

---

## 📜 License

Distributed under the **MIT License**.

---

<p align="center">
  Designed for premium Minecraft environments. Created by <b>f0rant (jaku49)</b>.
</p>
