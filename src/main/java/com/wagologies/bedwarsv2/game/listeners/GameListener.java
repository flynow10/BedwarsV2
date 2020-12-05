package com.wagologies.bedwarsv2.game.listeners;

import com.wagologies.bedwarsv2.BedwarsV2;
import com.wagologies.bedwarsv2.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class GameListener implements Listener {

    private final Game game;

    /***** INITIALIZATION *****/

    public GameListener(Game game)
    {
        this.game = game;
        Bukkit.getPluginManager().registerEvents(this, BedwarsV2.getInstance());
    }

    /*** END INITIALIZATION ***/

    /***** GETTER METHODS *****/

    public World getWorld() { return game.getWorld(); }

    /*** END GETTER METHODS ***/

    /******** LISTENERS *******/

    @EventHandler
    public void WeatherListener(WeatherChangeEvent event)
    {
        if(event.getWorld().equals(getWorld()))
            if(event.toWeatherState())
                if(!event.isCancelled())
                    event.setCancelled(true);
    }

    @EventHandler
    public void ThunderListener(ThunderChangeEvent event)
    {
        if(event.getWorld().equals(getWorld()))
            if(event.toThunderState())
                if(!event.isCancelled())
                    event.setCancelled(true);
    }

    @EventHandler
    public void PlaceBlock(BlockPlaceEvent event)
    {
        if(event.getBlockPlaced().getLocation().getWorld().equals(getWorld()) && !event.isCancelled())
        {
            game.placedBlocks.add(event.getBlockPlaced());
        }
    }

    @EventHandler
    public void BreakBlock(BlockBreakEvent event)
    {
        if(event.getBlock().getLocation().getWorld().equals(getWorld()))
        {
            if(!game.placedBlocks.remove(event.getBlock()))
            {
                if(!event.isCancelled() && !event.getPlayer().getGameMode().equals(GameMode.CREATIVE))
                    event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void FoodDepletion(FoodLevelChangeEvent event)
    {
        if(event.getEntity().getLocation().getWorld().equals(getWorld()))
        {
            if(!event.isCancelled())
                event.setCancelled(true);
        }
    }
    /****** END LISTENERS *****/
}
