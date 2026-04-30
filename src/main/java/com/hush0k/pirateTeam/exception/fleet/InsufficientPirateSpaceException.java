package com.hush0k.pirateTeam.exception.fleet;

public class InsufficientPirateSpaceException extends RuntimeException {
    public InsufficientPirateSpaceException(String product) {
        super("Недостаточно места для " + product);
    }
}
