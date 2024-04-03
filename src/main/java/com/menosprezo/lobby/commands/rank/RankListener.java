package com.menosprezo.lobby.commands.rank;

import com.menosprezo.lobby.Lobby;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class RankListener implements Listener {

    private Lobby lobby;

    public RankListener(Lobby lobby) {
        this.lobby = lobby;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        if (!player.hasPlayedBefore()) {
            lobby.getRankManager().setRank(player.getUniqueId(), Rank.MEMBRO);
        }

        String playerRank = lobby.getRankManager().getRank(player.getUniqueId()).getDisplay();

        if (lobby.getRankManager().getRank(player.getUniqueId()).getDisplay() != "§7§lMEMBRO§7") {
            e.setJoinMessage(playerRank + " " + player.getName() + " §eentrou no lobby");
        } else {
            e.setJoinMessage("");
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        e.setCancelled(true);

        Player player = e.getPlayer();

        String rankDisplay = lobby.getRankManager().getRank(player.getUniqueId()).getDisplay();
        String message = e.getMessage();

        if (lobby.getRankManager().getRank(player.getUniqueId()).getDisplay() != "§7§lMEMBRO§7") {
            Bukkit.broadcastMessage(rankDisplay + " " + player.getName() + ": §f" + message);
        } else {
            Bukkit.broadcastMessage("§7" + player.getName() + ": §7" + message);
        }
    }
}
