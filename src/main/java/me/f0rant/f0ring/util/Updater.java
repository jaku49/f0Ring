package me.f0rant.f0ring.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener; // Dodane
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import me.f0rant.f0ring.F0Ring;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Updater implements Listener { 

    private final F0Ring plugin; 
    private boolean updateAvailable = false;
    private String latestVersion = "";
    private String downloadLink = "";
    private BukkitTask task;
    private boolean firstCheck = true; 

    public Updater(F0Ring plugin) {
        this.plugin = plugin;
    }

    public void start() {
        if (task != null) {
            task.cancel();
        }

        int intervalMinutes = plugin.getConfig().getInt("updater.interval", 30);
        if (intervalMinutes <= 0) intervalMinutes = 30; 
        
        long intervalTicks = intervalMinutes * 60 * 20L;

        task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            if (!plugin.getConfig().getBoolean("updater.enabled", true)) {
                return;
            }

            String urlString = plugin.getConfig().getString("updater.url", "");
            if (urlString == null || urlString.isEmpty()) return;

            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String response = reader.readLine();
                reader.close();

                if (response != null && response.contains("|")) {
                    String[] parts = response.split("\\|");
                    latestVersion = parts[0];
                    downloadLink = parts[1];

                    String currentVersion = plugin.getDescription().getVersion();

                    if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                        if (!updateAvailable) { 
                            updateAvailable = true;
                            plugin.getLogger().info("========================================");
                            plugin.getLogger().info("Found a new version of f0Ring!");
                            plugin.getLogger().info("Current: " + currentVersion + " | Latest: " + latestVersion);
                            plugin.getLogger().info("Download here: " + downloadLink);
                            plugin.getLogger().info("========================================");
                        }
                    } else {
                        updateAvailable = false;
                        if (firstCheck) { 
                            plugin.getLogger().info("You have the latest version of f0Ring (" + currentVersion + ").");
                        }
                    }
                }
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to check for updates (Updater).");
            }

            firstCheck = false;
            
        }, 0L, intervalTicks);
    }

    @EventHandler
    public void onJoin(org.bukkit.event.player.PlayerJoinEvent e) {
        Player player = e.getPlayer();
        plugin.getRingManager().handleJoin(player);
    }

    public boolean isUpdateAvailable() { return updateAvailable; }
    public String getLatestVersion() { return latestVersion; }
    public String getDownloadLink() { return downloadLink; }
}