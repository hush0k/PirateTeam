package com.hush0k.pirateTeam.island.controller;

import com.hush0k.pirateTeam.auth.service.JwtService;
import com.hush0k.pirateTeam.exception.island.IslandNotOwnerException;
import com.hush0k.pirateTeam.island.dto.request.IslandCreateDto;
import com.hush0k.pirateTeam.island.dto.request.IslandTaxChangeDto;
import com.hush0k.pirateTeam.island.dto.request.IslandUpdateDto;
import com.hush0k.pirateTeam.island.dto.response.IslandResponseDto;
import com.hush0k.pirateTeam.island.dto.response.PayCheckDto;
import com.hush0k.pirateTeam.island.enums.IslandLevel;
import com.hush0k.pirateTeam.island.service.IslandManagementService;
import com.hush0k.pirateTeam.island.service.IslandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
    private final IslandManagementService islandManagementService;
    private final JwtService jwtService;

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
        return islandService.getById(id);
    }

    @PatchMapping("/{islandId}/owner/{ownerId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Assign a new owner to island")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Owner assigned successfully"),
            @ApiResponse(responseCode = "404", description = "Island or pirate not found")
    })
    public IslandResponseDto assignNewOwner(
            @Parameter(description = "Island UUID") @PathVariable UUID islandId,
            @Parameter(description = "Pirate UUID") @PathVariable UUID ownerId
    ) {
        return islandService.assignNewOwner(islandId, ownerId);
    }

    @PostMapping("/{id}/statistics/calculate")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Recalculate island population and gold turnover")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Island statistics recalculated successfully"),
            @ApiResponse(responseCode = "404", description = "Island not found")
    })
    public void calculateStatistics(
            @Parameter(description = "Island UUID") @PathVariable UUID id
    ) {
        islandService.calculateStatistics(id);
    }

    @PatchMapping("/{id}/tax/add/{ownerId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Add tax percentage to island")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Island tax increased successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Only owner can modify island"),
            @ApiResponse(responseCode = "404", description = "Island not found")
    })
    public IslandResponseDto addTax(
            @Parameter(description = "Island UUID") @PathVariable UUID id,
            @Parameter(description = "Owner pirate UUID") @PathVariable UUID ownerId,
            HttpServletRequest request,
            @Valid @RequestBody IslandTaxChangeDto dto
    ) {
        ensureActorIsOwner(id, ownerId, request);
        return islandManagementService.addTax(id, ownerId, dto);
    }

    @PatchMapping("/{id}/tax/withdraw/{ownerId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Withdraw tax percentage from island")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Island tax reduced successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Only owner can modify island"),
            @ApiResponse(responseCode = "404", description = "Island not found")
    })
    public IslandResponseDto withdrawTax(
            @Parameter(description = "Island UUID") @PathVariable UUID id,
            @Parameter(description = "Owner pirate UUID") @PathVariable UUID ownerId,
            HttpServletRequest request,
            @Valid @RequestBody IslandTaxChangeDto dto
    ) {
        ensureActorIsOwner(id, ownerId, request);
        return islandManagementService.withdrawTax(id, ownerId, dto);
    }

    @PatchMapping("/{id}/upgrade/{ownerId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Upgrade island level")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Island upgraded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid upgrade request"),
            @ApiResponse(responseCode = "403", description = "Only owner can upgrade island"),
            @ApiResponse(responseCode = "404", description = "Island not found")
    })
    public IslandResponseDto upgradeIsland(
            @Parameter(description = "Island UUID") @PathVariable UUID id,
            @Parameter(description = "Owner pirate UUID") @PathVariable UUID ownerId,
            @Parameter(description = "Target island level") @RequestParam IslandLevel level,
            HttpServletRequest request
    ) {
        ensureActorIsOwner(id, ownerId, request);
        return islandManagementService.upgradeIsland(id, ownerId, level);
    }

    @PostMapping("/{id}/profit/{ownerId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Take island profit")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Island profit taken successfully"),
            @ApiResponse(responseCode = "400", description = "Profit operation failed"),
            @ApiResponse(responseCode = "403", description = "Only owner can take profit"),
            @ApiResponse(responseCode = "404", description = "Island not found")
    })
    public PayCheckDto takeProfit(
            @Parameter(description = "Island UUID") @PathVariable UUID id,
            @Parameter(description = "Owner pirate UUID") @PathVariable UUID ownerId,
            HttpServletRequest request
    ) {
        ensureActorIsOwner(id, ownerId, request);
        return islandManagementService.takeProfit(id, ownerId);
    }

    @PatchMapping("/{id}/market/upgrade/{ownerId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Upgrade island market")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Island market upgraded successfully"),
            @ApiResponse(responseCode = "400", description = "Upgrade market operation failed"),
            @ApiResponse(responseCode = "403", description = "Only owner can upgrade island market"),
            @ApiResponse(responseCode = "404", description = "Island not found")
    })
    public IslandResponseDto upgradeMarket(
            @Parameter(description = "Island UUID") @PathVariable UUID id,
            @Parameter(description = "Owner pirate UUID") @PathVariable UUID ownerId,
            HttpServletRequest request
    ) {
        ensureActorIsOwner(id, ownerId, request);
        return islandManagementService.upgradeMarket(id, ownerId);
    }

    private void ensureActorIsOwner(UUID islandId, UUID ownerId, HttpServletRequest request) {
        UUID actorId = getCurrentPirateId(request);
        if (!actorId.equals(ownerId)) {
            throw new IslandNotOwnerException(islandId, actorId);
        }
    }

    private UUID getCurrentPirateId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        return jwtService.extractPirateId(token);
    }
}
