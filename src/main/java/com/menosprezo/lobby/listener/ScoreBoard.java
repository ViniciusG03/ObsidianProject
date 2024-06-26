package com.menosprezo.lobby.listener;

import com.menosprezo.lobby.Lobby;
import com.menosprezo.lobby.commands.rank.Rank;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreBoard implements Listener {

    private final Lobby lobby;

    public Map<UUID, FastBoard> boards = new HashMap<>();

    public ScoreBoard(Lobby lobby) {
        this.lobby = lobby;

        Bukkit.getPluginManager().registerEvents(this, lobby);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        FastBoard board = new FastBoard(player);
        board.updateTitle("§5§lOBSIDIAN");

        this.boards.put(player.getUniqueId(), board);

        if(!player.hasPlayedBefore()) {
            addTeam(player, "MEMBRO", board);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        FastBoard board = this.boards.remove(player.getUniqueId());

        Team team = board.getPlayer().getScoreboard().getTeam(player.getName());
        if (team != null) {
            team.removeEntry(player.getName());
        }

        board.delete();
    }

    public void updateBoard(FastBoard board) {
        Player player = board.getPlayer();
        Rank rank = lobby.getRankManager().getRank(player.getUniqueId());
        String rankDisplay = rank.getDisplay();

        board.updateLines(
                "",
                "§fRank: " + rankDisplay,
                "",
                "§fLobby: §7#1",
                "§fJogadores: §5" + lobby.getServer().getOnlinePlayers().size(),
                "",
                "§5obsidian.com.br"
        );

        Team team = player.getScoreboard().getEntryTeam(player.getName());

        if (team != null) {
            if (team.hasEntry(player.getName())) {
                if (rank == Rank.DONO) {
                    team.setPrefix("§4§lDONO§4 ");
                    team.setDisplayName("§4§lDONO§4 ");
                } else if (rank == Rank.ADMIN) {
                    team.setPrefix("§4§lADMIN§4 ");
                    team.setDisplayName("§4§lADMIN§4 ");
                } else if (rank == Rank.MEMBRO){
                    team.setPrefix("§7");
                    team.setDisplayName("§7");
                }
            }
        }
    }

    public void addTeam (Player player, String teamName, FastBoard board) {
        Team team = board.getPlayer().getScoreboard().getTeam(teamName);
        Rank rank = lobby.getRankManager().getRank(player.getUniqueId());

        if (team == null) {
            if (rank == Rank.DONO) {
                team = board.getPlayer().getScoreboard().registerNewTeam(teamName);
                team.setPrefix("§4§lDONO§4 ");
                team.setDisplayName("§4§lDONO§4 ");
            } else if (rank == Rank.ADMIN) {
                team = board.getPlayer().getScoreboard().registerNewTeam(teamName);
                team.setPrefix("§4§lADMIN§4 ");
                team.setDisplayName("§4§lADMIN§4 ");
            } else if (rank == Rank.MEMBRO) {
                team = board.getPlayer().getScoreboard().registerNewTeam(teamName);
                team.setPrefix("§7");
                team.setDisplayName("§7");
            }
        }

        if (!team.hasEntry(player.getName())) {
            team.addEntry(player.getName());
        }
    }
}

