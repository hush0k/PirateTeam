package com.hush0k.pirateTeam.fleet.controller;

import com.hush0k.pirateTeam.fleet.dto.request.FleetUpdateDto;
import com.hush0k.pirateTeam.fleet.dto.response.FleetResponseDto;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/fleets")
@RequiredArgsConstructor
@Tag(name = "Fleets", description = "API for managing fleets")
public class FleetController {

    private final FleetService fleetService;

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

    @GetMapping
    @Operation(summary = "Get all fleets")
    @ApiResponse(responseCode = "200", description = "List of all fleets")
    public List<FleetResponseDto> getAll() {
        return fleetService.getAll();
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
}
