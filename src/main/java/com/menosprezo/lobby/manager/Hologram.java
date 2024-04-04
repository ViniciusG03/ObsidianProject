package com.menosprezo.lobby.manager;

import lombok.Getter;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

@Getter
public class Hologram {

    private final EntityArmorStand stand;
    private HoloManager manager;

    public Hologram(String world_name, double x, double y, double z) {
        World world = ((CraftWorld) Bukkit.getWorld(world_name)).getHandle();
        stand = new EntityArmorStand(world, x, y, z);

        stand.setInvisible(true);
        stand.setGravity(false);
        stand.setSmall(true);
        stand.setCustomNameVisible(true);
        stand.setCustomName("Linha Vazia");

        manager = new HoloManager() {
            @Override
            public String get(EntityArmorStand stand) {
                return super.get(stand);
            }
        };
    }

    public Hologram setManager(HoloManager manager) {
        this.manager = manager;
        return this;
    }

    public Hologram setText(String text) {
        String name = text.replace('&', '§');
        stand.setCustomName(name);

        return this;
    }

    public Hologram setLocation(double x, double y, double z) {
        stand.setPosition(x, y, z);
        return this;
    }

    public Hologram update() {
        stand.getWorld().getWorld().getPlayers().forEach(loop_player -> {
            setText(manager.get(stand));
            PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(stand.getId(), stand.getDataWatcher(), true);

            PlayerConnection con = ((CraftPlayer) loop_player).getHandle().playerConnection;
            con.sendPacket(packet);
        });

        return this;
    }

    public Hologram show(Player player) {
        PlayerConnection con = ((CraftPlayer) player).getHandle().playerConnection;
        con.sendPacket(new PacketPlayOutSpawnEntityLiving(stand));

        return this;
    }

    public Hologram hide(Player player) {
        PlayerConnection con = ((CraftPlayer) player).getHandle().playerConnection;
        con.sendPacket(new PacketPlayOutEntityDestroy(stand.getBukkitEntity().getEntityId()));

        return this;
    }
}
