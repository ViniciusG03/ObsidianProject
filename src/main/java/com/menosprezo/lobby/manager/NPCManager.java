package com.menosprezo.lobby.manager;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.UUID;

public class NPCManager {
    private final ProtocolManager protocolManager;

    public NPCManager() {
        this.protocolManager = ProtocolLibrary.getProtocolManager();
    }

    public void createNPC(Player player, String name, Location location) {
        try {
            // Criar um GameProfile para o NPC
            UUID uuid = UUID.randomUUID();
            GameProfile profile = new GameProfile(uuid, name);

            // Adicionar texturas ao GameProfile (opcional)
            String skinValue = "ewogICJ0aW1lc3RhbXAiIDogMTcxNjI1MjEwODA2MywKICAicHJvZmlsZUlkIiA6ICI3ZjU2ZjY1MDI2NjY0ZmM1OWFjNWYyYjVjMTNlZGY3NyIsCiAgInByb2ZpbGVOYW1lIiA6ICJNYXhBbnRvbnkiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2M0Y2Q0MTAyYTA3OGI1NGZlYjZmNDZjZWQ2MGNlMDY4YWE5NGEwNTQzNWJiMjQ3ODk5NmVjMmRiYWRhMDcyIgogICAgfQogIH0KfQ=="; // Valor da textura
            String skinSignature = "DMYkfm6uqiOHGRYIj7zs4UShB/mIj9BrYT37KBe3gAmEpU72cjIolyEpFWO072osOVawNy03QXXS3EuAtddoiH+MakKF+zNUdZlzmINO3qYS4wu4GvG3U3HSixC3L1ZJnHe3CTmv+DWAJgsIYAuXemSHD55jusp8mZKXbX1kZFWohQ64ymGKLLZ6hxl0hsoe5nd81KCMzWmREgRPO+kmMNvWt6eMc3TjygGGj6Xgg/oyadNrRzX3Sa69FTk9+I72uNpdGkIqB3Y5aojnAlJAMJSoGcUjvmlUYTVkrL1uKALuwWA6VnRt7GGh1gZB45oq8rKXoi43vrd5al4hJRmhG0QOtE4cUeLg39bJy8u1RVc95BPEF6InJJUelTXJbQXH8xgjynLQhhxnUgr15PuAU/Deb8khB+X8JbboN1yksDvRYaGWns1uflMuMsbJxL9lUwBfzt/yY2WU9jNqPFx0wLs0p3WYxtgmvEPoQvssjAKWlOVTtLQH6COoooWaKh7IFDbEUaaKMUcG/yiKhxKxn5zTSnQPDhK1fTJ7l6ZBepFJCeX6lqO6Yhk8Dx4gWmwdJQbrVgkN5qvDSnWj2LVkhrhJuOYPL7cCCTqcRpWbj2WZJuREVD8hxdeo2ZDVOFcsSzmGJy4PQ+Lm7mmdeGqlCmrrHGHpeIT9rHknrRphs48="; // Assinatura da textura
            profile.getProperties().put("textures", new Property("textures", skinValue, skinSignature));

            // Enviar o pacote de criação do jogador
            PacketContainer addPlayerPacket = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO);
            addPlayerPacket.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);

            // PlayerInfoData construção
            WrappedGameProfile wrappedProfile = WrappedGameProfile.fromHandle(profile);
            Object playerInfoData = Class.forName("com.comphenix.protocol.wrappers.PlayerInfoData")
                    .getConstructor(WrappedGameProfile.class, int.class, EnumWrappers.NativeGameMode.class, WrappedChatComponent.class)
                    .newInstance(wrappedProfile, 0, EnumWrappers.NativeGameMode.SURVIVAL, WrappedChatComponent.fromText(name));

            addPlayerPacket.getPlayerInfoDataLists().write(0, Collections.singletonList((PlayerInfoData) playerInfoData));
            protocolManager.sendServerPacket(player, addPlayerPacket);

            // Enviar o pacote de spawn da entidade
            PacketContainer spawnPacket = protocolManager.createPacket(PacketType.Play.Server.NAMED_ENTITY_SPAWN);
            int entityId = 1234; // Entity ID do NPC, escolha um valor único
            spawnPacket.getIntegers().write(0, entityId);
            spawnPacket.getUUIDs().write(0, uuid);
            spawnPacket.getIntegers().write(1, (int) (location.getX() * 32));
            spawnPacket.getIntegers().write(2, (int) (location.getY() * 32));
            spawnPacket.getIntegers().write(3, (int) (location.getZ() * 32));
            spawnPacket.getBytes().write(0, (byte) ((location.getYaw() * 256.0F) / 360.0F));
            spawnPacket.getBytes().write(1, (byte) ((location.getPitch() * 256.0F) / 360.0F));
            protocolManager.sendServerPacket(player, spawnPacket);

            // Enviar o pacote de equipamento da entidade
            PacketContainer equipmentPacket = protocolManager.createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
            equipmentPacket.getIntegers().write(0, entityId);
            equipmentPacket.getItemSlots().write(0, EnumWrappers.ItemSlot.MAINHAND);
            equipmentPacket.getItemModifier().write(0, new ItemStack(Material.DIAMOND_SWORD));
            protocolManager.sendServerPacket(player, equipmentPacket);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
