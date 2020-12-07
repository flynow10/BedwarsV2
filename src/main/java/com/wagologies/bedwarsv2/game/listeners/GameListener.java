package com.wagologies.bedwarsv2.game.listeners;

import com.wagologies.bedwarsv2.BedwarsV2;
import com.wagologies.bedwarsv2.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

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

    @EventHandler
    public void OnTNTExplode(EntityExplodeEvent event)
    {
        if(event.getEntity().hasMetadata("autoTnt"))
        {
            event.blockList().removeIf(block -> !game.placedBlocks.contains(block));
            TNTPrimed tntPrimed = (TNTPrimed) event.getEntity();
            setKnockback(event.getEntity(), 2*1.4);
        }
    }
    @EventHandler
    public void OnFireballExplode(EntityExplodeEvent event)
    {
        if(event.getEntity().hasMetadata("flyingFireball"))
        {
            event.blockList().removeIf(block -> !game.placedBlocks.contains(block));
            setKnockback(event.getEntity(), 2*1.4);
        }
    }
    public boolean setKnockback(Entity center, double radius) {

        Location target = center.getLocation();

        int maxHeight = 8;

        List<Entity> nearbyEntities = center.getNearbyEntities(radius, radius, radius);

        if ((nearbyEntities == null) || nearbyEntities.isEmpty()) {
            return false;
        }

        List<LivingEntity> validEntities = new ArrayList<>();

        for (Entity entity : nearbyEntities) {
            if (entity.isValid() && (entity instanceof LivingEntity)) {
                validEntities.add((LivingEntity) entity);
            }
        }

        for (LivingEntity entity : validEntities) {

            if ((entity instanceof Player)) {
                Player player = (Player) entity;
                if (player.isFlying()||player.isConversing()) {
                    continue;
                }

            }

            double distance = (maxHeight - entity.getLocation().distance(target));

            double TWO_PI = 1.76 * Math.PI;

            Vector variantVel = entity.getLocation().getDirection().multiply(-1);
            variantVel = variantVel.setY(distance / TWO_PI);

            entity.setVelocity(variantVel);

        }

        return true;
    }
    /****** END LISTENERS *****/
}
