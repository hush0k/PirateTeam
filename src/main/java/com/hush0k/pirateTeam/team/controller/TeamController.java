package com.hush0k.pirateTeam.team.controller;

import com.hush0k.pirateTeam.team.dto.request.*;
import com.hush0k.pirateTeam.team.dto.response.TeamResponseDto;
import com.hush0k.pirateTeam.team.service.TeamService;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
@Tag(name = "Teams", description = "API for managing teams")
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new team")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Team created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public TeamResponseDto create(@Valid @RequestBody TeamCreateDto dto) {
        return teamService.create(dto);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update an existing team")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Team updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Team not found")
    })
    public TeamResponseDto update(
            @Valid @RequestBody TeamUpdateDto dto,
            @Parameter(description = "Team UUID") @PathVariable UUID id
    ) {
        return teamService.update(dto, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a team by ID", security = @SecurityRequirement(name = "Bearer Auth"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Team deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Team not found")
    })
    public void delete(
            @Parameter(description = "Team UUID") @PathVariable UUID id
    ) {
        teamService.delete(id);
    }

    @GetMapping
    @Operation(summary = "Get all teams")
    @ApiResponse(responseCode = "200", description = "List of all teams")
    public List<TeamResponseDto> getAll() {
        return teamService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a team by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Team found"),
            @ApiResponse(responseCode = "404", description = "Team not found")
    })
    public TeamResponseDto get(
            @Parameter(description = "Team UUID") @PathVariable UUID id
    ) {
        return teamService.findById(id);
    }

    @PatchMapping("/{id}/treasury/add")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Add treasure to team treasury")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Team treasury increased successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Team not found")
    })
    public TeamResponseDto addTreasury(
            @Parameter(description = "Team UUID") @PathVariable UUID id,
            @Valid @RequestBody TeamTreasuryChangeDto dto
    ) {
        return teamService.addTreasury(id, dto);
    }

    @PatchMapping("/{id}/treasury/withdraw")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Withdraw treasure from team treasury")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Team treasury reduced successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or insufficient treasury"),
            @ApiResponse(responseCode = "404", description = "Team not found")
    })
    public TeamResponseDto withdrawTreasury(
            @Parameter(description = "Team UUID") @PathVariable UUID id,
            @Valid @RequestBody TeamTreasuryChangeDto dto
    ) {
        return teamService.withdrawTreasury(id, dto);
    }

    @PatchMapping("/{id}/reputation/add")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Add reputation to team")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Team reputation increased successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Team not found")
    })
    public TeamResponseDto addReputation(
            @Parameter(description = "Team UUID") @PathVariable UUID id,
            @Valid @RequestBody TeamReputationChangeDto dto
    ) {
        return teamService.addReputation(id, dto);
    }

    @PatchMapping("/{id}/reputation/reduce")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Reduce team reputation")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Team reputation reduced successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Team not found")
    })
    public TeamResponseDto reduceReputation(
            @Parameter(description = "Team UUID") @PathVariable UUID id,
            @Valid @RequestBody TeamReputationChangeDto dto
    ) {
        return teamService.reduceReputation(id, dto);
    }

    @PatchMapping("/{id}/pirates/add")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Add new pirates to the team")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pirates added to the team successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or pirates already in team"),
            @ApiResponse(responseCode = "404", description = "Team not found")
    })
    public TeamResponseDto addPirate(
            @Parameter(description = "Team UUID") @PathVariable UUID id,
            @Valid @RequestBody TeamMembersChangeDto dto
    ) {
        return teamService.addNewPirate(id, dto);
    }

    @PatchMapping("/{id}/pirates/remove")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Remove pirates from the team")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pirates removed from the team successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or pirates are not in team"),
            @ApiResponse(responseCode = "404", description = "Team not found")
    })
    public TeamResponseDto removePirate(
            @Parameter(description = "Team UUID") @PathVariable UUID id,
            @Valid @RequestBody TeamMembersChangeDto dto
    ) {
        return teamService.removePirate(id, dto);
    }



}
