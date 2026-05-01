package com.hush0k.pirateTeam.fleet.service;

import com.hush0k.pirateTeam.exception.fleet.FleetNotFoundException;
import com.hush0k.pirateTeam.fleet.client.ShipClientService;
import com.hush0k.pirateTeam.fleet.client.TeamClientService;
import com.hush0k.pirateTeam.fleet.client.dto.PirateClientDto;
import com.hush0k.pirateTeam.fleet.client.dto.TeamDto;
import com.hush0k.pirateTeam.fleet.domain.Fleet;
import com.hush0k.pirateTeam.fleet.dto.request.FleetCreateDto;
import com.hush0k.pirateTeam.fleet.dto.request.FleetResourceChangeDto;
import com.hush0k.pirateTeam.fleet.dto.request.FleetUpdateDto;
import com.hush0k.pirateTeam.fleet.dto.response.FleetResponseDto;
import com.hush0k.pirateTeam.fleet.dto.response.FleetStatsDto;
import com.hush0k.pirateTeam.fleet.mapper.FleetMapper;
import com.hush0k.pirateTeam.fleet.repository.FleetRepository;
import feign.FeignException;
import com.hush0k.pirateTeam.ship.dto.response.FleetShipStatsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FleetService {

    private final FleetRepository fleetRepository;
    private final FleetMapper fleetMapper;
    private final ShipClientService shipClientService;
    private final TeamClientService teamClientService;

    public FleetResponseDto create(FleetCreateDto dto) {
        Optional<FleetResponseDto> fleetIsExists = getByOwnerId(dto.ownerId());
        if (fleetIsExists.isPresent()) {
            return fleetIsExists.get();
        }

        log.info("Creating new fleet for owner: {}", dto.ownerId());
        Fleet fleet = fleetMapper.toFleet(dto);
        Fleet savedFleet = fleetRepository.save(fleet);

        PirateClientDto pirate = teamClientService.getPirateById(dto.ownerId());
        if (pirate.teamId() != null) {
            teamClientService.assignFleetToTeam(pirate.teamId(), savedFleet.getId());
        }

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
    public Optional<FleetResponseDto> getByOwnerId(UUID id) {
        log.debug("Fetching fleet by owner id: {}", id);
        Fleet fleet = fleetRepository.findByOwnerId(id).orElse(null);
        return Optional.ofNullable(fleetMapper.toFleetResponseDto(fleet));
    }


    @Transactional(readOnly = true)
    Fleet getExisting(UUID id) {
        log.debug("Fetching fleet with id: {}", id);
        return fleetRepository.findById(id).orElseThrow(
                () -> {
                    log.warn("Fleet not found with id: {}", id);
                    return new FleetNotFoundException(id);
                }
        );
    }


    public FleetStatsDto getStats(UUID id) {
        log.debug("Fetching stats for fleet with id: {}", id);

        getExisting(id);

        FleetShipStatsDto fleetShipStats = shipClientService.getStats(id);
        TeamDto fleetTeamStats;
        try {
            fleetTeamStats = teamClientService.getByFleetId(id);
        } catch (FeignException.NotFound e) {
            log.warn("Team not found for fleet id: {}. Calculating fleet stats with zero team modifiers", id);
            fleetTeamStats = new TeamDto(null, "No team", null, null, 0, 0, 0, 0, 0, 0, 0, 0);
        }

        int militaryPower = fleetShipStats.totalPower() + fleetTeamStats.power();
        int boardingPower = fleetTeamStats.power() + fleetTeamStats.cohesion();
        int manoeuvrability = (int) Math.round(fleetShipStats.avgSpeed());
        int combatStability = Math.clamp(
                (fleetTeamStats.morale() + fleetTeamStats.cohesion() + fleetTeamStats.loyalty() - fleetTeamStats.fatigue()) / 3,
                0,
                100
        );
        float lootMultiplier = 1.0f + (fleetTeamStats.lootBonus() / 100.0f);

        return new FleetStatsDto(militaryPower, boardingPower, manoeuvrability, combatStability, lootMultiplier);
    }

    public FleetResponseDto addAmmo(UUID id, FleetResourceChangeDto dto) {
        Fleet fleet = getExisting(id);
        fleet.setAmmo(fleet.getAmmo() + dto.amount());
        Fleet updatedFleet = fleetRepository.save(fleet);
        return fleetMapper.toFleetResponseDto(updatedFleet);
    }

    public FleetResponseDto withdrawAmmo(UUID id, FleetResourceChangeDto dto) {
        Fleet fleet = getExisting(id);
        if (fleet.getAmmo() < dto.amount()) {
            throw new IllegalArgumentException("Not enough ammo in fleet");
        }
        fleet.setAmmo(fleet.getAmmo() - dto.amount());
        Fleet updatedFleet = fleetRepository.save(fleet);
        return fleetMapper.toFleetResponseDto(updatedFleet);
    }

    public FleetResponseDto addProvision(UUID id, FleetResourceChangeDto dto) {
        Fleet fleet = getExisting(id);
        fleet.setProvision(fleet.getProvision() + dto.amount());
        Fleet updatedFleet = fleetRepository.save(fleet);
        return fleetMapper.toFleetResponseDto(updatedFleet);
    }

    public FleetResponseDto withdrawProvision(UUID id, FleetResourceChangeDto dto) {
        Fleet fleet = getExisting(id);
        if (fleet.getProvision() < dto.amount()) {
            throw new IllegalArgumentException("Not enough provision in fleet");
        }
        fleet.setProvision(fleet.getProvision() - dto.amount());
        Fleet updatedFleet = fleetRepository.save(fleet);
        return fleetMapper.toFleetResponseDto(updatedFleet);
    }


}
