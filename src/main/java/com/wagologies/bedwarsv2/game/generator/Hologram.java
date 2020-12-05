package com.wagologies.bedwarsv2.game.generator;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class Hologram {
    List<ArmorStand> hologram = new ArrayList<>();
    private static double SPACING = 0.3;
    public Hologram(Location location, String...text)
    {
        location = location.add(0,-2,0);
        for (int i = 0; i < text.length; i++) {
            Location lineLocation = location.clone();
            lineLocation = lineLocation.add(0, ((text.length-i)* SPACING),0);
            hologram.add((ArmorStand) location.getWorld().spawnEntity(lineLocation, EntityType.ARMOR_STAND));
            ArmorStand line = hologram.get(i);
            line.setGravity(false);
            line.setVisible(false);
            line.setCanPickupItems(false);
            line.setRemoveWhenFarAway(false);
            line.setCustomName(text[i]);
            line.setCustomNameVisible(true);
        }
    }

    public boolean EditLine(int line, String newText)
    {
        if(line < hologram.size())
        {
            hologram.get(line).setCustomName(newText);
            return true;
        }
        return false;
    }
    public void Delete()
    {
        for (ArmorStand armorStand : hologram) {
            armorStand.remove();
        }
    }
}
