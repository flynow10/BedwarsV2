package com.wagologies.bedwarsv2.game.team.shop;

import com.wagologies.bedwarsv2.BedwarsV2;
import com.wagologies.bedwarsv2.game.team.shop.Shop;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

public class ShopListener implements Listener {

    private final Shop shop;

    public ShopListener(Shop shop)
    {
        this.shop = shop;
        Bukkit.getPluginManager().registerEvents(this, BedwarsV2.getInstance());
    }

    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent event)
    {
        if(!event.getInventory().equals(shop.getInventory())) return;
        event.setCancelled(true);
        final ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
        final Player p = (Player) event.getWhoClicked();
        shop.clickedItem(event.getSlot());
    }
    @EventHandler
    public void onInventoryClick(InventoryDragEvent e) {
        if (e.getInventory() == shop.getInventory()) {
            e.setCancelled(true);
        }
    }

}
