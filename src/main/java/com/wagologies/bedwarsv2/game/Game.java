package com.wagologies.bedwarsv2.game;

import com.wagologies.bedwarsv2.BedwarsV2;
import com.wagologies.bedwarsv2.game.events.Event;
import com.wagologies.bedwarsv2.game.events.EventManager;
import com.wagologies.bedwarsv2.game.generator.Generator;
import com.wagologies.bedwarsv2.game.listeners.GameListener;
import com.wagologies.bedwarsv2.game.menu.MainMenu;
import com.wagologies.bedwarsv2.game.menu.Menu;
import com.wagologies.bedwarsv2.game.team.Team;
import com.wagologies.bedwarsv2.utils.ConfigReader;
import com.wagologies.bedwarsv2.utils.DisplayPackets;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEnderDragon;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private List<Team> bedwarsTeams = new ArrayList<>();
    private ConfigReader config;
    private final World world;
    private boolean isRunning = true;
    public Objective bedInformationObjective, healthObjectiveTabList, healthObjectiveBelowName;
    public List<Generator> emeraldGens = new ArrayList<>(), diamondGens = new ArrayList<>();
    public List<Block> placedBlocks = new ArrayList<>();
    private GameListener listener;
    private Scoreboard bedwarsScoreboard;
    private final ScoreboardManager scoreboardManager;
    private EventManager eventManager;
    private boolean allBedsBroken = false;

    /***** INITIALIZATION *****/

    public Game(List<List<Player>> teamList, File templateFolder, File templateConfig)
    {
        config = new ConfigReader(templateConfig);
        world = BedwarsV2.getInstance().getCopier().copyTemplateWorld(templateFolder, "bedwars");
        listener = new GameListener(this);
        org.bukkit.scoreboard.ScoreboardManager sbm = Bukkit.getScoreboardManager();
        bedwarsScoreboard = sbm.getNewScoreboard();
        scoreboardManager = new ScoreboardManager(bedwarsScoreboard, ChatColor.YELLOW + ChatColor.BOLD.toString() + "Bed Wars");
        SetUpWorld();
        AddScoreboardObjectives(teamList);
        AssignTeams(teamList);
        UpdateHealth(teamList);
        SetupGenerators();
        SetUpEventManager();
        scoreboardManager.Set("--------------------", 0);
        scoreboardManager.Set("--------------------", getBedwarsTeams().size()+getExtraLines()+1);
    }

    public void AssignTeams(List<List<Player>> teamList)
    {
        for (int i = 0; i < teamList.size(); i++) {
            List<Player> playerList = teamList.get(i);
            Team team = new Team(this, i, playerList);
            bedwarsTeams.add(team);
        }
        for(int i = 0; i < teamList.size(); i++)
        {
            bedwarsTeams.get(i).updateScoreboard();
        }
        //VictoryCheck();
    }

    public void SetUpWorld()
    {
        world.setTime(6000);
        world.setGameRuleValue("doDaylightCycle", "false");
        world.setGameRuleValue("doFireTick", "false");
        world.setGameRuleValue("doMobSpawning", "false");
        world.setDifficulty(Difficulty.NORMAL);
    }

    public void SetupGenerators()
    {
        for (String emeralds : config.getConfigurationSection("Emeralds").getKeys(false)) {
            Generator emeraldGen = new Generator(config.getLocation("Emeralds." + emeralds, getWorld()));
            emeraldGen.setDisplayName(ChatColor.GREEN + "Emerald Generator");
            emeraldGen.setTicksBetweenSpawn(1300);
            emeraldGen.setItemsToProduce(new ItemStack(Material.EMERALD));
            emeraldGen.Start();
            emeraldGens.add(emeraldGen);
        }
        for (String diamonds : config.getConfigurationSection("Diamonds").getKeys(false)) {
            Generator diamondGen = new Generator(config.getLocation("Diamonds." + diamonds, getWorld()));
            diamondGen.setDisplayName(ChatColor.AQUA + "Diamond Generator");
            diamondGen.setTicksBetweenSpawn(600);
            diamondGen.setItemsToProduce(new ItemStack(Material.DIAMOND));
            diamondGen.Start();
            diamondGens.add(diamondGen);
        }
    }

    public void AddScoreboardObjectives(List<List<Player>> teamList)
    {


        // Below name objective
        healthObjectiveBelowName = bedwarsScoreboard.registerNewObjective("healthName", "health");
        healthObjectiveBelowName.setDisplaySlot(DisplaySlot.BELOW_NAME);
        healthObjectiveBelowName.setDisplayName("❤");
        // Tab list objective
        healthObjectiveTabList = bedwarsScoreboard.registerNewObjective("healthTab", "health");
        healthObjectiveTabList.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        for (List<Player> players : teamList) {
            for (Player player : players) {
                scoreboardManager.AddPlayer(player);
            }
        }
    }

    public void UpdateHealth(List<List<Player>> teamList)
    {
        for (List<Player> players : teamList) {
            for (Player player : players) {
                player.setHealth(player.getHealth());
            }
        }
    }

    public void SetUpEventManager()
    {
        eventManager = new EventManager(this);
        eventManager.AddQueueItems(new Event("Island Gen I", 20, (game) -> {
            for (Team bedwarsTeam : game.getBedwarsTeams()) {
                bedwarsTeam.getIronGenerator().setTicksBetweenSpawn(40);
                bedwarsTeam.getGoldGenerator().setTicksBetweenSpawn(160);
            }
        }));
        eventManager.AddQueueItems(new Event("Diamond Gen II", 90, game -> {
            for (Generator diamondGen : game.diamondGens) {
                diamondGen.setTicksBetweenSpawn(400);
            }
        }));
        eventManager.AddQueueItems(new Event("Emerald Gen II", 120, game -> {
            for (Generator emeraldGen : game.emeraldGens) {
                emeraldGen.setTicksBetweenSpawn(900);
            }
        }));
        eventManager.AddQueueItems(new Event("Diamond Gen III", 300, game -> {
            for (Generator diamondGen : game.diamondGens) {
                diamondGen.setTicksBetweenSpawn(300);
            }
        }));
        eventManager.AddQueueItems(new Event("Emerald Gen III", 300, game -> {
            for (Generator emeraldGen : game.emeraldGens) {
                emeraldGen.setTicksBetweenSpawn(700);
            }
        }));
        eventManager.AddQueueItems(new Event("Beds Break", 600, game -> {
            allBedsBroken = true;
            for (Team bedwarsTeam : game.getBedwarsTeams()) {
                bedwarsTeam.getBED().BreakBed();
            }
        }));
        eventManager.AddQueueItems(new Event("Sudden Death", 900, game -> {
            for (Team bedwarsTeam : bedwarsTeams) {
                if(bedwarsTeam.getPlayersAlive() > 0)
                {
                    EnderDragon dragon = (EnderDragon) game.getWorld().spawnEntity(new Location(game.getWorld(), 0,120,0), EntityType.ENDER_DRAGON);
                    dragon.setCustomName(ChatColor.valueOf(bedwarsTeam.getColor()) + bedwarsTeam.getName() + "'s dragon");
                }
            }
        }));
    }

    /*** END INITIALIZATION ***/

    /***** HELPER METHODS *****/

    public com.wagologies.bedwarsv2.game.player.Player getBedwarsPlayer(Player player)
    {
        for(Team team : bedwarsTeams)
        {
            for(com.wagologies.bedwarsv2.game.player.Player bedwarsPlayer : team.getPlayerList())
            {
                if(bedwarsPlayer.getPlayer().equals(player))
                {
                    return bedwarsPlayer;
                }
            }
        }
        return null;
    }

    /*** END HELPER METHODS ***/

    /***** PUBLIC METHODS *****/

    public void VictoryCheck()
    {
        int teamsAlive = 0;
        for(Team team : bedwarsTeams)
        {
            if(team.getPlayersAlive() > 0)
            {
                teamsAlive++;
            }
        }
        if(teamsAlive == 1)
        {
            for(Team team : bedwarsTeams)
            {
                if(team.getPlayersAlive() > 0)
                {
                    Victory(team);
                    return;
                }
            }
        }
    }

    public void Victory(Team victor)
    {
        isRunning = false;
        for(Team team : bedwarsTeams)
        {
            team.EndGame();
            if(team.equals(victor)) {
                team.Victory();
            }
        }
        Bukkit.getScheduler().runTaskLater(BedwarsV2.getInstance(), () -> {
            HandlerList.unregisterAll(listener);
            for (Generator emeraldGen : emeraldGens) {
                emeraldGen.Stop();
            }
            for (Generator diamondGen : diamondGens) {
                diamondGen.Stop();
            }
            for(Team team : bedwarsTeams)
            {
                team.RemoveListeners();
            }
            world.getPlayers().forEach(player -> {
                player.getInventory().clear();
                player.getEquipment().clear();
                player.setHealth(20);

            });
        }, 100);
    }

    public void OpenMenu(Player player)
    {
        new Menu(this, player, new MainMenu());
    }

    public void EndGame() {
        isRunning = false;
        for (Team bedwarsTeam : bedwarsTeams) {
            bedwarsTeam.EndGame(true);
        }
        HandlerList.unregisterAll(listener);
        for (Generator emeraldGen : emeraldGens) {
            emeraldGen.Stop();
        }
        for (Generator diamondGen : diamondGens) {
            diamondGen.Stop();
        }
        for(Team team : bedwarsTeams)
        {
            team.RemoveListeners();
        }
        world.getPlayers().forEach(player -> {
            player.getInventory().clear();
            player.getEquipment().clear();
            player.setHealth(20);
        });
    }

    /*** END PUBLIC METHODS ***/

    /***** GETTER METHODS *****/

    public World getWorld() { return world; }

    public ConfigReader getConfig() { return config; }

    public boolean getIsRunning() { return isRunning; }

    public List<Team> getBedwarsTeams() { return bedwarsTeams; }

    public Scoreboard getBedwarsScoreboard() { return bedwarsScoreboard; }

    public ScoreboardManager getScoreboardManager() { return scoreboardManager; }

    public boolean isAllBedsBroken() { return allBedsBroken; }

    public EventManager getEventManager() { return eventManager; }

    public int getExtraLines() { return 5; }
    /*** END GETTER METHODS ***/
}
