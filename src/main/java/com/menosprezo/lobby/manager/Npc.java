package com.menosprezo.lobby.manager;

import com.google.gson.JsonObject;
import com.menosprezo.lobby.Lobby;
import com.menosprezo.lobby.util.Util;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Npc {

    private List<Player> players = new ArrayList<>();
    private UUID uuid = UUID.randomUUID();
    private String npcId = RandomStringUtils.random(6, true, true);
    private String name;
    private GameProfile profile;
    private DataWatcher watcher = new DataWatcher(null);
    private MinecraftServer server;
    private EntityPlayer npc;

    private double x = 0.5, y = 60, z = 0.5;
    private float yaw = 0, yaw2 = 0, pitch = 0;

    private WorldServer world;

    private Hologram header = new Hologram("world", 0, 0, 0);
    private Hologram footer = new Hologram("world", 0, 0, 0);

    public Npc() {
        name = (("§8[NPC] " + RandomStringUtils.random(8, true, true)).substring(0, 16));
        profile = new GameProfile(uuid, name);

        server = MinecraftServer.getServer();
        NpcManager.npcs.add(this);
    }

    public Npc fast(String world, String skin, double x, double y, double z, float yaw, float pitch, String subtitle, String title) {
        setWorld(world);
        setSkin(skin);
        setLocation(x, y, z, yaw, pitch);
        getFooter().setText(subtitle);
        getHeader().setText(title);
        return this;
    }

    public Npc setWorld(String name) {
        CraftWorld world1 = (CraftWorld) Bukkit.getWorld(name);
        world = world1.getHandle();
        return this;
    }

    public Npc setSkin(String nick) {
        JsonObject uuid = Util.getContentFromURL("https://api.mojang.com/users/profiles/minecraft/" + nick);

        if (uuid.has("error"))
            return this;

        JsonObject values = Util.getContentFromURL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.get("id").getAsString() + "?unsigned=false");
        JsonObject parsed = values.get("properties").getAsJsonArray().get(0).getAsJsonObject();

        String signature = parsed.get("signature").getAsString();
        String texture = parsed.get("value").getAsString();

        profile.getProperties().put("textures", new Property("textures", texture, signature));
        return this;
    }

    public Npc setLocation(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;

        footer.setLocation(x, y + 0.85, z);
        header.setLocation(x, y + 1.15, z);
        return this;
    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld("world"), x, y, z);
    }

    public Npc setup(int off) {
        npc = new EntityPlayer(server, world, profile, new PlayerInteractManager(world));
        npc.setLocation(x, y, z, 0, 0);
        yaw2 = 128 * ((yaw + off) / 180);
        yaw = 128 * (yaw / 180);
        pitch = 127 * (pitch / 180);
        watcher.a(10, (byte) 127);
        return this;
    }

    public void update() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            Location npc_loc = getLocation();
            Location p_loc = player.getLocation();

            if (Util.distance2D(p_loc, npc_loc) < 48) {
                header.hide(player);
                footer.hide(player);
                hide(player);

                Bukkit.getScheduler().scheduleSyncDelayedTask(Lobby.plugin, () -> {
                    header.show(player);
                    footer.show(player);

                    npc = new EntityPlayer(server, world, profile, new PlayerInteractManager(world));
                    npc.setLocation(x, y, z, 0, 0);

                    show(player, 10);
                }, 10);
            }
        });
    }

    public void show(Player p, int delay) {
        header.show(p);
        footer.show(p);

        List<String> npcs = new ArrayList<>();
        NpcManager.npcs.forEach(i -> npcs.add(i.name));

        PlayerConnection con = ((CraftPlayer) p).getHandle().playerConnection;
        con.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
        con.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
        con.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) yaw));
        con.sendPacket(new PacketPlayOutEntity.PacketPlayOutEntityLook(npc.getId(), (byte) yaw2, (byte) pitch, true));
        con.sendPacket(new PacketPlayOutEntityMetadata(npc.getId(), watcher, true));
        con.sendPacket(new PacketPlayOutScoreboardTeam(NpcManager.team, 1));
        con.sendPacket(new PacketPlayOutScoreboardTeam(NpcManager.team, 0));
        con.sendPacket(new PacketPlayOutScoreboardTeam(NpcManager.team, npcs, 3));

        Bukkit.getScheduler().scheduleSyncDelayedTask(Lobby.plugin, () -> {
            con.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc));
        }, delay);
    }

    public void hide(Player p) {
        header.hide(p);
        footer.hide(p);

        PlayerConnection con = ((CraftPlayer) p).getHandle().playerConnection;
        con.sendPacket(new PacketPlayOutEntityDestroy(npc.getBukkitEntity().getEntityId()));
        con.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc));
    }
}