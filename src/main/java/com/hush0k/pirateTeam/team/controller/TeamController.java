package com.hush0k.pirateTeam.team.controller;

import com.hush0k.pirateTeam.team.dto.request.TeamCreateDto;
import com.hush0k.pirateTeam.team.dto.request.TeamReputationChangeDto;
import com.hush0k.pirateTeam.team.dto.request.TeamTreasuryChangeDto;
import com.hush0k.pirateTeam.team.dto.request.TeamUpdateDto;
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

    @PatchMapping("/{id}/treasury")
    @Operation(summary = "Change team treasury")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Team treasury changed"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Team not found")
    })
    public TeamResponseDto changeTreasury(
            @Parameter(description = "Team UUID") @PathVariable UUID id,
            @Valid @RequestBody TeamTreasuryChangeDto dto
    ) {
        return teamService.changeTreasury(id, dto);
    }

    @PatchMapping("/{id}/reputation")
    @Operation(summary = "Change team reputation")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Team reputation changed"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Team not found")
    })
    public TeamResponseDto changeReputation(
            @Parameter(description = "Team UUID") @PathVariable UUID id,
            @Valid @RequestBody TeamReputationChangeDto dto
    ) {
        return teamService.changeReputation(id, dto);
    }
}
