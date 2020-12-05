package com.wagologies.bedwarsv2.game.team.shop;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.wagologies.bedwarsv2.game.player.Player;
import com.wagologies.bedwarsv2.game.team.shop.item.ShopCategory;
import com.wagologies.bedwarsv2.game.team.shop.item.ShopItem;
import com.wagologies.bedwarsv2.utils.GlowEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class Shop {
    private Inventory inventory;
    private ShopPreset preset;
    private ShopCategory category;
    private HashMap<Integer, ShopItem> shopItemHashMap = new HashMap<>();
    private HashMap<Integer, ShopCategory> shopCategoryHashMap = new HashMap<>();
    private Player player;

    /***** INITIALIZATION *****/

    public Shop(ShopPreset preset, Player player)
    {
        this.preset = preset;
        this.player = player;
        inventory = Bukkit.createInventory(null, 54, preset.getName());
        new ShopListener(this);
        category = preset.getFirstCategory();
        ResetInventory();
    }

    public void ResetInventory()
    {
        inventory.clear();
        shopItemHashMap.clear();
        shopCategoryHashMap.clear();
        for(int i = 0; i < Integer.min(preset.getCategories().size(), 9); i++) {
            ShopCategory category = preset.getCategories().get(i);
            ItemStack placeHolder = category.getPlaceHolder();
            if(this.category.equals(category))
                placeHolder = GlowEnchantment.ApplyGlow(placeHolder);
            inventory.setItem(i, placeHolder);
            shopCategoryHashMap.put(i, category);
        }
        List<ShopItem> currentCategoryItems = Lists.newArrayList(Collections2.filter(preset.getShopItems(), (x) -> x.getCategory().equals(category)));
        for(int i = 0; i < Integer.min(currentCategoryItems.size(), 36); i++)
        {
            ShopItem item = currentCategoryItems.get(i);
            ItemStack stack = item.getPlaceholder().apply(player);
            ItemMeta stackMeta = stack.getItemMeta();
            List<String> lore = new ArrayList<>();
            if(stackMeta.getLore() != null)
                lore.addAll(stackMeta.getLore());
            String currencyDisplayname = "";
            String currencyChatColor = "";
            Function<Player, Integer> cost = item.getCost();
            switch (item.getCurrency().apply(player))
            {
                case IRON_INGOT:
                    currencyDisplayname = "Iron Ingot" + ((cost.apply(player) > 1) ? "s" : "");
                    currencyChatColor = String.valueOf(ChatColor.GRAY);
                    break;
                case GOLD_INGOT:
                    currencyDisplayname = "Gold Ingot" + ((cost.apply(player) > 1) ? "s" : "");
                    currencyChatColor = String.valueOf(ChatColor.YELLOW);
                    break;
                case DIAMOND:
                    currencyDisplayname = "Diamond" + ((cost.apply(player) > 1) ? "s" : "");
                    currencyChatColor = String.valueOf(ChatColor.AQUA);
                    break;
                case EMERALD:
                    currencyDisplayname = "Emerald" + ((cost.apply(player) > 1) ? "s" : "");
                    currencyChatColor = String.valueOf(ChatColor.GREEN);
                    break;
            }
            lore.add("");
            if(!(cost.apply(player) == Integer.MAX_VALUE))
                lore.add(currencyChatColor+ "Costs " + cost.apply(player) + " " + currencyDisplayname);
            else
                lore.add(ChatColor.RED + "MAX LEVEL");
            stackMeta.setLore(lore);
            stack.setItemMeta(stackMeta);
            inventory.setItem(i+18, stack);
            shopItemHashMap.put(i+18, item);
        }
        for(int i = 0; i < 9; i++)
        {
            if(inventory.getItem(i) == null || inventory.getItem(i).getType().equals(Material.AIR))
            {
                inventory.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE));
            }
        }
        for(int i = 9; i < 18; i++)
        {
            if(inventory.getItem(i) == null || inventory.getItem(i).getType().equals(Material.AIR))
            {
                inventory.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7));
            }
        }
        for(int i = 18; i < 54; i++)
        {
            if(inventory.getItem(i) == null || inventory.getItem(i).getType().equals(Material.AIR))
            {
                inventory.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE));
            }
        }
    }

    /*** END INITIALIZATION ***/

    /**** PURCHASE METHODS ****/

    public void clickedItem(int slot)
    {
        if(slot < 18)
        {
            if(shopCategoryHashMap.containsKey(slot))
            {
                category = shopCategoryHashMap.get(slot);
                ResetInventory();
            }
        }
        else if(slot >= 18)
        {
            if(shopItemHashMap.containsKey(slot))
            {
                Purchase(shopItemHashMap.get(slot));
                ResetInventory();
            }
        }
    }

    public void Purchase(ShopItem item)
    {
        if(player.getPlayer().getInventory().contains(item.getCurrency().apply(player), item.getCost().apply(player)))
        {
            if(item.getCurrency() == null || player.getPlayer().getInventory() == null)
                return;
            int cost = item.getCost().apply(player);
            String name = item.getPlaceholder().apply(player).getItemMeta().getDisplayName();
            Material currency = item.getCurrency().apply(player);
            if(item.getCallback().apply(player))
            {
                player.getPlayer().getInventory().removeItem(new ItemStack(currency, cost));
                player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ORB_PICKUP, 1, 1);
                player.getPlayer().sendMessage(ChatColor.GREEN + "You purchased: " + ChatColor.GOLD + name);
            }
        }
        else
        {
            if(item.getCost().apply(player) == Integer.MAX_VALUE)
            {
                player.getPlayer().sendMessage(ChatColor.RED + "This upgrade is already max level!");
            }
            else
            {
                player.getPlayer().sendMessage(ChatColor.RED + "You don't have enough to purchase this!");
            }
        }
        ResetInventory();
    }

    /** END PURCHASE METHODS **/

    /***** GETTER METHODS *****/

    public Inventory getInventory() { return inventory; }

    /*** END GETTER METHODS ***/

}
