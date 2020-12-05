package com.wagologies.bedwarsv2.utils.npc;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NPCInteractEvent extends Event  {
    private static final HandlerList handlers = new HandlerList();
    private final NPC npc;
    private final Player player;
    private final InteractType type;
    public NPCInteractEvent(NPC npc, Player player, InteractType type)
    {
        this.npc = npc;
        this.player = player;
        this.type = type;
    }
    /***** GETTER METHODS *****/

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() { return handlers; }

    public NPC getNpc() { return npc; }

    public Player getPlayer() { return player; }

    public InteractType getInteractType() { return type; }

    /*** END GETTER METHODS ***/

    /********** ENUMS *********/

    public enum InteractType
    {
        ATTACK,
        INTERACT,
        SHIFT_ATTACK,
        SHIFT_INTERACT,
    }

    /******** END ENUMS *******/
}
