package me.f0rant.f0ring.gui;

import me.f0rant.f0ring.F0Ring;
import me.f0rant.f0ring.manager.RingManager;
import me.f0rant.f0ring.model.CreatorSession;
import me.f0rant.f0ring.model.PlayerData;
import me.f0rant.f0ring.model.RingData;
import me.f0rant.f0ring.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class GuiManager {

    private final F0Ring plugin;
    private final RingManager ringManager;
    
    public static final Map<UUID, String> ACTIVE_MENUS = new HashMap<>();

    public GuiManager(F0Ring plugin, RingManager ringManager) {
        this.plugin = plugin;
        this.ringManager = ringManager;
    }

    public Map<UUID, String> getActiveMenus() {
        return ACTIVE_MENUS;
    }

    private void fillBorders(Inventory inv, Material borderMat) {
        ItemStack border = new ItemStack(borderMat);
        ItemMeta meta = border.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(" ");
            border.setItemMeta(meta);
        }
        for (int i = 0; i < 54; i++) {
            if (i < 9 || i >= 45 || i % 9 == 0 || i % 9 == 8) inv.setItem(i, border);
        }
    }

    private void safeOpen(Player player, Inventory inv, String type) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            player.openInventory(inv);
            ACTIVE_MENUS.put(player.getUniqueId(), type);
        });
    }

    public void openMainMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, ChatUtil.format(plugin.getConfig().getString("gui.title", "Choose Your Ring")));
        fillBorders(inv, Material.valueOf(plugin.getConfig().getString("gui.border-material", "GRAY_STAINED_GLASS_PANE")));

        ItemStack settings = new ItemStack(Material.valueOf(plugin.getConfig().getString("gui.settings-button.material", "COMPARATOR")));
        ItemMeta setMeta = settings.getItemMeta();
        setMeta.setDisplayName(ChatUtil.format(plugin.getConfig().getString("gui.settings-button.name", "&eSettings")));
        List<String> setLore = new ArrayList<>();
        plugin.getConfig().getStringList("gui.settings-button.lore").forEach(l -> setLore.add(ChatUtil.format(l)));
        setMeta.setLore(setLore);
        settings.setItemMeta(setMeta);
        inv.setItem(plugin.getConfig().getInt("gui.settings-button.slot", 0), settings);

        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        headMeta.setOwningPlayer(player);
        headMeta.setDisplayName(ChatUtil.format(plugin.getConfig().getString("gui.player-info.name", "&eYour Statistics")));
        List<String> headLore = new ArrayList<>();
        boolean papi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
        for (String line : plugin.getConfig().getStringList("gui.player-info.lore")) {
            headLore.add(ChatUtil.format(papi ? me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, line) : line));
        }
        headMeta.setLore(headLore);
        head.setItemMeta(headMeta);
        inv.setItem(plugin.getConfig().getInt("gui.player-info.slot", 4), head);

        if (player.hasPermission(plugin.getConfig().getString("gui.creator-button.permission", "f0ring.creator"))) {
            ItemStack creator = new ItemStack(Material.valueOf(plugin.getConfig().getString("gui.creator-button.material", "COMMAND_BLOCK")));
            ItemMeta creMeta = creator.getItemMeta();
            creMeta.setDisplayName(ChatUtil.format(plugin.getConfig().getString("gui.creator-button.name", "&dRing Creator")));
            List<String> creLore = new ArrayList<>();
            plugin.getConfig().getStringList("gui.creator-button.lore").forEach(l -> creLore.add(ChatUtil.format(l)));
            creMeta.setLore(creLore);
            creator.setItemMeta(creMeta);
            inv.setItem(plugin.getConfig().getInt("gui.creator-button.slot", 8), creator);
        }

        ItemStack barrier = new ItemStack(Material.valueOf(plugin.getConfig().getString("gui.remove-button.material", "BARRIER")));
        ItemMeta barMeta = barrier.getItemMeta();
        barMeta.setDisplayName(ChatUtil.format(plugin.getConfig().getString("gui.remove-button.name", "&cRemove Ring")));
        List<String> barLore = new ArrayList<>();
        plugin.getConfig().getStringList("gui.remove-button.lore").forEach(l -> barLore.add(ChatUtil.format(l)));
        barMeta.setLore(barLore);
        barrier.setItemMeta(barMeta);
        inv.setItem(plugin.getConfig().getInt("gui.remove-button.slot", 49), barrier);

        List<String> loreHasPerm = plugin.getConfig().getStringList("gui.lore-has-permission");
        List<String> loreNoPerm = plugin.getConfig().getStringList("gui.lore-no-permission");
        RingData activeRing = ringManager.getPlayerData(player).getActiveRing();

        for (RingData ring : ringManager.getAllRings()) {
            boolean hasPermission = ring.permissions().isEmpty() || ring.permissions().stream().anyMatch(player::hasPermission);
            List<String> finalLore = new ArrayList<>();
            String translatedShape = plugin.getConfig().getString("shape-names." + ring.shape().toUpperCase(), ring.shape());
            
            for (String line : (hasPermission ? loreHasPerm : loreNoPerm)) {
                finalLore.add(ChatUtil.format(line.replace("{shape}", translatedShape)));
            }

            ItemStack item = new ItemStack(ring.material());
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatUtil.format(ring.name()));
            
            if (activeRing != null && activeRing.slot() == ring.slot()) {
                meta.addEnchant(Enchantment.UNBREAKING, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                finalLore.add("");
                finalLore.add(ChatUtil.format(plugin.getConfig().getString("gui.equipped-text", "&#33FF33(Active)")));
            }
            meta.setLore(finalLore);
            item.setItemMeta(meta);
            inv.setItem(ring.slot(), item);
        }

        safeOpen(player, inv, "MAIN");
    }

    public void openSettingsMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, ChatUtil.format(plugin.getConfig().getString("settings-gui.title", "Ringo Settings")));
        fillBorders(inv, Material.valueOf(plugin.getConfig().getString("settings-gui.border", "BLACK_STAINED_GLASS_PANE")));
        PlayerData data = ringManager.getPlayerData(player);

        String statusOn = ChatUtil.format(plugin.getConfig().getString("settings-gui.status-visible", "&a✔ Visible"));
        String statusOff = ChatUtil.format(plugin.getConfig().getString("settings-gui.status-hidden", "&c✖ Hidden"));
        String statusEnabled = ChatUtil.format(plugin.getConfig().getString("settings-gui.status-enabled", "&a✔ Enabled"));
        String statusDisabled = ChatUtil.format(plugin.getConfig().getString("settings-gui.status-disabled", "&c✖ Disabled"));

        ItemStack self = new ItemStack(Material.valueOf(plugin.getConfig().getString("settings-gui.hide-self.material", "ENDER_EYE")));
        ItemMeta selfMeta = self.getItemMeta();
        selfMeta.setDisplayName(ChatUtil.format(plugin.getConfig().getString("settings-gui.hide-self.name", "My Rings")));
        selfMeta.setLore(List.of(data.isHideSelf() ? statusOff : statusOn));
        self.setItemMeta(selfMeta);
        inv.setItem(plugin.getConfig().getInt("settings-gui.hide-self.slot", 20), self);

        ItemStack resume = new ItemStack(Material.valueOf(plugin.getConfig().getString("settings-gui.resume.material", "CLOCK")));
        ItemMeta resMeta = resume.getItemMeta();
        resMeta.setDisplayName(ChatUtil.format(plugin.getConfig().getString("settings-gui.resume.name", "Resume on Join")));
        resMeta.setLore(List.of(data.isResumeRingo() ? statusEnabled : statusDisabled));
        resume.setItemMeta(resMeta);
        inv.setItem(plugin.getConfig().getInt("settings-gui.resume.slot", 22), resume);

        ItemStack others = new ItemStack(Material.valueOf(plugin.getConfig().getString("settings-gui.hide-others.material", "SPYGLASS")));
        ItemMeta othersMeta = others.getItemMeta();
        othersMeta.setDisplayName(ChatUtil.format(plugin.getConfig().getString("settings-gui.hide-others.name", "Others' Rings")));
        othersMeta.setLore(List.of(data.isHideOthers() ? statusOff : statusOn));
        others.setItemMeta(othersMeta);
        inv.setItem(plugin.getConfig().getInt("settings-gui.hide-others.slot", 24), others);

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(ChatUtil.format(plugin.getConfig().getString("gui.back-button", "&cBack")));
        back.setItemMeta(backMeta);
        inv.setItem(49, back);

        safeOpen(player, inv, "SETTINGS");
    }

    public void openCreatorMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, ChatUtil.format(plugin.getConfig().getString("creator-gui.title", "Ringo Creator")));
        fillBorders(inv, Material.valueOf(plugin.getConfig().getString("creator-gui.border", "BLACK_STAINED_GLASS_PANE")));
        
        updateCreatorMenu(player, inv);

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(ChatUtil.format(plugin.getConfig().getString("gui.back-button", "&cBack")));
        back.setItemMeta(backMeta);
        inv.setItem(49, back);

        ItemStack save = new ItemStack(Material.valueOf(plugin.getConfig().getString("creator-gui.items.save.material", "EMERALD_BLOCK")));
        ItemMeta saveMeta = save.getItemMeta();
        saveMeta.setDisplayName(ChatUtil.format(plugin.getConfig().getString("creator-gui.items.save.name", "&aSave")));
        save.setItemMeta(saveMeta);
        inv.setItem(plugin.getConfig().getInt("creator-gui.items.save.slot", 40), save);

        safeOpen(player, inv, "CREATOR");
    }

    public void updateCreatorMenu(Player player, Inventory inv) {
        CreatorSession session = ringManager.getSession(player);
        
        String selectedFormat = plugin.getConfig().getString("creator-gui.carousel.selected", "&a> {val}");
        String unselectedFormat = plugin.getConfig().getString("creator-gui.carousel.unselected", "&7  {val}");
        String instructions = plugin.getConfig().getString("creator-gui.carousel.instructions", "&7Left / Right Click");

        ItemStack shape = new ItemStack(Material.valueOf(plugin.getConfig().getString("creator-gui.items.shape.material", "SLIME_BALL")));
        ItemMeta sMeta = shape.getItemMeta();
        sMeta.setDisplayName(ChatUtil.format(plugin.getConfig().getString("creator-gui.items.shape.name", "Shape").replace("{val}", session.shape)));
        List<String> shapeLore = new ArrayList<>();
        shapeLore.add(ChatUtil.format(instructions));
        shapeLore.add("");
        String[] shapes = {"CIRCLE", "DOUBLE_RING", "STAR", "HELIX", "WAVE", "HEART", "DNA", "ORBITALS"};
        for (String s : shapes) {
            String translated = plugin.getConfig().getString("shape-names." + s, s);
            String format = s.equals(session.shape) ? selectedFormat : unselectedFormat;
            shapeLore.add(ChatUtil.format(format.replace("{val}", translated)));
        }
        sMeta.setLore(shapeLore);
        shape.setItemMeta(sMeta);
        inv.setItem(plugin.getConfig().getInt("creator-gui.items.shape.slot", 20), shape);

        ItemStack type = new ItemStack(Material.valueOf(plugin.getConfig().getString("creator-gui.items.type.material", "PRISMARINE_CRYSTALS")));
        ItemMeta tMeta = type.getItemMeta();
        tMeta.setDisplayName(ChatUtil.format(plugin.getConfig().getString("creator-gui.items.type.name", "Typ").replace("{val}", session.type)));
        List<String> typeLore = new ArrayList<>();
        typeLore.add(ChatUtil.format(instructions));
        typeLore.add("");
        String[] types = {"SOLID", "DUAL", "RAINBOW"};
        for (String t : types) {
            String format = t.equals(session.type) ? selectedFormat : unselectedFormat;
            typeLore.add(ChatUtil.format(format.replace("{val}", t)));
        }
        tMeta.setLore(typeLore);
        type.setItemMeta(tMeta);
        inv.setItem(plugin.getConfig().getInt("creator-gui.items.type.slot", 22), type);
        
        int color1Slot = plugin.getConfig().getInt("creator-gui.items.color1.slot", 24);
        int color2Slot = plugin.getConfig().getInt("creator-gui.items.color2.slot", 25);
        inv.setItem(color1Slot, null);
        inv.setItem(color2Slot, null);

        if (!session.type.equalsIgnoreCase("RAINBOW")) {
            ItemStack c1 = new ItemStack(Material.valueOf(plugin.getConfig().getString("creator-gui.items.color1.material", "RED_DYE")));
            ItemMeta c1Meta = c1.getItemMeta();
            
            String rawName1 = plugin.getConfig().getString("creator-gui.items.color1.name", "Color 1: {val}");
            String coloredHex1 = "&" + session.color1 + session.color1;
            c1Meta.setDisplayName(ChatUtil.format(rawName1.replace("{val}", coloredHex1)));
            
            c1Meta.setLore(List.of(ChatUtil.format(plugin.getConfig().getString("creator-gui.click-chat", "&7Click!"))));
            c1.setItemMeta(c1Meta);
            inv.setItem(color1Slot, c1);
            
            if (session.type.equalsIgnoreCase("DUAL")) {
                ItemStack c2 = new ItemStack(Material.valueOf(plugin.getConfig().getString("creator-gui.items.color2.material", "BLUE_DYE")));
                ItemMeta c2Meta = c2.getItemMeta();
                
                String rawName2 = plugin.getConfig().getString("creator-gui.items.color2.name", "Color 2: {val}");
                String coloredHex2 = "&" + session.color2 + session.color2;
                c2Meta.setDisplayName(ChatUtil.format(rawName2.replace("{val}", coloredHex2)));
                
                c2Meta.setLore(List.of(ChatUtil.format(plugin.getConfig().getString("creator-gui.click-chat", "&7Click!"))));
                c2.setItemMeta(c2Meta);
                inv.setItem(color2Slot, c2);
            }
        }
    }
}