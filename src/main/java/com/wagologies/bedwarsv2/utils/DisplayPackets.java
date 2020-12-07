package com.wagologies.bedwarsv2.utils;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class DisplayPackets {
    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut)
    {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        PlayerConnection playerConnection = craftPlayer.getHandle().playerConnection;
        IChatBaseComponent titleJson = IChatBaseComponent.ChatSerializer.a("{'text' : '" + title.replace("'", "\\'").replace("\"", "\\\"") + "'}");
        IChatBaseComponent subtitleJSON = IChatBaseComponent.ChatSerializer.a("{'text': '" + subtitle.replace("'", "\\'").replace("\"", "\\\"") + "'}");
        PacketPlayOutTitle timesPacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, fadeIn, stay, fadeOut);
        PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleJson);
        PacketPlayOutTitle subtitlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, subtitleJSON);
        playerConnection.sendPacket(timesPacket);
        playerConnection.sendPacket(titlePacket);
        playerConnection.sendPacket(subtitlePacket);
    }
    public static void sendJsonMessage(Player player, String json)
    {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        PlayerConnection playerConnection = craftPlayer.getHandle().playerConnection;
        IChatBaseComponent message = IChatBaseComponent.ChatSerializer.a(json);
        PacketPlayOutChat chatMessage = new PacketPlayOutChat(message);
        playerConnection.sendPacket(chatMessage);
    }
    public static void sendJsonMessage(World world, String json)
    {
        for (Player player : world.getPlayers()) {
            sendJsonMessage(player, json);
        }
    }

    public static void sendActionBar(Player player, String json)
    {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        PlayerConnection playerConnection = craftPlayer.getHandle().playerConnection;
        IChatBaseComponent message = IChatBaseComponent.ChatSerializer.a(json);
        PacketPlayOutChat actionMessage = new PacketPlayOutChat(message, (byte) 2);
        playerConnection.sendPacket(actionMessage);
    }
}
