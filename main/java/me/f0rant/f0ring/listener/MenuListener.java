package me.f0rant.f0ring.listener;

import me.f0rant.f0ring.manager.RingManager;
import me.f0rant.f0ring.model.RingData;
import me.f0rant.f0ring.util.ChatUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MenuListener implements Listener {

    private final JavaPlugin plugin;
    private final RingManager ringManager;

    public MenuListener(JavaPlugin plugin, RingManager ringManager) {
        this.plugin = plugin;
        this.ringManager = ringManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        String title = ChatUtil.format(plugin.getConfig().getString("gui.title", "Wybierz Ringo"));
        if (!e.getView().getTitle().equals(title)) return;

        e.setCancelled(true);
        if (!(e.getWhoClicked() instanceof Player player)) return;

        int slot = e.getRawSlot();

        if (slot == 49) {
            ringManager.removeActiveRing(player);
            player.sendMessage(ChatUtil.format(plugin.getConfig().getString("messages.ring-removed")));
            player.closeInventory();
            return;
        }

        RingData ring = ringManager.getRingBySlot(slot);
        if (ring != null) {
            
            boolean hasPermission = ring.permissions().isEmpty();
            for (String perm : ring.permissions()) {
                if (player.hasPermission(perm)) {
                    hasPermission = true;
                    break;
                }
            }

            if (!hasPermission) {
                player.sendMessage(ChatUtil.format(plugin.getConfig().getString("messages.no-permission")));
                return;
            }

            ringManager.setActiveRing(player, ring);
            player.sendMessage(ChatUtil.format(plugin.getConfig().getString("messages.ring-equipped")));
            player.closeInventory();
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        ringManager.removeActiveRing(e.getPlayer());
    }
}