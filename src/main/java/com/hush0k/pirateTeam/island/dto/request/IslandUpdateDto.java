package com.hush0k.pirateTeam.island.dto.request;

import com.hush0k.pirateTeam.island.enums.IslandLevel;
import com.hush0k.pirateTeam.island.enums.IslandLocation;
import jakarta.validation.constraints.*;

import java.util.Set;
import java.util.UUID;

public record IslandUpdateDto(

        @Size(min = 3, max = 100, message = "Название острова должно быть от 3 до 100 символов")
        String name,

        @Size(min = 2, max = 100, message = "Прозвище острова должно быть от 2 до 100 символов")
        String nickname,

        @Positive(message = "Площадь острова должна быть больше 0")
        Double area,

        IslandLocation location,

        UUID ownerId,

        @PositiveOrZero(message = "Население не может быть отрицательным")
        Integer population,

        @PositiveOrZero(message = "Трафик кораблей не может быть отрицательным")
        Integer shipTrafficPerDay,

        @PositiveOrZero(message = "Оборот золота не может быть отрицательным")
        Long goldTurnover,

        @PositiveOrZero(message = "Налог не может быть отрицательным")
        @DecimalMax(value = "100.0", message = "Налог не может быть больше 100 процентов")
        Double taxPercentage,

        IslandLevel level,

        Set<UUID> legendaryPirateIds

) {}
