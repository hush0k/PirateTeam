package com.hush0k.pirateTeam.island.enums;

import lombok.Getter;

@Getter
public enum DefenceType {

    CROWD(100, 50, 20),
    MILITIA(300, 70, 40),
    GUARD_GROUP(800, 100, 60),
    TRAINED_GUARDS(1_500, 120, 80),
    PROFESSIONAL_GUARDS(3_000, 150, 100),
    VETERANS(6_000, 180, 130),
    ELITE_GUARD(10_000, 220, 160),
    ROYAL_GUARD(18_000, 260, 200),
    MERCENARIES(11_000, 240, 110),
    SPECIAL_FORCES(30_000, 300, 250);

    private final int upgradePrice;
    private final double defencePower;
    private final double cohesion;

    DefenceType(int upgradePrice, double defencePower, double cohesion) {
        this.upgradePrice = upgradePrice;
        this.defencePower = defencePower;
        this.cohesion = cohesion;
    }

}