package com.menosprezo.lobby.listener;

import com.menosprezo.lobby.Lobby;
import com.menosprezo.lobby.commands.rank.RankManager;
import com.menosprezo.lobby.database.ManagerDB;
import net.minecraft.server.v1_8_R3.ChatMessage;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.weather.LightningStrikeEvent;

import java.lang.reflect.Field;

public class PlayerListener implements Listener {

    ManagerDB managerDB = new ManagerDB();
    private Lobby lobby;

    public PlayerListener(Lobby lobby) {
        this.lobby = lobby;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.setGameMode(GameMode.ADVENTURE);

        String rankDisplay = lobby.getRankManager().getRank(player.getUniqueId()).getDisplay();
        Location spawn = new Location(player.getWorld(), 44, 86, 32, 138, 1);
        player.teleport(spawn);

        if (rankDisplay != "§7§lMEMBRO§7") {
            Location spawnVip = new Location(player.getWorld(), 44, 88, 32, 138, 1);
            player.teleport(spawnVip);
            player.setAllowFlight(true);
            player.setFlying(true);
        }

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

        //Perms do mundo
        World mundo = player.getWorld();
        mundo.setPVP(false);
        mundo.setStorm(false);
        mundo.setSpawnFlags(false, false);

        if (uuid.equalsIgnoreCase("66bea841-f4ad-4104-9b86-6e9ca9d63e33")) {
            managerDB.insertPlayer(uuid, player.getName());
            System.out.println("Raphael Cadastrado na db dnv!");
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

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (player.getGameMode() == GameMode.CREATIVE) {
            event.setCancelled(true);
        } else {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (event.getCause() == BlockIgniteEvent.IgniteCause.LIGHTNING) {
            event.setCancelled(true);
        }
    }
}

