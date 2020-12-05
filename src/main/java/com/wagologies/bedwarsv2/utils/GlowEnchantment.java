package com.wagologies.bedwarsv2.utils;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GlowEnchantment extends Enchantment {

    public GlowEnchantment(int id)
    {
        super(id);
    }

    public static ItemStack ApplyGlow(ItemStack stack)
    {
        ItemMeta meta = stack.getItemMeta();
        GlowEnchantment glow = new GlowEnchantment(70);
        meta.addEnchant(glow, 1, true);
        stack.setItemMeta(meta);
        return stack;
    }
    /**
     * Gets the unique name of this enchantment
     *
     * @return Unique name
     */
    @Override
    public String getName() {
        return null;
    }

    /**
     * Gets the maximum level that this Enchantment may become.
     *
     * @return Maximum level of the Enchantment
     */
    @Override
    public int getMaxLevel() {
        return 0;
    }

    /**
     * Gets the level that this Enchantment should start at
     *
     * @return Starting level of the Enchantment
     */
    @Override
    public int getStartLevel() {
        return 0;
    }

    /**
     * Gets the type of {@link ItemStack} that may fit this Enchantment.
     *
     * @return Target type of the Enchantment
     */
    @Override
    public EnchantmentTarget getItemTarget() {
        return null;
    }

    /**
     * Check if this enchantment conflicts with another enchantment.
     *
     * @param other The enchantment to check against
     * @return True if there is a conflict.
     */
    @Override
    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    /**
     * Checks if this Enchantment may be applied to the given {@link
     * ItemStack}.
     * <p>
     * This does not check if it conflicts with any enchantments already
     * applied to the item.
     *
     * @param item Item to test
     * @return True if the enchantment may be applied, otherwise False
     */
    @Override
    public boolean canEnchantItem(ItemStack item) {
        return false;
    }
}
