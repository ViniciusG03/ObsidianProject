package com.menosprezo.lobby;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.menosprezo.lobby.commands.build.Build;
import com.menosprezo.lobby.commands.gamemode.Gamemode;
import com.menosprezo.lobby.commands.rank.RankCommand;
import com.menosprezo.lobby.commands.rank.RankListener;
import com.menosprezo.lobby.commands.rank.RankManager;
import com.menosprezo.lobby.database.ConnectionDB;
import com.menosprezo.lobby.listener.PlayerListener;
import com.menosprezo.lobby.listener.ScoreBoard;
import com.menosprezo.lobby.manager.NPCManager;
import com.menosprezo.lobby.util.TitlePacket;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Lobby extends JavaPlugin {
    public static Lobby plugin;
    private RankManager rankManager;
    private ScoreBoard scoreBoard;

    private ProtocolManager protocolManager;
    private NPCManager npcManager;

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    @Override
    public void onEnable() {
        npcManager = new NPCManager();
        plugin = this;

        protocolManager = ProtocolLibrary.getProtocolManager();
        getLogger().info("Lobby iniciado com sucesso!");
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new TitlePacket(), this);
        Bukkit.getPluginManager().registerEvents(new RankListener(this), this);

        scoreBoard = new ScoreBoard(this);
        getCommand("rank").setExecutor(new RankCommand(this, scoreBoard));

        Build build = new Build();
        getCommand("Build").setExecutor(build);
        Gamemode gamemode = new Gamemode();
        getCommand("gm").setExecutor(gamemode);

        getServer().getScheduler().runTaskTimer(this, () -> {
            for (FastBoard board : this.scoreBoard.boards.values()) {
                scoreBoard.updateBoard(board);
            }
        }, 0, 20);

        rankManager = new RankManager();

        ConnectionDB connectionDB = new ConnectionDB();
        ConnectionDB.getConexaoMySQL();
        System.out.println(connectionDB.statusConection());
    }

    public RankManager getRankManager() {
        return rankManager;
    }

    @Override
    public void onDisable() {
        ConnectionDB connectionDB = new ConnectionDB();
        connectionDB.FecharConexao();
    }


}
