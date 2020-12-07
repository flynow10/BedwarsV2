package com.wagologies.bedwarsv2.game.listeners;

import com.github.juliarn.npc.event.PlayerNPCInteractEvent;
import com.wagologies.bedwarsv2.BedwarsV2;
import com.wagologies.bedwarsv2.game.player.Player;
import com.wagologies.bedwarsv2.game.team.shop.presets.ItemShopPreset;
import com.wagologies.bedwarsv2.game.team.shop.ShopPreset;
import com.wagologies.bedwarsv2.game.team.shop.presets.UpgradeShopPreset;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void ExplosionDamage(EntityDamageEvent event)
    {
        if(event.getEntity() instanceof org.bukkit.entity.Player)
        {
            org.bukkit.entity.Player player = (org.bukkit.entity.Player) event.getEntity();
            if(player.equals(this.player.getPlayer())) {
                if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) || event.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)) {
                    event.setDamage(0);
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

    @EventHandler
    public void PlayerDisconnect(PlayerQuitEvent event)
    {
        if(event.getPlayer().equals(player.getPlayer()))
        {
            player.Disconnect();
            event.setQuitMessage("");
        }
    }

    @EventHandler
    public void PlaceTNT(BlockPlaceEvent event)
    {
        if(event.getPlayer().equals(player.getPlayer())) {
            if (event.getBlockPlaced().getType().equals(Material.TNT)) {
                if(!event.isCancelled())
                    event.setCancelled(true);
                TNTPrimed tntPrimed = (TNTPrimed) event.getBlockPlaced().getWorld().spawnEntity(event.getBlockPlaced().getLocation().add(0.5,0,0.5), EntityType.PRIMED_TNT);
                tntPrimed.setMetadata("autoTnt",new FixedMetadataValue(BedwarsV2.getInstance(), true));
                ItemStack tntItem = event.getPlayer().getItemInHand();
                int consume = 1;
                if(event.getPlayer().getGameMode() == GameMode.CREATIVE)
                    consume = 0;
                if (tntItem.getAmount() == consume) {
                    event.getPlayer().getInventory().removeItem(new ItemStack(tntItem));
                } else {
                    tntItem.setAmount(tntItem.getAmount() - consume);
                }
                tntPrimed.setYield(4f);
            }
        }
    }

    @EventHandler
    public void SendFireball(PlayerInteractEvent event)
    {
        if(event.getPlayer().equals(this.player.getPlayer()))
        {
            if(event.getItem() == null)
                return;
            if(event.getItem().getType().equals(Material.FIREBALL))
            {
                if(!event.isCancelled())
                    event.setCancelled(true);
                Fireball fireballEntity = event.getPlayer().launchProjectile(Fireball.class);
                fireballEntity.setMetadata("flyingFireball", new FixedMetadataValue(BedwarsV2.getInstance(), true));
                fireballEntity.setIsIncendiary(true);
                ItemStack fireBall = event.getPlayer().getItemInHand();
                int consume = 1;
                if(event.getPlayer().getGameMode() == GameMode.CREATIVE)
                    consume = 0;
                if (fireBall.getAmount() == consume) {
                    event.getPlayer().getInventory().removeItem(new ItemStack(fireBall));
                } else {
                    fireBall.setAmount(fireBall.getAmount() - consume);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void HeightLimit(BlockPlaceEvent event)
    {
        if(event.getPlayer().equals(this.player.getPlayer()))
            if (event.getBlockPlaced().getY() > 140)
                if (!event.isCancelled())
                    event.setCancelled(true);
    }
}
