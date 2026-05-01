package com.hush0k.pirateTeam.fleet.client;

import com.hush0k.pirateTeam.fleet.client.dto.PirateClientDto;
import com.hush0k.pirateTeam.fleet.client.dto.TeamDto;
import com.hush0k.pirateTeam.fleet.client.dto.TeamTreasuryCharacteristicClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(
        name = "team-client",
        contextId = "fleetTeamClient",
        url = "${app.base-url:http://localhost:8080}"
)
public interface TeamClientService {

    @GetMapping("/api/teams/{id}")
    TeamDto getTeam(@PathVariable UUID id);

    @GetMapping("/api/teams/by-fleet/{fleetId}")
    TeamDto getByFleetId(@PathVariable UUID fleetId);

    @PatchMapping("/api/teams/{teamId}/fleet/{fleetId}")
    void assignFleetToTeam(@PathVariable UUID teamId, @PathVariable UUID fleetId);

    @GetMapping("/api/pirates/{id}")
    PirateClientDto getPirateById(@PathVariable UUID id);

    @PatchMapping("/api/teams/{id}/treasury/add")
    void addTreasuryToTeam(@PathVariable UUID id, @RequestBody TeamTreasuryCharacteristicClient dto);

    @PatchMapping("/api/teams/{id}/treasury/withdraw")
    void withdrawTreasuryToTeam(@PathVariable UUID id, @RequestBody TeamTreasuryCharacteristicClient dto);

    @PatchMapping("/api/teams/{id}/fatigue/add")
    void addFatigueToTeam(@PathVariable UUID id, @RequestBody TeamTreasuryCharacteristicClient dto);

    @PatchMapping("/api/teams/{id}/loyalty/add")
    void addLoyaltyToTeam(@PathVariable UUID id, @RequestBody TeamTreasuryCharacteristicClient dto);

    @PatchMapping("/api/teams/{id}/loyalty/remove")
    void withdrawLoyaltyToTeam(@PathVariable UUID id, @RequestBody TeamTreasuryCharacteristicClient dto);

    @PatchMapping("/api/teams/{id}/morale/add")
    void addMoraleToTeam(@PathVariable UUID id, @RequestBody TeamTreasuryCharacteristicClient dto);

    @PatchMapping("/api/teams/{id}/morale/remove")
    void withdrawMoraleToTeam(@PathVariable UUID id, @RequestBody TeamTreasuryCharacteristicClient dto);
}
