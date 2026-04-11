package me.f0rant.f0ring.util;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatUtil {

    private static final Pattern HEX_PATTERN = Pattern.compile("&#[a-fA-F0-9]{6}");

    public static String format(String text) {
        if (text == null) return "";
        Matcher matcher = HEX_PATTERN.matcher(text);
        while (matcher.find()) {
            String color = text.substring(matcher.start() + 1, matcher.end());
            text = text.replace(matcher.group(), ChatColor.of(color) + "");
            matcher = HEX_PATTERN.matcher(text);
        }
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static Color hexToColor(String hex) {
        if (hex.startsWith("#")) hex = hex.substring(1);
        try {
            return Color.fromRGB(
                    Integer.valueOf(hex.substring(0, 2), 16),
                    Integer.valueOf(hex.substring(2, 4), 16),
                    Integer.valueOf(hex.substring(4, 6), 16)
            );
        } catch (Exception e) {
            return Color.WHITE;
        }
    }
}