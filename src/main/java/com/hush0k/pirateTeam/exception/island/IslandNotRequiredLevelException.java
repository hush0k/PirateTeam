package com.hush0k.pirateTeam.exception.island;

import com.hush0k.pirateTeam.island.enums.IslandLevel;

public class IslandNotRequiredLevelException extends RuntimeException {
    public IslandNotRequiredLevelException(IslandLevel islandLevel) {
        super("Требование по уровню острова не выполнены. Для этого улучшение вам нужно: " + islandLevel);
    }
}
