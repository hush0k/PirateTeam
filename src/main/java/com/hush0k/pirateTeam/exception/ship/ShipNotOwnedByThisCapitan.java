package com.hush0k.pirateTeam.exception.ship;

import java.util.UUID;

public class ShipNotOwnedByThisCapitan extends RuntimeException {
    public ShipNotOwnedByThisCapitan(UUID shipId, UUID capitanId) {
        super("Капитан с ID: " + capitanId + " не является владельцем корабля с ID: " + shipId);
    }
}
