package com.wagologies.bedwarsv2.game.player;

public enum Pickaxe {
    NONE,
    WOOD,
    IRON,
    GOLD,
    DIAMOND;

    public static Pickaxe[] vals = values();

    public Pickaxe next()
    {
        return vals[(ordinal()+1) % vals.length];
    }

    public Pickaxe previous()
    {
        return vals[(ordinal()-1) % vals.length];
    }
}
