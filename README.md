# 💍 f0Ring | Premium Particle Cosmetics

![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21-green?style=for-the-badge&logo=minecraft)
![Java Version](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![Author](https://img.shields.io/badge/Author-f0rant-blue?style=for-the-badge)

**f0Ring** is a high-quality, lightweight cosmetic plugin for Minecraft 1.21 that adds stunning, customizable particle rings under players' feet. Designed with performance and aesthetics in mind, it's the perfect addition for VIP/Premium ranks on your server.

---

## ✨ Features

* **Dynamic GUI Menu:** A sleek, 54-slot interface with a gray glass frame and 24 pre-defined ring variants for easy selection.
* **4 Distinct Particle Styles:**
    * ⭕ **Circle:** A classic, elegant ring.
    * ♾️ **Double Ring:** Two intertwined layers of particles.
    * 🌀 **Helix:** A swirling vortex effect.
    * ⭐ **Star:** A sharp, five-pointed mystical star.
* **Advanced Color System:**
    * **Solid:** Clean, single-color effects.
    * **Dual:** Two-tone alternating patterns.
    * **Rainbow:** Dynamic, shifting RGB spectrum.
* **Full HEX Support:** Complete compatibility with `&#RRGGBB` formats in messages, lores, and even particle colors.
* **Optimized Performance:** Built using asynchronous tasks to ensure zero impact on server TPS.
* **Smart Permissions:** Support for multiple permission nodes per effect (e.g., granting access via `f0ring.red` OR `f0ring.vip`).
* **Auto-Updater:** Integrated asynchronous version checker via GitHub to keep your plugin up to date.

---

## 📸 Media & Showcase

> [!TIP]
> *Add your own screenshots or GIFs here to showcase the effects!*

| Style: Helix (Rainbow) | Style: Star (Solid HEX) |
| :---: | :---: |
| ![Placeholder GIF](https://via.placeholder.com/300x200?text=Helix+Preview) | ![Placeholder GIF](https://via.placeholder.com/300x200?text=Star+Preview) |

---

## 🛠 Commands & Permissions

| Command | Description | Permission |
| :--- | :--- | :--- |
| `/f0ring` | Opens the main Ring Selection GUI | `f0ring.use` |
| `/f0ring reload` | Reloads the configuration file | `f0ring.admin` |
| `/f0ring alloff` | Disables effects for all online players | `f0ring.admin` |

---

## ⚙️ Configuration

The plugin is highly customizable. Below is an example snippet of the `config.yml`:

```yaml
# f0Ring Configuration
# Support for HEX: &#FFFFFF

settings:
  prefix: "&#5dade2&lf0Ring &8» "
  check-updates: true

rings:
  red_star:
    display-name: "&#ff0000Star Effect"
    style: STAR
    color-type: SOLID
    color-1: "&#ff0000"
    permissions:
      - "f0ring.red"
      - "f0ring.vip"
    gui-slot: 10
