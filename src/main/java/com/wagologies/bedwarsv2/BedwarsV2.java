package com.wagologies.bedwarsv2;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.github.juliarn.npc.NPCPool;
import com.wagologies.bedwarsv2.commands.Menu;
import com.wagologies.bedwarsv2.commands.StartGame;
import com.wagologies.bedwarsv2.game.Game;
import com.wagologies.bedwarsv2.utils.GlowEnchantment;
import com.wagologies.bedwarsv2.utils.WorldCopier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public final class BedwarsV2 extends JavaPlugin {
    private WorldCopier copier;
    public Game game;
    private static BedwarsV2 instance;
    public static ProtocolManager protocolManager;
    public static NPCPool npcPool;
    /***** ENABLE/DISABLE *****/

    @Override
    public void onEnable() {
        copier = new WorldCopier(this);
        instance = this;
        getCommand("startgame").setExecutor(new StartGame());
        getCommand("bmenu").setExecutor(new Menu());
        protocolManager = ProtocolLibrary.getProtocolManager();
        npcPool = new NPCPool(this);
        registerGlow();
    }

    @Override
    public void onDisable() {
        instance = null;
    }
    /*** END ENABLE/DISABLE ***/

    /***** UTILITY METHODS ****/

    private void registerGlow()
    {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            GlowEnchantment glow = new GlowEnchantment(70);
            Enchantment.registerEnchantment(glow);
        }
        catch (IllegalArgumentException e){
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /*** END UTILITY METHODS **/

    /***** GETTER METHODS *****/

    public static BedwarsV2 getInstance() { return instance; }

    public WorldCopier getCopier() { return copier; }

    public ProtocolManager getProtocolManager() { return protocolManager; }

    /*** END GETTER METHODS ***/
}
