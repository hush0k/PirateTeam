package com.hush0k.pirateTeam.market.client;

import com.hush0k.pirateTeam.market.client.dto.TeamClientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "team-service",
        contextId = "teamMarketClient",
        url = "${app.base-url:http://localhost:8080}"
)
public interface TeamFeignClient {

    @GetMapping("/api/teams/{id}")
    TeamClientDto getTeamById(@PathVariable UUID id);

    @GetMapping("/api/teams/by-fleet/{fleetId}")
    TeamClientDto getByFleetId(@PathVariable UUID fleetId);
}
