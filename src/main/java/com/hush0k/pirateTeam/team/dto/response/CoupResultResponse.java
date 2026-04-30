package com.hush0k.pirateTeam.team.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record CoupResultResponse(
       String result,
       String successRate,
       LocalDateTime timestamp
) {}
