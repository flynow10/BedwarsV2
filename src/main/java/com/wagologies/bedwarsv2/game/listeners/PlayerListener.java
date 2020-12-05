package com.wagologies.bedwarsv2.game.listeners;

import com.github.juliarn.npc.event.PlayerNPCInteractEvent;
import com.wagologies.bedwarsv2.BedwarsV2;
import com.wagologies.bedwarsv2.game.player.Player;
import com.wagologies.bedwarsv2.game.team.shop.presets.ItemShopPreset;
import com.wagologies.bedwarsv2.game.team.shop.ShopPreset;
import com.wagologies.bedwarsv2.game.team.shop.presets.UpgradeShopPreset;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerListener implements Listener {
    private Player player;
    public PlayerListener(Player player)
    {
        this.player = player;
        Bukkit.getPluginManager().registerEvents(this, BedwarsV2.getInstance());
    }

    @EventHandler
    public void ShopOpen(PlayerNPCInteractEvent event) {
        ShopPreset preset;
        if(event.getPlayer().equals(player.getPlayer())) {
            if (event.getNPC().getEntityId() == player.getTeam().getItemShopId()) {
                preset = new ItemShopPreset();
            }
            else if(event.getNPC().getEntityId() == player.getTeam().getUpgradeShopId())
            {
                preset = new UpgradeShopPreset();
            }
            else
                return;
            player.OpenShop(preset);
        }
    }
    @EventHandler
    public void DisableCrafting(CraftItemEvent event)
    {
        if(event.getWhoClicked() instanceof org.bukkit.entity.Player)
        {
            org.bukkit.entity.Player player = (org.bukkit.entity.Player) event.getWhoClicked();
            if(player.equals(this.player.getPlayer()))
                if(!event.isCancelled())
                    event.setCancelled(true);
        }
    }
    @EventHandler
    public void DeathCheck(EntityDamageEvent event)
    {
        if(event.getEntity() instanceof org.bukkit.entity.Player)
        {
            org.bukkit.entity.Player player = (org.bukkit.entity.Player) event.getEntity();
            if(player.equals(this.player.getPlayer()))
            {
                if(!this.player.getTeam().getGame().getIsRunning())
                {
                    if(!event.isCancelled())
                        event.setCancelled(true);
                    return;
                }
                if(event.getCause().equals(EntityDamageEvent.DamageCause.VOID) || player.getHealth() - event.getDamage() <= 0)
                {
                    if(!event.isCancelled())
                        event.setCancelled(true);
                    this.player.OnDeath();
                }
            }
        }
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void preventArmorRemoving(InventoryClickEvent event)
    {
        if(event.getSlotType() == InventoryType.SlotType.ARMOR)
        {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void ChatPrefix(AsyncPlayerChatEvent event)
    {
        if(event.getPlayer().equals(player.getPlayer()))
        {
            event.setFormat(ChatColor.WHITE + "[" + player.getTeam().getName() + ChatColor.WHITE + "]" + ChatColor.valueOf(player.getTeam().getColor()) + " %s" + ChatColor.WHITE + "> %s");
        }
    }
}
