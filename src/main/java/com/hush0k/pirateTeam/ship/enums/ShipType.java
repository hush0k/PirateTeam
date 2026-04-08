package com.hush0k.pirateTeam.ship.enums;

import lombok.Getter;

@Getter
public enum ShipType {
    SLOOP(1, 2),          // лёгкий, быстрый
    BRIGANTINE(2,  4),
    SCHOONER(3, 5),
    FRIGATE(4, 12),
    GALLEON(5, 20),
    BARQUE(4, 10),
    MAN_OF_WAR(7, 35);   // флагман, максимум мощи

    private final int tier;       // уровень корабля
    private final int cannons;    // кол-во пушек

    ShipType(int tier, int cannons) {
        this.tier = tier;
        this.cannons = cannons;
    }
}
