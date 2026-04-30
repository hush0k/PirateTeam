package com.hush0k.pirateTeam.fleet.dto.response;

import java.util.UUID;

public record FleetAttackResult(
        UUID fleetId,
        UUID enemyFleetId,
        String winnerFleet,
        int result,
        int fatigue,
        int spentAmmo,
        String lootedTreasury
) {}
