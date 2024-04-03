package com.menosprezo.lobby.listener;

import com.menosprezo.lobby.database.ManagerDB;
import net.minecraft.server.v1_8_R3.ChatMessage;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.lang.reflect.Field;

public class PlayerListener implements Listener {

    ManagerDB managerDB = new ManagerDB();


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.setGameMode(GameMode.ADVENTURE);

        Location spawn = new Location(player.getWorld(), 44, 86, 32, 138, 1);
        player.teleport(spawn);

        try {
            IChatBaseComponent header = new ChatMessage("\n" + "\n§5§lOBSIDIAN\n" + " " + "\n   §7O melhor em produção!\n");
            IChatBaseComponent footer = new ChatMessage("\n" + " " + "\n§5§lOBSIDIAN.COM.BR\n");

            CraftPlayer craft_player = (CraftPlayer) player;
            PacketPlayOutPlayerListHeaderFooter list = new PacketPlayOutPlayerListHeaderFooter(header);

            Field field = PacketPlayOutPlayerListHeaderFooter.class.getDeclaredField("b");
            field.setAccessible(true);
            field.set(list, footer);

            PlayerConnection connection = craft_player.getHandle().playerConnection;
            connection.sendPacket(list);
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            throw new IllegalArgumentException(exception);
        }

        String uuid = player.getUniqueId().toString();
        if (!managerDB.CheckPlayer(uuid)) {
            managerDB.insertPlayer(uuid, player.getName());
            System.out.println(player.getName() + " foi cadastrado no Banco de Dados");
        } else {
            System.out.println("Jogador ja esta cadastrado no Banco de Dados");
        }
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.getLocation().getY() < 65) {
            Location spawn = new Location(player.getWorld(), 44, 86, 32);
            player.teleport(spawn);
        }
    }

    @EventHandler
    public void EntityDamageEvent(EntityDamageEvent event) {
        event.setCancelled(true);
    }
}
