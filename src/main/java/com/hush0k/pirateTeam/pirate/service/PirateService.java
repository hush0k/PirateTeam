package com.hush0k.pirateTeam.pirate.service;

import com.hush0k.pirateTeam.exception.PirateNotFoundException;
import com.hush0k.pirateTeam.pirate.client.ShipFeignClient;
import com.hush0k.pirateTeam.pirate.domain.Pirate;
import com.hush0k.pirateTeam.pirate.dto.request.PirateCreateDto;
import com.hush0k.pirateTeam.pirate.dto.request.PirateUpdateDto;
import com.hush0k.pirateTeam.pirate.dto.response.PirateResponseDto;
import com.hush0k.pirateTeam.pirate.enums.Rank;
import com.hush0k.pirateTeam.pirate.mapper.PirateMapper;
import com.hush0k.pirateTeam.pirate.repository.PirateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PirateService {

    private final PirateRepository pirateRepository;
    private final PirateMapper pirateMapper;
    private final PasswordEncoder passwordEncoder;
    private final ShipFeignClient shipFeignClient;

    @Transactional(readOnly = true)
    private Pirate getExisting(UUID pirateId) {
        log.debug("Fetching pirate with id: {}", pirateId);
        return pirateRepository.findById(pirateId).orElseThrow(
                () -> {
                    log.warn("Pirate not found with id: {}", pirateId);
                    return new PirateNotFoundException(pirateId);
                }
        );
    }

    public PirateResponseDto create(PirateCreateDto dto) {
        log.info("Creating new pirate with name: {}", dto.firstName());
        Pirate pirate = pirateMapper.toPirate(dto);
        pirate.setPassword(passwordEncoder.encode(dto.password()));
        Pirate savedPirate = pirateRepository.save(pirate);
        log.info("Pirate created successfully with id: {}", savedPirate.getId());
        return pirateMapper.toPirateResponseDto(savedPirate);
    }

    public PirateResponseDto update(PirateUpdateDto dto, UUID id) {
        log.info("Updating pirate with name: {}", dto.firstName());
        Pirate pirate = getExisting(id);
        pirateMapper.updatePirateDto(dto, pirate);
        Pirate updatedPirate = pirateRepository.save(pirate);
        log.info("Pirate updated successfully with name: {}", dto.firstName());
        return pirateMapper.toPirateResponseDto(updatedPirate);
    }

    public void delete(UUID id) {
        log.info("Deleting pirate with id: {}", id);
        Pirate pirate = getExisting(id);
        pirateRepository.delete(pirate);
        log.info("Pirate deleted successfully with id: {}", id);
    }

    @Transactional(readOnly = true)
    public PirateResponseDto findById(UUID id) {
        log.debug("Fetching pirate by id: {}", id);
        Pirate pirate = getExisting(id);
        return pirateMapper.toPirateResponseDto(pirate);
    }

    @Transactional(readOnly = true)
    public List<PirateResponseDto> getAll() {
        log.debug("Fetching all pirates");
        List<Pirate> pirates = pirateRepository.findAll();
        log.debug("Found {} pirates", pirates.size());
        return pirateMapper.toPirateAllResponseDto(pirates);
    }

    public PirateResponseDto changeRank(UUID id, Rank rank) {
        log.info("Changing rank for pirate with id: {} to rank: {}", id, rank);
        Pirate pirate = getExisting(id);
        Rank previousRank = pirate.getRank();
        pirate.setRank(rank);
        Pirate updatedPirate = pirateRepository.save(pirate);
        log.info("Pirate rank changed successfully: {} -> {} for pirate id: {}", previousRank, rank, id);
        return pirateMapper.toPirateResponseDto(updatedPirate);
    }

    public PirateResponseDto assignToShip(UUID pirateId, UUID shipId) {
        log.info("Assigning pirate {} to ship {}", pirateId, shipId);
        Pirate pirate = getExisting(pirateId);
        shipFeignClient.getShipById(shipId); // Ensure ship exists

        Set<UUID> shipIds = pirate.getShipIds();
        shipIds.add(shipId);
        pirate.setShipIds(shipIds);
        Pirate updatedPirate = pirateRepository.save(pirate);
        log.info("Pirate assigned successfully with id: {}", pirateId);
        return pirateMapper.toPirateResponseDto(updatedPirate);
    }
}