package com.wagologies.bedwarsv2.game.team.shop.presets;

import com.wagologies.bedwarsv2.game.team.shop.ShopPreset;
import com.wagologies.bedwarsv2.game.team.shop.item.ShopCategory;
import com.wagologies.bedwarsv2.game.team.shop.item.ShopItem;
import com.wagologies.bedwarsv2.utils.GlowEnchantment;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class UpgradeShopPreset implements ShopPreset {
    List<ShopItem> items = new ArrayList<>();

    public UpgradeShopPreset()
    {
        AddUpgrades();
        AddIslandBuffs();
    }

    private void AddUpgrades()
    {
        items.add(new ShopItem(ShopCategory.UPGRADES, player -> {
            if(!player.getTeam().getHasSharpness())
            {
                ItemStack sharpness = new ItemStack(Material.IRON_SWORD);
                ItemMeta sharpnessMeta = sharpness.getItemMeta();
                sharpnessMeta.setDisplayName("Sharpness I");
                sharpness.setItemMeta(sharpnessMeta);
                sharpness = GlowEnchantment.ApplyGlow(sharpness);
                return sharpness;
            }
            else
            {
                ItemStack maxed = new ItemStack(Material.BARRIER);
                ItemMeta maxedMeta = maxed.getItemMeta();
                maxedMeta.setDisplayName(ChatColor.RED + "Sharpness Maxed!");
                maxed.setItemMeta(maxedMeta);
                maxed = GlowEnchantment.ApplyGlow(maxed);
                return maxed;
            }
        }, player -> Material.DIAMOND, player -> {
            if(player.getTeam().getHasSharpness())
                return Integer.MAX_VALUE;
            return 4;
        }, player ->
        {
            player.getTeam().setHasSharpness(true);
            return true;
        }));
        items.add(new ShopItem(ShopCategory.UPGRADES, player -> {
            ItemStack stack = new ItemStack(Material.IRON_CHESTPLATE);
            ItemMeta stackMeta = stack.getItemMeta();
            switch (player.getTeam().getProtectionLevel())
            {
                case 0:
                    stackMeta.setDisplayName("Protection I");
                    break;
                case 1:
                    stackMeta.setDisplayName("Protection II");
                    break;
                case 2:
                    stackMeta.setDisplayName("Protection III");
                    break;
                case 3:
                    stackMeta.setDisplayName("Protection IV");
                    break;
                case 4:
                    ItemStack maxed = new ItemStack(Material.BARRIER);
                    ItemMeta maxedMeta = maxed.getItemMeta();
                    maxedMeta.setDisplayName("Protection Maxed");
                    maxed.setItemMeta(maxedMeta);
                    maxed = GlowEnchantment.ApplyGlow(maxed);
                    return maxed;
                default:
                    stackMeta.setDisplayName("Protection NaN");
                    break;
            }
            stack.setItemMeta(stackMeta);
            stack = GlowEnchantment.ApplyGlow(stack);
            return stack;
        }, player -> Material.DIAMOND, player -> {
            switch (player.getTeam().getProtectionLevel())
            {
                case 0:
                    return 2;
                case 1:
                    return 4;
                case 2:
                    return 8;
                case 3:
                    return 16;
                default:
                    return Integer.MAX_VALUE;
            }
        }, player -> player.getTeam().UpgradeProtection()));
    }

    private void AddIslandBuffs()
    {
        items.add(new ShopItem(ShopCategory.ISLANDBUFFS, player -> {
            if(player.getTeam().getBED().getIsBedBroken() && !player.getTeam().getGame().isAllBedsBroken())
            {
                ItemStack stack = new ItemStack(Material.BED);
                ItemMeta stackMeta = stack.getItemMeta();
                stackMeta.setDisplayName("Respawn Bed");
                stack.setItemMeta(stackMeta);
                return stack;
            }
            else
            {

                ItemStack maxed = new ItemStack(Material.BARRIER);
                ItemMeta maxedMeta = maxed.getItemMeta();
                if(!player.getTeam().getGame().isAllBedsBroken())
                    maxedMeta.setDisplayName("You're bed has not been broken!");
                else
                    maxedMeta.setDisplayName("You cannot respawn your bed at this point!");
                maxed.setItemMeta(maxedMeta);
                maxed = GlowEnchantment.ApplyGlow(maxed);
                return maxed;
            }
        }, player -> Material.DIAMOND, player -> 120, player -> {
            player.getTeam().getBED().RespawnBed();
            return true;
        }));
    }

    @Override
    public ShopCategory getFirstCategory() {
        return ShopCategory.UPGRADES;
    }

    @Override
    public List<ShopItem> getShopItems() {
        return items;
    }

    @Override
    public List<ShopCategory> getCategories() {
        List<ShopCategory> categories = new ArrayList<>();
        categories.add(ShopCategory.UPGRADES);
        categories.add(ShopCategory.ISLANDBUFFS);
        return categories;
    }

    @Override
    public String getName() {
        return "Upgrade Shop";
    }
}
