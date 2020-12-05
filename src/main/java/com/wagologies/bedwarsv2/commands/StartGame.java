package com.wagologies.bedwarsv2.commands;

import com.wagologies.bedwarsv2.BedwarsV2;
import com.wagologies.bedwarsv2.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class StartGame implements CommandExecutor {
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
            List<List<Player>> teams = new ArrayList<>();
            teams.add(new ArrayList<>());
            teams.get(0).add(player);
            teams.add(new ArrayList<>());
            teams.add(new ArrayList<>());
            teams.add(new ArrayList<>());
            teams.add(new ArrayList<>());
            teams.add(new ArrayList<>());
            teams.add(new ArrayList<>());
            teams.add(new ArrayList<>());
            if(args.length == 1)
            {
                Player player2 = Bukkit.getPlayer(args[0]);
                teams.get(ThreadLocalRandom.current().nextInt(1, 8)).add(player2);
            }
            BedwarsV2.getInstance().game = new Game(teams, new File(BedwarsV2.getInstance().getCopier().WORLD_FOLDER.getAbsolutePath() + File.separatorChar + "Bedwars Template 3"), new File(BedwarsV2.getInstance().getCopier().WORLD_FOLDER.getAbsolutePath() + File.separatorChar + "Bedwars Template3.yml"));
            return true;
        }
        else
        {
            sender.sendMessage("This can only be run by a player");
            return true;
        }
    }
}
