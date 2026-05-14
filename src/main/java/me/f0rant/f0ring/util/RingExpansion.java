package me.f0rant.f0ring.util;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.f0rant.f0ring.F0Ring;
import me.f0rant.f0ring.manager.RingManager;
import me.f0rant.f0ring.model.RingData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RingExpansion extends PlaceholderExpansion {

    private final F0Ring plugin;
    private final RingManager ringManager;

    public RingExpansion(F0Ring plugin, RingManager ringManager) {
        this.plugin = plugin;
        this.ringManager = ringManager;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "f0ring";
    }

    @Override
    public @NotNull String getAuthor() {
        return "f0rant";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) return "";

        RingData activeRing = ringManager.getActiveRings().get(player.getUniqueId());

        // %f0ring_status% - Enabled/Disabled
        if (params.equalsIgnoreCase("status")) {
            String path = (activeRing != null) ? "placeholders.status-on" : "placeholders.status-off";
            return ChatUtil.format(plugin.getConfig().getString(path));
        }

        // %f0ring_name% - Name of choosen ring (e.g. "Heart Aura")
        if (params.equalsIgnoreCase("name")) {
            if (activeRing == null) {
                return ChatUtil.format(plugin.getConfig().getString("placeholders.no-ring", "&7None"));
            }
            return ChatUtil.format(activeRing.name());
        }

        // %f0ring_shape% - Name of the equipped shape (e.g. "Heart Aura")
        if (params.equalsIgnoreCase("shape")) {
            if (activeRing == null) {
                return ChatUtil.format(plugin.getConfig().getString("placeholders.no-shape", "&7None"));
            }
            return ChatUtil.format(plugin.getConfig().getString("shape-names." + activeRing.shape().toUpperCase(), activeRing.shape()));
        }

        // %f0ring_total% - Number of all available rings on the server
        if (params.equalsIgnoreCase("total")) {
            return String.valueOf(ringManager.getAllRings().size());
        }

        // %f0ring_unlocked% - Number of rings the player has access to (has permission)
        if (params.equalsIgnoreCase("unlocked")) {
            int unlockedCount = 0;
            for (RingData ring : ringManager.getAllRings()) {
                boolean hasPermission = ring.permissions().isEmpty(); 
                for (String perm : ring.permissions()) {
                    if (player.hasPermission(perm)) {
                        hasPermission = true;
                        break;
                    }
                }
                if (hasPermission) {
                    unlockedCount++;
                }
            }
            return String.valueOf(unlockedCount);
        }

        return null;
    }
}