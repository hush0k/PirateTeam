package com.hush0k.pirateTeam.ship.service;

import com.hush0k.pirateTeam.exception.pirate.PirateNotCaptainException;
import com.hush0k.pirateTeam.pirate.enums.Rank;
import com.hush0k.pirateTeam.ship.client.PirateFeignClient;
import com.hush0k.pirateTeam.ship.client.dto.PirateClientDto;
import com.hush0k.pirateTeam.ship.domain.Ship;
import com.hush0k.pirateTeam.ship.dto.request.ShipCreateDto;
import com.hush0k.pirateTeam.ship.dto.request.ShipUpdateDto;
import com.hush0k.pirateTeam.ship.dto.response.FleetShipStatsDto;
import com.hush0k.pirateTeam.ship.dto.response.ShipResponseDto;
import com.hush0k.pirateTeam.ship.mapper.ShipMapper;
import com.hush0k.pirateTeam.ship.repository.ShipRepository;
import com.hush0k.pirateTeam.exception.ship.ShipNotFoundException;
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
public class ShipService {

    private final ShipRepository shipRepository;
    private final ShipMapper shipMapper;
    private final PirateFeignClient pirateFeignClient;

    @Transactional(readOnly = true)
    private Ship getExisting(UUID id) {
        log.debug("Fetching ship with id: {}", id);
        return shipRepository.findById(id).orElseThrow(
                () -> {
                    log.warn("Ship not found with id: {}", id);
                    return new ShipNotFoundException(id);
                }
        );
    }

    public ShipResponseDto create(ShipCreateDto dto) {
        log.info("Creating new ship with name: {}", dto.name());
        Ship ship = shipMapper.toShip(dto);
        Ship savedShip = shipRepository.save(ship);
        log.info("Ship created successfully with id: {}", savedShip.getId());
        return shipMapper.toShipDto(savedShip);
    }

    public ShipResponseDto assignCaptain(UUID shipId, UUID captainId) {
        log.info("Assigning captain {} to ship {}", captainId, shipId);
        PirateClientDto pirate = pirateFeignClient.getPirateById(captainId);

        if (!pirate.rank().isHigherThan(Rank.NAVIGATOR)) {
            throw new PirateNotCaptainException(captainId);
        }

        Ship ship = getExisting(shipId);
        ship.setCapitanId(captainId);
        Ship savedShip = shipRepository.save(ship);

        return shipMapper.toShipDto(savedShip);
    }

    public ShipResponseDto update(ShipUpdateDto dto, UUID id) {
        log.info("Updating ship with id: {}", id);
        Ship ship = getExisting(id);
        shipMapper.updateShipDto(dto, ship);
        Ship updatedShip = shipRepository.save(ship);
        log.info("Ship updated successfully with id: {}", id);
        return shipMapper.toShipDto(updatedShip);
    }

    public void delete(UUID id) {
        log.info("Deleting ship with id: {}", id);
        Ship ship = getExisting(id);
        shipRepository.delete(ship);
        log.info("Ship deleted successfully with id: {}", id);
    }

    @Transactional(readOnly = true)
    public ShipResponseDto getById(UUID id) {
        log.debug("Fetching ship by id: {}", id);
        Ship ship = getExisting(id);
        return shipMapper.toShipDto(ship);
    }

    @Transactional(readOnly = true)
    public List<ShipResponseDto> getAll() {
        log.debug("Fetching all ships");
        List<Ship> ships = shipRepository.findAll();
        log.debug("Found {} ships", ships.size());
        return shipMapper.toShipDtoList(ships);
    }

    @Transactional(readOnly = true)
    public FleetShipStatsDto getByFleetId(UUID fleetId) {
        List<Ship> ships = shipRepository.getByFleetId(fleetId);
        log.debug("Found {} ships", ships.size());

        int totalPower = ships.stream()
                .mapToInt(ship -> ship.getShipType().getPower())
                .sum();

        double avgSpeed = ships.stream()
                .mapToInt(Ship::getMaxSpeed)
                .average()
                .orElse(0.0);

        int maxCrew = ships.stream()
                .mapToInt(Ship::getMaxCrew)
                .sum();

        int maxCargo = ships.stream()
                .mapToInt(Ship::getCargoCapacity)
                .sum();

        int filledCargoSpace = ships.stream()
                .mapToInt(Ship::getFilledCargoSpace)
                .sum();

        return new FleetShipStatsDto(totalPower, avgSpeed, maxCrew, maxCargo, filledCargoSpace);
    }

    public void loadCargo(UUID fleetId, int amount) {
        List<Ship> ships = shipRepository.getByFleetId(fleetId);
        int remaining = amount;

        for (Ship ship : ships) {
            if (remaining <= 0) break;
            int free = ship.getCargoCapacity() - ship.getFilledCargoSpace();
            int load = Math.min(free, remaining);
            ship.setFilledCargoSpace(ship.getFilledCargoSpace() + load);
            remaining -= load;
        }

        shipRepository.saveAll(ships);
    }
}
