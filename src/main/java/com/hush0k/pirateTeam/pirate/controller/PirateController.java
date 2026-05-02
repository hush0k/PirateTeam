package com.hush0k.pirateTeam.pirate.controller;

import com.hush0k.pirateTeam.pirate.dto.request.*;
import com.hush0k.pirateTeam.pirate.dto.response.PirateResponseDto;
import com.hush0k.pirateTeam.pirate.enums.Rank;
import com.hush0k.pirateTeam.pirate.service.PirateGamePlayService;
import com.hush0k.pirateTeam.pirate.service.PirateService;
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
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/pirates")
@RequiredArgsConstructor
@Tag(name = "Pirates", description = "API for managing pirates")
public class PirateController {

    private final PirateService pirateService;
    private final PirateGamePlayService pirateGamePlayService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new pirate")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pirate created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public PirateResponseDto create(@Valid @RequestBody PirateCreateDto dto) {
        return pirateService.create(dto);
    }


    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update an existing pirate")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pirate updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Pirate not found")
    })
    public PirateResponseDto update(
            @Valid @RequestBody PirateUpdateDto dto,
            @Parameter(description = "Pirate UUID") @PathVariable UUID id
    ) {
        return pirateService.update(dto, id);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a pirate by ID", security = @SecurityRequirement(name = "Bearer Auth"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pirate deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Pirate not found")
    })
    public void delete(
            @Parameter(description = "Pirate UUID") @PathVariable UUID id
    ) {
        pirateService.delete(id);
    }


    @GetMapping
    @Operation(summary = "Get all pirates")
    @ApiResponse(responseCode = "200", description = "List of all pirates")
    public List<PirateResponseDto> getAll() {
        return pirateService.getAll();
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get a pirate by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pirate found"),
            @ApiResponse(responseCode = "404", description = "Pirate not found")
    })
    public PirateResponseDto get(
            @Parameter(description = "Pirate UUID") @PathVariable UUID id
    ) {
        return pirateService.findById(id);
    }


    @PatchMapping("/{id}/rank")
    @Operation(summary = "Change rank of pirate")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rank of pirate changed"),
            @ApiResponse(responseCode = "404", description = "Pirate not found")
    })
    public PirateResponseDto changeRank(
            @Parameter(description = "Pirate UUID") @PathVariable UUID id,
            @Parameter(description = "New rank for the pirate") @RequestParam Rank rank
    ) {
        return pirateService.changeRank(id, rank);
    }

    @PatchMapping("/{id}/reputation/add")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Add reputation to pirate")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pirate reputation increased successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Pirate not found")
    })
    public PirateResponseDto addReputation(
            @Parameter(description = "Pirate UUID") @PathVariable UUID id,
            @Valid @RequestBody PirateReputationChange dto
    ) {
        return pirateService.addReputation(id, dto);
    }

    @PatchMapping("/{id}/reputation/remove")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Remove reputation from pirate")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pirate reputation reduced successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Pirate not found")
    })
    public PirateResponseDto removeReputation(
            @Parameter(description = "Pirate UUID") @PathVariable UUID id,
            @Valid @RequestBody PirateReputationChange dto
    ) {
        return pirateService.removeReputation(id, dto);
    }

    @PatchMapping("/{id}/treasury/add")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Add gold to pirate treasury")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pirate treasury increased successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Pirate not found")
    })
    public PirateResponseDto addTreasury(
            @Parameter(description = "Pirate UUID") @PathVariable UUID id,
            @Valid @RequestBody PirateTreasuryChangeDto dto
    ) {
        return pirateService.addTreasury(id, dto);
    }

    @PatchMapping("/{id}/treasury/withdraw")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Withdraw gold from pirate treasury")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pirate treasury reduced successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or insufficient treasury"),
            @ApiResponse(responseCode = "404", description = "Pirate not found")
    })
    public PirateResponseDto withdrawTreasury(
            @Parameter(description = "Pirate UUID") @PathVariable UUID id,
            @Valid @RequestBody PirateTreasuryChangeDto dto
    ) {
        return pirateService.withdrawTreasury(id, dto);
    }

    @PatchMapping("/{id}/exp/add")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Add exp to pirate")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pirate exp increased successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Pirate not found")
    })
    public PirateResponseDto addExp(
            @Parameter(description = "Pirate UUID") @PathVariable UUID id,
            @Valid @RequestBody PirateExpChangeDto dto
    ) {
        return pirateGamePlayService.addExp(id, dto);
    }

    @PatchMapping("/{id}/exp/remove")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Remove exp from pirate")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pirate exp reduced successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Pirate not found")
    })
    public PirateResponseDto removeExp(
            @Parameter(description = "Pirate UUID") @PathVariable UUID id,
            @Valid @RequestBody PirateExpChangeDto dto
    ) {
        return pirateGamePlayService.removeExp(id, dto);
    }

    @PatchMapping("/{id}/upgrade/bloodlust")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Upgrade pirate bloodlust using exp")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pirate bloodlust upgraded successfully"),
            @ApiResponse(responseCode = "400", description = "Not enough exp or invalid amount"),
            @ApiResponse(responseCode = "404", description = "Pirate not found")
    })
    public PirateResponseDto upgradeBloodlust(
            @Parameter(description = "Pirate UUID") @PathVariable UUID id,
            @Parameter(description = "Amount to upgrade") @RequestParam int amount
    ) {
        return pirateGamePlayService.upgradeBloodlust(id, amount);
    }

    @PatchMapping("/{id}/upgrade/intelligence")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Upgrade pirate intelligence using exp")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pirate intelligence upgraded successfully"),
            @ApiResponse(responseCode = "400", description = "Not enough exp or invalid amount"),
            @ApiResponse(responseCode = "404", description = "Pirate not found")
    })
    public PirateResponseDto upgradeIntelligence(
            @Parameter(description = "Pirate UUID") @PathVariable UUID id,
            @Parameter(description = "Amount to upgrade") @RequestParam int amount
    ) {
        return pirateGamePlayService.upgradeIntelligence(id, amount);
    }

    @PatchMapping("/{id}/upgrade/strength")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Upgrade pirate strength using exp")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pirate strength upgraded successfully"),
            @ApiResponse(responseCode = "400", description = "Not enough exp or invalid amount"),
            @ApiResponse(responseCode = "404", description = "Pirate not found")
    })
    public PirateResponseDto upgradeStrength(
            @Parameter(description = "Pirate UUID") @PathVariable UUID id,
            @Parameter(description = "Amount to upgrade") @RequestParam int amount
    ) {
        return pirateGamePlayService.upgradeStrength(id, amount);
    }

    @PatchMapping("/{id}/upgrade/rank")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Upgrade pirate rank using exp")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pirate rank upgraded successfully"),
            @ApiResponse(responseCode = "400", description = "Not enough exp"),
            @ApiResponse(responseCode = "404", description = "Pirate not found")
    })
    public PirateResponseDto upgradeLevel(
            @Parameter(description = "Pirate UUID") @PathVariable UUID id
    ) {
        return pirateGamePlayService.upgradeLevel(id);
    }


    @PatchMapping("/team/{teamId}/bulk")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Assign multiple pirates to a team (batch, called by TeamService via Feign)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pirates assigned to team successfully"),
            @ApiResponse(responseCode = "404", description = "One or more pirates not found")
    })
    public void assignManyToTeam(
            @Parameter(description = "Team UUID") @PathVariable UUID teamId,
            @RequestBody Set<UUID> pirateIds
    ) {
        pirateService.assignManyToTeam(pirateIds, teamId);
    }

    @PostMapping("/team/{teamId}/bulk/remove")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove multiple pirates from a team (batch, called by TeamService via Feign)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pirates removed from team successfully")
    })
    public void removeManyFromTeam(
            @Parameter(description = "Team UUID") @PathVariable UUID teamId,
            @RequestBody Set<UUID> pirateIds
    ) {
        pirateService.removeManyFromTeam(pirateIds, teamId);
    }
}
