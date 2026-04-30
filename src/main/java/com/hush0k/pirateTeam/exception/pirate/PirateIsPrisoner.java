package com.hush0k.pirateTeam.exception.pirate;

import java.util.UUID;

public class PirateIsPrisoner extends RuntimeException {
    public PirateIsPrisoner(UUID id) {
        super("Этот пират с ID: " + id + " является узником и не может выполнять это действие.");
    }
}
