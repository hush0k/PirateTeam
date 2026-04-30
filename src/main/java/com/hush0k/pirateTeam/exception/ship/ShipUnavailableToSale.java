package com.hush0k.pirateTeam.exception.ship;

import java.util.UUID;

public class ShipUnavailableToSale extends RuntimeException {
    public ShipUnavailableToSale(UUID shipId) {
        super("Корабль с ID: " + shipId + "не доступен к продаже");
    }
}
