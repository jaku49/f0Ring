package me.f0rant.f0ring.task;

import me.f0rant.f0ring.manager.RingManager;
import me.f0rant.f0ring.model.RingData;
import me.f0rant.f0ring.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;

public class ParticleTask extends BukkitRunnable {

    private final RingManager ringManager;
    private int rainbowTick = 0;

    public ParticleTask(JavaPlugin plugin, RingManager ringManager) {
        this.ringManager = ringManager;
    }

    @Override
    public void run() {
        rainbowTick = (rainbowTick + 5) % 360;

        for (Map.Entry<UUID, RingData> entry : ringManager.getActiveRings().entrySet()) {
            Player player = Bukkit.getPlayer(entry.getKey());
            if (player == null || !player.isOnline()) continue;

            RingData ring = entry.getValue();
            double radius = 1.0;
            Location loc = player.getLocation();

            switch (ring.shape().toUpperCase()) {
                case "DOUBLE_RING":
                    spawnDoubleRing(player, loc, radius, ring);
                    break;
                case "HELIX":
                    spawnHelix(player, loc, radius, ring);
                    break;
                case "STAR":
                    spawnStar(player, loc, radius, ring);
                    break;
                case "CIRCLE":
                default:
                    spawnCircle(player, loc, radius, ring);
                    break;
            }
        }
    }

    private void spawnCircle(Player p, Location center, double r, RingData ring) {
        int particles = 30;
        for (int i = 0; i < particles; i++) {
            double angle = 2 * Math.PI * i / particles;
            double x = Math.cos(angle) * r;
            double z = Math.sin(angle) * r;
            center.add(x, 0.1, z);
            spawnSingleParticle(p, center, i, ring);
            center.subtract(x, 0.1, z);
        }
    }

    private void spawnDoubleRing(Player p, Location center, double r, RingData ring) {
        spawnCircle(p, center, r, ring);
        Location upper = center.clone().add(0, 0.5, 0);
        int particles = 20;
        for (int i = 0; i < particles; i++) {
            double angle = -2 * Math.PI * i / particles;
            double x = Math.cos(angle) * (r * 0.75);
            double z = Math.sin(angle) * (r * 0.75);
            upper.add(x, 0, z);
            spawnSingleParticle(p, upper, i, ring);
            upper.subtract(x, 0, z);
        }
    }

    private void spawnHelix(Player p, Location center, double r, RingData ring) {
        double yOffset = (Math.sin(Math.toRadians(rainbowTick * 4)) * 0.5) + 0.6;
        int particles = 25;
        for (int i = 0; i < particles; i++) {
            double angle = 2 * Math.PI * i / particles + Math.toRadians(rainbowTick * 2);
            double x = Math.cos(angle) * r;
            double z = Math.sin(angle) * r;
            center.add(x, yOffset, z);
            spawnSingleParticle(p, center, i, ring);
            center.subtract(x, yOffset, z);
        }
    }

    private void spawnStar(Player p, Location center, double r, RingData ring) {
        int particles = 40;
        for (int i = 0; i < particles; i++) {
            double angle = 2 * Math.PI * i / particles;
            double currentRadius = r * (0.6 + 0.4 * Math.cos(5 * angle));
            double x = Math.cos(angle) * currentRadius;
            double z = Math.sin(angle) * currentRadius;
            center.add(x, 0.1, z);
            spawnSingleParticle(p, center, i, ring);
            center.subtract(x, 0.1, z);
        }
    }

    private void spawnSingleParticle(Player player, Location loc, int index, RingData ring) {
        Color dustColor = Color.WHITE;
        switch (ring.type().toUpperCase()) {
            case "SOLID":
                dustColor = ChatUtil.hexToColor(ring.color1());
                break;
            case "DUAL":
                dustColor = ChatUtil.hexToColor((index % 2 == 0) ? ring.color1() : ring.color2());
                break;
            case "RAINBOW":
                java.awt.Color awtColor = java.awt.Color.getHSBColor((rainbowTick + (index * 10)) / 360f, 1f, 1f);
                dustColor = Color.fromRGB(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue());
                break;
        }
        Particle.DustOptions dustOptions = new Particle.DustOptions(dustColor, 1.0F);
        player.getWorld().spawnParticle(Particle.DUST, loc, 1, 0, 0, 0, 0, dustOptions);
    }
}