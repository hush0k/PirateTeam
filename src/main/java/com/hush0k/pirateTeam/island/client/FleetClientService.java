package com.hush0k.pirateTeam.island.client;

import com.hush0k.pirateTeam.island.client.dto.FleetClientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "fleet-client",
        contextId = "islandFleetClient",
        url = "${app.base-url:http://localhost:8080}"
)
public interface FleetClientService {

    @GetMapping("/api/fleets/by-owner/{ownerId}")
    public FleetClientDto getFleetByOwnerId(@PathVariable("ownerId") UUID ownerId);
}
