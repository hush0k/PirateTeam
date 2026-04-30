package com.hush0k.pirateTeam.market.client;

import com.hush0k.pirateTeam.fleet.dto.request.FleetResourceChangeDto;
import com.hush0k.pirateTeam.market.client.dto.FleetClientDto;
import com.hush0k.pirateTeam.market.client.dto.FleetCreateClientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(
        name = "fleet-service",
        contextId = "fleetMarketClient",
        url = "${app.base-url:http://localhost:8080}"
)
public interface FleetFeignClient {

    @GetMapping("/api/fleets/{id}")
    FleetClientDto getFleetById(@PathVariable("id") UUID id);

    @PatchMapping("/api/fleets/{id}/ammo/add")
    void addAmmo(
            @PathVariable("id") UUID id,
            @RequestBody FleetResourceChangeDto dto
    );

    @PatchMapping("/api/fleets/{id}/provision/add")
    void addProvision(
            @PathVariable("id") UUID id,
            @RequestBody FleetResourceChangeDto dto
    );

    @PostMapping("/api/fleets/owners/{ownerId}/ensure")
    FleetClientDto createFleet(@PathVariable("ownerId") UUID ownerId, @RequestBody FleetCreateClientDto dto);
}
