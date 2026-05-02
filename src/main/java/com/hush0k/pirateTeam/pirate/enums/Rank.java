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
    CAPTAIN(12),
    LEGEND(13),
    PIRATE_KING(14);

    private final int level;

    Rank(int level) {
        this.level = level;
    }

    public boolean isHigherThan(Rank other) {
        return this.level > other.level;
    }

    public Rank next() {
        return fromLevel(this.level + 1, this);
    }

    public static Rank fromLevel(int level) {
        return fromLevel(level, null);
    }

    private static Rank fromLevel(int level, Rank fallback) {
        for (Rank rank : values()) {
            if (rank.level == level) {
                return rank;
            }
        }

        if (fallback != null) {
            return fallback;
        }

        throw new IllegalArgumentException("Rank with level " + level + " not found");
    }

}
