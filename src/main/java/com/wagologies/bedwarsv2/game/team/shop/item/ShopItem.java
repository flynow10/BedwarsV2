package com.wagologies.bedwarsv2.game.team.shop.item;

import com.wagologies.bedwarsv2.game.player.Player;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ShopItem {
    private final ShopCategory category;
    private final Function<Player, ItemStack> placeholder;
    private final Function<Player, Material> currency;
    private final Function<Player, Integer> cost;
    private final Function<Player, Boolean> callback;

    public ShopItem(ShopCategory category, Function<Player, ItemStack> placeholder, Function<Player, Material> currency, Function<Player, Integer> cost, Function<Player, Boolean> callback)
    {
        this.category = category;
        this.placeholder = placeholder;
        this.currency = currency;
        this.cost = cost;
        this.callback = callback;
    }

    /***** GETTER METHODS *****/

    public ShopCategory getCategory() { return category; }

    public Function<Player, ItemStack> getPlaceholder() { return placeholder; }

    public Function<Player, Material> getCurrency() { return currency; }

    public Function<Player, Integer> getCost() { return cost; }

    public Function<Player, Boolean> getCallback() { return callback; }

    /*** END GETTER METHODS ***/
}
