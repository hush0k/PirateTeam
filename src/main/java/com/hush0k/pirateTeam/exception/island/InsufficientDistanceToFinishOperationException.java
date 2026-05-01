package com.hush0k.pirateTeam.exception.island;

public class InsufficientDistanceToFinishOperationException extends RuntimeException {
    public InsufficientDistanceToFinishOperationException(int distance) {
        super("Пират слишком далеко для завершений операции. Еще " + distance + " нужно приблизится");
    }
}
