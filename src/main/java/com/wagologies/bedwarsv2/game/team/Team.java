package com.wagologies.bedwarsv2.game.team;

import com.github.juliarn.npc.NPC;
import com.github.juliarn.npc.NPCPool;
import com.github.juliarn.npc.profile.Profile;
import com.google.common.collect.Lists;
import com.wagologies.bedwarsv2.BedwarsV2;
import com.wagologies.bedwarsv2.game.Game;
import com.wagologies.bedwarsv2.game.generator.Generator;
import com.wagologies.bedwarsv2.game.listeners.TeamListener;
import com.wagologies.bedwarsv2.game.player.Player;
import com.wagologies.bedwarsv2.game.team.bed.Bed;
import com.wagologies.bedwarsv2.utils.DisplayPackets;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Team {

    /******** CONSTANTS *******/

    private final Game GAME;
    private final int ID;
    private final String CHECKMARK = ChatColor.GREEN + "✔";
    private final String X_SYMBOL = ChatColor.RED + "\u2718";
    private final Generator ironGenerator, goldGenerator;
    private final Bed BED;

    /****** END CONSTANTS *****/

    /******** VARIABLES *******/

    private String name, color;
    private List<Player> playerList = new ArrayList<>();
    private int playersAlive;
    private boolean hasSharpness;
    private Score bedScore;
    private int generatorSpeed = 20;
    private int protectionLevel = 0;
    private int itemShopId;
    private int upgradeShopId;
    private BukkitTask lookAtPlayerTask;
    private TeamListener listener;

    /****** END VARIABLES *****/

    /***** INITIALIZATION *****/

    public Team(Game game, int ID, List<org.bukkit.entity.Player> playerList)
    {
        this.GAME = game;
        this.ID = ID;
        this.BED = new Bed(this);
        this.ironGenerator = new Generator(getGeneratorLocation());
        this.goldGenerator = new Generator(getGeneratorLocation());
        this.listener = new TeamListener(this);
        FillConfig();
        FillPlayerList(playerList);
        playersAlive = playerList.size();
        AddGenerators();
        SpawnNPCS();
        updateScoreboard();
        if(playersAlive == 0)
        {
            BreakBed(false);
        }
    }

    private void FillConfig()
    {
        name = getGame().getConfig().getString("teams.team" + getID() + ".name");
        color = getGame().getConfig().getString("teams.team" + getID() + ".color");
    }

    private void FillPlayerList(List<org.bukkit.entity.Player> playerList)
    {
        for (int i = 0; i < playerList.size(); i++) {
            org.bukkit.entity.Player player = playerList.get(i);
            Player bedwarsPlayer = new Player(this, player);
            this.playerList.add(bedwarsPlayer);
        }
    }
    private void AddGenerators()
    {
        goldGenerator.setItemsToProduce(new ItemStack(Material.GOLD_INGOT));
        goldGenerator.setTicksBetweenSpawn(80);
        goldGenerator.setMaxItems(8);
        goldGenerator.setHasHologram(false);
        goldGenerator.Start();
        ironGenerator.setItemsToProduce(new ItemStack(Material.IRON_INGOT));
        ironGenerator.setTicksBetweenSpawn(20);
        ironGenerator.setMaxItems(40);
        ironGenerator.setHasHologram(false);
        ironGenerator.Start();
    }
    private void SpawnNPCS()
    {
        Location itemShopLocation = getGame().getConfig().getLocation("teams.team" + ID + ".itemVillager", getGame().getWorld());
        Location upgradeShopLocation = getGame().getConfig().getLocation("teams.team" + ID + ".soloVillager", getGame().getWorld());
        /*
        if(playerList.size() > 0) {
            shopNPC = new ShopNPC(itemShopLocation, "Item Shop", playerList.get(0).getPlayer().getUniqueId(), new ItemShopPreset());
            upgradeNPC = new ShopNPC(upgradeShopLocation, "Upgrade Shop", playerList.get(0).getPlayer().getUniqueId(), new ItemShopPreset());
        }
        else
        {
            shopNPC = new ShopNPC(itemShopLocation, "Item Shop", new ItemShopPreset());
            upgradeNPC = new ShopNPC(upgradeShopLocation, "Upgrade Shop", new ItemShopPreset());
        }
        getGame().healthObjectiveBelowName.getScoreboard().resetScores(shopNPC.getNpc().getName());
        getGame().healthObjectiveBelowName.getScoreboard().resetScores(upgradeNPC.getNpc().getName());
        lookAtPlayerTask = Bukkit.getScheduler().runTaskTimer(BedwarsV2.getInstance(), () -> {
            for(Player player : playerList)
            {
                shopNPC.LookAt(player.getPlayer(), player.getPlayer().getLocation());
                upgradeNPC.LookAt(player.getPlayer(), player.getPlayer().getLocation());
            }
        }, 1,1);
         */
        if(playerList.size() > 0) {
            List<Profile.Property> propertyList = new ArrayList<>();
            propertyList.add(new Profile.Property("textures",
                    "ewogICJ0aW1lc3RhbXAiIDogMTYwNjkxOTcwNzU1OSwKICAicHJvZmlsZUlkIiA6ICJhYjNiMjRkNjRiZDk0NWRmOGI3OThjOTA1MmM2YTcwZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJfY29waWxvdF8iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmIzNGMyZTUxMzc5NmI0OTJiNGM3NjUyOTFlOTAyMTBkMjgwM2NlZDZkNWQzYzhhZTJmZGMxYjAyNWI5YzQwNiIKICAgIH0KICB9Cn0=",
                    "UP9yTVEWR0GvmQJu4YSqNPEMgstbBJNrgugPkAqH5+2SepHIk+JjXfRrbiRmSk2nEkMoPwqYoMli8utN8t3+QhXWF1tP5BKStgkKOS55YkPJ8hDB0QWY9HtDAAi9q2iTdOkhtYRnvnQ7BRjkb6jSySMTT+Op3lQ7I3K+zTFOMGz/uA02wU1VfvTS+dCcehYOJEcMklpxa0rwfJ4JOU1PikJt+31RRrB14+Mm7TaJUhZ/7xJ6BcmxlpE1TxKJ8Ra68n2CBMoUO6JedVzQyMARdwvnIntA8rXbO19DBECfrbYxq6OcIaShwyUBmN/vY7NI1ydJ//QuP+MzacnEcD6ZsAqxHnjAuJeH66dYYDpb/tNMQ+IaZu0uf08MSlYt6IaMN2DnpYOo+gHpQeC1C1yisSQzcKAUZ8I28AVGf+SabhZJem5ygnVu0OC6UFVwTZU37kAiYIS7IzXoDs0rSXIfPJkRsWQGvT0FiNWFZYm1KLYWiR/c6bc+obr8TaIDJQJbkYux+HMGEsxmYqX1T3H635wWXovY5IwoXs/Sr64gz2HSWs5aaUYlgCpBwnkaIio98Otk1LQpqB+0acCCxI4KxLbgfL1747KDhzRAN8vW9aRaw0kN1f27cx710KwuR79fTW5UNWTF90o86Nt5RDbwYxXgJo7RyHk4ew/zlk1NGew="));
            Profile profile = new Profile("Item Shop", propertyList);
            profile.setUniqueId(UUID.randomUUID());
            itemShopId = new NPC.Builder(profile)
                    .imitatePlayer(false)
                    .lookAtPlayer(true)
                    .location(itemShopLocation)
                    .build(BedwarsV2.npcPool)
                    .getEntityId();
            List<Profile.Property> upgradePropertyList = new ArrayList<>();
            propertyList.add(new Profile.Property("textures",
                    "ewogICJ0aW1lc3RhbXAiIDogMTYwNjkxOTcwNzU1OSwKICAicHJvZmlsZUlkIiA6ICJhYjNiMjRkNjRiZDk0NWRmOGI3OThjOTA1MmM2YTcwZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJfY29waWxvdF8iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmIzNGMyZTUxMzc5NmI0OTJiNGM3NjUyOTFlOTAyMTBkMjgwM2NlZDZkNWQzYzhhZTJmZGMxYjAyNWI5YzQwNiIKICAgIH0KICB9Cn0=",
                    "UP9yTVEWR0GvmQJu4YSqNPEMgstbBJNrgugPkAqH5+2SepHIk+JjXfRrbiRmSk2nEkMoPwqYoMli8utN8t3+QhXWF1tP5BKStgkKOS55YkPJ8hDB0QWY9HtDAAi9q2iTdOkhtYRnvnQ7BRjkb6jSySMTT+Op3lQ7I3K+zTFOMGz/uA02wU1VfvTS+dCcehYOJEcMklpxa0rwfJ4JOU1PikJt+31RRrB14+Mm7TaJUhZ/7xJ6BcmxlpE1TxKJ8Ra68n2CBMoUO6JedVzQyMARdwvnIntA8rXbO19DBECfrbYxq6OcIaShwyUBmN/vY7NI1ydJ//QuP+MzacnEcD6ZsAqxHnjAuJeH66dYYDpb/tNMQ+IaZu0uf08MSlYt6IaMN2DnpYOo+gHpQeC1C1yisSQzcKAUZ8I28AVGf+SabhZJem5ygnVu0OC6UFVwTZU37kAiYIS7IzXoDs0rSXIfPJkRsWQGvT0FiNWFZYm1KLYWiR/c6bc+obr8TaIDJQJbkYux+HMGEsxmYqX1T3H635wWXovY5IwoXs/Sr64gz2HSWs5aaUYlgCpBwnkaIio98Otk1LQpqB+0acCCxI4KxLbgfL1747KDhzRAN8vW9aRaw0kN1f27cx710KwuR79fTW5UNWTF90o86Nt5RDbwYxXgJo7RyHk4ew/zlk1NGew="));
            Profile upgrade_shop = new Profile("Upgrade Shop", upgradePropertyList);
            upgrade_shop.setUniqueId(UUID.randomUUID());
            upgradeShopId = new NPC.Builder(upgrade_shop)
                    .imitatePlayer(false)
                    .location(upgradeShopLocation)
                    .lookAtPlayer(true)
                    .build(BedwarsV2.npcPool)
                    .getEntityId();
        }
        else
        {

        }
    }

    /*** END INITIALIZATION ***/

    /***** PUBLIC METHODS *****/
    public void BreakBed()
    {
        BreakBed(true);
    }
    public void BreakBed(boolean effect)
    {
        if(!effect)
        {
            getBedBlock(1).getBlock().setType(Material.AIR, false);
            getBedBlock(2).getBlock().setType(Material.AIR, false);
            BED.isBedBroken = true;
        }
        for (Player player : playerList) {
            player.BreakBed();
        }
        updateScoreboard();
        if(effect) {
            getGame().getWorld().playSound(getBedBlock(1), Sound.ENDERDRAGON_GROWL, 10, 1);
            getGame().getWorld().strikeLightningEffect(getBedBlock(1));
            DisplayPackets.sendJsonMessage(getGame().getWorld(), "[{text:'" + getName().replace("'", "\\'").replace("\"", "\\\"") +"\\'s', color:'" + getColor().toLowerCase() +"', bold: true},{ text:' bed was broken!', color:'red', bold: false}]");
        }
    }

    public void RespawnBed()
    {
        for(Player player : playerList)
        {
            player.RespawnBed();
        }
        updateScoreboard();
        DisplayPackets.sendJsonMessage(getGame().getWorld(), "[{text:'" + getName().replace("'", "\\'").replace("\"", "\\\"") +"\\'s', color:'" + getColor().toLowerCase() +"', bold: true},{ text:' bed was respawned!', color:'green', bold: false}]");
    }

    public void EndGame()
    {
        EndGame(false);
    }
    public void EndGame(boolean prematurly)
    {
        ironGenerator.Stop();
        goldGenerator.Stop();
        BedwarsV2.npcPool.removeNPC(upgradeShopId);
        BedwarsV2.npcPool.removeNPC(itemShopId);
        //lookAtPlayerTask.cancel();
        for (Player player : getPlayerList()) {
            if(!prematurly) {
                DisplayPackets.sendTitle(player.getPlayer(), ChatColor.RED + "Game Over", "Maybe next time", 10, 100, 10);
            }
            else
            {
                DisplayPackets.sendTitle(player.getPlayer(), ChatColor.RED + "Game Ended", "The game was ended by an admin", 10, 100 ,10);
            }
        }
    }

    public void Victory()
    {
        for (Player player : getPlayerList()) {
            DisplayPackets.sendTitle(player.getPlayer(), ChatColor.YELLOW + "VICTORY!", "Nice Job!", 10 ,100 ,10);
        }
    }

    public void RemoveListeners()
    {
        for (Player player : playerList) {
            player.RemoveListener();
        }
        BED.UnregisterListener();
        HandlerList.unregisterAll(listener);
    }

    /*** END PUBLIC METHODS ***/

    /***** PRIVATE METHODS ****/

    public void updateScoreboard()
    {
        String colorText = ChatColor.valueOf(color).toString();
        String entry = colorText + ChatColor.BOLD.toString() + name + " " + getObjectiveString();
        getGame().getScoreboardManager().Set(entry, ID + getGame().getExtraLines());
        for (Player player : playerList) {
            getGame().getScoreboardManager().Set(player.getPlayer(), entry + ChatColor.RESET + ChatColor.GRAY + " (You)", ID + getGame().getExtraLines());
        }
    }

    /*** END PRIVATE METHODS **/

    /****** DEATH METHODS *****/

    public void OnFinalDeath(Player player)
    {
        playersAlive--;
        getGame().VictoryCheck();
        updateScoreboard();
    }

    public void PlayerDisconnect(Player player)
    {
        playersAlive--;
        if(playersAlive == 0 && !BED.getIsBedBroken())
            BED.BreakBed();
        playerList.remove(player);
        getGame().VictoryCheck();
        updateScoreboard();
    }

    /**** END DEATH METHODS ***/

    /****** SHOP METHODS ******/

    public boolean UpgradeProtection()
    {
        if(protectionLevel < 4) {
            protectionLevel++;
            for (Player player : playerList) {
                player.GiveArmor();
            }
            return true;
        }
        return false;
    }

    /**** END SHOP METHODS ****/

    /***** GETTER METHODS *****/

    public Location getSpawnLocation() {
        Location l = GAME.getConfig().getLocation("teams.team"+ ID +".spawnLocation", GAME.getWorld());
        l.setDirection(getBedBlock(1).toVector().subtract(l.toVector()));
        return l;
    }

    public Location getGeneratorLocation() { return GAME.getConfig().getLocation("teams.team" + ID + ".ironGen", GAME.getWorld()); }

    public boolean getHasSharpness() { return hasSharpness; }

    public Game getGame() { return GAME; }

    public List<Player> getPlayerList() { return playerList; }

    public int getID() { return ID; }

    public String getName() { return name; }

    public String getColor() { return color; }

    public String getObjectiveString() {
        if(BED.getIsBedBroken())
        {
            if(playersAlive > 0)
                return String.valueOf(playersAlive);
            else
                return X_SYMBOL;
        }
        else
        {
            return CHECKMARK;
        }
    }

    public Location getBedBlock(int number) {
        if(number == 1)
            return getGame().getConfig().getLocation("teams.team" + ID + ".bedLocation", getGame().getWorld());
        else if(number == 2)
            return new Location(getGame().getWorld(), getGame().getConfig().getInt("teams.team" + ID + ".bedLocation.x2"),getGame().getConfig().getInt("teams.team" + ID + ".bedLocation.y"), getGame().getConfig().getInt("teams.team" + ID + ".bedLocation.z2"));
        else
            return null;
    }

    public Bed getBED() { return BED; }

    public int getPlayersAlive() { return playersAlive; }

    public int getItemShopId() { return itemShopId; }

    public int getUpgradeShopId() { return upgradeShopId; }

    public int getProtectionLevel() { return protectionLevel; }

    public Generator getIronGenerator() { return ironGenerator; }

    public Generator getGoldGenerator() { return goldGenerator; }

    /*** END GETTER METHODS ***/

    /***** SETTER METHODS *****/

    public void setHasSharpness(boolean hasSharpness) {
        this.hasSharpness = hasSharpness;
        for (Player player : playerList) {
            player.AddSharpness(hasSharpness);
        }
    }

    /*** END SETTER METHODS ***/
}
