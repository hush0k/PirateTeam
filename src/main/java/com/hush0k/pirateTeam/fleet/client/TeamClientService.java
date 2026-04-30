package com.hush0k.pirateTeam.fleet.client;

import com.hush0k.pirateTeam.fleet.client.dto.TeamDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "team-client",
        contextId = "fleetTeamClient",
        url = "${app.base-url:http://localhost:8080}"
)
public interface TeamClientService {

    @GetMapping("api/teams/{id}")
    public TeamDto getTeam(@PathVariable UUID id);

    @GetMapping("/api/teams/by-fleet/{fleetId}")
    public TeamDto getByFleetId(@PathVariable UUID fleetId);

}
