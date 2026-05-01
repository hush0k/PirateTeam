package com.hush0k.pirateTeam.island.service;

import com.hush0k.pirateTeam.exception.island.IslandNotFoundException;
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

    @Transactional(readOnly = true)
    private Island getExisting(UUID id) {
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
        log.info("Island created successfully with id: {}", savedIsland.getId());
        return islandMapper.toIslandResponseDto(savedIsland);
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
    public IslandResponseDto findById(UUID id) {
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
}
