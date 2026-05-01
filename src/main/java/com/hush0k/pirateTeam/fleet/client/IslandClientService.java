package com.hush0k.pirateTeam.fleet.client;

import com.hush0k.pirateTeam.fleet.client.dto.IslandClientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "island-client",
        contextId = "fleetIslandClient",
        url = "${app.base-url:http://localhost:8080}"
)
public interface IslandClientService {

    @GetMapping("/api/islands/{id}")
    IslandClientDto getIsland(@PathVariable UUID id);

    @PatchMapping("/api/islands/{islandId}/owner/{ownerId}")
    IslandClientDto assignOwner(@PathVariable UUID islandId, @PathVariable UUID ownerId);
}
