package com.hush0k.pirateTeam.fleet.service;

import com.hush0k.pirateTeam.exception.fleet.FleetNotFoundException;
import com.hush0k.pirateTeam.fleet.domain.Fleet;
import com.hush0k.pirateTeam.fleet.dto.request.FleetCreateDto;
import com.hush0k.pirateTeam.fleet.dto.request.FleetUpdateDto;
import com.hush0k.pirateTeam.fleet.dto.response.FleetResponseDto;
import com.hush0k.pirateTeam.fleet.mapper.FleetMapper;
import com.hush0k.pirateTeam.fleet.repository.FleetRepository;
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
public class FleetService {

    private final FleetRepository fleetRepository;
    private final FleetMapper fleetMapper;

    public FleetResponseDto create(FleetCreateDto dto) {
        log.info("Creating new fleet for owner: {}", dto.ownerId());
        Fleet fleet = fleetMapper.toFleet(dto);
        Fleet savedFleet = fleetRepository.save(fleet);
        log.info("Fleet created successfully with id: {}", savedFleet.getId());
        return fleetMapper.toFleetResponseDto(savedFleet);
    }

    public FleetResponseDto update(FleetUpdateDto dto, UUID id) {
        log.info("Updating fleet with id: {}", id);
        Fleet fleet = getExisting(id);
        fleetMapper.updateFleet(dto, fleet);
        Fleet updatedFleet = fleetRepository.save(fleet);
        log.info("Fleet updated successfully with id: {}", id);
        return fleetMapper.toFleetResponseDto(updatedFleet);
    }

    public void delete(UUID id) {
        log.info("Deleting fleet with id: {}", id);
        Fleet fleet = getExisting(id);
        fleetRepository.delete(fleet);
        log.info("Fleet deleted successfully with id: {}", id);
    }

    @Transactional(readOnly = true)
    public FleetResponseDto findById(UUID id) {
        log.debug("Fetching fleet by id: {}", id);
        Fleet fleet = getExisting(id);
        return fleetMapper.toFleetResponseDto(fleet);
    }

    @Transactional(readOnly = true)
    public List<FleetResponseDto> getAll() {
        log.debug("Fetching all fleets");
        List<Fleet> fleets = fleetRepository.findAll();
        log.debug("Found {} fleets", fleets.size());
        return fleetMapper.toFleetResponseDto(fleets);
    }

    @Transactional(readOnly = true)
    private Fleet getExisting(UUID id) {
        log.debug("Fetching fleet with id: {}", id);
        return fleetRepository.findById(id).orElseThrow(
                () -> {
                    log.warn("Fleet not found with id: {}", id);
                    return new FleetNotFoundException(id);
                }
        );
    }
}
