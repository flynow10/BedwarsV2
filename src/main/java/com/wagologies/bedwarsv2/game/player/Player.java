package com.wagologies.bedwarsv2.game.player;

import com.wagologies.bedwarsv2.BedwarsV2;
import com.wagologies.bedwarsv2.game.team.Team;
import com.wagologies.bedwarsv2.game.team.shop.Shop;
import com.wagologies.bedwarsv2.game.team.shop.ShopPreset;
import com.wagologies.bedwarsv2.game.listeners.PlayerListener;
import com.wagologies.bedwarsv2.utils.DisplayPackets;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitScheduler;
import org.json.simple.JSONObject;

import java.util.Arrays;

public class Player {

    private final Team team;
    private final org.bukkit.entity.Player player;
    private Armor armorLevel = Armor.LEATHER;
    private BukkitScheduler playerScheduler;
    private boolean playerAlive = true;
    private Shop openShop = null;
    private PlayerListener listener;
    private boolean hasShears = false;
    private Pickaxe pickaxe = Pickaxe.NONE;
    private Axe axe = Axe.NONE;

    /***** INITIALIZATION *****/

    public Player(Team team, org.bukkit.entity.Player player)
    {
        this.team = team;
        this.player = player;
        listener = new PlayerListener(this);
        TeleportPlayer();
        SetBaseStats();
        GiveBaseItems();
    }

    public void TeleportPlayer()
    {
        player.teleport(team.getSpawnLocation());
    }

    public void SetBaseStats()
    {
        player.setSaturation(20);
        player.setFoodLevel(20);
        player.setHealth(20);

        player.setWalkSpeed(0.2f);
        player.setAllowFlight(false);
        player.setLevel(0);
        player.setExp(0);
        player.setGameMode(GameMode.SURVIVAL);
        player.resetPlayerWeather();
        player.resetPlayerTime();
        player.getEnderChest().clear();
        for (PotionEffect effect : player.getActivePotionEffects())
            player.removePotionEffect(effect.getType());
    }

    /*** END INITIALIZATION ***/

    /***** PUBLIC METHODS *****/

    /******* BED METHODS ******/

    public void BreakBed()
    {
        DisplayPackets.sendTitle(player, ChatColor.RED + "BED DESTROYED", "You will no longer respawn", 10, 20, 10);
    }

    public void RespawnBed()
    {
        DisplayPackets.sendTitle(player, ChatColor.GREEN + "BED RESPAWNED", "You will now respawn when you die", 10, 20, 10);
    }

    /***** END BED METHODS ****/

    /****** SHOP METHODS ******/

    public void OpenShop(ShopPreset shopPreset)
    {
        openShop = new Shop(shopPreset, this);
        player.openInventory(openShop.getInventory());
    }
    public void AddSharpness(boolean sharp)
    {
        for(int i = 0; i < player.getInventory().getContents().length; i++)
        {
            ItemStack item = player.getInventory().getItem(i);
            if(item == null || item.getType() == null)
                continue;
            if(Arrays.stream(Sword.values()).anyMatch(sword -> item.getType().equals(sword.getMaterial())))
            {
                if(sharp)
                    item.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                else
                {
                    item.removeEnchantment(Enchantment.DAMAGE_ALL);
                }
            }
        }
    }
    public boolean UpgradeArmor(Armor armorLevel)
    {
        if(canUpgradeArmor(armorLevel))
        {
            this.armorLevel = armorLevel;
            GiveArmor();
            return true;
        }
        return false;
    }

    private boolean canUpgradeArmor(Armor armorLevel)
    {
        switch (armorLevel)
        {
            case CHAIN:
                if(this.armorLevel == Armor.LEATHER)
                    return true;
                return false;
            case IRON:
                if(this.armorLevel == Armor.DIAMOND)
                    return false;
                return true;
            case DIAMOND:
                return true;
            case LEATHER:
            default:
                return false;
        }
    }

    public boolean UpgradePickaxe()
    {
        if(pickaxe.next() == Pickaxe.NONE)
            return false;
        pickaxe = pickaxe.next();
        GiveTools(0);
        return true;
    }

