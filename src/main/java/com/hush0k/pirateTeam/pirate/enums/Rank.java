package com.hush0k.pirateTeam.pirate.enums;

import lombok.Getter;

@Getter
public enum Rank {
    CABIN_BOY(1),
    SAILOR(2),
    LOOKOUT(3),
    BOARDER(4),
    COOK(5),
    TREASURER(6),
    SHIP_SURGEON(7),
    GUNNER(8),
    BOATSWAIN(9),
    NAVIGATOR(10),
    QUARTERMASTER(11),
    CAPTAIN(12);

    private final int level;

    Rank(int level) {
        this.level = level;
    }

    public boolean isHigherThan(Rank other) {
        return this.level > other.level;
    }
}
