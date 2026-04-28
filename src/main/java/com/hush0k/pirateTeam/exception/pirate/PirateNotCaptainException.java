package com.hush0k.pirateTeam.exception.pirate;

import java.util.UUID;

public class PirateNotCaptainException extends RuntimeException {
    public PirateNotCaptainException(UUID id) {
        super("Пират по ID:" + id + " не является рангом выше NAVIGATOR");
    }
}
