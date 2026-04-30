package com.hush0k.pirateTeam.exception.fleet;

public class InsufficientDistanceToBattle extends RuntimeException {
    public InsufficientDistanceToBattle(int distance) {
        super("Слишком далеко чтобы начать бой. Сейчас дистанция: " + distance);
    }
}
