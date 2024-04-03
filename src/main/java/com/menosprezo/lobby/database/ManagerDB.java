package com.menosprezo.lobby.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManagerDB {

    public void insertPlayer (String uuid, String name) {
        Connection conn;
        PreparedStatement pstm;

        String sql = "insert into obisidian_players (player_uuid, player_name) values (?,?)";
        conn = ConnectionDB.getConexaoMySQL();

        try {
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, uuid);
            pstm.setString(2, name);

            pstm.execute();
            pstm.close();
        } catch (Exception e) {
            System.out.println("Nao foi possivel inserir o jogador ao Banco de Dados");
            throw new RuntimeException(e);
        }
    }

    public boolean CheckPlayer(String uuid) {
        Connection conn;
        PreparedStatement pstm;
        ResultSet rs;

        String sql = "select player_uuid from obisidian_players where player_uuid = ?";
        conn = ConnectionDB.getConexaoMySQL();

        try {
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, uuid);
            rs = pstm.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            System.out.println("Não foi possivel verificar o jogador no Banco de Dados");
            e.printStackTrace();

            return false;
        }
    }
}
