package me.f0rant.f0ring.listener;

import me.f0rant.f0ring.F0Ring;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final F0Ring plugin;

    public PlayerJoinListener(F0Ring plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        plugin.getDatabaseManager().loadPlayer(player);
    }
}