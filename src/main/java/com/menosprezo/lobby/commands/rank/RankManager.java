package com.menosprezo.lobby.commands.rank;

import com.menosprezo.lobby.database.ConnectionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class RankManager {
    public void setRank(UUID uuid, Rank rank) {
        Connection conn;
        PreparedStatement pstm;

        String sql = "UPDATE obisidian_players SET player_rank = ? WHERE player_uuid = ?";
        conn = ConnectionDB.getConexaoMySQL();

        try {
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, rank.toString());
            pstm.setString(2, uuid.toString());
            pstm.executeUpdate();
            pstm.close();
        } catch (SQLException e) {
            System.out.println("Não foi possível atualizar o rank do jogador!");
            throw new RuntimeException(e);
        }
    }

    public Rank getRank(UUID uuid) {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Rank playerRank = null;

        try {
            conn = ConnectionDB.getConexaoMySQL();
            String sql = "SELECT player_rank FROM obisidian_players WHERE player_uuid = ?";
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, uuid.toString());
            rs = pstm.executeQuery();

            if (rs.next()) {
                String rankName = rs.getString("player_rank");
                playerRank = Rank.valueOf(rankName);
            } else {
                System.out.println("Jogador não encontrado no banco de dados.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao consultar o rank do jogador: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar as conexões: " + e.getMessage());
            }
        }

        return playerRank;
    }
}
