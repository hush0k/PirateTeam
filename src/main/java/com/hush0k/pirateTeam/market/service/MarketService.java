package com.hush0k.pirateTeam.market.service;

import com.hush0k.pirateTeam.exception.fleet.InsufficientPirateSpaceException;
import com.hush0k.pirateTeam.exception.team.InsufficientTreasuryException;
import com.hush0k.pirateTeam.market.client.FleetFeignClient;
import com.hush0k.pirateTeam.market.client.ShipFeignClient;
import com.hush0k.pirateTeam.market.client.TeamFeignClient;
import com.hush0k.pirateTeam.market.client.dto.FleetClientStatsDto;
import com.hush0k.pirateTeam.market.client.dto.TeamClientDto;
import com.hush0k.pirateTeam.market.dto.response.ReceiptDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MarketService {

    private TeamFeignClient teamFeignClient;
    private FleetFeignClient fleetFeignClient;
    private ShipFeignClient shipFeignClient;

    public ReceiptDto buyAmmo(UUID fleetId, int quantity) {
        TeamClientDto team = teamFeignClient.getByFleetId(fleetId);

        int totalPrice = quantity * 500;

        if (totalPrice > team.treasury()) {
            throw new InsufficientTreasuryException("боеприпасы", totalPrice-team.treasury());
        }

        FleetClientStatsDto fleetStats = shipFeignClient.getFleetShipStatsByFleetId(fleetId);

        int freeSpace = fleetStats.maxCargo() - fleetStats.filledCargoSpace();

        if (freeSpace < quantity * 5) {
            throw new InsufficientPirateSpaceException("боеприпасы");
        }

//        fleetStats.setFilledCargoSpace(fleetStats.getFilledCargoSpace() + quantity * 5);

    }
}
