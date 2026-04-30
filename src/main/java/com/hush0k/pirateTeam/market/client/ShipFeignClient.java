package com.hush0k.pirateTeam.market.client;

import com.hush0k.pirateTeam.market.client.dto.FleetClientStatsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(
        name = "ship-service",
        contextId = "shipMarketClient",
        url = "${app.base-url:http://localhost:8080}"
)
public interface ShipFeignClient {

    @GetMapping("/by-fleet/{fleetId}/stats")
    FleetClientStatsDto getFleetShipStatsByFleetId(@PathVariable UUID fleetId);

    @PatchMapping("/by-fleet/{fleetId}/load-cargo")
    void loadCargo(@PathVariable UUID fleetId, @RequestParam int amount);
}
