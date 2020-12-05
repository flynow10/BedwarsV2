package com.wagologies.bedwarsv2.game.player;

import org.bukkit.Material;

public enum Sword {
    WOOD(Material.WOOD_SWORD),
    STONE(Material.STONE_SWORD),
    IRON(Material.IRON_SWORD),
    DIAMOND(Material.DIAMOND_SWORD),
    SAM(Material.GOLD_SWORD);
    private Material material;
    public Material getMaterial() { return material;}
    Sword(Material material) {
        this.material = material;
    }
}
