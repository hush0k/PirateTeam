package com.hush0k.pirateTeam.pirate.controller;

import com.hush0k.pirateTeam.pirate.dto.request.PirateCreateDto;
import com.hush0k.pirateTeam.pirate.dto.request.PirateUpdateDto;
import com.hush0k.pirateTeam.pirate.dto.response.PirateResponseDto;
import com.hush0k.pirateTeam.pirate.enums.Rank;
import com.hush0k.pirateTeam.pirate.service.PirateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pirates")
@RequiredArgsConstructor
@Tag(name = "Pirates", description = "API for managing pirates")
public class PirateController {

    private final PirateService pirateService;

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
    @Operation(summary = "Delete a pirate by ID")
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
}