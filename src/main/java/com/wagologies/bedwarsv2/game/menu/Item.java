package com.wagologies.bedwarsv2.game.menu;

import com.wagologies.bedwarsv2.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class Item {
    public BiFunction<Game, Player, ItemStack> placeHolder;
    public BiConsumer<Game, Player> callback;
    public int slot;
    public Item(int slot, BiFunction<Game, Player, ItemStack> placeHolder, BiConsumer<Game, Player> callback)
    {
        this.slot = slot;
        this.placeHolder = placeHolder;
        this.callback = callback;
    }
}
