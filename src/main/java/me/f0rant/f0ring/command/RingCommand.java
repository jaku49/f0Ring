package me.f0rant.f0ring.command;

import me.f0rant.f0ring.gui.GuiManager;
import me.f0rant.f0ring.manager.RingManager;
import me.f0rant.f0ring.util.ChatUtil;
import me.f0rant.f0ring.util.Updater;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class RingCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    private final RingManager ringManager;
    private final Updater updater;
    private final GuiManager guiManager;

    public RingCommand(JavaPlugin plugin, RingManager ringManager, Updater updater, GuiManager guiManager) {
        this.plugin = plugin;
        this.ringManager = ringManager;
        this.updater = updater;
        this.guiManager = guiManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = plugin.getConfig().getString("messages.prefix", "");

        if (args.length > 0) {
            if (!sender.hasPermission("f0ring.admin")) {
                sender.sendMessage(ChatUtil.format(plugin.getConfig().getString("messages.no-permission-command", "&cYou don't have permission to use this command!"), prefix));
                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                ringManager.loadRings();
                updater.start(); 
                sender.sendMessage(ChatUtil.format(plugin.getConfig().getString("messages.reload-success", "&aConfiguration reloaded!"), prefix));
                return true;
            }

            if (args[0].equalsIgnoreCase("alloff")) {
                ringManager.clearAllActiveRings();
                sender.sendMessage(ChatUtil.format(plugin.getConfig().getString("messages.alloff-success", "&eAll rings disabled for all players!"), prefix));
                return true;
            }
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatUtil.format(plugin.getConfig().getString("messages.only-players"), prefix));
            return true;
        }

        guiManager.openMainMenu(player);
        return true;
    }
}