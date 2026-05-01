package com.hush0k.pirateTeam.island.client;


import com.hush0k.pirateTeam.fleet.client.dto.TeamTreasuryCharacteristicClient;
import com.hush0k.pirateTeam.island.client.dto.PirateClientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "pirate-client",
        contextId = "islandPirateClient",
        url = "${app.base-url:http://localhost:8080}"
)
public interface PirateClientService {

    @GetMapping("/api/pirates/{id}")
    PirateClientDto getPirate(@PathVariable UUID id);

    @PatchMapping("/api/pirates/reputation/add")
    void addReputationToPirate(@PathVariable UUID id, TeamTreasuryCharacteristicClient dto);

    @PatchMapping("/api/pirates/treasury/add")
    void addTreasuryToPirate(@PathVariable UUID id, TeamTreasuryCharacteristicClient dto);

    @PatchMapping("/api/pirates/reputation/remove")
    void removeReputationToPirate(@PathVariable UUID id,  TeamTreasuryCharacteristicClient dto);

    @PatchMapping("/api/pirates/{id}/treasury/withdraw")
    void withdrawPirateTreasury(@PathVariable UUID id, TeamTreasuryCharacteristicClient dto);
}
