package com.hush0k.pirateTeam.exception.pirate;

import java.util.UUID;

public class PirateNotEnoughTreasury extends RuntimeException {
    public PirateNotEnoughTreasury(UUID id) {
        super("У пирата с ID:" + id + "не хватает золото для завершение операции!");
    }
}
