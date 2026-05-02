package com.hush0k.pirateTeam.exception.pirate;

import java.util.UUID;

public class InsufficientExpForUpgradeException extends RuntimeException {
    public InsufficientExpForUpgradeException(UUID id, int neededExp) {
        super("У пирата с ID: " + id + " не хватает опыта для улучшения. Нужно опыта: " + neededExp);
    }
}
