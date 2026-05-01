package com.hush0k.pirateTeam.fleet.client;

import com.hush0k.pirateTeam.fleet.client.dto.PirateClientDto;
import com.hush0k.pirateTeam.fleet.client.dto.TeamTreasuryCharacteristicClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(
        name = "pirate-client",
        contextId = "fleetPirateClient",
        url = "${app.base-url:http://localhost:8080}"
)
public interface PirateClientService {

    @GetMapping("/api/pirates/{id}")
    PirateClientDto  getPirate(@PathVariable UUID id);

    @PatchMapping("/api/pirates/{id}/reputation/add")
    void addReputationToPirate(@PathVariable UUID id, @RequestBody TeamTreasuryCharacteristicClient dto);

    @PatchMapping("/api/pirates/{id}/reputation/remove")
    void removeReputationToPirate(@PathVariable UUID id, @RequestBody TeamTreasuryCharacteristicClient dto);

    @PatchMapping("/api/pirates/{id}/exp/add")
    void addExpToPirate(@PathVariable UUID id, @RequestBody TeamTreasuryCharacteristicClient dto);

    @PatchMapping("/api/pirates/{id}/exp/remove")
    void removeExpToPirate(@PathVariable UUID id, @RequestBody TeamTreasuryCharacteristicClient dto);
}
