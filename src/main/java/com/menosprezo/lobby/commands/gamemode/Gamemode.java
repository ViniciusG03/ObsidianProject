package com.menosprezo.lobby.commands.gamemode;

import com.menosprezo.lobby.commands.rank.Rank;
import com.menosprezo.lobby.commands.rank.RankManager;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Gamemode implements CommandExecutor {

    RankManager rankManager = new RankManager();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Esse comando só pode ser executado por um player!");
        }

        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage("§cUse /gm (0/1/2/3)");
        }
        Rank rank = rankManager.getRank(player.getUniqueId());
        switch (args[0]) {
            case "0":
                player.setGameMode(GameMode.SURVIVAL);
                player.sendMessage("§aVocê mudou seu modo para o Survival");
                return true;
            case "1":
                player.setGameMode(GameMode.CREATIVE);

                if(rank == Rank.DONO) {
                    player.setAllowFlight(true);
                    player.setFlying(true);
                } else if (rank == Rank.ADMIN) {
                    player.setAllowFlight(true);
                    player.setFlying(true);
                }
                player.sendMessage("§aVocê mudou seu modo para o Criativo");
                return true;
            case "2":
                player.setGameMode(GameMode.ADVENTURE);

                if(rank == Rank.DONO) {
                    player.setAllowFlight(true);
                    player.setFlying(true);
                } else if (rank == Rank.ADMIN) {
                    player.setAllowFlight(true);
                    player.setFlying(true);
                }
                player.sendMessage("§aVocê mudou seu modo para o Adventure");
                return true;
            case "3":
                player.setGameMode(GameMode.SPECTATOR);

                if(rank == Rank.DONO) {
                    player.setAllowFlight(true);
                    player.setFlying(true);
                } else if (rank == Rank.ADMIN) {
                    player.setAllowFlight(true);
                    player.setFlying(true);
                }
                player.sendMessage("§aVocê mudou seu modo para o Spectador");
                return true;
            }
        return false;
    }
}
