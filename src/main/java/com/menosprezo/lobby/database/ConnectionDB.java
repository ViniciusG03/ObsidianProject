package com.menosprezo.lobby.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    public static String status = "Nao conectou...";

    public ConnectionDB() {

    }


    public static java.sql.Connection getConexaoMySQL() {

        Connection connection = null;


        try {

// Carregando o JDBC Driver padrão

            String driverName = "com.mysql.jdbc.Driver";

            Class.forName(driverName);


            String serverName = "localhost:3306";

            String mydatabase = "obsidian?characterEncoding=UTF-8";

            String url = "jdbc:mysql://" + serverName + "/" + mydatabase;

            String username = "root";

            String password = "Vinix6capudo321!";

            connection = DriverManager.getConnection(url, username, password);


            //Testa a conexão//

            if (connection != null) {

                status = ("STATUS--->Conectado com sucesso!");

            } else {

                status = ("STATUS--->Não foi possivel realizar conexão");

            }


            return connection;


        } catch (ClassNotFoundException e) {  //Driver não encontrado


            System.out.println("O driver expecificado nao foi encontrado.");

            return null;

        } catch (SQLException e) {

//Não conseguindo se conectar ao banco

            System.out.println("Nao foi possivel conectar ao Banco de Dados.");
            throw new RuntimeException(e);

        }


    }


    //Método que retorna o status da conexão//

    public static String statusConection() {

        return status;

    }


    //Método que fecha a conexão//

    public static boolean FecharConexao() {

        try {

            ConnectionDB.getConexaoMySQL().close();

            return true;

        } catch (SQLException e) {

            return false;

        }


    }


    //Método que reinicia a conexão//

    public static java.sql.Connection ReiniciarConexao() {

        FecharConexao();


        return ConnectionDB.getConexaoMySQL();

    }

}