package com.menosprezo.lobby.commands.build;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class Build implements CommandExecutor {

    public static boolean buildEnabled = true;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player))   {
            sender.sendMessage("Esse comando só pode ser usado por jogadores!");
        }

        Player player = (Player) sender;

        if (!player.hasPermission("lobby.Build")) {
            player.sendMessage("§cVocê não tem permissão para usar este comando.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("§cUse /Build <on/off> para ligar ou desligar a construção.");
            return true;
        }

        String subCommand = args[0].toLowerCase();
        if (subCommand.equals("on")) {
            buildEnabled = true;
            player.sendMessage("§aAgora você pode construir.");
        } else if (subCommand.equals("off")) {
            buildEnabled = false;
            player.sendMessage("§aAgora você não pode mais construir.");
        } else {
            player.sendMessage("§cUse /Build <on/off> para ligar ou desligar a construção.");
        }

        return true;
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!buildEnabled) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!buildEnabled) {
            event.setCancelled(true);
        }
    }
}
