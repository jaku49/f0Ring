package me.f0rant.f0ring.model;

import org.bukkit.Material;
import java.util.List;

public record RingData(
        String id,
        int slot,
        Material material,
        String name,
        List<String> permissions,
        String type,
        String shape,
        String color1,
        String color2
) {}