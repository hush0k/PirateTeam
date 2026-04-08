package com.hush0k.pirateTeam.ship.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public record ShipResponseDto(
        UUID id,
        UUID capitanId,
        String name,
        LocalDate constructionDate

) {}
