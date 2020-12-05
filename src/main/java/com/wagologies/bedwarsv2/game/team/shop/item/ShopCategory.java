package com.wagologies.bedwarsv2.game.team.shop.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum ShopCategory {
    BLOCKS(Material.STAINED_CLAY, "Blocks"),
    COMBAT(Material.IRON_SWORD, "Combat"),
    TOOLS(Material.IRON_PICKAXE, "Tools"),
    ARMOR(Material.IRON_CHESTPLATE, "Armor"),
    SPECIAL(Material.TNT, "Special"),
    UPGRADES(Material.ENCHANTED_BOOK, "Upgrades"),
    ISLANDBUFFS(Material.BED, "Island Buffs");

    Material placeHolderMaterial;
    String name;
    ShopCategory(Material placeHolderMaterial, String name)
    {
        this.placeHolderMaterial = placeHolderMaterial;
        this.name = name;
    }

    public ItemStack getPlaceHolder() {
        ItemStack placeHolder = new ItemStack(placeHolderMaterial);
        ItemMeta itemMeta = placeHolder.getItemMeta();
        itemMeta.setDisplayName(name);
        placeHolder.setItemMeta(itemMeta);
        return placeHolder;
    }
}
