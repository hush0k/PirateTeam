package com.hush0k.pirateTeam.fleet.client;

import com.hush0k.pirateTeam.ship.dto.response.FleetShipStatsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "ship-client",
        contextId = "fleetShipClient",
        url = "${app.base-url:http://localhost:8080}"
)
public interface ShipClientService {
    @GetMapping("/api/ships/by-fleet/{fleetId}/stats")
    public FleetShipStatsDto  getStats(@PathVariable("fleetId") UUID fleetId);
}
