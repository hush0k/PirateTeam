package com.hush0k.pirateTeam.fleet.controller;

import com.hush0k.pirateTeam.fleet.dto.request.FleetCreateDto;
import com.hush0k.pirateTeam.fleet.dto.request.FleetMoveDto;
import com.hush0k.pirateTeam.fleet.dto.request.FleetResourceChangeDto;
import com.hush0k.pirateTeam.fleet.dto.request.FleetUpdateDto;
import com.hush0k.pirateTeam.fleet.dto.response.FleetAttackResult;
import com.hush0k.pirateTeam.fleet.dto.response.FleetCaptureIsland;
import com.hush0k.pirateTeam.fleet.dto.response.FleetFindTreasure;
import com.hush0k.pirateTeam.fleet.dto.response.FleetNewCoordinate;
import com.hush0k.pirateTeam.fleet.dto.response.FleetResponseDto;
import com.hush0k.pirateTeam.fleet.dto.response.FleetStatsDto;
import com.hush0k.pirateTeam.fleet.service.FleetGameplayService;
import com.hush0k.pirateTeam.fleet.service.FleetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/fleets")
@RequiredArgsConstructor
@Tag(name = "Fleets", description = "API for managing fleets")
public class FleetController {

    private final FleetService fleetService;
    private final FleetGameplayService fleetGameplayService;


    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update an existing fleet")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fleet updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Fleet not found")
    })
    public FleetResponseDto update(
            @Valid @RequestBody FleetUpdateDto dto,
            @Parameter(description = "Fleet UUID") @PathVariable UUID id
    ) {
        return fleetService.update(dto, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a fleet by ID", security = @SecurityRequirement(name = "Bearer Auth"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Fleet deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Fleet not found")
    })
    public void delete(
            @Parameter(description = "Fleet UUID") @PathVariable UUID id
    ) {
        fleetService.delete(id);
    }

    @PostMapping("/owners/{ownerId}/ensure")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Create or get existing fleet for owner")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fleet created or already exists"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public FleetResponseDto createFleet(
            @Valid @RequestBody FleetCreateDto dto,
            @Parameter(description = "Owner UUID") @PathVariable UUID ownerId
    ) {
        return fleetService.create(dto);
    }

    @GetMapping
    @Operation(summary = "Get all fleets")
    @ApiResponse(responseCode = "200", description = "List of all fleets")
    public List<FleetResponseDto> getAll() {
        return fleetService.getAll();
    }

    @GetMapping("/by-owner/{ownerId}")
    @Operation(summary = "Get a fleet by owner ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fleet found"),
            @ApiResponse(responseCode = "404", description = "Fleet not found")
    })
    public Optional<FleetResponseDto> getByOwnerId(
            @Parameter(description = "Owner pirate UUID") @PathVariable UUID ownerId
    ) {
        return fleetService.getByOwnerId(ownerId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a fleet by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fleet found"),
            @ApiResponse(responseCode = "404", description = "Fleet not found")
    })
    public FleetResponseDto get(
            @Parameter(description = "Fleet UUID") @PathVariable UUID id
    ) {
        return fleetService.findById(id);
    }

    @GetMapping("/{id}/stats")
    @Operation(summary = "Get combat stats for a fleet")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fleet stats calculated successfully"),
            @ApiResponse(responseCode = "404", description = "Fleet not found")
    })
    public FleetStatsDto getStats(
            @Parameter(description = "Fleet UUID") @PathVariable UUID id
    ) {
        return fleetService.getStats(id);
    }

    @PatchMapping("/{id}/ammo/add")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Add ammo to fleet")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fleet ammo increased successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Fleet not found")
    })
    public FleetResponseDto addAmmo(
            @Parameter(description = "Fleet UUID") @PathVariable UUID id,
            @Valid @RequestBody FleetResourceChangeDto dto
    ) {
        return fleetService.addAmmo(id, dto);
    }

    @PatchMapping("/{id}/ammo/withdraw")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Withdraw ammo from fleet")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fleet ammo reduced successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or not enough ammo"),
            @ApiResponse(responseCode = "404", description = "Fleet not found")
    })
    public FleetResponseDto withdrawAmmo(
            @Parameter(description = "Fleet UUID") @PathVariable UUID id,
            @Valid @RequestBody FleetResourceChangeDto dto
    ) {
        return fleetService.withdrawAmmo(id, dto);
    }

    @PatchMapping("/{id}/provision/add")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Add provision to fleet")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fleet provision increased successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Fleet not found")
    })
    public FleetResponseDto addProvision(
            @Parameter(description = "Fleet UUID") @PathVariable UUID id,
            @Valid @RequestBody FleetResourceChangeDto dto
    ) {
        return fleetService.addProvision(id, dto);
    }

    @PatchMapping("/{id}/provision/withdraw")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Withdraw provision from fleet")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fleet provision reduced successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or not enough provision"),
            @ApiResponse(responseCode = "404", description = "Fleet not found")
    })
    public FleetResponseDto withdrawProvision(
            @Parameter(description = "Fleet UUID") @PathVariable UUID id,
            @Valid @RequestBody FleetResourceChangeDto dto
    ) {
        return fleetService.withdrawProvision(id, dto);
    }

    @PostMapping("/{fleetId}/move")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Move fleet to target coordinates")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fleet moved successfully"),
            @ApiResponse(responseCode = "400", description = "Not enough provision"),
            @ApiResponse(responseCode = "404", description = "Fleet not found")
    })
    public FleetNewCoordinate moveToTarget(
            @Parameter(description = "Fleet UUID") @PathVariable UUID fleetId,
            @Valid @RequestBody FleetMoveDto dto
    ) {
        return fleetGameplayService.moveToTarget(fleetId, dto);
    }

    @PostMapping("/{fleetId}/attack/{enemyFleetId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Attack enemy fleet")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Battle completed"),
            @ApiResponse(responseCode = "400", description = "Not enough ammo or enemy out of range"),
            @ApiResponse(responseCode = "404", description = "Fleet not found")
    })
    public FleetAttackResult attackToEnemyFleet(
            @Parameter(description = "Attacking fleet UUID") @PathVariable UUID fleetId,
            @Parameter(description = "Enemy fleet UUID") @PathVariable UUID enemyFleetId
    ) {
        return fleetGameplayService.attackToEnemyFleet(fleetId, enemyFleetId);
    }

    @PostMapping("/{fleetId}/treasure/find")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Search treasure with fleet")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Treasure search completed"),
            @ApiResponse(responseCode = "404", description = "Fleet or team not found")
    })
    public FleetFindTreasure findTreasure(
            @Parameter(description = "Fleet UUID") @PathVariable UUID fleetId
    ) {
        return fleetGameplayService.findTreasury(fleetId);
    }

    @PostMapping("/{fleetId}/capture/{islandId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Capture island with fleet")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Island capture completed"),
            @ApiResponse(responseCode = "400", description = "Not enough ammo or island out of range"),
            @ApiResponse(responseCode = "404", description = "Fleet, island, team, or pirate not found")
    })
    public FleetCaptureIsland captureIsland(
            @Parameter(description = "Fleet UUID") @PathVariable UUID fleetId,
            @Parameter(description = "Island UUID") @PathVariable UUID islandId
    ) {
        return fleetGameplayService.captureIsland(fleetId, islandId);
    }

}
