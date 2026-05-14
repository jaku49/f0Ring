package me.f0rant.f0ring.listener;

import me.f0rant.f0ring.F0Ring;
import me.f0rant.f0ring.gui.GuiManager;
import me.f0rant.f0ring.manager.RingManager;
import me.f0rant.f0ring.model.CreatorSession;
import me.f0rant.f0ring.model.PlayerData;
import me.f0rant.f0ring.model.RingData;
import me.f0rant.f0ring.util.ChatUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.List;

public class MenuListener implements Listener {

    private final F0Ring plugin;
    private final RingManager ringManager;
    private final GuiManager guiManager;

    public MenuListener(F0Ring plugin, RingManager ringManager, GuiManager guiManager) {
        this.plugin = plugin;
        this.ringManager = ringManager;
        this.guiManager = guiManager;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        GuiManager.ACTIVE_MENUS.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player player)) return;

        String menuType = GuiManager.ACTIVE_MENUS.get(player.getUniqueId());
        if (menuType == null) return; 

        e.setCancelled(true);

        if (e.getClickedInventory() != e.getView().getTopInventory()) return;
        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;

        int slot = e.getRawSlot();
        String prefix = plugin.getConfig().getString("messages.prefix", "");

        if (menuType.equals("MAIN")) {
            if (slot == plugin.getConfig().getInt("gui.settings-button.slot", 0)) {
                guiManager.openSettingsMenu(player);
            } else if (slot == plugin.getConfig().getInt("gui.creator-button.slot", 8)) {
                if (player.hasPermission(plugin.getConfig().getString("gui.creator-button.permission", "f0ring.creator"))) {
                    guiManager.openCreatorMenu(player);
                }
            } else if (slot == plugin.getConfig().getInt("gui.remove-button.slot", 49)) {
                ringManager.removeActiveRing(player);
                player.sendMessage(ChatUtil.format(plugin.getConfig().getString("messages.ring-removed", "&eZdjęto ringo.").replace("%prefix%", prefix)));
                player.closeInventory();
            } else {
                RingData ring = ringManager.getRingBySlot(slot);
                if (ring != null) {
                    boolean hasPerm = ring.permissions().isEmpty() || ring.permissions().stream().anyMatch(player::hasPermission);
                    if (hasPerm) {
                        ringManager.setActiveRing(player, ring);
                        player.sendMessage(ChatUtil.format(plugin.getConfig().getString("messages.ring-equipped", "&aEquipped!").replace("%prefix%", prefix)));
                        player.closeInventory();
                    } else {
                        player.sendMessage(ChatUtil.format(plugin.getConfig().getString("messages.no-permission", "&cYou don't have permission!").replace("%prefix%", prefix)));
                    }
                }
            }
        }

        else if (menuType.equals("SETTINGS")) {
            PlayerData data = ringManager.getPlayerData(player);
            if (slot == plugin.getConfig().getInt("settings-gui.hide-self.slot", 20)) {
                data.setHideSelf(!data.isHideSelf());
                guiManager.openSettingsMenu(player);
            } else if (slot == plugin.getConfig().getInt("settings-gui.resume.slot", 22)) {
                // Zmiana opcji wznawiania
                data.setResumeRingo(!data.isResumeRingo());
                guiManager.openSettingsMenu(player);
            } else if (slot == plugin.getConfig().getInt("settings-gui.hide-others.slot", 24)) {
                data.setHideOthers(!data.isHideOthers());
                guiManager.openSettingsMenu(player);
            } else if (slot == 49) {
                guiManager.openMainMenu(player);
            }
        } 

        else if (menuType.equals("CREATOR")) {
            CreatorSession s = ringManager.getSession(player);
            
            // Karuzela Kształtów
            if (slot == plugin.getConfig().getInt("creator-gui.items.shape.slot", 20)) {
                String[] shapes = {"CIRCLE", "DOUBLE_RING", "STAR", "HELIX", "WAVE", "HEART", "DNA", "ORBITALS"};
                int i = 0; for(; i < shapes.length; i++) if(shapes[i].equals(s.shape)) break;
                
                if (e.isLeftClick()) i = (i - 1 + shapes.length) % shapes.length;
                else if (e.isRightClick()) i = (i + 1) % shapes.length;
                
                s.shape = shapes[i];
                guiManager.updateCreatorMenu(player, e.getView().getTopInventory());
            } 
            else if (slot == plugin.getConfig().getInt("creator-gui.items.type.slot", 22)) {
                String[] types = {"SOLID", "DUAL", "RAINBOW"};
                int i = 0; for(; i < types.length; i++) if(types[i].equals(s.type)) break;
                
                if (e.isLeftClick()) i = (i - 1 + types.length) % types.length;
                else if (e.isRightClick()) i = (i + 1) % types.length;
                
                s.type = types[i];
                guiManager.updateCreatorMenu(player, e.getView().getTopInventory());
            } 
            else if (slot == plugin.getConfig().getInt("creator-gui.items.color1.slot", 24)) {
                s.awaitingColor1 = true;
                player.closeInventory();
                player.sendTitle(ChatUtil.format(plugin.getConfig().getString("messages.creator-title", "&eHEX")), ChatUtil.format(plugin.getConfig().getString("messages.creator-sub", "&7example #FF0000")), 10, 70, 10);
            } 
            else if (slot == plugin.getConfig().getInt("creator-gui.items.color2.slot", 25)) {
                s.awaitingColor2 = true;
                player.closeInventory();
                player.sendTitle(ChatUtil.format(plugin.getConfig().getString("messages.creator-title", "&eHEX")), ChatUtil.format(plugin.getConfig().getString("messages.creator-sub", "&7example #FF0000")), 10, 70, 10);
            } 
            else if (slot == plugin.getConfig().getInt("creator-gui.items.save.slot", 40)) {
                String customName = plugin.getConfig().getString("creator-gui.custom-ring-name", "&#FF55FFMy Custom Ring");
                RingData customRing = new RingData("custom", -1, Material.BARRIER, customName, List.of(), s.type, s.shape, s.color1, s.color2);
                ringManager.setActiveRing(player, customRing);
                
                player.sendMessage(ChatUtil.format(plugin.getConfig().getString("messages.ring-equipped", "&aEquipped ring!").replace("%prefix%", prefix)));
                player.closeInventory();
            } 
            else if (slot == 49) {
                guiManager.openMainMenu(player);
            }
        }
    }
}