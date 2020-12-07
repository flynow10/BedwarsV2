package com.wagologies.bedwarsv2.game.team.shop.presets;

import com.wagologies.bedwarsv2.game.player.Armor;
import com.wagologies.bedwarsv2.game.player.Sword;
import com.wagologies.bedwarsv2.game.team.shop.ShopPreset;
import com.wagologies.bedwarsv2.game.team.shop.item.ShopCategory;
import com.wagologies.bedwarsv2.game.team.shop.item.ShopItem;
import com.wagologies.bedwarsv2.utils.GlowEnchantment;
import javafx.scene.effect.Glow;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MonsterEggs;
import org.bukkit.material.SpawnEgg;
import org.bukkit.material.Wool;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemShopPreset implements ShopPreset {
    private List<ShopItem> shopItems = new ArrayList<>();

    public ItemShopPreset()
    {
        AddBlockItems();
        AddCombatItems();
        AddRangedItems();
        AddToolItems();
        AddArmorItems();
        AddSpecialItems();
    }

    private void AddBlockItems()
    {
        shopItems.add(new ShopItem(ShopCategory.BLOCKS, (player) -> {
            ItemStack stack = new ItemStack(Material.WOOL, 16);
            ItemMeta stackMeta = stack.getItemMeta();
            stackMeta.setDisplayName("Wool x 16");
            stack.setItemMeta(stackMeta);
            return stack;
        }, (player) -> Material.IRON_INGOT, (player) -> 4, (player) -> {

            DyeColor dyeColor = stringToColor(player.getTeam().getColor());
            Wool wool = new Wool(dyeColor);
            ItemStack woolItem = wool.toItemStack(16);
            player.getPlayer().getInventory().addItem(woolItem);
            return true;
        }));
        shopItems.add(new ShopItem(ShopCategory.BLOCKS, (player) -> {
            ItemStack stack = new ItemStack(Material.STAINED_CLAY, 16, (byte) 0);
            ItemMeta stackMeta = stack.getItemMeta();
            stackMeta.setDisplayName("Hardened Clay x 16");
            stack.setItemMeta(stackMeta);
            return stack;
        }, (player) -> Material.IRON_INGOT, (player) -> 12, (player) -> {
            ItemStack stack = new ItemStack(Material.STAINED_CLAY, 16, (byte) stringToInteger(player.getTeam().getColor()));
            player.getPlayer().getInventory().addItem(stack);
            return true;
        }));
        shopItems.add(new ShopItem(ShopCategory.BLOCKS, (player) -> {
            ItemStack stack = new ItemStack(Material.ENDER_STONE, 24);
            ItemMeta stackMeta = stack.getItemMeta();
            stackMeta.setDisplayName("End Stone x 24");
            stack.setItemMeta(stackMeta);
            return stack;
        }, (player) -> Material.IRON_INGOT, (player) -> 32, (player) -> {
            ItemStack stack = new ItemStack(Material.ENDER_STONE, 24);
            player.getPlayer().getInventory().addItem(stack);
            return true;
        }));
        shopItems.add(new ShopItem(ShopCategory.BLOCKS, (player) -> {
            ItemStack stack = new ItemStack(Material.WOOD, 16);
            ItemMeta stackMeta = stack.getItemMeta();
            stackMeta.setDisplayName("Wood x 16");
            stack.setItemMeta(stackMeta);
            return stack;
        }, (player) -> Material.GOLD_INGOT, (player) -> 4, (player) -> {
            ItemStack stack = new ItemStack(Material.WOOD, 16);
            player.getPlayer().getInventory().addItem(stack);
            return true;
        }));
        shopItems.add(new ShopItem(ShopCategory.BLOCKS, (player) -> {
            ItemStack stack = new ItemStack(Material.OBSIDIAN, 4);
            ItemMeta stackMeta = stack.getItemMeta();
            stackMeta.setDisplayName("Obsidian x 4");
            stack.setItemMeta(stackMeta);
            return stack;
        }, (player) -> Material.EMERALD, (player) -> 4, (player) -> {
            ItemStack stack = new ItemStack(Material.OBSIDIAN, 4);
            player.getPlayer().getInventory().addItem(stack);
            return true;
        }));
    }

    private void AddCombatItems()
    {
        shopItems.add(new ShopItem(ShopCategory.COMBAT, (player) -> {
            ItemStack stack = new ItemStack(Material.STONE_SWORD);
            ItemMeta stackMeta = stack.getItemMeta();
            stackMeta.setDisplayName("Stone Sword");
            stack.setItemMeta(stackMeta);
            if(player.getTeam().getHasSharpness())
                stack = GlowEnchantment.ApplyGlow(stack);
            return stack;
        }, (player) -> Material.IRON_INGOT, (player) -> 10, (player) -> {
            player.GiveSword(Sword.STONE);
            return true;
        }));
        shopItems.add(new ShopItem(ShopCategory.COMBAT, player -> {
            ItemStack stack = new ItemStack(Material.IRON_SWORD);
            ItemMeta stackMeta = stack.getItemMeta();
            stackMeta.setDisplayName("Iron Sword");
            stack.setItemMeta(stackMeta);
            if(player.getTeam().getHasSharpness())
                stack = GlowEnchantment.ApplyGlow(stack);
            return stack;
        }, player -> Material.GOLD_INGOT, player -> 7, player -> {
            player.GiveSword(Sword.IRON);
            return true;
        }));
        shopItems.add(new ShopItem(ShopCategory.COMBAT, player -> {
            ItemStack stack = new ItemStack(Material.DIAMOND_SWORD);
            ItemMeta stackMeta = stack.getItemMeta();
            stackMeta.setDisplayName("Diamond Sword");
            stack.setItemMeta(stackMeta);
            if(player.getTeam().getHasSharpness())
                stack = GlowEnchantment.ApplyGlow(stack);
            return stack;
        }, player -> Material.EMERALD, player -> 4, player -> {
            player.GiveSword(Sword.DIAMOND);
            return true;
        }));
        shopItems.add(new ShopItem(ShopCategory.COMBAT, player -> {
            ItemStack stack = new ItemStack(Material.STICK);
            ItemMeta stackMeta = stack.getItemMeta();
            stackMeta.setDisplayName("Knockback Stick ( Knockback I )");
            stack.setItemMeta(stackMeta);
            stack = GlowEnchantment.ApplyGlow(stack);
            return stack;
        }, player -> Material.GOLD_INGOT, player -> 5, player -> {
            ItemStack stack = new ItemStack(Material.STICK);
            ItemMeta stackMeta = stack.getItemMeta();
            stackMeta.setDisplayName("Knockback Stick");
            stackMeta.addEnchant(Enchantment.KNOCKBACK, 1, true);
            stack.setItemMeta(stackMeta);
            player.getPlayer().getInventory().addItem(stack);
            return true;
        }));
        shopItems.add(new ShopItem(ShopCategory.COMBAT, player -> {
            ItemStack stack = new ItemStack(Material.STICK);
            ItemMeta stackMeta = stack.getItemMeta();
            stackMeta.setDisplayName("Knockback Stick ( Knockback III )");
            stack.setItemMeta(stackMeta);
            stack = GlowEnchantment.ApplyGlow(stack);
            return stack;
        }, player -> Material.GOLD_INGOT, player -> 24, player -> {
            ItemStack stack = new ItemStack(Material.STICK);
            ItemMeta stackMeta = stack.getItemMeta();
            stackMeta.setDisplayName("Knockback Stick");
            stackMeta.addEnchant(Enchantment.KNOCKBACK, 3, true);
            stack.setItemMeta(stackMeta);
            player.getPlayer().getInventory().addItem(stack);
            return true;
        }));
        shopItems.add(new ShopItem(ShopCategory.COMBAT, player -> {
            ItemStack stack = new ItemStack(Material.GOLD_SWORD);
            ItemMeta stackMeta = stack.getItemMeta();
            stackMeta.setDisplayName("Midas Sword ( Sharpness V )");
            stack.setItemMeta(stackMeta);
            stack = GlowEnchantment.ApplyGlow(stack);
            return stack;
        }, (player) -> Material.GOLD_INGOT, (player) -> 64, player -> {
            if(player.getPlayer().getUniqueId().equals(UUID.fromString("fce3e031-dd1b-4146-8b0a-dae71bd64276"))) {
                player.GiveSword(Sword.SAM);
                return true;
            }
            return false;
        }));
    }

    private void AddRangedItems()
    {
        shopItems.add(new ShopItem(ShopCategory.RANGED, player -> {
            ItemStack stack = new ItemStack(Material.BOW);
            ItemMeta stackMeta = stack.getItemMeta();
            stackMeta.setDisplayName("Bow");
            stack.setItemMeta(stackMeta);
            return stack;
        }, player -> Material.GOLD_INGOT, player -> 12, player -> {
            ItemStack stack = new ItemStack(Material.BOW);
            ItemMeta meta = stack.getItemMeta();
            meta.spigot().setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            stack.setItemMeta(meta);
            player.getPlayer().getInventory().addItem(stack);
            return true;
        }));
        shopItems.add(new ShopItem(ShopCategory.RANGED, player -> {
            ItemStack stack = new ItemStack(Material.BOW);
            ItemMeta stackMeta = stack.getItemMeta();
            stackMeta.setDisplayName("Bow ( Power I )");
            stack.setItemMeta(stackMeta);
            stack = GlowEnchantment.ApplyGlow(stack);
            return stack;
        }, player -> Material.GOLD_INGOT, player -> 24, player -> {
            ItemStack stack = new ItemStack(Material.BOW);
            ItemMeta meta = stack.getItemMeta();
            meta.spigot().setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            stack.setItemMeta(meta);
            stack.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
            player.getPlayer().getInventory().addItem(stack);
            return true;
        }));
        shopItems.add(new ShopItem(ShopCategory.RANGED, player -> {
            ItemStack stack = new ItemStack(Material.BOW);
            ItemMeta stackMeta = stack.getItemMeta();
            stackMeta.setDisplayName("Bow ( Power II, Punch II)");
            stack.setItemMeta(stackMeta);
            stack = GlowEnchantment.ApplyGlow(stack);
            return stack;
        }, player -> Material.EMERALD, player -> 6, player -> {
            ItemStack stack = new ItemStack(Material.BOW);
            ItemMeta meta = stack.getItemMeta();
            meta.spigot().setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            stack.setItemMeta(meta);
            stack.addEnchantment(Enchantment.ARROW_DAMAGE, 2);
            stack.addEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
            player.getPlayer().getInventory().addItem(stack);
            return true;
        }));
        shopItems.add(new ShopItem(ShopCategory.RANGED, player -> {
            ItemStack stack = new ItemStack(Material.ARROW, 8);
            ItemMeta stackMeta = stack.getItemMeta();
            stackMeta.setDisplayName("Arrows x 8");
            stack.setItemMeta(stackMeta);
            return stack;
        }, player -> Material.GOLD_INGOT, player -> 2, player -> {
            ItemStack stack = new ItemStack(Material.ARROW, 8);
            player.getPlayer().getInventory().addItem(stack);
            return true;
        }));
    }

    private void AddToolItems()
    {
        shopItems.add(new ShopItem(ShopCategory.TOOLS, (player) -> {
            switch (player.getPickaxe())
            {
                case NONE:
                    ItemStack wood = new ItemStack(Material.WOOD_PICKAXE);
                    ItemMeta woodMeta = wood.getItemMeta();
                    woodMeta.setDisplayName("Wood Pickaxe ( Efficiency I )");
                    wood.setItemMeta(woodMeta);
                    wood = GlowEnchantment.ApplyGlow(wood);
                    return wood;
                case WOOD:
                    ItemStack iron = new ItemStack(Material.IRON_PICKAXE);
                    ItemMeta ironMeta = iron.getItemMeta();
                    ironMeta.setDisplayName("Iron Pickaxe ( Efficiency II )");
                    iron.setItemMeta(ironMeta);
                    iron = GlowEnchantment.ApplyGlow(iron);
                    return iron;
                case IRON:
                    ItemStack gold = new ItemStack(Material.GOLD_PICKAXE);
                    ItemMeta goldMeta = gold.getItemMeta();
                    goldMeta.setDisplayName("Gold Pickaxe ( Efficiency III, Sharpness II )");
                    gold.setItemMeta(goldMeta);
                    gold = GlowEnchantment.ApplyGlow(gold);
                    return gold;
                case GOLD:
                    ItemStack diamond = new ItemStack(Material.DIAMOND_PICKAXE);
                    ItemMeta diamondMeta = diamond.getItemMeta();
                    diamondMeta.setDisplayName("Diamond Pickaxe ( Efficiency III )");
                    diamond.setItemMeta(diamondMeta);
                    diamond = GlowEnchantment.ApplyGlow(diamond);
                    return diamond;
                default:
                    ItemStack maxed = new ItemStack(Material.BARRIER);
                    ItemMeta maxedMeta = maxed.getItemMeta();
                    maxedMeta.setDisplayName(ChatColor.RED + "Pickaxe Maxed!");
                    maxed.setItemMeta(maxedMeta);
                    maxed = GlowEnchantment.ApplyGlow(maxed);
                    return maxed;
            }
        }, (player) -> {
            switch (player.getPickaxe())
            {
                case NONE:
                case WOOD:
                    return Material.IRON_INGOT;
                case IRON:
                case GOLD:
                    return Material.GOLD_INGOT;
                default:
                    return Material.BARRIER;
            }
        }, (player) -> {
            switch (player.getPickaxe())
            {
                case NONE:
                case WOOD:
                    return 10;
                case IRON:
                    return 3;
                case GOLD:
                    return 6;
                case DIAMOND:
                default:
                    return Integer.MAX_VALUE;
            }
        }, (player) -> player.UpgradePickaxe()));
        shopItems.add(new ShopItem(ShopCategory.TOOLS, (player) -> {
            switch (player.getAxe())
            {
                case NONE:
                    ItemStack wood = new ItemStack(Material.WOOD_AXE);
                    ItemMeta woodMeta = wood.getItemMeta();
                    woodMeta.setDisplayName("Wood Axe ( Efficiency II )");
                    wood.setItemMeta(woodMeta);
                    wood = GlowEnchantment.ApplyGlow(wood);
                    return wood;
                case WOOD:
                    ItemStack iron = new ItemStack(Material.STONE_AXE);
                    ItemMeta ironMeta = iron.getItemMeta();
                    ironMeta.setDisplayName("Stone Axe ( Efficiency II )");
                    iron.setItemMeta(ironMeta);
                    iron = GlowEnchantment.ApplyGlow(iron);
                    return iron;
                case STONE:
                    ItemStack gold = new ItemStack(Material.IRON_AXE);
                    ItemMeta goldMeta = gold.getItemMeta();
                    goldMeta.setDisplayName("Iron Axe ( Efficiency III )");
                    gold.setItemMeta(goldMeta);
                    gold = GlowEnchantment.ApplyGlow(gold);
                    return gold;
                case IRON:
                    ItemStack diamond = new ItemStack(Material.DIAMOND_AXE);
                    ItemMeta diamondMeta = diamond.getItemMeta();
                    diamondMeta.setDisplayName("Diamond Axe ( Efficiency III )");
                    diamond.setItemMeta(diamondMeta);
                    diamond = GlowEnchantment.ApplyGlow(diamond);
                    return diamond;
                default:
                    ItemStack maxed = new ItemStack(Material.BARRIER);
                    ItemMeta maxedMeta = maxed.getItemMeta();
                    maxedMeta.setDisplayName(ChatColor.RED + "Axe Maxed!");
                    maxed.setItemMeta(maxedMeta);
                    maxed = GlowEnchantment.ApplyGlow(maxed);
                    return maxed;
            }
        }, (player) -> {
            switch (player.getAxe())
            {
                case NONE:
                case WOOD:
                    return Material.IRON_INGOT;
                case STONE:
                case IRON:
                    return Material.GOLD_INGOT;
                default:
                    return Material.BARRIER;
            }
        }, (player) -> {
            switch (player.getAxe())
            {
                case NONE:
                case WOOD:
                    return 10;
                case STONE:
                    return 3;
                case IRON:
                    return 6;
                case DIAMOND:
                default:
                    return Integer.MAX_VALUE;
            }
        }, (player) -> player.UpgradeAxe()));
        shopItems.add(new ShopItem(ShopCategory.TOOLS, (player) -> {
            if (!player.getHasShears()) {
                ItemStack shears = new ItemStack(Material.SHEARS);
                ItemMeta shearsMeta = shears.getItemMeta();
                shearsMeta.setDisplayName("Shears");
                shears.setItemMeta(shearsMeta);
                return shears;
            }
            else
            {
                ItemStack maxed = new ItemStack(Material.BARRIER);
                ItemMeta maxedMeta = maxed.getItemMeta();
                maxedMeta.setDisplayName(ChatColor.RED + "Shears Maxed!");
                maxed.setItemMeta(maxedMeta);
                maxed = GlowEnchantment.ApplyGlow(maxed);
                return maxed;
            }
        }, (player) -> Material.IRON_INGOT, (player) -> {
            if(!player.getHasShears())
                return 20;
            return Integer.MAX_VALUE;
        }, (player) -> player.UpgradeShears()));
    }

    private void AddArmorItems()
    {
        shopItems.add(new ShopItem(ShopCategory.ARMOR, (player) -> {
            ItemStack stack = new ItemStack(Material.CHAINMAIL_BOOTS);
            if(player.getTeam().getProtectionLevel() != 0)
                stack = GlowEnchantment.ApplyGlow(stack);
            ItemMeta stackMeta = stack.getItemMeta();
            stackMeta.setDisplayName("Chainmail Armor");
            stack.setItemMeta(stackMeta);
            return stack;
        }, (player) -> Material.IRON_INGOT, (player) -> 40, (player) -> player.UpgradeArmor(Armor.CHAIN)));
        shopItems.add(new ShopItem(ShopCategory.ARMOR, (player) -> {
            ItemStack stack = new ItemStack(Material.IRON_BOOTS);
            if(player.getTeam().getProtectionLevel() != 0)
                stack = GlowEnchantment.ApplyGlow(stack);
            ItemMeta stackMeta = stack.getItemMeta();
            stackMeta.setDisplayName("Iron Armor");
            stack.setItemMeta(stackMeta);
            return stack;
        }, (player) -> Material.GOLD_INGOT, (player) -> 12, (player) -> player.UpgradeArmor(Armor.IRON)));
        shopItems.add(new ShopItem(ShopCategory.ARMOR, (player) -> {
            ItemStack stack = new ItemStack(Material.DIAMOND_BOOTS);
            if(player.getTeam().getProtectionLevel() != 0)
                stack = GlowEnchantment.ApplyGlow(stack);
            ItemMeta stackMeta = stack.getItemMeta();
            stackMeta.setDisplayName("Diamond Armor");
            stack.setItemMeta(stackMeta);
            return stack;
        }, (player) -> Material.EMERALD, (player) -> 6, (player) -> player.UpgradeArmor(Armor.DIAMOND)));
    }

    private void AddSpecialItems()
    {
        shopItems.add(new ShopItem(ShopCategory.SPECIAL, player -> {
            ItemStack stack = new ItemStack(Material.GOLDEN_APPLE);
            ItemMeta stackMeta = stack.getItemMeta();
            stackMeta.setDisplayName("Golden Apple");
            stack.setItemMeta(stackMeta);
            return stack;
        }, player -> Material.GOLD_INGOT, player -> 3, player -> {
            player.getPlayer().getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
            return true;
        }));
        /*shopItems.add(new ShopItem(ShopCategory.SPECIAL, player -> {
            SpawnEgg spawnEgg = new SpawnEgg(EntityType.IRON_GOLEM);
            ItemStack stack = spawnEgg.toItemStack(1);
            ItemMeta stackMeta = stack.getItemMeta();
            stackMeta.setDisplayName("Dream Defender");
            stack.setItemMeta(stackMeta);
            return stack;
        }, player -> Material.IRON_INGOT, player -> 120, player -> {
            ItemStack spawnEgg = new ItemStack(Material.MONSTER_EGG, 1, (short) 99);
            player.getPlayer().getInventory().addItem(CraftItemStack.asBukkitCopy(itemStackNMS));
            return true;
        }));*/
        shopItems.add(new ShopItem(ShopCategory.SPECIAL, player -> {
            ItemStack stack = new ItemStack(Material.FIREBALL);
            ItemMeta stackMeta = stack.getItemMeta();
            stackMeta.setDisplayName("Fireball");
            stack.setItemMeta(stackMeta);
            return stack;
        }, player -> Material.IRON_INGOT, player -> 40, player -> {
            player.getPlayer().getInventory().addItem(new ItemStack(Material.FIREBALL));
            return true;
        }));
        shopItems.add(new ShopItem(ShopCategory.SPECIAL, player -> {
            ItemStack stack = new ItemStack(Material.TNT);
            ItemMeta stackMeta = stack.getItemMeta();
            stackMeta.setDisplayName("TNT");
            stack.setItemMeta(stackMeta);
            return stack;
        }, player -> Material.GOLD_INGOT, player -> 4, player -> {
            player.getPlayer().getInventory().addItem(new ItemStack(Material.TNT));
            return true;
        }));
        shopItems.add(new ShopItem(ShopCategory.SPECIAL, player -> {
            ItemStack stack = new ItemStack(Material.ENDER_PEARL);
            ItemMeta stackMeta = stack.getItemMeta();
            stackMeta.setDisplayName("Ender Pearl");
            stack.setItemMeta(stackMeta);
            return stack;
        }, player -> Material.EMERALD, player -> 4, player -> {
            player.getPlayer().getInventory().addItem(new ItemStack(Material.ENDER_PEARL));
            return true;
        }));
        shopItems.add(new ShopItem(ShopCategory.SPECIAL, player -> {
            ItemStack stack = new ItemStack(Material.WATER_BUCKET);
            ItemMeta stackMeta = stack.getItemMeta();
            stackMeta.setDisplayName("MLG bucket");
            stack.setItemMeta(stackMeta);
            return stack;
        }, player -> Material.GOLD_INGOT, player -> 3, player -> {
            player.getPlayer().getInventory().addItem(new ItemStack(Material.WATER_BUCKET));
            return true;
        }));
        shopItems.add(new ShopItem(ShopCategory.SPECIAL, player -> {
            ItemStack stack = new ItemStack(Material.SPONGE, 4);
            ItemMeta stackMeta = stack.getItemMeta();
            stackMeta.setDisplayName("Sponge");
            stack.setItemMeta(stackMeta);
            return stack;
        }, player -> Material.GOLD_INGOT, player -> 3, player -> {
            player.getPlayer().getInventory().addItem(new ItemStack(Material.SPONGE, 4));
            return true;
        }));
    }
    @Override
    public ShopCategory getFirstCategory() {
        return ShopCategory.BLOCKS;
    }

    @Override
    public List<ShopItem> getShopItems() {
        return shopItems;
    }

    private DyeColor stringToColor(String color)
    {

        switch (color)
        {
            case "RED":
                return DyeColor.RED;
            case "BLUE":
                return DyeColor.BLUE;
            case "GREEN":
                return DyeColor.LIME;
            case "YELLOW":
                return DyeColor.YELLOW;
            case "AQUA":
                return DyeColor.CYAN;
            case "LIGHT_PURPLE":
                return DyeColor.PINK;
            case "DARK_GRAY":
                return DyeColor.GRAY;
            default:
                return DyeColor.WHITE;
        }
    }
    private int stringToInteger(String color)
    {

        switch (color)
        {
            case "RED":
                return 14;
            case "BLUE":
                return 11;
            case "GREEN":
                return 5;
            case "YELLOW":
                return 4;
            case "AQUA":
                return 9;
            case "PINK":
                return 6;
            case "DARK_GRAY":
                return 7;
            default:
                return 0;
        }
    }

    @Override
    public List<ShopCategory> getCategories() {
        List<ShopCategory> categories = new ArrayList<>();
        categories.add(ShopCategory.BLOCKS);
        categories.add(ShopCategory.COMBAT);
        categories.add(ShopCategory.RANGED);
        categories.add(ShopCategory.TOOLS);
        categories.add(ShopCategory.ARMOR);
        categories.add(ShopCategory.SPECIAL);
        return categories;
    }

    @Override
    public String getName() {
        return "Item Shop";
    }
}
