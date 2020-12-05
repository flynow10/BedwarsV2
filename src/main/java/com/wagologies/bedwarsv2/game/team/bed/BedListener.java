package com.wagologies.bedwarsv2.game.team.bed;

import com.wagologies.bedwarsv2.BedwarsV2;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class BedListener implements Listener {

    private Bed bed;

    public BedListener(Bed bed)
    {
        this.bed = bed;
        Bukkit.getPluginManager().registerEvents(this, BedwarsV2.getInstance());
    }
    /*@EventHandler
    public void EnderDragonBreak(EntityChangeBlockEvent event)
    {
        if(event.getBlock().equals(bed.getBedBlock(1).getBlock()) || event.getBlock().equals(bed.getBedBlock(2).getBlock())){
            if(!event.isCancelled())
                event.setCancelled(true);
        }
    }*/

    @EventHandler
    public void BedBreak(BlockBreakEvent event)
    {
        if(event.getBlock().getType() == Material.BED_BLOCK)
        {
            if(event.getBlock().equals(bed.getBedBlock(1).getBlock()) || event.getBlock().equals(bed.getBedBlock(2).getBlock())) {
                if(!event.isCancelled())
                    event.setCancelled(true);
                if(!bed.IsPlayerOnTeam(event.getPlayer())) {
                    bed.BreakBed();
                }
                else
                {
                    event.getPlayer().sendMessage(ChatColor.RED + "You can't break your own bed!");
                }
                return;
            }
            return;
        }
    }
}
