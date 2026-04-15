package com.hush0k.pirateTeam.ship.dto.request;

import com.hush0k.pirateTeam.pirate.enums.Country;
import com.hush0k.pirateTeam.ship.enums.ShipType;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

public record ShipCreateDto(

        @NotBlank(message = "Name cannot be blank")
        @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
        String name,

        @NotNull(message = "Construction date must be specified")
        @Past(message = "Construction date must be in the past")
        LocalDate constructionDate,

        @NotNull(message = "Ship type must be specified")
        ShipType shipType,

        @Positive(message = "Max crew must be positive")
        int maxCrew,

        @Positive(message = "Max speed must be positive")
        float maxSpeed,

        @NotNull(message = "Builder country must be specified")
        Country builderCountry,

        @Positive(message = "Cargo capacity must be positive")
        int cargoCapacity

) {}
