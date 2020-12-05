package com.wagologies.bedwarsv2.game.menu;

import com.wagologies.bedwarsv2.game.Game;
import com.wagologies.bedwarsv2.utils.GlowEnchantment;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import java.util.ArrayList;
import java.util.List;

public class MainMenu implements Preset {

    @Override
    public void setGame(Game game) {
        return;
    }

    @Override
    public String getName() {
        return "Bedwars Menu";
    }

    @Override
    public int getSize() {
        return 36;
    }

    @Override
    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        items.add(new Item(12, (game, player) -> {
            ItemStack stack = new ItemStack(Material.BED);
            ItemMeta stackMeta = stack.getItemMeta();
            stackMeta.setDisplayName("Destroy a bed");
            List<String> lore = new ArrayList<>();
            lore.add("Allow's you to pick");
            lore.add("a bed to destroy!");
            stackMeta.setLore(lore);
            stack.setItemMeta(stackMeta);
            stack = GlowEnchantment.ApplyGlow(stack);
            return stack;
        }, (game, player) -> {
            new Menu(game, player, new BedMenu(true));
        }));
        items.add(new Item(14, (game, player) -> {
            ItemStack stack = new ItemStack(Material.BED);
            ItemMeta stackMeta = stack.getItemMeta();
            stackMeta.setDisplayName("Respawn a bed");
            List<String> lore = new ArrayList<>();
            lore.add("Allow's you to pick");
            lore.add("a bed to respawn!");
            stackMeta.setLore(lore);
            stack.setItemMeta(stackMeta);
            stack = GlowEnchantment.ApplyGlow(stack);
            return stack;
        }, (game, player) -> {
            new Menu(game, player, new BedMenu(false));
        }));
        items.add(new Item(31, (game, player) -> {
            ItemStack stack = new Wool(DyeColor.RED).toItemStack(1);
            ItemMeta stackMeta = stack.getItemMeta();
            stackMeta.setDisplayName(ChatColor.RED + "Stop Game");
            stack.setItemMeta(stackMeta);
            return stack;
        }, (game, player) -> {
            new Menu(game, player, new Preset() {

                @Override
                public void setGame(Game game) {
                    return;
                }

                @Override
                public String getName() {
                    return ChatColor.RED + "Are you Sure?";
                }

                @Override
                public int getSize() {
                    return 27;
                }

                @Override
                public List<Item> getItems() {
                    List<Item> items1 = new ArrayList<>();
                    items1.add(new Item(12, (game1, player1) -> {
                        ItemStack stack = new Wool(DyeColor.GREEN).toItemStack(1);
                        ItemMeta stackMeta = stack.getItemMeta();
                        stackMeta.setDisplayName(ChatColor.GREEN + "YES End the game!");
                        stack.setItemMeta(stackMeta);
                        return stack;
                    }, (game1, player1) -> game1.EndGame()));
                    items1.add(new Item(14, (game1, player1) -> {
                        ItemStack stack = new Wool(DyeColor.RED).toItemStack(1);
                        ItemMeta stackMeta = stack.getItemMeta();
                        stackMeta.setDisplayName(ChatColor.RED + "No I stil need to think about it");
                        stack.setItemMeta(stackMeta);
                        return stack;
                    }, (game1, player1) -> {
                        new Menu(game1, player1, new MainMenu());
                    }));
                    return items1;
                }
            });
        }));
        items.add(new Item(22, (game, player) -> {
            ItemStack stack = new ItemStack(Material.SIGN);
            ItemMeta stackMeta = stack.getItemMeta();
            stackMeta.setDisplayName("Run next event now");
            stack.setItemMeta(stackMeta);
            return stack;
        }, (game, player) -> {
            game.getEventManager().RunEventNow();
        }));
        return items;
    }
}
