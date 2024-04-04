package com.menosprezo.lobby.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;
public class MojangAuthenticator {

    private static final String MOJANG_API_URL = "https://api.mojang.com/users/profiles/minecraft/";
    private static final Gson GSON = new Gson();

    public static boolean isOriginalPlayer(String playerName) {
        try {
            URL url = new URL(MOJANG_API_URL + playerName);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String jsonResponse = reader.readLine();
                reader.close();

                if (jsonResponse != null && !jsonResponse.isEmpty()) {
                    // O jogador é original se a resposta não estiver vazia
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Se ocorrer algum erro ou se a resposta estiver vazia, o jogador não é original
        return false;
    }
}
