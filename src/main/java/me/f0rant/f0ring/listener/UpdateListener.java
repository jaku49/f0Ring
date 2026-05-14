package me.f0rant.f0ring.listener;

import me.f0rant.f0ring.F0Ring;
import me.f0rant.f0ring.util.ChatUtil;
import me.f0rant.f0ring.util.Updater;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdateListener implements Listener {

    private final F0Ring plugin;
    private final Updater updater;

    public UpdateListener(F0Ring plugin, Updater updater) {
        this.plugin = plugin;
        this.updater = updater;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        if (updater.isUpdateAvailable() && (player.isOp() || player.hasPermission("f0ring.admin") || player.hasPermission("f0ring.update"))) {
            
            String prefix = plugin.getConfig().getString("messages.prefix", "&8[&eF0Ring&8] ");
            String updateMsg = plugin.getConfig().getString("messages.update-available", "&aNew version available: &f%version%");
            String downloadMsg = plugin.getConfig().getString("messages.update-link", "&aDownload here: &f%link%");

            String finalUpdateMsg = updateMsg.replace("%version%", updater.getLatestVersion() != null ? updater.getLatestVersion() : "No version available").replace("%prefix%", prefix);
            String finalDownloadMsg = downloadMsg.replace("%link%", updater.getDownloadLink() != null ? updater.getDownloadLink() : "No download link available").replace("%prefix%", prefix);

            player.sendMessage(ChatUtil.format(finalUpdateMsg));
            player.sendMessage(ChatUtil.format(finalDownloadMsg));
        }
    }
}