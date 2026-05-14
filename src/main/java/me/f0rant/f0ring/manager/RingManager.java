package me.f0rant.f0ring.manager;

import me.f0rant.f0ring.F0Ring;
import me.f0rant.f0ring.model.CreatorSession;
import me.f0rant.f0ring.model.PlayerData;
import me.f0rant.f0ring.model.RingData;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

public class RingManager {

    private final F0Ring plugin;
    private final Map<Integer, RingData> ringsBySlot = new HashMap<>();
    private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();
    private final Map<UUID, CreatorSession> creatorSessions = new HashMap<>();

    public RingManager(F0Ring plugin) {
        this.plugin = plugin;
    }

    public void loadRings() {
        ringsBySlot.clear();
        plugin.reloadConfig();
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("rings");
        if (section == null) return;

        for (String key : section.getKeys(false)) {
            try {
                List<String> perms = section.getStringList(key + ".permissions");
                RingData ring = new RingData(
                        key,
                        section.getInt(key + ".slot"),
                        Material.valueOf(section.getString(key + ".material", "STONE")),
                        section.getString(key + ".name"),
                        perms,
                        section.getString(key + ".type"),
                        section.getString(key + ".shape"),
                        section.getString(key + ".color1", "#FFFFFF"),
                        section.getString(key + ".color2", "#FFFFFF")
                );
                ringsBySlot.put(ring.slot(), ring);
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to load ring: " + key);
            }
        }
    }

    public PlayerData getPlayerData(Player player) {
        return playerDataMap.computeIfAbsent(player.getUniqueId(), k -> new PlayerData(null, false, false, true));
    }

    public void setActiveRing(Player player, RingData ring) {
        getPlayerData(player).setActiveRing(ring);
    }

    public void removeActiveRing(Player player) {
        getPlayerData(player).setActiveRing(null);
    }

    public Map<UUID, RingData> getActiveRings() {
        Map<UUID, RingData> active = new HashMap<>();
        playerDataMap.forEach((uuid, data) -> {
            if (data.getActiveRing() != null) active.put(uuid, data.getActiveRing());
        });
        return active;
    }

    public void handleJoin(Player player) {
    PlayerData data = getPlayerData(player);
    if (!data.isResumeRingo()) {
        data.setActiveRing(null);
    }
}

    public Collection<RingData> getAllRings() { return ringsBySlot.values(); }
    public RingData getRingBySlot(int slot) { return ringsBySlot.get(slot); }
    public void clearAllActiveRings() { playerDataMap.values().forEach(d -> d.setActiveRing(null)); }
    public CreatorSession getSession(Player p) { return creatorSessions.computeIfAbsent(p.getUniqueId(), k -> new CreatorSession()); }
}