package com.wagologies.bedwarsv2.game.player;

public enum  Axe {
    NONE,
    WOOD,
    STONE,
    IRON,
    DIAMOND;

    public static Axe[] vals = values();

    public Axe next()
    {
        return vals[(ordinal()+1) % vals.length];
    }

    public Axe previous()
    {
        return vals[(ordinal()-1) % vals.length];
    }
}
