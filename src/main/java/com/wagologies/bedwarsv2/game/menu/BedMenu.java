package com.wagologies.bedwarsv2.game.menu;

import com.wagologies.bedwarsv2.game.Game;
import com.wagologies.bedwarsv2.game.team.Team;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import java.util.ArrayList;
import java.util.List;

public class BedMenu implements Preset {
    private Game game;
    private boolean breakBed;
    public BedMenu(boolean breakBed)
    {
        this.breakBed = breakBed;
    }
    @Override
    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public String getName() {
        return "Break Bed";
    }

    @Override
    public int getSize() {
        return 9;
    }

    @Override
    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < game.getBedwarsTeams().size(); i++) {
            Team bedwarsTeam = game.getBedwarsTeams().get(i);
            if(bedwarsTeam.getBED().getIsBedBroken() == !breakBed)
            {
                items.add(new Item(items.size(), (game1, player) -> {
                    DyeColor dyeColor = stringToColor(bedwarsTeam.getColor());
                    Wool wool = new Wool(dyeColor);
                    ItemStack woolItem = wool.toItemStack(1);
                    ItemMeta woolItemMeta = woolItem.getItemMeta();
                    if(breakBed)
                        woolItemMeta.setDisplayName("Break " + bedwarsTeam.getName() + "'s Bed");
                    else
                        woolItemMeta.setDisplayName("Respawn " + bedwarsTeam.getName() + "'s Bed");
                    woolItem.setItemMeta(woolItemMeta);
                    return woolItem;
                }, (game1, player) -> {
                    if(breakBed)
                        bedwarsTeam.getBED().BreakBed();
                    else
                        bedwarsTeam.getBED().RespawnBed();
                    new Menu(game1, player, new MainMenu());
                }));
            }
        }
        items.add(new Item(8, (game1, player) -> {
            ItemStack stack = new ItemStack(Material.BARRIER);
            ItemMeta stackMeta = stack.getItemMeta();
            stackMeta.setDisplayName("Back");
            stack.setItemMeta(stackMeta);
            return stack;
        }, (game1, player) -> new Menu(game1, player, new MainMenu())));
        return items;
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
}
