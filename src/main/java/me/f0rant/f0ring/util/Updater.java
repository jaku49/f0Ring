package me.f0rant.f0ring.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Updater {

    private final JavaPlugin plugin;
    private final String urlString;
    private boolean updateAvailable = false;
    private String latestVersion = "";
    private String downloadLink = "";

    public Updater(JavaPlugin plugin) {
        this.plugin = plugin;
        this.urlString = plugin.getConfig().getString("updater.url", "");
    }

    public void check() {
        if (!plugin.getConfig().getBoolean("updater.enabled", true) || urlString.isEmpty()) {
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
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
                        updateAvailable = true;
                        plugin.getLogger().info("========================================");
                        plugin.getLogger().info("Found a new version of f0Ring!");
                        plugin.getLogger().info("Current: " + currentVersion + " | Latest: " + latestVersion);
                        plugin.getLogger().info("Download here: " + downloadLink);
                        plugin.getLogger().info("========================================");
                    } else {
                        plugin.getLogger().info("You have the latest version of f0Ring (" + currentVersion + ").");
                    }
                }
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to check for updates (Updater).");
            }
        });
    }

    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public String getDownloadLink() {
        return downloadLink;
    }
}