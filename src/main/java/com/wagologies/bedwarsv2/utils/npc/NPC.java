package com.wagologies.bedwarsv2.utils.npc;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.wagologies.bedwarsv2.BedwarsV2;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

public class NPC implements Listener {
    EntityPlayer npc;
    PacketListener listener;

    public NPC(Location location, String displayname)
    {
        this(location, displayname, UUID.randomUUID());
    }

    public NPC(Location location, String displayname, UUID uuid)
    {
        Bukkit.getPluginManager().registerEvents(this, BedwarsV2.getInstance());
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
        GameProfile profile = new GameProfile(uuid, displayname);
        npc = new EntityPlayer(server, world, profile, new PlayerInteractManager(world));
        npc.setCustomNameVisible(false);
        npc.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        for(Player all : Bukkit.getOnlinePlayers()){
            PlayerConnection connection = ((CraftPlayer)all).getHandle().playerConnection;
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
            connection.sendPacket(new PacketPlayOutEntityHeadRotation());
            connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
        }
        Bukkit.getScheduler().runTaskLater(BedwarsV2.getInstance(), () -> {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                PlayerConnection connection = ((CraftPlayer) onlinePlayer).getHandle().playerConnection;
                connection.sendPacket(new PacketPlayOutAnimation(npc, 0));
            }
        }, 100);
        NPC that = this;
        listener = new PacketAdapter(BedwarsV2.getInstance(), ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Client.USE_ENTITY) {
                    PacketContainer container = event.getPacket();
                    if(container.getIntegers().read(0).equals(npc.getId()))
                    {
                        if(container.getEntityUseActions().read(0).equals(EnumWrappers.EntityUseAction.ATTACK))
                        {
                            if(event.getPlayer().isSneaking())
                            {
                                Bukkit.getPluginManager().callEvent(new NPCInteractEvent(that, event.getPlayer(), NPCInteractEvent.InteractType.SHIFT_ATTACK));
                            }
                            else
                            {
                                Bukkit.getPluginManager().callEvent(new NPCInteractEvent(that, event.getPlayer(), NPCInteractEvent.InteractType.ATTACK));
                            }
                        }
                        else if(container.getEntityUseActions().read(0).equals(EnumWrappers.EntityUseAction.INTERACT))
                        {
                            if(event.getPlayer().isSneaking())
                            {
                                Bukkit.getPluginManager().callEvent(new NPCInteractEvent(that, event.getPlayer(), NPCInteractEvent.InteractType.SHIFT_INTERACT));
                            }
                            else
                            {
                                Bukkit.getPluginManager().callEvent(new NPCInteractEvent(that, event.getPlayer(), NPCInteractEvent.InteractType.INTERACT));
                            }
                        }
                    }
                }
            }
        };
        BedwarsV2.getInstance().getProtocolManager().addPacketListener(listener);
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * <p>
     * The {@code equals} method implements an equivalence relation
     * on non-null object references:
     * <ul>
     * <li>It is <i>reflexive</i>: for any non-null reference value
     *     {@code x}, {@code x.equals(x)} should return
     *     {@code true}.
     * <li>It is <i>symmetric</i>: for any non-null reference values
     *     {@code x} and {@code y}, {@code x.equals(y)}
     *     should return {@code true} if and only if
     *     {@code y.equals(x)} returns {@code true}.
     * <li>It is <i>transitive</i>: for any non-null reference values
     *     {@code x}, {@code y}, and {@code z}, if
     *     {@code x.equals(y)} returns {@code true} and
     *     {@code y.equals(z)} returns {@code true}, then
     *     {@code x.equals(z)} should return {@code true}.
     * <li>It is <i>consistent</i>: for any non-null reference values
     *     {@code x} and {@code y}, multiple invocations of
     *     {@code x.equals(y)} consistently return {@code true}
     *     or consistently return {@code false}, provided no
     *     information used in {@code equals} comparisons on the
     *     objects is modified.
     * <li>For any non-null reference value {@code x},
     *     {@code x.equals(null)} should return {@code false}.
     * </ul>
     * <p>
     * The {@code equals} method for class {@code Object} implements
     * the most discriminating possible equivalence relation on objects;
     * that is, for any non-null reference values {@code x} and
     * {@code y}, this method returns {@code true} if and only
     * if {@code x} and {@code y} refer to the same object
     * ({@code x == y} has the value {@code true}).
     * <p>
     * Note that it is generally necessary to override the {@code hashCode}
     * method whenever this method is overridden, so as to maintain the
     * general contract for the {@code hashCode} method, which states
     * that equal objects must have equal hash codes.
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     * argument; {@code false} otherwise.
     * @see #hashCode()
     * @see HashMap
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof NPC)
        {
            NPC npc2 = (NPC) obj;
            if(npc == npc2.npc)
            {
                return true;
            }
        }
        return false;
    }

    public void Remove()
    {
        BedwarsV2.getInstance().getProtocolManager().removePacketListener(listener);
        PacketPlayOutEntityDestroy destroyPacket = new PacketPlayOutEntityDestroy(1, npc.getId());
        for(Player all : Bukkit.getOnlinePlayers())
        {
            PlayerConnection connection = ((CraftPlayer)all).getHandle().playerConnection;
            connection.sendPacket(destroyPacket);
        }
    }
    public void LookAt(Player player, Location location)
    {
        CraftPlayer player1 = ((CraftPlayer)player);
        PlayerConnection playerConnection = player1.getHandle().playerConnection;
        Vector locationV = new Vector(location.getX(), 0, location.getZ());
        Vector npcV = new Vector(npc.locX, 0, npc.locZ);

        Vector direction = locationV.subtract(npcV);
        double y = direction.getZ();
        double x = direction.getX();
        double d = Math.toDegrees(Math.atan(y/x)) - 90;
        if(x<0)
            d+=180;
        else if(y<0)
            d+=360;
        double scaledD = 256*(d/360);

        PacketPlayOutEntityHeadRotation headRotation = new PacketPlayOutEntityHeadRotation(npc, (byte) (scaledD));
        PacketPlayOutEntity.PacketPlayOutEntityLook look = new PacketPlayOutEntity.PacketPlayOutEntityLook(npc.getId(), (byte) (scaledD), (byte) 0, false);
        playerConnection.sendPacket(headRotation);
        playerConnection.sendPacket(look);
    }
    public void LookAt(Player player, double degrees)
    {
        PacketPlayOutEntityHeadRotation headRotation = new PacketPlayOutEntityHeadRotation(npc, (byte) degrees);
        PacketPlayOutEntity.PacketPlayOutEntityLook look = new PacketPlayOutEntity.PacketPlayOutEntityLook(npc.getId(), (byte) degrees,(byte) 0,false);
        CraftPlayer player1 = ((CraftPlayer)player);
        PlayerConnection playerConnection = player1.getHandle().playerConnection;
        playerConnection.sendPacket(headRotation);
        playerConnection.sendPacket(look);
    }
    public void LookAt(Location location)
    {
        for(Player player : Bukkit.getOnlinePlayers())
        {
            LookAt(player, location);
        }
    }

    public EntityPlayer getNpc() { return npc; }
}
