package me.f0rant.f0ring;

import me.f0rant.f0ring.command.RingCommand;
import me.f0rant.f0ring.command.RingTabCompleter;
import me.f0rant.f0ring.database.DatabaseManager;
import me.f0rant.f0ring.gui.GuiManager;
import me.f0rant.f0ring.listener.ChatInputListener;
import me.f0rant.f0ring.listener.MenuListener;
import me.f0rant.f0ring.listener.PlayerJoinListener;
import me.f0rant.f0ring.listener.PlayerQuitListener;
import me.f0rant.f0ring.listener.UpdateListener;
import me.f0rant.f0ring.manager.RingManager;
import me.f0rant.f0ring.task.ParticleTask;
import me.f0rant.f0ring.util.Updater;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class F0Ring extends JavaPlugin {

    private RingManager ringManager;
    private DatabaseManager databaseManager;
    private Updater updater;
    private GuiManager guiManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.databaseManager = new DatabaseManager(this);
        this.databaseManager.connect();

        this.ringManager = new RingManager(this);
        this.ringManager.loadRings(); 
        
        this.updater = new Updater(this);
        this.updater.start();
        this.guiManager = new GuiManager(this, ringManager);

        if (getCommand("ring") != null) {
            getCommand("ring").setExecutor(new RingCommand(this, ringManager, updater, guiManager));
            getCommand("ring").setTabCompleter(new RingTabCompleter());
        }
        
        getServer().getPluginManager().registerEvents(new MenuListener(this, ringManager, guiManager), this);
        getServer().getPluginManager().registerEvents(new ChatInputListener(this, ringManager, guiManager), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new UpdateListener(this, updater), this);
        getServer().getPluginManager().registerEvents(this.updater, this);

        new ParticleTask(this, ringManager).runTaskTimerAsynchronously(this, 0L, 2L);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new me.f0rant.f0ring.util.RingExpansion(this, ringManager).register();
        }

        getLogger().info("Successfully enabled f0Ring v" + getDescription().getVersion() + " by f0rant! Made with <3");
    }

    public RingManager getRingManager() {
        return ringManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
    // ==========================================

    @Override
    public void onDisable() {
        for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
            if (databaseManager != null) {
                databaseManager.savePlayer(p);
            }
        }
        
        if (databaseManager != null) {
            databaseManager.close();
        }
        getLogger().info("Disabled f0Ring, database connection closed.");
    }
}