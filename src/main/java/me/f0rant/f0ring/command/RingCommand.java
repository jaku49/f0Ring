package me.f0rant.f0ring.command;

import me.f0rant.f0ring.manager.RingManager;
import me.f0rant.f0ring.model.RingData;
import me.f0rant.f0ring.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class RingCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    private final RingManager ringManager;

    public RingCommand(JavaPlugin plugin, RingManager ringManager) {
        this.plugin = plugin;
        this.ringManager = ringManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (args.length > 0) {
            if (!sender.hasPermission("f0ring.admin")) {
                sender.sendMessage(ChatUtil.format(plugin.getConfig().getString("messages.no-permission-command", "&cNie masz uprawnien!")));
                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                ringManager.loadRings();
                sender.sendMessage(ChatUtil.format(plugin.getConfig().getString("messages.reload-success", "&aPrzeladowano konfiguracje!")));
                return true;
            }

            if (args[0].equalsIgnoreCase("alloff")) {
                ringManager.clearAllActiveRings();
                sender.sendMessage(ChatUtil.format(plugin.getConfig().getString("messages.alloff-success", "&eWylaczono ringo wszystkim graczom!")));
                return true;
            }
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatUtil.format(plugin.getConfig().getString("messages.only-players", "&cKomenda tylko dla graczy!")));
            return true;
        }

        if (args.length > 0) {
            sender.sendMessage(ChatUtil.format(plugin.getConfig().getString("messages.usage", "&cUzycie: /f0ring <reload|alloff>")));
            return true;
        }

        openRingGUI(player);
        return true;
    }

    private void openRingGUI(Player player) {
        String title = ChatUtil.format(plugin.getConfig().getString("gui.title", "Wybierz Ringo"));
        Inventory inv = Bukkit.createInventory(null, 54, title);

        Material borderMat = Material.valueOf(plugin.getConfig().getString("gui.border-material", "GRAY_STAINED_GLASS_PANE"));
        ItemStack borderItem = new ItemStack(borderMat);
        ItemMeta borderMeta = borderItem.getItemMeta();
        borderMeta.setDisplayName(" ");
        borderItem.setItemMeta(borderMeta);

        for (int i = 0; i < 54; i++) {
            if (i < 9 || i >= 45 || i % 9 == 0 || i % 9 == 8) {
                inv.setItem(i, borderItem);
            }
        }

        List<String> loreHasPerm = plugin.getConfig().getStringList("gui.lore-has-permission");
        List<String> loreNoPerm = plugin.getConfig().getStringList("gui.lore-no-permission");

        for (RingData ring : ringManager.getAllRings()) {
            boolean hasPermission = ring.permissions().isEmpty();
            for (String perm : ring.permissions()) {
                if (player.hasPermission(perm)) {
                    hasPermission = true;
                    break;
                }
            }

            List<String> rawLore = hasPermission ? loreHasPerm : loreNoPerm;
            List<String> finalLore = new ArrayList<>();

            for (String line : rawLore) {
                finalLore.add(ChatUtil.format(line.replace("{shape}", ring.shape())));
            }

            ItemStack item = new ItemStack(ring.material());
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatUtil.format(ring.name()));
            meta.setLore(finalLore);
            item.setItemMeta(meta);

            inv.setItem(ring.slot(), item);
        }

        Material removeMat = Material.valueOf(plugin.getConfig().getString("gui.remove-button.material", "BARRIER"));
        ItemStack barrier = new ItemStack(removeMat);
        ItemMeta barrierMeta = barrier.getItemMeta();
        barrierMeta.setDisplayName(ChatUtil.format(plugin.getConfig().getString("gui.remove-button.name", "&#FF3333&lZdejmij Ringo")));
        
        List<String> barrierLore = new ArrayList<>();
        for(String line : plugin.getConfig().getStringList("gui.remove-button.lore")) {
            barrierLore.add(ChatUtil.format(line));
        }
        barrierMeta.setLore(barrierLore);
        barrier.setItemMeta(barrierMeta);
        
        int removeSlot = plugin.getConfig().getInt("gui.remove-button.slot", 49);
        inv.setItem(removeSlot, barrier);

        player.openInventory(inv);
    }
}