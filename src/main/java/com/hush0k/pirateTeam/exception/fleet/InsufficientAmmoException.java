package com.hush0k.pirateTeam.exception.fleet;

import java.util.UUID;

public class InsufficientAmmoException extends RuntimeException {
    public InsufficientAmmoException(UUID fleetId) {
        super("Недостаточно боезапаса для флота с ID: " + fleetId);
    }
}
