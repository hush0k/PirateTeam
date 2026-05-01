package com.hush0k.pirateTeam.island.enums;

import lombok.Getter;

@Getter
public enum IslandLevel {
    WILD_SHORE(1.0, 1.0, 0, 20, null, 1),
    BAY(1.9, 1.1, 1_000, 30, WILD_SHORE, 2),
    FISHING_VILLAGE(2.6, 0.9, 2_500, 45, BAY, 5),
    TRADE_POST(3.8, 1.3, 18_000, 80, FISHING_VILLAGE, 15),
    HARBOR(4.5, 1.15, 22_000, 110, TRADE_POST, 24),
    FREE_PORT(4.0, 1.4, 35_000, 130, HARBOR, 28),
    FORTRESS_ISLAND(3.2, 1.6, 50_000, 800, HARBOR, 21),
    SMUGGLER_DEN(1.8, 1.8, 5_000, 210, TRADE_POST, 8),
    PIRATE_BAY(1.4, 2.6, 60_000, 650, SMUGGLER_DEN, 30),
    PROSPEROUS_CITY(5.8, 0.8, 80_000, 710, FORTRESS_ISLAND, 42);

    private final double goldMultiplier;
    private final double reputationMultiplier;
    private final int upgradePrice;
    private final int maxAmmoToCapture;
    private final IslandLevel requiredLevel;
    private final int populationMultiplier;

    IslandLevel(double goldMultiplier, double reputationMultiplier, int upgradePrice, int  maxAmmoToCapture,
                IslandLevel requiredLevel, int populationMultiplier) {
        this.goldMultiplier = goldMultiplier;
        this.reputationMultiplier = reputationMultiplier;
        this.upgradePrice = upgradePrice;
        this.maxAmmoToCapture = maxAmmoToCapture;
        this.requiredLevel = requiredLevel;
        this.populationMultiplier = populationMultiplier;
    }
}
