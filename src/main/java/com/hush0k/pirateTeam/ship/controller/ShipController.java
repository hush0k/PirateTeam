package com.hush0k.pirateTeam.ship.controller;

import com.hush0k.pirateTeam.ship.dto.request.ShipCreateDto;
import com.hush0k.pirateTeam.ship.dto.request.ShipUpdateDto;
import com.hush0k.pirateTeam.ship.dto.response.FleetShipStatsDto;
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

    @GetMapping("/market")
    @Operation(summary = "Get ships available for sale")
    @ApiResponse(responseCode = "200", description = "List of ships available for sale")
    public List<ShipResponseDto> showMarket() {
        return shipService.showMarket();
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
        return shipService.getById(id);
    }

    @GetMapping("/by-fleet/{fleetId}/stats")
    @Operation(summary = "Get ship stats by fleet ID")
    @ApiResponse(responseCode = "200", description = "Fleet ship stats calculated successfully")
    public FleetShipStatsDto getStatsByFleetId(
            @Parameter(description = "Fleet UUID") @PathVariable UUID fleetId
    ) {
        return shipService.getByFleetId(fleetId);
    }

    @PatchMapping("/{shipId}/capitan/{captainId}")
    @Operation(summary = "Assign a captain to a ship")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Captain assigned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or pirate is not a captain"),
            @ApiResponse(responseCode = "404", description = "Ship or pirate not found")
    })
    public ShipResponseDto assignCaptain(
            @Parameter(description = "Ship UUID") @PathVariable UUID shipId,
            @Parameter(description = "Captain UUID") @PathVariable UUID captainId
    ) {
        return shipService.assignCaptain(shipId, captainId);
    }

    @PatchMapping("/{shipId}/buy/{captainId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buy a ship and assign captain with auto-created fleet")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ship bought successfully"),
            @ApiResponse(responseCode = "404", description = "Ship or pirate not found")
    })
    public ShipResponseDto buyShip(
            @Parameter(description = "Ship UUID") @PathVariable UUID shipId,
            @Parameter(description = "Captain UUID") @PathVariable UUID captainId
    ) {
        return shipService.buyShip(shipId, captainId);
    }

    @PatchMapping("/{shipId}/sale/{captainId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Offer ship for sale by captain")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ship offered for sale successfully"),
            @ApiResponse(responseCode = "403", description = "Captain is not owner of this ship"),
            @ApiResponse(responseCode = "404", description = "Ship not found")
    })
    public ShipResponseDto offerShipForSale(
            @Parameter(description = "Ship UUID") @PathVariable UUID shipId,
            @Parameter(description = "Captain UUID") @PathVariable UUID captainId
    ) {
        return shipService.offerForSale(shipId, captainId);
    }



}
