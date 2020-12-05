package com.wagologies.bedwarsv2.game.menu;

import com.wagologies.bedwarsv2.game.Game;
import com.wagologies.bedwarsv2.utils.GlowEnchantment;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
        return items;
    }
}
