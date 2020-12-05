package com.wagologies.bedwarsv2.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigReader extends YamlConfiguration {
    public ConfigReader() {
        super();
    }

    public ConfigReader(File preLoadFile)
    {
        try {
            load(preLoadFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public Location getLocation(String path, World world)
    {
        return getLocation(path, null, world);
    }

    public Location getLocation(String path, Location def, World world)
    {
        double x = getDouble(path + ".x", Double.NaN);
        double y = getDouble(path + ".y", Double.NaN);
        double z = getDouble(path + ".z", Double.NaN);
        return (Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z)) ? def : new Location(world, x, y, z);
    }
}
