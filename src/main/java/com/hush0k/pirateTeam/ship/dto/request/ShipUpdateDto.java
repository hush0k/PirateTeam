package com.hush0k.pirateTeam.ship.dto.request;

import com.hush0k.pirateTeam.pirate.enums.Country;
import com.hush0k.pirateTeam.ship.enums.ShipType;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

public record ShipUpdateDto(

        @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
        String name,

        @Past(message = "Construction date must be in the past")
        LocalDate constructionDate,

        ShipType shipType,

        @Positive(message = "Max crew must be positive")
        Integer maxCrew,

        @Positive(message = "Max speed must be positive")
        Integer maxSpeed,

        Country builderCountry,

        @Positive(message = "Cargo capacity must be positive")
        Integer cargoCapacity,

        @PositiveOrZero(message = "Цена не может быть отрицательной")
        Integer price

) {}
