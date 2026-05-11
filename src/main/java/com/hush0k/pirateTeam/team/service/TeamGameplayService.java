package com.hush0k.pirateTeam.team.service;

import com.hush0k.pirateTeam.common.random.RandomService;
import com.hush0k.pirateTeam.exception.team.TeamNotFoundException;
import com.hush0k.pirateTeam.kafka.CapitanAssignedEvent;
import com.hush0k.pirateTeam.kafka.PirateStatChangedEvent;
import com.hush0k.pirateTeam.ship.domain.Ship;
import com.hush0k.pirateTeam.ship.enums.ShipOwnership;
import com.hush0k.pirateTeam.ship.kafka.ShipKafkaProducer;
import com.hush0k.pirateTeam.ship.mapper.ShipMapper;
import com.hush0k.pirateTeam.ship.repository.ShipRepository;
import com.hush0k.pirateTeam.team.client.PirateFeignClient;
import com.hush0k.pirateTeam.team.client.dto.CaptainClientDto;
import com.hush0k.pirateTeam.team.domain.Team;
import com.hush0k.pirateTeam.team.dto.response.ShipSearchResultResponse;
import com.hush0k.pirateTeam.team.kafka.TeamKafkaProducer;
import com.hush0k.pirateTeam.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TeamGameplayService {

    private static final int MAX_CHARACTERISTIC_VALUE = 100;

    private final TeamRepository teamRepository;
    private final ShipRepository shipRepository;
    private final ShipMapper shipMapper;
    private final PirateFeignClient pirateFeignClient;
    private final RandomService randomService;
    private final TeamKafkaProducer teamKafkaProducer;
    private final ShipKafkaProducer shipKafkaProducer;

    public ShipSearchResultResponse findAbandonedShip(UUID teamId) {
        Team team = getExisting(teamId);
        CaptainClientDto captain = pirateFeignClient.getPirateById(team.getCapitanId());
        List<Ship> abandonedShips = shipRepository.getByOwnership(ShipOwnership.ABANDONED);

        int chancePercent = calculateSearchChance(captain.intelligence(), team.getCohesion(), abandonedShips.size());
        int rollPercent = randomService.simpleRandom(1, 100);

        if (abandonedShips.isEmpty() || rollPercent > chancePercent) {
            int cohesionLoss = randomService.simpleRandom(3, 8);
            team.setCohesion(Math.max(0, team.getCohesion() - cohesionLoss));
            teamRepository.save(team);

            return new ShipSearchResultResponse(
                    teamId,
                    false,
                    chancePercent,
                    rollPercent,
                    abandonedShips.size(),
                    -cohesionLoss,
                    0,
                    0,
                    null
            );
        }

        Ship foundShip = pickAbandonedShip(abandonedShips);
        foundShip.setOwnership(ShipOwnership.OWNED);
        foundShip.setCapitanId(captain.id());
        foundShip.setOwnerId(captain.id());
        foundShip.setFleetId(team.getFleetId());

        int cohesionGain = randomService.simpleRandom(8, 15);
        int moraleGain = randomService.simpleRandom(6, 12);
        int reputationGain = randomService.simpleRandom(4, 9);

        team.setCohesion(capCharacteristic(team.getCohesion() + cohesionGain));
        team.setMorale(capCharacteristic(team.getMorale() + moraleGain));

        Ship savedShip = shipRepository.save(foundShip);
        teamRepository.save(team);

        team.getPirateIds().forEach(pirateId ->
                teamKafkaProducer.sendPirateStatChange(new PirateStatChangedEvent(pirateId, "REP_ADD", reputationGain))
        );
        shipKafkaProducer.sendCaptainAssigned(new CapitanAssignedEvent(savedShip.getId(), captain.id()));

        return new ShipSearchResultResponse(
                teamId,
                true,
                chancePercent,
                rollPercent,
                abandonedShips.size(),
                cohesionGain,
                moraleGain,
                reputationGain,
                shipMapper.toShipDto(savedShip)
        );
    }

    private int calculateSearchChance(int captainIntelligence, int cohesion, int abandonedShipsCount) {
        if (abandonedShipsCount == 0) {
            return 0;
        }

        int abandonedBoost = Math.min(abandonedShipsCount, 20) * 2;
        return randomService.clamp(5 + captainIntelligence / 3 + cohesion / 3 + abandonedBoost, 1, 90);
    }

    private Ship pickAbandonedShip(List<Ship> ships) {
        double totalWeight = ships.stream()
                .mapToDouble(this::shipSearchWeight)
                .sum();

        double roll = ThreadLocalRandom.current().nextDouble(totalWeight);
        double cursor = 0;

        List<Ship> sortedShips = ships.stream()
                .sorted(Comparator.comparing(ship -> ship.getShipType().getTier()))
                .toList();

        for (Ship ship : sortedShips) {
            cursor += shipSearchWeight(ship);
            if (roll < cursor) {
                return ship;
            }
        }

        return sortedShips.getLast();
    }

    private double shipSearchWeight(Ship ship) {
        int tier = ship.getShipType().getTier();
        if (tier >= 7) {
            return 0.4;
        }
        return Math.pow(8 - tier, 3);
    }

    private int capCharacteristic(int value) {
        return Math.min(MAX_CHARACTERISTIC_VALUE, value);
    }

    private Team getExisting(UUID id) {
        return teamRepository.findById(id).orElseThrow(
                () -> {
                    log.warn("Team not found with id: {}", id);
                    return new TeamNotFoundException(id);
                }
        );
    }
}
