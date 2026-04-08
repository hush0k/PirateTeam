package com.hush0k.pirateTeam.ship.controller;

import com.hush0k.pirateTeam.ship.dto.request.ShipCreateDto;
import com.hush0k.pirateTeam.ship.dto.request.ShipUpdateDto;
import com.hush0k.pirateTeam.ship.dto.response.ShipResponseDto;
import com.hush0k.pirateTeam.ship.service.ShipService;
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
@RequestMapping("/api/ships")
@RequiredArgsConstructor
@Tag(name = "Ships", description = "API for managing ships")
public class ShipController {

    private final ShipService shipService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new ship")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ship created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ShipResponseDto create(@Valid @RequestBody ShipCreateDto dto) {
        return shipService.create(dto);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update an existing ship")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ship updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Ship not found")
    })
    public ShipResponseDto update(
            @Valid @RequestBody ShipUpdateDto dto,
            @Parameter(description = "Ship UUID") @PathVariable UUID id
    ) {
        return shipService.update(dto, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a ship by ID", security = @SecurityRequirement(name = "Bearer Auth"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Ship deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Ship not found")
    })
    public void delete(
            @Parameter(description = "Ship UUID") @PathVariable UUID id
    ) {
        shipService.delete(id);
    }

    @GetMapping
    @Operation(summary = "Get all ships")
    @ApiResponse(responseCode = "200", description = "List of all ships")
    public List<ShipResponseDto> getAll() {
        return shipService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a ship by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ship found"),
            @ApiResponse(responseCode = "404", description = "Ship not found")
    })
    public ShipResponseDto get(
            @Parameter(description = "Ship UUID") @PathVariable UUID id
    ) {
        return shipService.findById(id);
    }
}
