package com.hush0k.pirateTeam.island.enums;

import lombok.Getter;

@Getter
public enum IslandLevel {
    WILD_SHORE(1.0, 1.0, 0),
    BAY(1.3, 1.1, 1_000),
    FISHING_VILLAGE(1.2, 0.9, 2_500),
    SMUGGLER_DEN(1.15, 1.8, 5_000),
    PIRATE_BAY(1.1, 2.2, 9_000),
    TRADE_POST(1.5, 1.1, 14_000),
    HARBOR(1.7, 1.0, 22_000),
    FREE_PORT(1.9, 1.4, 35_000),
    FORTRESS_ISLAND(1.8, 1.6, 50_000),
    PROSPEROUS_CITY(2.2, 0.8, 80_000);

    private final double goldMultiplier;
    private final double reputationMultiplier;
    private final long upgradePrice;

    IslandLevel(double goldMultiplier, double reputationMultiplier, long upgradePrice) {
        this.goldMultiplier = goldMultiplier;
        this.reputationMultiplier = reputationMultiplier;
        this.upgradePrice = upgradePrice;
    }
}
