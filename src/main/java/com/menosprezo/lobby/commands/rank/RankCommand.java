package com.menosprezo.lobby.commands.rank;

import com.menosprezo.lobby.Lobby;
import com.menosprezo.lobby.listener.ScoreBoard;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
                                    Scoreboard board = target.getPlayer().getScoreboard();
                                    Team team = board.getEntryTeam(target.getName());

                                    if (team != null) {
                                        team.removeEntry(target.getName());
                                    }

                                    scoreBoard.addTeam(target.getPlayer(), "DONO", scoreBoard.boards.get(target.getUniqueId()));
                                    scoreBoard.updateBoard(scoreBoard.boards.get(target.getUniqueId()));
                                } else if (args[1].equalsIgnoreCase("admin") || args[1].equalsIgnoreCase("ADMIN")) {
                                    Scoreboard board = target.getPlayer().getScoreboard();
                                    Team team = board.getEntryTeam(target.getName());

                                    if (team != null) {
                                        team.removeEntry(target.getPlayer().getName());
                                    }

                                    scoreBoard.addTeam(target.getPlayer(), "ADMIN", scoreBoard.boards.get(target.getUniqueId()));
                                    scoreBoard.updateBoard(scoreBoard.boards.get(target.getUniqueId()));
                                } else if (args[1].equalsIgnoreCase("membro") || args[1].equalsIgnoreCase("MEMBRO")) {
                                    Scoreboard board = target.getPlayer().getScoreboard();
                                    Team team = board.getEntryTeam(target.getName());

                                    if (team != null) {
                                        team.removeEntry(target.getName());
                                    }

                                    scoreBoard.addTeam(target.getPlayer(), "MEMBRO", scoreBoard.boards.get(target.getUniqueId()));
                                    scoreBoard.updateBoard(scoreBoard.boards.get(target.getUniqueId()));
                                }

                                lobby.getRankManager().setRank(target.getUniqueId(), rank);

                                player.sendMessage("§aVocê mudou seu rank para: " + rank.getDisplay());

                                if (target.isOnline()) {
                                    if(target != player) {
                                        target.getPlayer().sendMessage("§a" + player.getName() + " §amudou seu rank para " + rank.getDisplay());
                                    }

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
