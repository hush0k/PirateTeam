package com.hush0k.pirateTeam.exception.fleet;

import java.util.UUID;

public class InsufficientProvisionException extends RuntimeException {
    public InsufficientProvisionException(UUID fleetId) {
        super("Недостаточно провизии для флота с ID: " + fleetId);
    }
}
