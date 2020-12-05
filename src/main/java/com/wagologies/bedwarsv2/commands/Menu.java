package com.wagologies.bedwarsv2.commands;

import com.wagologies.bedwarsv2.BedwarsV2;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEnderDragon;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;

public class Menu implements CommandExecutor {

    /**
     * Executes the given command, returning its success
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player)
        {
            Player player = ((Player) sender);

            if(BedwarsV2.getInstance().game != null) {
                if(BedwarsV2.getInstance().game.getIsRunning()) {
                    if (player.getLocation().getWorld().equals(BedwarsV2.getInstance().game.getWorld())) {
                        BedwarsV2.getInstance().game.OpenMenu(player);
                        return true;
                    }
                }
            }
            player.sendMessage(ChatColor.RED + "You are not in a game!");
            return true;
        }
        sender.sendMessage("You must be a player to use this command!");
        return true;
    }
}
