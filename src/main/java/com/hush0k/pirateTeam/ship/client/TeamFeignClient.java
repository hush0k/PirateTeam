package com.hush0k.pirateTeam.ship.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "team-client",
        contextId = "shipTeamClient",
        url = "${app.base-url:http://localhost:8080}"
)
public interface TeamFeignClient {

    @GetMapping("/api/teams/{teamId}/fleet/{fleetId}")
    void assignFleetToTeam(@PathVariable UUID teamId, @PathVariable UUID fleetId);
}
