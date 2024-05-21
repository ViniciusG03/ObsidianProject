package com.menosprezo.lobby.listener;

import com.menosprezo.lobby.Lobby;
import com.menosprezo.lobby.commands.rank.Rank;
import com.menosprezo.lobby.commands.rank.RankManager;
import com.menosprezo.lobby.database.ManagerDB;
import com.menosprezo.lobby.manager.NPCManager;
import com.menosprezo.lobby.util.MojangAuthenticator;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Dye;

import java.lang.reflect.Field;
import java.util.*;

import static com.menosprezo.lobby.commands.build.Build.buildEnabled;

public class PlayerListener implements Listener {

    private Map<UUID, NPC> playerNPCMap;
    ManagerDB managerDB = new ManagerDB();
    RankManager rankManager = new RankManager();
    MojangAuthenticator mojangAuthenticator = new MojangAuthenticator();
    private Lobby lobby;
    NPCManager npcManager = new NPCManager();
    public PlayerListener(Lobby lobby) {
        this.lobby = lobby;
    }

    @EventHandler
    public void onNPCInteract(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() == EntityType.ARMOR_STAND) {
            Player player = event.getPlayer();
            if (event.getRightClicked().getCustomName() != null && event.getRightClicked().getCustomName().equals("MINIGAMES")) {
                player.sendMessage("Você interagiu com o NPC!");
                event.setCancelled(true); // Impede a ação padrão de interação com o NPC
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        npcManager.createNPC(player, "§3§lMINIGAMES", new Location(player.getWorld(), 0.70, 83.0, 5.30,-90,0));

        player.setGameMode(GameMode.ADVENTURE);

        if (!player.hasPlayedBefore()) {
            rankManager.setRank(player.getUniqueId(), Rank.MEMBRO);
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

        Rank rank = lobby.getRankManager().getRank(player.getUniqueId());
        Location spawn = new Location(player.getWorld(), 44, 86, 32, 138, 1);
        player.teleport(spawn);

        if (rank != Rank.MEMBRO) {
            Location spawnVip = new Location(player.getWorld(), 44, 88, 32, 138, 1);
            player.teleport(spawnVip);
            player.setAllowFlight(true);
            player.setFlying(true);
        }

        //Perms do mundo
        World mundo = player.getWorld();
        mundo.setPVP(false);
        mundo.setStorm(false);
        mundo.setSpawnFlags(false, false);

        //Items
        ItemStack Compass = new ItemStack(Material.COMPASS);
        ItemStack Chest = new ItemStack(Material.CHEST);

        Dye GrayDye = new Dye();
        GrayDye.setColor(DyeColor.LIME);
        ItemStack Jogadores = GrayDye.toItemStack(1);

        ItemStack Star = new ItemStack(Material.NETHER_STAR);

        ItemMeta compass = Compass.getItemMeta();
        compass.setDisplayName("§aSelecionar Jogo");
        Compass.setItemMeta(compass);

        ItemStack Head = new ItemStack(getPlayerHead(player));
        ItemMeta head = Head.getItemMeta();
        head.setDisplayName("§aMeu perfil");
        Head.setItemMeta(head);

        ItemMeta chest = Chest.getItemMeta();
        chest.setDisplayName("§aColecionáveis");
        Chest.setItemMeta(chest);

        ItemMeta jogadores = Jogadores.getItemMeta();
        jogadores.setDisplayName("§fJogadores: §aON");
        Jogadores.setItemMeta(jogadores);

        ItemMeta star = Star.getItemMeta();
        star.setDisplayName("§aSelecionar Lobby");
        Star.setItemMeta(star);

        player.getInventory().setItem(0, Compass);
        player.getInventory().setItem(1, Head);
        player.getInventory().setItem(4, Chest);
        player.getInventory().setItem(7, Jogadores);
        player.getInventory().setItem(8, Star);

        buildEnabled = false;
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
        if (!buildEnabled || player.getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!buildEnabled || player.getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (event.getCause() == BlockIgniteEvent.IgniteCause.LIGHTNING) {
            event.setCancelled(true);
        }
    }

    public static ItemStack getPlayerHead(Player player) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        if (meta != null) {
            meta.setOwner(player.getName());
            head.setItemMeta(meta);
        }
        return head;
    }
}

