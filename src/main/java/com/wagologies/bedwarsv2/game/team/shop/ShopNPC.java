package com.wagologies.bedwarsv2.game.team.shop;

import com.wagologies.bedwarsv2.utils.npc.NPC;
import org.bukkit.Location;

import java.util.UUID;

public class ShopNPC extends NPC {

    private final ShopPreset preset;

    public ShopNPC(Location location, String displayname, ShopPreset preset) {
        super(location, displayname);
        this.preset = preset;
    }

    public ShopNPC(Location location, String displayname, UUID uuid, ShopPreset preset) {
        super(location, displayname, uuid);
        this.preset = preset;
    }

    public ShopPreset getPreset() { return preset; }
}
