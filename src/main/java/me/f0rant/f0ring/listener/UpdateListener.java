package me.f0rant.f0ring.listener;

import me.f0rant.f0ring.util.ChatUtil;
import me.f0rant.f0ring.util.Updater;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class UpdateListener implements Listener {

    private final JavaPlugin plugin;
    private final Updater updater;

    public UpdateListener(JavaPlugin plugin, Updater updater) {
        this.plugin = plugin;
        this.updater = updater;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("f0ring.admin") && updater.isUpdateAvailable()) {
            p.sendMessage(ChatUtil.format(plugin.getConfig().getString("messages.update-available")
                    .replace("{version}", updater.getLatestVersion())
                    .replace("{link}", updater.getDownloadLink())));
        }
    }
}