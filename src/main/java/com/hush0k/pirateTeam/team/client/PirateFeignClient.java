package com.hush0k.pirateTeam.team.client;

import com.hush0k.pirateTeam.team.client.dto.CaptainClientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@FeignClient(
        name = "pirate-client",
        contextId = "teamPirateClient",
        url = "${app.base-url:http://localhost:8080}"
)
public interface PirateFeignClient {

    @GetMapping("/api/pirates/{id}")
    CaptainClientDto getPirateById(@PathVariable("id") UUID id);

    @PatchMapping("/api/pirates/team/{teamId}/bulk")
    void assignManyToTeam(
            @PathVariable("teamId") UUID teamId,
            @RequestBody Set<UUID> pirateIds
    );

    @PostMapping("/api/pirates/team/{teamId}/bulk/remove")
    void removeManyFromTeam(
            @PathVariable("teamId") UUID teamId,
            @RequestBody Set<UUID> pirateIds
    );
}