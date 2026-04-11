package me.f0rant.f0ring.manager;

import me.f0rant.f0ring.model.RingData;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RingManager {

    private final JavaPlugin plugin;
    private final Map<UUID, RingData> activeRings = new HashMap<>();
    private final Map<Integer, RingData> ringsBySlot = new HashMap<>();

    public RingManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadRings() {
        ringsBySlot.clear();
        plugin.reloadConfig(); // Odświeżamy plik z dysku przy reloadzie
        ConfigurationSection ringsSection = plugin.getConfig().getConfigurationSection("rings");
        
        if (ringsSection != null) {
            for (String key : ringsSection.getKeys(false)) {
                
                List<String> perms = ringsSection.getStringList(key + ".permissions");
                if (perms.isEmpty() && ringsSection.contains(key + ".permission")) {
                    perms = new ArrayList<>();
                    perms.add(ringsSection.getString(key + ".permission"));
                }

                RingData ring = new RingData(
                        key,
                        ringsSection.getInt(key + ".slot"),
                        Material.valueOf(ringsSection.getString(key + ".material", "STONE")),
                        ringsSection.getString(key + ".name", key),
                        perms,
                        ringsSection.getString(key + ".type", "SOLID"),
                        ringsSection.getString(key + ".shape", "CIRCLE"),
                        ringsSection.getString(key + ".color1", "#FFFFFF"),
                        ringsSection.getString(key + ".color2", "#000000")
                );
                ringsBySlot.put(ring.slot(), ring);
            }
        }
    }

    public void clearAllActiveRings() {
        activeRings.clear();
    }

    public RingData getRingBySlot(int slot) {
        return ringsBySlot.get(slot);
    }

    public Collection<RingData> getAllRings() {
        return ringsBySlot.values();
    }

    public void setActiveRing(Player player, RingData ring) {
        activeRings.put(player.getUniqueId(), ring);
    }

    public void removeActiveRing(Player player) {
        activeRings.remove(player.getUniqueId());
    }

    public Map<UUID, RingData> getActiveRings() {
        return activeRings;
    }
}