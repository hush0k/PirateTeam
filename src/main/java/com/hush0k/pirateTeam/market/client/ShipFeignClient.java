package com.hush0k.pirateTeam.market.client;

import com.hush0k.pirateTeam.market.client.dto.FleetClientStatsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;

@FeignClient(
        name = "ship-service",
        contextId = "shipMarketClient",
        url = "${app.base-url:http://localhost:8080}"
)
public interface ShipFeignClient {

    @GetMapping("/by-fleet/{fleetId}/stats")
    FleetClientStatsDto getFleetShipStatsByFleetId(UUID fleetId);
}
