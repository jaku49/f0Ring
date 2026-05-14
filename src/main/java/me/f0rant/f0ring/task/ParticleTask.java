package me.f0rant.f0ring.task;

import me.f0rant.f0ring.manager.RingManager;
import me.f0rant.f0ring.model.PlayerData;
import me.f0rant.f0ring.model.RingData;
import me.f0rant.f0ring.util.ChatUtil;
import me.f0rant.f0ring.util.FastMath; 
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ParticleTask extends BukkitRunnable {

    private final JavaPlugin plugin;
    private final RingManager ringManager;
    private int tick = 0;

    public ParticleTask(JavaPlugin plugin, RingManager ringManager) {
        this.plugin = plugin;
        this.ringManager = ringManager;
    }

    @Override
    public void run() {
        tick++;
        if (tick > 360) tick = 0;

        for (Player p : Bukkit.getOnlinePlayers()) {
            RingData ring = ringManager.getPlayerData(p).getActiveRing();
            if (ring == null || p.isDead() || p.getGameMode() == GameMode.SPECTATOR) continue;

            Location loc = p.getLocation();

            List<Player> validViewers = new ArrayList<>();
            for (Player viewer : loc.getWorld().getPlayers()) {
                if (viewer.getLocation().distanceSquared(loc) < 1024) { 
                    PlayerData viewerData = ringManager.getPlayerData(viewer);
                    boolean isSelf = viewer.getUniqueId().equals(p.getUniqueId());

                    if (isSelf && viewerData.isHideSelf()) continue;
                    if (!isSelf && viewerData.isHideOthers()) continue;

                    validViewers.add(viewer);
                }
            }

            if (validViewers.isEmpty()) continue;

            double radius = 1.0;

            switch (ring.shape().toUpperCase()) {
                case "CIRCLE": spawnCircle(p, loc, radius, ring, validViewers); break;
                case "DOUBLE_RING": spawnDoubleRing(p, loc, radius, ring, validViewers); break;
                case "STAR": spawnStar(p, loc, radius, ring, validViewers); break;
                case "HELIX": spawnHelix(p, loc, radius, ring, validViewers); break;
                case "WAVE": spawnWave(p, loc, radius, ring, validViewers); break;
                case "HEART": spawnHeart(p, loc, radius, ring, validViewers); break;
                case "DNA": spawnDNA(p, loc, radius, ring, validViewers); break;
                case "ORBITALS": spawnOrbitals(p, loc, radius, ring, validViewers); break;
            }
        }
    }

    private void spawnSingleParticle(Player p, Location loc, int index, RingData ring, List<Player> viewers) {
        Color color = Color.WHITE;

        if (ring.type().equalsIgnoreCase("SOLID")) {
            color = ChatUtil.hexToColor(ring.color1());
        } else if (ring.type().equalsIgnoreCase("DUAL")) {
            color = (index % 2 == 0) ? ChatUtil.hexToColor(ring.color1()) : ChatUtil.hexToColor(ring.color2());
        } else if (ring.type().equalsIgnoreCase("RAINBOW")) {
            float hue = (tick + index * 5) % 360 / 360.0f;
            java.awt.Color awtColor = java.awt.Color.getHSBColor(hue, 1.0f, 1.0f);
            color = Color.fromRGB(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue());
        }

        Particle.DustOptions dust = new Particle.DustOptions(color, 1.0f);

        for (Player viewer : viewers) {
            viewer.spawnParticle(Particle.DUST, loc, 1, 0, 0, 0, 0, dust);
        }
    }


    private void spawnCircle(Player p, Location center, double r, RingData ring, List<Player> viewers) {
        int particles = 20;
        for (int i = 0; i < particles; i++) {
            double angle = 2 * Math.PI * i / particles + Math.toRadians(tick * 2);
            double x = FastMath.cos(angle) * r;
            double z = FastMath.sin(angle) * r;
            center.add(x, 0.1, z);
            spawnSingleParticle(p, center, i, ring, viewers);
            center.subtract(x, 0.1, z);
        }
    }

    private void spawnDoubleRing(Player p, Location center, double r, RingData ring, List<Player> viewers) {
        int particles = 15;
        for (int i = 0; i < particles; i++) {
            double angle1 = 2 * Math.PI * i / particles + Math.toRadians(tick * 3);
            double x1 = FastMath.cos(angle1) * (r * 0.6);
            double z1 = FastMath.sin(angle1) * (r * 0.6);
            center.add(x1, 0.1, z1);
            spawnSingleParticle(p, center, i, ring, viewers);
            center.subtract(x1, 0.1, z1);

            double angle2 = 2 * Math.PI * i / particles - Math.toRadians(tick * 2);
            double x2 = FastMath.cos(angle2) * r;
            double z2 = FastMath.sin(angle2) * r;
            center.add(x2, 0.1, z2);
            spawnSingleParticle(p, center, i + 1, ring, viewers);
            center.subtract(x2, 0.1, z2);
        }
    }

    private void spawnStar(Player p, Location center, double r, RingData ring, List<Player> viewers) {
        int points = 5;
        for (int i = 0; i < points; i++) {
            double angle = Math.toRadians((360.0 / points) * i + tick * 3);
            double x = FastMath.cos(angle) * r;
            double z = FastMath.sin(angle) * r;
            center.add(x, 0.1, z);
            spawnSingleParticle(p, center, i, ring, viewers);
            center.subtract(x, 0.1, z);
        }
    }

    private void spawnHelix(Player p, Location center, double r, RingData ring, List<Player> viewers) {
        int particles = 10;
        for (int i = 0; i < particles; i++) {
            double height = (tick % 40) / 20.0 + (i * 0.2);
            if (height > 2.0) height -= 2.0;
            double angle = height * Math.PI * 2 + Math.toRadians(tick * 4);
            double x = FastMath.cos(angle) * r;
            double z = FastMath.sin(angle) * r;
            center.add(x, height, z);
            spawnSingleParticle(p, center, i, ring, viewers);
            center.subtract(x, height, z);
        }
    }

    private void spawnWave(Player p, Location center, double r, RingData ring, List<Player> viewers) {
        double currentRadius = r + (FastMath.sin(Math.toRadians(tick * 4)) * 0.5);
        int particles = 30;
        for (int i = 0; i < particles; i++) {
            double angle = 2 * Math.PI * i / particles;
            double x = FastMath.cos(angle) * currentRadius;
            double z = FastMath.sin(angle) * currentRadius;
            center.add(x, 0.1, z);
            spawnSingleParticle(p, center, i, ring, viewers);
            center.subtract(x, 0.1, z);
        }
    }

    private void spawnHeart(Player p, Location center, double r, RingData ring, List<Player> viewers) {
        int particles = 40;
        for (int i = 0; i < particles; i++) {
            double t = 2 * Math.PI * i / particles;
            double x = 16 * Math.pow(FastMath.sin(t), 3);
            double z = -(13 * FastMath.cos(t) - 5 * FastMath.cos(2 * t) - 2 * FastMath.cos(3 * t) - FastMath.cos(4 * t));
            
            x = x * 0.05 * r;
            z = z * 0.05 * r;

            center.add(x, 0.1, z);
            spawnSingleParticle(p, center, i, ring, viewers);
            center.subtract(x, 0.1, z);
        }
    }

    private void spawnDNA(Player p, Location center, double r, RingData ring, List<Player> viewers) {
        double yOffset = (FastMath.sin(Math.toRadians(tick * 4)) * 0.5) + 0.6;
        int particles = 20;
        
        for (int i = 0; i < particles; i++) {
            double angle1 = 2 * Math.PI * i / particles + Math.toRadians(tick * 2);
            double x1 = FastMath.cos(angle1) * r;
            double z1 = FastMath.sin(angle1) * r;
            center.add(x1, yOffset, z1);
            spawnSingleParticle(p, center, i, ring, viewers);
            center.subtract(x1, yOffset, z1);

            double angle2 = angle1 + Math.PI;
            double x2 = FastMath.cos(angle2) * r;
            double z2 = FastMath.sin(angle2) * r;
            center.add(x2, yOffset, z2);
            spawnSingleParticle(p, center, i + 1, ring, viewers);
            center.subtract(x2, yOffset, z2);
        }
    }

    private void spawnOrbitals(Player p, Location center, double r, RingData ring, List<Player> viewers) {
        int particles = 15;
        for (int i = 0; i < particles; i++) {
            double angle = 2 * Math.PI * i / particles + Math.toRadians(tick * 3);
            
            double x1 = FastMath.cos(angle) * r;
            double z1 = FastMath.sin(angle) * r;
            
            double x2 = FastMath.cos(angle) * r;
            double y2 = FastMath.sin(angle) * r * 0.5 + 1.0;
            
            double z3 = FastMath.cos(angle) * r;
            double y3 = FastMath.sin(angle) * r * 0.5 + 1.0;

            center.add(x1, 0.1, z1);
            spawnSingleParticle(p, center, i, ring, viewers);
            center.subtract(x1, 0.1, z1);

            center.add(x2, y2, 0);
            spawnSingleParticle(p, center, i+1, ring, viewers);
            center.subtract(x2, y2, 0);
            
            center.add(0, y3, z3);
            spawnSingleParticle(p, center, i+2, ring, viewers);
            center.subtract(0, y3, z3);
        }
    }
}