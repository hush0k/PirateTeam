package com.hush0k.pirateTeam.island.service;

import com.hush0k.pirateTeam.exception.island.IslandNotFoundException;
import com.hush0k.pirateTeam.island.client.PirateClientService;
import com.hush0k.pirateTeam.island.domain.Island;
import com.hush0k.pirateTeam.island.dto.request.IslandCreateDto;
import com.hush0k.pirateTeam.island.dto.request.IslandUpdateDto;
import com.hush0k.pirateTeam.island.dto.response.IslandResponseDto;
import com.hush0k.pirateTeam.island.mapper.IslandMapper;
import com.hush0k.pirateTeam.island.repository.IslandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class IslandService {

    private final IslandRepository islandRepository;
    private final IslandMapper islandMapper;
    private final PirateClientService  pirateClientService;

    @Transactional(readOnly = true)
    public Island getExisting(UUID id) {
        log.debug("Fetching island with id: {}", id);
        return islandRepository.findById(id).orElseThrow(
                () -> {
                    log.warn("Island not found with id: {}", id);
                    return new IslandNotFoundException(id);
                }
        );
    }

    public IslandResponseDto create(IslandCreateDto dto) {
        log.info("Creating new island with name: {}", dto.name());
        Island island = islandMapper.toIsland(dto);
        Island savedIsland = islandRepository.save(island);
        calculateStatistics(savedIsland.getId());
        Island updatedIsland = islandRepository.save(island);
        log.info("Island created successfully with id: {}", updatedIsland.getId());
        return islandMapper.toIslandResponseDto(updatedIsland);
    }

    public IslandResponseDto update(IslandUpdateDto dto, UUID id) {
        log.info("Updating island with id: {}", id);
        Island island = getExisting(id);
        islandMapper.updateIslandDto(dto, island);
        Island updatedIsland = islandRepository.save(island);
        log.info("Island updated successfully with id: {}", id);
        return islandMapper.toIslandResponseDto(updatedIsland);
    }

    public void delete(UUID id) {
        log.info("Deleting island with id: {}", id);
        Island island = getExisting(id);
        islandRepository.delete(island);
        log.info("Island deleted successfully with id: {}", id);
    }

    @Transactional(readOnly = true)
    public IslandResponseDto getById(UUID id) {
        log.debug("Fetching island by id: {}", id);
        Island island = getExisting(id);
        return islandMapper.toIslandResponseDto(island);
    }

    @Transactional(readOnly = true)
    public List<IslandResponseDto> getAll() {
        log.debug("Fetching all islands");
        List<Island> islands = islandRepository.findAll();
        log.debug("Found {} islands", islands.size());
        return islandMapper.toIslandResponseDtoList(islands);
    }

    public void calculateStatistics(UUID id) {
        log.debug("Calculating statistics for island with id: {}", id);
        Island island = getExisting(id);

        int population = 100 * island.getLevel().getPopulationMultiplier();
        population = (int)(island.getTaxPercentage() == 1 ? population * 1.8 :
                island.getTaxPercentage() < 1.3 ? population * 1.5 :
                island.getTaxPercentage() < 1.6 ? population * 1.2 :
                island.getTaxPercentage() < 2.0 ? population :
                island.getTaxPercentage() < 2.3 ? population * 0.8 :
                island.getTaxPercentage() < 2.7 ? population * 0.5 :
                island.getTaxPercentage() < 3.0 ? population * 0.2 :
                population * 0.0);

        int traffic = island.getShipTrafficPerDay();
        double multiplier = 1.0 + Math.min(traffic / 5, 9) * 0.1;
        population = (int)(population * multiplier);

        int goldTurnover = population * 3 + traffic * 50;

        island.setGoldTurnover(goldTurnover);
        island.setPopulation(population);

        Island updatedIsland = islandRepository.save(island);
        log.info("Island updated successfully with id: {}", updatedIsland.getId());
        islandMapper.toIslandResponseDto(updatedIsland);
    }


    public IslandResponseDto assignNewOwner(UUID islandId, UUID ownerId){
        Island island = getExisting(islandId);
        pirateClientService.getPirate(ownerId);

        island.setOwnerId(ownerId);
        Island updatedIsland = islandRepository.save(island);
        log.info("Island assigned successfully with id: {}", island.getId());
        return islandMapper.toIslandResponseDto(updatedIsland);
    }
}
