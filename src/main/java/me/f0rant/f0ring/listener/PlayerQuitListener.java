package me.f0rant.f0ring.listener;

import me.f0rant.f0ring.F0Ring;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final F0Ring plugin;

    public PlayerQuitListener(F0Ring plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        
        plugin.getDatabaseManager().savePlayer(player);
        
        plugin.getRingManager().removeActiveRing(player);
    }
}