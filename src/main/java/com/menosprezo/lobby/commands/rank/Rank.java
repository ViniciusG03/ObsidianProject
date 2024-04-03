package com.menosprezo.lobby.commands.rank;

public enum Rank {

    DONO("§4§lDONO§4"),
    ADMIN("§4§lADMIN§4"),
    MEMBRO("§7§lMEMBRO§7");

    private String display;

    Rank(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}
