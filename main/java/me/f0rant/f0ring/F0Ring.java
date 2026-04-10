package me.f0rant.f0ring;

import me.f0rant.f0ring.command.RingCommand;
import me.f0rant.f0ring.listener.MenuListener;
import me.f0rant.f0ring.listener.UpdateListener;
import me.f0rant.f0ring.manager.RingManager;
import me.f0rant.f0ring.task.ParticleTask;
import me.f0rant.f0ring.util.Updater;
import org.bukkit.plugin.java.JavaPlugin;

public class F0Ring extends JavaPlugin {

    private RingManager ringManager;
    private Updater updater;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.ringManager = new RingManager(this);
        this.ringManager.loadRings();

        this.updater = new Updater(this);
        this.updater.check();

        getCommand("ring").setExecutor(new RingCommand(this, ringManager));
        getServer().getPluginManager().registerEvents(new MenuListener(this, ringManager), this);
        getServer().getPluginManager().registerEvents(new UpdateListener(this, updater), this);

        new ParticleTask(this, ringManager).runTaskTimerAsynchronously(this, 0L, 2L);

        getLogger().info("f0Ring zaladowany pomyslnie! Wersja: " + getDescription().getVersion());
    }
}