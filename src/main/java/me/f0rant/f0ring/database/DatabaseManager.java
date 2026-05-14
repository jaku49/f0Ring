package me.f0rant.f0ring.database;

import me.f0rant.f0ring.F0Ring;
import me.f0rant.f0ring.model.PlayerData;
import me.f0rant.f0ring.model.RingData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.List;

public class DatabaseManager {

    private final F0Ring plugin;
    private Connection connection;
    private String tableName = "f0ring_users";

    public DatabaseManager(F0Ring plugin) {
        this.plugin = plugin;
    }

    public void connect() {
        try {
            String dbType = plugin.getConfig().getString("database.type", "SQLITE").toUpperCase();
            
            this.tableName = plugin.getConfig().getString("database.mysql.table", "f0ring_users");

            if (dbType.equals("MYSQL")) {
                String host = plugin.getConfig().getString("database.mysql.host", "localhost");
                String port = plugin.getConfig().getString("database.mysql.port", "3306");
                String database = plugin.getConfig().getString("database.mysql.database", "minecraft");
                String username = plugin.getConfig().getString("database.mysql.username", "minecraft");
                String password = plugin.getConfig().getString("database.mysql.password", "mc123");

                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&useSSL=false", username, password);
                plugin.getLogger().info("Connected to MySQL database! Using table: " + tableName);
            } else {
                // Typ SQLITE
                connection = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder() + "/database.db");
                plugin.getLogger().info("Connected to local SQLite database! Using table: " + tableName);
            }

            createTable();
        } catch (SQLException e) {
            plugin.getLogger().severe("Error connecting to database! Check login details in config.yml.");
            e.printStackTrace();
        }
    }

    private void createTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                "uuid VARCHAR(36) PRIMARY KEY, " +
                "active_ring VARCHAR(255), " +
                "hide_self BOOLEAN, " +
                "hide_others BOOLEAN, " +
                "resume_ringo BOOLEAN)";
        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
        }
    }

    public void savePlayer(Player player) {
        if (connection == null) return;
        
        PlayerData data = plugin.getRingManager().getPlayerData(player);
        String uuid = player.getUniqueId().toString();
        
        String ringString = "none";
        RingData active = data.getActiveRing();
        if (active != null) {
            if (active.id().equals("custom")) {
                ringString = "custom;" + active.type() + ";" + active.shape() + ";" + active.color1() + ";" + active.color2();
            } else {
                ringString = active.id();
            }
        }

        String finalRingString = ringString;
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                String query = "REPLACE INTO " + tableName + " (uuid, active_ring, hide_self, hide_others, resume_ringo) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement ps = connection.prepareStatement(query)) {
                    ps.setString(1, uuid);
                    ps.setString(2, finalRingString);
                    ps.setBoolean(3, data.isHideSelf());
                    ps.setBoolean(4, data.isHideOthers());
                    ps.setBoolean(5, data.isResumeRingo());
                    ps.executeUpdate();
                }
            } catch (SQLException e) {
                plugin.getLogger().warning("Error saving player data: " + player.getName());
            }
        });
    }

    public void loadPlayer(Player player) {
        if (connection == null) return;

        String uuid = player.getUniqueId().toString();
        
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                String query = "SELECT * FROM " + tableName + " WHERE uuid = ?";
                try (PreparedStatement ps = connection.prepareStatement(query)) {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    
                    if (rs.next()) {
                        String ringString = rs.getString("active_ring");
                        boolean hideSelf = rs.getBoolean("hide_self");
                        boolean hideOthers = rs.getBoolean("hide_others");
                        boolean resumeRingo = rs.getBoolean("resume_ringo");
                        
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            PlayerData data = plugin.getRingManager().getPlayerData(player);
                            data.setHideSelf(hideSelf);
                            data.setHideOthers(hideOthers);
                            data.setResumeRingo(resumeRingo);

                            if (!ringString.equals("none") && resumeRingo) {
                                if (ringString.startsWith("custom;")) {
                                    String[] parts = ringString.split(";");
                                    String customName = plugin.getConfig().getString("creator-gui.custom-ring-name", "&#FF55FFMy Custom Ring");
                                    RingData customRing = new RingData("custom", -1, Material.BARRIER, customName, List.of(), parts[1], parts[2], parts[3], parts[4]);
                                    data.setActiveRing(customRing);
                                } else {
                                    for (RingData r : plugin.getRingManager().getAllRings()) {
                                        if (r.id().equals(ringString)) {
                                            data.setActiveRing(r);
                                            break;
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            } catch (SQLException e) {
                plugin.getLogger().warning("Error loading player data: " + player.getName());
            }
        });
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}