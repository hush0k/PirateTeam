package com.hush0k.pirateTeam.ship.client;

import com.hush0k.pirateTeam.ship.client.dto.FleetClientDto;
import com.hush0k.pirateTeam.ship.client.dto.FleetCreateClientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(
        name = "fleet-client",
        contextId = "shipFleetClient",
        url = "${app.base-url:http://localhost:8080}"
)
public interface FleetFeignClient {

    @GetMapping("/api/fleets/{id}")
    FleetClientDto getFleetById(@PathVariable UUID id);

    @GetMapping("/api/fleets/by-owner/{ownerId}")
    boolean getFleetByOwner(@PathVariable UUID ownerId);

    @PostMapping("/api/fleets/owners/{ownerId}/ensure")
    FleetClientDto createFleet(@PathVariable UUID ownerId, @RequestBody FleetCreateClientDto dto);
}
