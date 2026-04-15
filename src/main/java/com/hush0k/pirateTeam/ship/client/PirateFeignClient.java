package com.hush0k.pirateTeam.ship.client;

import com.hush0k.pirateTeam.ship.client.dto.PirateClientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name="pirate-client", url="${app.base-url:http://localhost:8080}")
public interface PirateFeignClient {

    @GetMapping("/api/pirates/{id}")
    PirateClientDto getPirateById(@PathVariable UUID id);

    @PatchMapping("/api/pirates/{pirateId}/ship/{shipId}")
    PirateClientDto assignToShip(@PathVariable UUID pirateId, @PathVariable UUID shipId);
}
