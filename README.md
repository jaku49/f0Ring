<h1 align="center">💍 f0Ring</h1>

<p align="center">
  Premium Aesthetic Particle Rings for Modern Minecraft Servers
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Minecraft-1.21-blue.svg" alt="Minecraft 1.21">
  <img src="https://img.shields.io/badge/Java-21-blue.svg" alt="Java 21">
  <img src="https://img.shields.io/badge/Platform-Spigot%20%7C%20Paper-blue" alt="Spigot | Paper">
  <img src="https://img.shields.io/badge/Architecture-Modular-blue" alt="Modular Architecture">
  <img src="https://img.shields.io/badge/Status-Active%20Development-blue" alt="Active Development">
</p>

---

## ✨ Overview

**f0Ring** is a high-end cosmetic plugin designed for competitive and hub servers. It allows players to decorate themselves with dynamic, high-performance particle rings that follow their movement in real time.

Built with **Java 21**, the plugin is highly optimized and offers deep customization through a modular system. It is a great perk for VIP and Premium ranks, making players stand out in the crowd.

---

## 🎨 Particle Selection GUI

<p align="center">
  <img src="https://raw.githubusercontent.com/jaku49/f0Ring/refs/heads/main/img/javaw_q2zb9qrfRa.png" width="600" alt="Particle Selection GUI">
</p>

### Professional Interface

The plugin features a large **54-slot GUI** designed for clarity:

- **24 predefined variants** ready to use.
- **Visual feedback** with gray glass framing and intuitive icons.
- **One-click activation** to instantly equip or change your style.

---

## 🌀 Particle Styles & Shapes

The plugin uses advanced geometry to render effects in 4 unique styles. Each style is calculated asynchronously to help keep performance high.

| Style | Visualisation | Description | Visual Impact |
| :--- | :---: | :--- | :--- |
| **Circle** | <img src="https://raw.githubusercontent.com/jaku49/f0Ring/refs/heads/main/img/iln4OGg.png" width="75"> | A clean, single-layer ring around the feet. | Minimalist and clean |
| **Double Ring** | <img src="https://raw.githubusercontent.com/jaku49/f0Ring/refs/heads/main/img/J4g4Pwv.png" width="75"> | Two intertwined circles at different heights. | Premium and visible |
| **Helix** | <img src="https://raw.githubusercontent.com/jaku49/f0Ring/refs/heads/main/img/5LB6t3X.png" width="75"> | A swirling vortex of particles moving upwards. | Dynamic and energetic |
| **Star** | <img src="https://raw.githubusercontent.com/jaku49/f0Ring/refs/heads/main/img/MBAzpOc.png" width="75"> | A perfect five-pointed mystical star shape. | Unique and prestigious |

---

## 🌈 Advanced Color System

**f0Ring** supports the latest Minecraft features, including full **HEX support** (`&#RRGGBB`) for messages and particles.

### Three Coloring Modes

- **Solid:** A constant, clean color of your choice.
- **Dual:** Two colors alternating in the pattern for a striped look.
- **Rainbow:** A smooth, dynamic RGB transition that cycles through colors.

---

## 🏗 Developer Info & Architecture

The project follows a **modular architecture** to ensure maintainability and scalability, making it easy to add new styles or features.

- **Language:** Java 21, using modern features like `records`.
- **Project structure:**
  - `manager/`: Core logic and data handling.
  - `task/`: Asynchronous particle rendering engine.
  - `model/`: Data objects and Java records.
  - `util/`: Formatting, HEX parsing, and helper classes.
- **Built-in updater:** Async version checker via GitHub `version.txt`.

---

## 📋 Commands & Permissions

| Command | Description | Permission |
| :--- | :--- | :--- |
| `/f0ring` or `/ring`| Opens the main ring selection GUI | `f0ring.use` |
| `/f0ring reload` | Reloads the configuration file | `f0ring.admin` |
| `/f0ring alloff` | Disables effects for all online players | `f0ring.admin` |

---

## ⚙ Configuration

Fully customizable `config.yml` with support for custom ring definitions:

```yaml
# f0Ring Configuration Snippet
# Support for HEX: &#FFFFFF

settings:
  prefix: "&#5dade2&lf0Ring &8» "
  check-updates: true

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

## 🚀 Roadmap

### Current Progress

- [x] Core Particle Engine (Circle, Double, Helix, Star)
- [x] Asynchronous Task Management
- [x] Java 21 and Minecraft 1.21 Support
- [x] Full HEX/RGB Support
- [x] Dynamic 54-slot GUI System
- [x] Async Version Updater

### Planned Features

- [ ] **Automatically**: Resume after reconnecting to the server, provided the player still has the necessary permissions.
- [ ] **MySQL/MariaDB Support:** Cross-server synchronization for networks.
- [ ] **Multi-Ring System:** Ability to stack multiple effects simultaneously.
- [ ] **Density Toggle:** Command or GUI option to change particle amount for low-end PCs.
- [ ] **Localization:** Built-in support for Polish (PL) and German (DE).
- [ ] **Developer API:** Custom events for other plugins to hook into ring changes.

---

## 📜 License

Distributed under the **MIT License**.

---

## 📈 Why f0Ring?

- **Competitive performance:** All particle calculations are offloaded from the main server thread.
- **Premium aesthetics:** Designed to look like a high-end product.
- **Modern tech stack:** No legacy code, built for the future of Minecraft.
- **Scalable:** Easy to add new custom rings via simple config entries.

---

<p align="center">
  Designed for premium Minecraft environments. Created by <b>f0rant (jaku49)</b>.
</p>
