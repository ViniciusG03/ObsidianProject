package com.menosprezo.lobby.manager;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
public abstract class HoloManager {

    public String get(EntityArmorStand stand) {
        return stand.getCustomName();
    }
}
