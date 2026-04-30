package com.hush0k.pirateTeam.market.controller;

import com.hush0k.pirateTeam.market.dto.response.ReceiptDto;
import com.hush0k.pirateTeam.market.service.MarketService;
import com.hush0k.pirateTeam.ship.dto.response.ShipResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/market")
@RequiredArgsConstructor
@Tag(name = "Market", description = "API for buying market goods")
public class MarketController {

    private final MarketService marketService;

    @PostMapping("/fleets/{fleetId}/ammo/buy")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buy ammo for fleet")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ammo bought successfully"),
            @ApiResponse(responseCode = "400", description = "Insufficient treasury or cargo space"),
            @ApiResponse(responseCode = "404", description = "Fleet or team not found")
    })
    public ReceiptDto buyAmmo(
            @Parameter(description = "Fleet UUID") @PathVariable UUID fleetId,
            @Parameter(description = "Ammo quantity") @RequestParam int quantity
    ) {
        return marketService.buyAmmo(fleetId, quantity);
    }

    @PostMapping("/fleets/{fleetId}/provision/buy")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buy provision for fleet")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Provision bought successfully"),
            @ApiResponse(responseCode = "400", description = "Insufficient treasury or cargo space"),
            @ApiResponse(responseCode = "404", description = "Fleet or team not found")
    })
    public ReceiptDto buyProvision(
            @Parameter(description = "Fleet UUID") @PathVariable UUID fleetId,
            @Parameter(description = "Provision quantity") @RequestParam int quantity
    ) {
        return marketService.buyProvision(fleetId, quantity);
    }

    @GetMapping("/ships")
    @Operation(summary = "Get ships available for sale")
    @ApiResponse(responseCode = "200", description = "List of ships available for sale")
    public List<ShipResponseDto> showMarket() {
        return marketService.showMarket();
    }

    @PatchMapping("/ships/{shipId}/buy/{captainId}")
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
        return marketService.buyShip(shipId, captainId);
    }

    @PatchMapping("/ships/{shipId}/sale/{captainId}")
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
        return marketService.offerForSale(shipId, captainId);
    }
}