    public boolean UpgradeAxe()
    {
        if(axe.next() == Axe.NONE)
            return false;
        axe = axe.next();
        GiveTools(1);
        return true;
    }
    public boolean UpgradeShears()
    {
        if(hasShears)
            return false;
        hasShears = true;
        GiveTools(2);
        return true;
    }

    /**** END SHOP METHODS ****/

    /****** DEATH/RESPAWN *****/

    public void GiveBaseItems()
    {
        player.getEquipment().clear();
        player.getInventory().clear();
        GiveArmor();
        GiveSword(Sword.WOOD);
        GiveTools();
    }

    public void GiveArmor()
    {
        ItemStack[] armor = new ItemStack[4];
        armor[2] = new ItemStack(Material.LEATHER_CHESTPLATE);
        armor[3] = new ItemStack(Material.LEATHER_HELMET);
        switch (armorLevel) {
            case LEATHER:
                armor[0] = new ItemStack(Material.LEATHER_BOOTS);
                armor[1] = new ItemStack(Material.LEATHER_LEGGINGS);
                break;
            case CHAIN:
                armor[0] = new ItemStack(Material.CHAINMAIL_BOOTS);
                armor[1] = new ItemStack(Material.CHAINMAIL_LEGGINGS);
                break;
            case IRON:
                armor[0] = new ItemStack(Material.IRON_BOOTS);
                armor[1] = new ItemStack(Material.IRON_LEGGINGS);
                break;
            case DIAMOND:
                armor[0] = new ItemStack(Material.DIAMOND_BOOTS);
                armor[1] = new ItemStack(Material.DIAMOND_LEGGINGS);
                break;
        }
        for(ItemStack item : armor)
        {
            ItemMeta enchantment = item.getItemMeta();
            enchantment.spigot().setUnbreakable(true);
            enchantment.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            if(team.getProtectionLevel() != 0)
                enchantment.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, team.getProtectionLevel(), false);
            item.setItemMeta(enchantment);
        }
        player.getInventory().setArmorContents(armor);
    }

    public void GiveTools()
    {
        GiveTools(0);
        GiveTools(1);
        GiveTools(2);
    }

    public void GiveTools(int tool)
    {
        switch (tool)
        {
            case 0:
                switch (pickaxe)
                {
                    case WOOD:
                        ItemStack woodPick = new ItemStack(Material.WOOD_PICKAXE);
                        ItemMeta woodPickMeta = woodPick.getItemMeta();
                        woodPickMeta.spigot().setUnbreakable(true);
                        woodPickMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                        woodPick.setItemMeta(woodPickMeta);
                        woodPick.addEnchantment(Enchantment.DIG_SPEED, 1);
                        player.getInventory().addItem(woodPick);
                        break;
                    case IRON:
                        ItemStack ironPick = new ItemStack(Material.IRON_PICKAXE);
                        ItemMeta ironPickMeta = ironPick.getItemMeta();
                        ironPickMeta.spigot().setUnbreakable(true);
                        ironPickMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                        ironPick.setItemMeta(ironPickMeta);
                        ironPick.addEnchantment(Enchantment.DIG_SPEED, 2);
                        player.getInventory().addItem(ironPick);
                        break;
                    case GOLD:
                        ItemStack goldPick = new ItemStack(Material.GOLD_PICKAXE);
                        ItemMeta goldPickMeta = goldPick.getItemMeta();
                        goldPickMeta.spigot().setUnbreakable(true);
                        goldPickMeta.addEnchant(Enchantment.DAMAGE_ALL, 2, true);
                        goldPickMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                        goldPick.setItemMeta(goldPickMeta);
                        goldPick.addEnchantment(Enchantment.DIG_SPEED, 3);
                        player.getInventory().addItem(goldPick);
                        break;
                    case DIAMOND:
                        ItemStack stack = new ItemStack(Material.DIAMOND_PICKAXE);
                        ItemMeta stackMeta = stack.getItemMeta();
                        stackMeta.spigot().setUnbreakable(true);
                        stackMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                        stack.setItemMeta(stackMeta);
                        stack.addEnchantment(Enchantment.DIG_SPEED, 3);
                        player.getInventory().addItem(stack);
                        break;
                    case NONE:
                    default:
                        break;
                }
                break;
            case 1:
                switch (axe)
                {
                    case WOOD:
                        ItemStack woodAxe = new ItemStack(Material.WOOD_AXE);
                        ItemMeta woodAxeMeta = woodAxe.getItemMeta();
                        woodAxeMeta.spigot().setUnbreakable(true);
                        woodAxeMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                        woodAxe.setItemMeta(woodAxeMeta);
                        woodAxe.addEnchantment(Enchantment.DIG_SPEED, 2);
                        player.getInventory().addItem(woodAxe);
                        break;
                    case STONE:
                        ItemStack stoneAxe = new ItemStack(Material.STONE_AXE);
                        ItemMeta stoneAxeMeta = stoneAxe.getItemMeta();
                        stoneAxeMeta.spigot().setUnbreakable(true);
                        stoneAxeMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                        stoneAxe.setItemMeta(stoneAxeMeta);
                        stoneAxe.addEnchantment(Enchantment.DIG_SPEED, 2);
                        player.getInventory().addItem(stoneAxe);
                        break;
                    case IRON:
                        ItemStack ironAxe = new ItemStack(Material.IRON_AXE);
                        ItemMeta ironAxeMeta = ironAxe.getItemMeta();
                        ironAxeMeta.spigot().setUnbreakable(true);
                        ironAxeMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                        ironAxe.setItemMeta(ironAxeMeta);
                        ironAxe.addEnchantment(Enchantment.DIG_SPEED, 3);
                        player.getInventory().addItem(ironAxe);
                        break;
                    case DIAMOND:
                        ItemStack diamondAxe = new ItemStack(Material.DIAMOND_AXE);
                        ItemMeta diamondAxeMeta = diamondAxe.getItemMeta();
                        diamondAxeMeta.spigot().setUnbreakable(true);
                        diamondAxeMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                        diamondAxe.setItemMeta(diamondAxeMeta);
                        diamondAxe.addEnchantment(Enchantment.DIG_SPEED, 3);
                        player.getInventory().addItem(diamondAxe);
                        break;
                    case NONE:
                    default:
                        break;
                }
                break;
            case 2:
                if(hasShears) {
                    ItemStack shears = new ItemStack(Material.SHEARS);
                    ItemMeta shearsMeta = shears.getItemMeta();
                    shearsMeta.spigot().setUnbreakable(true);
                    shearsMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                    shears.setItemMeta(shearsMeta);
                    player.getInventory().addItem(shears);
                }
                break;
        }
    }

    public void GiveSword(Sword sword)
    {
        ItemStack swordItem = new ItemStack(sword.getMaterial(), 1);
        ItemMeta swordMeta = swordItem.getItemMeta();
        if(sword.equals(Sword.SAM))
        {
            swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 5, false);
            swordMeta.setDisplayName("Midas Sword");
            swordItem.setItemMeta(swordMeta);
        }
        else {
            if (team.getHasSharpness())
                swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 1, false);
        }
        swordMeta.spigot().setUnbreakable(true);
        swordMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        swordItem.setItemMeta(swordMeta);
        if(player.getInventory().getItem(0) == null || player.getInventory().getItem(0).getType() == Material.WOOD_SWORD) {
            player.getInventory().setItem(0, swordItem);
            return;
        }
        player.getInventory().addItem(swordItem);
    }

    public void OnDeath()
    {
        int iron = 0, gold = 0, diamond = 0, emerald = 0;
        for(ItemStack item : player.getInventory().getContents())
        {
            if(item == null || item.getType() == null)
                continue;
            switch (item.getType())
            {
                case IRON_INGOT:
                    iron += item.getAmount();
                    break;
                case GOLD_INGOT:
                    gold += item.getAmount();
                    break;
                case DIAMOND:
                    diamond += item.getAmount();
                    break;
                case EMERALD:
                    emerald += item.getAmount();
                    break;
            }
        }
        if(iron > 0)
        {
            getTeam().getGame().getWorld().dropItem(player.getLocation(), new ItemStack(Material.IRON_INGOT, iron));
        }
        if(gold > 0)
        {
            getTeam().getGame().getWorld().dropItem(player.getLocation(), new ItemStack(Material.GOLD_INGOT, gold));
        }
        if(diamond > 0)
        {
            getTeam().getGame().getWorld().dropItem(player.getLocation(), new ItemStack(Material.DIAMOND, diamond));
        }
        if(emerald > 0)
        {
            getTeam().getGame().getWorld().dropItem(player.getLocation(), new ItemStack(Material.EMERALD, emerald));
        }
        getTeam().getGame().getWorld().playEffect(player.getLocation().add(0, 1, 0), Effect.STEP_SOUND, 152);
        getTeam().getGame().getWorld().playSound(player.getLocation().add(0, 1, 0), Sound.ORB_PICKUP, 100, 0.6f);
        if(pickaxe != Pickaxe.NONE && pickaxe != Pickaxe.WOOD)
            pickaxe = pickaxe.previous();
        if(axe != Axe.NONE && axe != Axe.WOOD)
            axe = axe.previous();
        player.setHealth(20);
        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(new Location(getTeam().getGame().getWorld(), 0, 150, 0));
        player.getInventory().clear();
        if(getTeam().getBED().getIsBedBroken())
        {
            playerAlive = false;
            DisplayPackets.sendJsonMessage(team.getGame().getWorld(), "[{text: '" + player.getDisplayName().replace("'", "\\'").replace("\"", "\\\"") + "', color: '" + team.getColor().toLowerCase() + "', bold: true}, {text: ' died', bold: false, color:'white'}, { text: ' FINAL KILL.', color: 'aqua'}]");
            DisplayPackets.sendTitle(player, ChatColor.RED + "You Died", "You won't respawn because your bed is broken", 10, 40, 10);
            getTeam().OnFinalDeath(this);
        }
        else
        {
            DisplayPackets.sendJsonMessage(team.getGame().getWorld(), "[{text: '" + player.getDisplayName().replace("'", "\\'").replace("\"", "\\\"") + "', color: '" + team.getColor().toLowerCase() + "', bold: true}, {text: ' died.', bold: false, color:'white'}]");
            DisplayPackets.sendTitle(player, ChatColor.RED + "You Died", "Respawning in 5 seconds", 10, 40, 10);
            Bukkit.getScheduler().runTaskLater(BedwarsV2.getInstance(), this::OnRespawn, 100);
        }
    }

    public void OnRespawn()
    {
        GiveBaseItems();
        player.setGameMode(GameMode.SURVIVAL);
        player.teleport(getTeam().getSpawnLocation());
        player.setHealth(20);
    }

    public void RemoveListener()
    {
        HandlerList.unregisterAll(listener);
    }


    public void Disconnect()
    {
        playerAlive = false;
        DisplayPackets.sendJsonMessage(team.getGame().getWorld(), "[{text: '" + player.getDisplayName().replace("'", "\\'").replace("\"", "\\\"") + "', color: '" + team.getColor().toLowerCase() + "', bold: true}, {text: ' disconnected.', bold: false, color:'white'}]");
        getTeam().PlayerDisconnect(this);
    }

    /**** END DEATH/RESPAWN ***/

    /**** SCHEDULED EVENTS ****/

    public void CheckIfGiveSword() {
        if(team.getGame().getIsRunning()) {
            if (playerAlive) {
                boolean hasSword = false;
                ItemStack[] inventory = player.getInventory().getContents();
                for (int i = 0; i < inventory.length; i++) {
                    ItemStack item = inventory[i];
                    if (item == null)
                        continue;
                    Material[] swords = new Material[] {
                            Material.WOOD_SWORD,
                            Material.STONE_SWORD,
                            Material.IRON_SWORD,
                            Material.DIAMOND_SWORD
                    };
                    if (Arrays.stream(swords).anyMatch(item.getType()::equals)) {
                        if (hasSword) {
                            if (item.getType() == Material.WOOD_SWORD) {
                                player.getInventory().setItem(i, new ItemStack(Material.AIR));
                            }
                        }
                        hasSword = true;
                    }
                }
                if (!hasSword)
                    GiveSword(Sword.WOOD);

            }
        }
    }

    /** END SCHEDULED EVENTS **/

    /***** GETTER METHODS *****/

    public String getDisplayName() { return ChatColor.valueOf(team.getColor()) + player.getDisplayName(); }

    public org.bukkit.entity.Player getPlayer() { return player; }

    public Team getTeam() { return team; }

    public Axe getAxe() { return axe; }

    public Pickaxe getPickaxe() { return pickaxe; }

    public boolean getHasShears() { return hasShears; }

    /*** END GETTER METHODS ***/
}
