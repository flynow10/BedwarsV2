package com.wagologies.bedwarsv2.game.generator;

import com.wagologies.bedwarsv2.BedwarsV2;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class GeneratorListener implements Listener {
    private final Generator generator;
    public GeneratorListener(Generator generator)
    {
        this.generator = generator;
        Bukkit.getPluginManager().registerEvents(this, BedwarsV2.getInstance());
    }

    @EventHandler
    public void ItemPickup(PlayerPickupItemEvent event)
    {
        if(event.getItem().getCustomName() == generator.getGeneratorId()) {
            generator.spawnedItems -= event.getItem().getItemStack().getAmount();
        }
    }
    @EventHandler
    public void ItemDespawn(ItemDespawnEvent event)
    {
        if(event.getEntity().getCustomName() == generator.getGeneratorId()) {
            if(!event.isCancelled())
                event.setCancelled(true);
        }
    }
}
