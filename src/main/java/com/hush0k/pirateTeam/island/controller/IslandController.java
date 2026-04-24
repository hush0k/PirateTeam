package com.hush0k.pirateTeam.island.controller;

import com.hush0k.pirateTeam.island.dto.request.IslandCreateDto;
import com.hush0k.pirateTeam.island.dto.request.IslandUpdateDto;
import com.hush0k.pirateTeam.island.dto.response.IslandResponseDto;
import com.hush0k.pirateTeam.island.service.IslandService;
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
@RequestMapping("/api/islands")
@RequiredArgsConstructor
@Tag(name = "Islands", description = "API for managing islands")
public class IslandController {

    private final IslandService islandService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new island")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Island created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public IslandResponseDto create(@Valid @RequestBody IslandCreateDto dto) {
        return islandService.create(dto);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update an existing island")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Island updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Island not found")
    })
    public IslandResponseDto update(
            @Valid @RequestBody IslandUpdateDto dto,
            @Parameter(description = "Island UUID") @PathVariable UUID id
    ) {
        return islandService.update(dto, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an island by ID", security = @SecurityRequirement(name = "Bearer Auth"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Island deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Island not found")
    })
    public void delete(
            @Parameter(description = "Island UUID") @PathVariable UUID id
    ) {
        islandService.delete(id);
    }

    @GetMapping
    @Operation(summary = "Get all islands")
    @ApiResponse(responseCode = "200", description = "List of all islands")
    public List<IslandResponseDto> getAll() {
        return islandService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an island by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Island found"),
            @ApiResponse(responseCode = "404", description = "Island not found")
    })
    public IslandResponseDto get(
            @Parameter(description = "Island UUID") @PathVariable UUID id
    ) {
        return islandService.findById(id);
    }
}
