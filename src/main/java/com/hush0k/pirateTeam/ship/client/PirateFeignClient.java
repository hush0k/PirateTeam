package com.hush0k.pirateTeam.ship.client;

import com.hush0k.pirateTeam.pirate.dto.request.PirateTreasuryChangeDto;
import com.hush0k.pirateTeam.ship.client.dto.PirateClientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "pirate-client",
        contextId = "shipPirateClient",
        url = "${app.base-url:http://localhost:8080}"
)
public interface PirateFeignClient {

    @GetMapping("/api/pirates/{id}")
    PirateClientDto getPirateById(@PathVariable UUID id);

    @PatchMapping("api/pirates/{id}/treasury/withdraw")
    PirateClientDto withdrawPirate(@PathVariable UUID id, PirateTreasuryChangeDto dto);

    @PatchMapping("api/pirates/{id}/treasury/add")
    PirateClientDto addPirate(@PathVariable UUID id, PirateTreasuryChangeDto dto);
}
