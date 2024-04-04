package com.menosprezo.lobby.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.server.v1_8_R3.ChatMessage;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Util {

    public static JsonObject getContentFromURL(String URL) {
        try {
            URL url = new URL(URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder content = new StringBuilder();
            String input;

            while ((input = in.readLine()) != null)
                content.append(input);

            in.close();
            con.disconnect();

            return new JsonParser().parse(content.toString()).getAsJsonObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static double distance2D(Location a, Location b) {
        return Math.sqrt(
                Math.pow(a.getX() - b.getX(), 2) +
                        Math.pow(a.getZ() - b.getZ(), 2)
        );
    }

    public static void sendTitle(Player player, String title, int tfi, int ts, int tfo, String subtitle, int sfi, int ss, int sfo) {
        ChatMessage titulo = new ChatMessage(title);
        ChatMessage subtitulo = new ChatMessage(subtitle);

        PacketPlayOutTitle title_packet = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titulo);
        PacketPlayOutTitle title_lenght = new PacketPlayOutTitle(tfi, ts, tfo);

        PacketPlayOutTitle subtitle_packet = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, subtitulo);
        PacketPlayOutTitle subtitle_lenght = new PacketPlayOutTitle(sfi, ss, sfo);

        CraftPlayer craft_player = (CraftPlayer) player;

        craft_player.getHandle().playerConnection.sendPacket(title_packet);
        craft_player.getHandle().playerConnection.sendPacket(title_lenght);

        craft_player.getHandle().playerConnection.sendPacket(subtitle_packet);
        craft_player.getHandle().playerConnection.sendPacket(subtitle_lenght);
    }

    public static void sendBar(Player p, String text) {
        IChatBaseComponent comp = new ChatMessage(text);
        PacketPlayOutChat actionbar = new PacketPlayOutChat(comp, (byte) 2);

        CraftPlayer cp = (CraftPlayer) p;
        cp.getHandle().playerConnection.sendPacket(actionbar);
    }

    public static int random(int min, int max) {
        int inter = max - min + 1;
        return (int) ((Math.random() * inter) + min);
    }
}
