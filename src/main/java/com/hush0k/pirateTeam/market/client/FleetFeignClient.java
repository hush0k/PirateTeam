package com.hush0k.pirateTeam.market.client;

import com.hush0k.pirateTeam.market.client.dto.FleetClientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;

@FeignClient(
        name = "fleet-service",
        contextId = "fleetMarketClient",
        url = "${app.base-url:http://localhost:8080}"
)
public interface FleetFeignClient {

    @GetMapping("/api/fleets/{id}")
    FleetClientDto getFleetById(UUID id);


}
