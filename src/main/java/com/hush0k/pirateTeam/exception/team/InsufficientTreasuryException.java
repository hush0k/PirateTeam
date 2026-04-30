package com.hush0k.pirateTeam.exception.team;

import java.util.UUID;

public class InsufficientTreasuryException extends RuntimeException {
    public InsufficientTreasuryException(String product, int money) {
        super("Не достаточно " + money + " золото для покупки " + product);
    }
}
