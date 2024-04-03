package com.menosprezo.lobby.commands.rank;

import com.menosprezo.lobby.Lobby;
import com.menosprezo.lobby.listener.ScoreBoard;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class RankCommand implements CommandExecutor {

    private Lobby lobby;

    private ScoreBoard scoreBoard;

    public RankCommand(Lobby lobby, ScoreBoard scoreboard) {
        this.lobby = lobby;
        this.scoreBoard = scoreboard;
    }

    // /rank <player> rank

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (player.isOp()) {
                if (args.length == 2) {
                    if (Bukkit.getOfflinePlayer(args[0]) != null) {
                        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

                        for (Rank rank : Rank.values()) {
                            if (rank.name().equalsIgnoreCase(args[1])) {
                                if (args[1].equalsIgnoreCase("dono") || args[1].equalsIgnoreCase("DONO")) {
                                    Scoreboard board = player.getScoreboard();
                                    Team team = board.getEntryTeam(player.getName());

                                    if (team != null) {
                                        team.removeEntry(player.getName());
                                    }

                                    scoreBoard.addTeam(player, "DONO", scoreBoard.boards.get(player.getUniqueId()));
                                    scoreBoard.updateBoard(scoreBoard.boards.get(player.getUniqueId()));
                                } else if (args[1].equalsIgnoreCase("admin") || args[1].equalsIgnoreCase("ADMIN")) {
                                    Scoreboard board = player.getScoreboard();
                                    Team team = board.getEntryTeam(player.getName());

                                    if (team != null) {
                                        team.removeEntry(player.getName());
                                    }

                                    scoreBoard.addTeam(player, "ADMIN", scoreBoard.boards.get(player.getUniqueId()));
                                    scoreBoard.updateBoard(scoreBoard.boards.get(player.getUniqueId()));
                                } else if (args[1].equalsIgnoreCase("membro") || args[1].equalsIgnoreCase("MEMBRO")) {
                                    Scoreboard board = player.getScoreboard();
                                    Team team = board.getEntryTeam(player.getName());

                                    if (team != null) {
                                        team.removeEntry(player.getName());
                                    }

                                    scoreBoard.addTeam(player, "MEMBRO", scoreBoard.boards.get(player.getUniqueId()));
                                    scoreBoard.updateBoard(scoreBoard.boards.get(player.getUniqueId()));
                                }

                                lobby.getRankManager().setRank(target.getUniqueId(), rank);

                                player.sendMessage("§a" + target.getName() + " teve seu rank alterado para: " + rank.getDisplay());
                                if (target.isOnline()) {
                                    target.getPlayer().sendMessage("§a" + player.getName() + " §amudou seu rank para " + rank.getDisplay());
                                }
                                return false;
                            }
                        }

                        player.sendMessage("§cRank não encontrado!");
                    } else {
                        player.sendMessage("§cEsse player nunca entrou no servidor!");
                    }
                } else {
                    player.sendMessage("§cUse: /rank <player> <rank>");
                }
            } else {
                player.sendMessage("§cApenas Administradores podem usar esse comando!");
            }

        }


        return false;
    }
}
