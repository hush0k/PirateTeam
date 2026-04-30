package com.hush0k.pirateTeam.island.dto.request;

import com.hush0k.pirateTeam.island.enums.IslandLevel;
import com.hush0k.pirateTeam.island.enums.IslandLocation;
import jakarta.validation.constraints.*;

import java.util.Set;
import java.util.UUID;

public record IslandCreateDto(

        @NotBlank(message = "Название острова не может быть пустым")
        @Size(min = 3, max = 100, message = "Название острова должно быть от 3 до 100 символов")
        String name,

        @Size(min = 2, max = 100, message = "Прозвище острова должно быть от 2 до 100 символов")
        String nickname,

        int coordinateX,
        int coordinateY,

        @Positive(message = "Площадь острова должна быть больше 0")
        double area,

        @NotNull(message = "Локация острова должна быть указана")
        IslandLocation location,

        UUID ownerId,

        @PositiveOrZero(message = "Население не может быть отрицательным")
        int population,

        @PositiveOrZero(message = "Трафик кораблей не может быть отрицательным")
        int shipTrafficPerDay,

        @PositiveOrZero(message = "Оборот золота не может быть отрицательным")
        long goldTurnover,

        @PositiveOrZero(message = "Налог не может быть отрицательным")
        @DecimalMax(value = "100.0", message = "Налог не может быть больше 100 процентов")
        double taxPercentage,

        @NotNull(message = "Уровень острова должен быть указан")
        IslandLevel level,

        Set<UUID> legendaryPirateIds

) {}
