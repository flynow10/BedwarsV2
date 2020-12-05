package com.wagologies.bedwarsv2.game.generator;

import com.wagologies.bedwarsv2.BedwarsV2;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Generator {
    private final Location spawnLocation;
    private final String generatorId;
    private ItemStack[] itemsToProduce;
    private Hologram hologram;
    private int ticksBetweenSpawn;
    private int ticksTillSpawn;
    private boolean isRunning = false;
    private boolean hasHologram = true;
    private int maxItems = 4;
    private String displayName;
    public int spawnedItems;
    public Generator(Location location, String name, int ticksBetweenSpawn, ItemStack...producedItems)
    {
        this(location);
        hologram = new Hologram(location, name, String.valueOf(ticksBetweenSpawn/20));
        this.ticksBetweenSpawn = ticksBetweenSpawn;
        this.itemsToProduce = producedItems;
        ticksTillSpawn = ticksBetweenSpawn;
    }
    public Generator(Location location)
    {
        spawnLocation = location;
        hologram = new Hologram(location,"", "");
        generatorId = UUID.randomUUID().toString();
        new GeneratorListener(this);
    }

    private void secondCounter()
    {
        if(isRunning) {
            ticksTillSpawn -= 20;
            if (ticksTillSpawn <= 0) {
                ticksTillSpawn = ticksBetweenSpawn;
                SpawnItem();
            }
            if(hasHologram)
                hologram.EditLine(1, ChatColor.GOLD + String.valueOf(ticksTillSpawn / 20));
            new BukkitRunnable() {
                @Override
                public void run() {
                    secondCounter();
                }
            }.runTaskLater(BedwarsV2.getInstance(), 20);
        }
    }

    public void SpawnItem()
    {
        if(spawnedItems < maxItems) {
            for (ItemStack itemStack : itemsToProduce) {
                Item item = spawnLocation.getWorld().dropItem(spawnLocation, itemStack);
                item.setVelocity(new Vector(0, 0, 0));
                item.teleport(spawnLocation);
                item.setCustomName(generatorId);
            }
            spawnedItems++;
        }
    }

    public void Start()
    {
        if(!isRunning)
        {
            isRunning = true;
            secondCounter();
        }
    }

    public void Stop()
    {
        isRunning = false;
        if(hasHologram)
            hologram.Delete();
    }

    /***** GETTER METHODS *****/

    public String getGeneratorId() { return generatorId; }

    public String getDisplayName() { return displayName; }
    public ItemStack[] getItemsToProduce() { return itemsToProduce; }

    /*** END GETTER METHODS ***/

    /***** SETTER METHODS *****/

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
        if(hasHologram)
        {
            hologram.EditLine(0, this.displayName);
        }
    }

    public void setTicksBetweenSpawn(int ticksBetweenSpawn)
    {
        this.ticksBetweenSpawn = ticksBetweenSpawn;
    }

    public void setMaxItems(int maxItems)
    {
        this.maxItems = maxItems;
    }

    public void setHasHologram(boolean hasHologram)
    {
        if(this.hasHologram != hasHologram) {
            if (hasHologram) {
                hologram = new Hologram(spawnLocation, displayName, String.valueOf(ticksBetweenSpawn / 20));
            }
            else
            {
                hologram.Delete();
            }
            this.hasHologram = hasHologram;
        }
    }

    public void setItemsToProduce(ItemStack...itemsToProduce) { this.itemsToProduce = itemsToProduce; }
    /*** END SETTER METHODS ***/
}
