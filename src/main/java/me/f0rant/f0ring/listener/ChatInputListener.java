package me.f0rant.f0ring.listener;

import me.f0rant.f0ring.F0Ring;
import me.f0rant.f0ring.gui.GuiManager;
import me.f0rant.f0ring.manager.RingManager;
import me.f0rant.f0ring.model.CreatorSession;
import me.f0rant.f0ring.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatInputListener implements Listener {

    private final F0Ring plugin;
    private final RingManager ringManager;
    private final GuiManager guiManager;

    public ChatInputListener(F0Ring plugin, RingManager ringManager, GuiManager guiManager) {
        this.plugin = plugin;
        this.ringManager = ringManager;
        this.guiManager = guiManager;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        CreatorSession session = ringManager.getSession(p);

        if (session.awaitingColor1 || session.awaitingColor2) {
            e.setCancelled(true);
            String input = e.getMessage().trim().toUpperCase();
            String prefix = plugin.getConfig().getString("messages.prefix", "");

            if (!input.matches("^#[0-9A-F]{6}$")) {
                String errorMsg = plugin.getConfig().getString("messages.creator-error", "&cError!");
                p.sendMessage(ChatUtil.format(errorMsg.replace("%prefix%", prefix)));
                
                Bukkit.getScheduler().runTask(plugin, () -> guiManager.openCreatorMenu(p));
                session.awaitingColor1 = false; session.awaitingColor2 = false;
                return;
            }

            if (session.awaitingColor1) {
                session.color1 = input; session.awaitingColor1 = false;
            } else {
                session.color2 = input; session.awaitingColor2 = false;
            }

            Bukkit.getScheduler().runTask(plugin, () -> {
                String successMsg = plugin.getConfig().getString("messages.creator-success", "&aColor saved: &f");
                p.sendMessage(ChatUtil.format(successMsg.replace("%prefix%", prefix) + input));
                guiManager.openCreatorMenu(p);
            });
        }
    }
}