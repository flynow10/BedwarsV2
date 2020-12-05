package com.wagologies.bedwarsv2.game.listeners;

import com.wagologies.bedwarsv2.BedwarsV2;
import com.wagologies.bedwarsv2.game.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class TeamListener implements Listener {

    private Team team;

    public TeamListener(Team team)
    {
        this.team = team;
        Bukkit.getPluginManager().registerEvents(this, BedwarsV2.getInstance());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void ProtectedBlocks(BlockPlaceEvent event)
    {
        if(event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            if (event.getBlockPlaced().getLocation().distance(team.getSpawnLocation().getBlock().getLocation()) < 5) {
                event.getPlayer().sendMessage(ChatColor.RED + "This area is protected");
                if (!event.isCancelled())
                    event.setCancelled(true);
            }
        }
    }
}
