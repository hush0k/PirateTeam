package com.hush0k.pirateTeam.island.service;

import com.hush0k.pirateTeam.exception.island.InsufficientDistanceToFinishOperationException;
import com.hush0k.pirateTeam.exception.island.IslandNotOwnerException;
import com.hush0k.pirateTeam.exception.island.IslandNotRequiredLevelException;
import com.hush0k.pirateTeam.exception.team.InsufficientTreasuryException;
import com.hush0k.pirateTeam.fleet.client.dto.TeamTreasuryCharacteristicClient;
import com.hush0k.pirateTeam.island.client.FleetClientService;
import com.hush0k.pirateTeam.island.client.PirateClientService;
import com.hush0k.pirateTeam.island.client.dto.FleetClientDto;
import com.hush0k.pirateTeam.island.client.dto.PirateClientDto;
import com.hush0k.pirateTeam.island.domain.Island;
import com.hush0k.pirateTeam.island.dto.request.IslandTaxChangeDto;
import com.hush0k.pirateTeam.island.dto.response.IslandResponseDto;
import com.hush0k.pirateTeam.island.dto.response.PayCheckDto;
import com.hush0k.pirateTeam.island.enums.IslandLevel;
import com.hush0k.pirateTeam.island.mapper.IslandMapper;
import com.hush0k.pirateTeam.island.repository.IslandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class IslandManagementService {

    private final IslandService islandService;
    private final IslandRepository islandRepository;
    private final IslandMapper islandMapper;
    private final PirateClientService pirateClientService;
    private final FleetClientService fleetClientService;

    public IslandResponseDto upgradeIsland(UUID islandId, UUID islandOwnerId, IslandLevel islandLevel) {
        Island island = islandService.getExisting(islandId);
        checkOwner(islandId, islandOwnerId);

        if (island.getLevel() != islandLevel.getRequiredLevel()) {
            throw new IslandNotRequiredLevelException(islandLevel.getRequiredLevel());
        }

        PirateClientDto owner = pirateClientService.getPirate(islandOwnerId);
        if (owner.treasury() < islandLevel.getUpgradePrice()) {
            throw new InsufficientTreasuryException("улучшение острова",
                    (islandLevel.getUpgradePrice() - owner.treasury()));
        }

        pirateClientService.withdrawPirateTreasury(islandOwnerId,
                new TeamTreasuryCharacteristicClient(islandLevel.getUpgradePrice()));

        island.setLevel(islandLevel);

        Island updatedIsland = islandRepository.save(island);
        return islandMapper.toIslandResponseDto(updatedIsland);
    }

    public PayCheckDto takeProfit(UUID islandId,  UUID ownerId) {
        Island island = islandService.getExisting(islandId);
        checkOwner(islandId, ownerId);

        FleetClientDto owner = fleetClientService.getFleetByOwnerId(island.getOwnerId());

        int distance = calculateDistance(island.getCoordinateX(), island.getCoordinateY(), owner.coordinateX(), owner.coordinateY());

        if(distance < 10){
            throw new InsufficientDistanceToFinishOperationException(distance);
        }

        islandService.calculateStatistics(islandId);

        int treasury =
                (int)((1500 * island.getLevel().getGoldMultiplier()) +
                        (island.getPopulation() * 2) +
                        (island.getGoldTurnover() * island.getTaxPercentage()));
        int reputation = (int)(5 * island.getLevel().getReputationMultiplier());

        pirateClientService.addReputationToPirate(owner.id(),
                new TeamTreasuryCharacteristicClient(reputation));
        pirateClientService.addTreasuryToPirate(owner.id(),
                new TeamTreasuryCharacteristicClient(treasury));

        return  new PayCheckDto(treasury, reputation);
    }

    public IslandResponseDto upgradeMarket(UUID id, UUID ownerId){
        Island island = islandService.getExisting(id);
        checkOwner(id, ownerId);

        PirateClientDto owner = pirateClientService.getPirate(id);

        if (owner.treasury() < 3000){
            throw new InsufficientTreasuryException("улучшение города", 3000);
        }

        pirateClientService.withdrawPirateTreasury(owner.id(),
                new TeamTreasuryCharacteristicClient(3000));

        island.setShipTrafficPerDay(island.getShipTrafficPerDay() + 1);

        Island updatedIsland = islandRepository.save(island);
        return islandMapper.toIslandResponseDto(updatedIsland);
    }

    public IslandResponseDto addTax(UUID id, UUID ownerId, IslandTaxChangeDto dto) {
        Island island = islandService.getExisting(id);
        checkOwner(id, ownerId);
        island.setTaxPercentage(island.getTaxPercentage() + dto.amount());
        Island updatedIsland = islandRepository.save(island);
        return islandMapper.toIslandResponseDto(updatedIsland);
    }

    public IslandResponseDto withdrawTax(UUID id, UUID ownerId, IslandTaxChangeDto dto) {
        Island island = islandService.getExisting(id);
        checkOwner(id, ownerId);
        island.setTaxPercentage(Math.max(0, island.getTaxPercentage() - dto.amount()));
        Island updatedIsland = islandRepository.save(island);
        return islandMapper.toIslandResponseDto(updatedIsland);
    }


    public void checkOwner(UUID id, UUID ownerId) {
        Island island = islandService.getExisting(id);
        if (island.getOwnerId().equals(ownerId)) {
            throw new IslandNotOwnerException(id, ownerId);
        }
    }

    public int calculateDistance(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}
