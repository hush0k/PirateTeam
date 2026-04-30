package com.hush0k.pirateTeam.fleet.service;

import com.hush0k.pirateTeam.common.random.RandomService;
import com.hush0k.pirateTeam.exception.fleet.InsufficientAmmoException;
import com.hush0k.pirateTeam.exception.fleet.InsufficientDistanceToBattle;
import com.hush0k.pirateTeam.exception.fleet.InsufficientProvisionException;
import com.hush0k.pirateTeam.fleet.client.TeamClientService;
import com.hush0k.pirateTeam.fleet.client.dto.TeamDto;
import com.hush0k.pirateTeam.fleet.client.dto.TeamTreasuryCharacteristicClient;
import com.hush0k.pirateTeam.fleet.domain.Fleet;
import com.hush0k.pirateTeam.fleet.dto.request.FleetMoveDto;
import com.hush0k.pirateTeam.fleet.dto.response.FleetAttackResult;
import com.hush0k.pirateTeam.fleet.dto.response.FleetNewCoordinate;
import com.hush0k.pirateTeam.fleet.dto.response.FleetStatsDto;
import com.hush0k.pirateTeam.fleet.repository.FleetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FleetGameplayService {

    private final FleetRepository fleetRepository;
    private final RandomService  randomService;
    private final FleetService fleetService;
    private final TeamClientService teamClientService;

    public int calculateDistance(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public FleetNewCoordinate moveToTarget(UUID fleetId, FleetMoveDto dto) {
        Fleet fleet = fleetService.getExisting(fleetId);
        int distance = calculateDistance(fleet.getCoordinateX(), fleet.getCoordinateY(), dto.coordinateX(), dto.coordinateY());

        TeamDto team = teamClientService.getByFleetId(fleetId);
        int teamSize = team.pirateIds().map(Set::size).orElse(0);

        int spentProvision = (int)(teamSize * 0.5 * distance);

        if (spentProvision > fleet.getProvision()) {
            throw new InsufficientProvisionException(fleetId);
        }

        fleet.setProvision(fleet.getProvision() - spentProvision);
        fleet.setCoordinateX(dto.coordinateX());
        fleet.setCoordinateY(dto.coordinateY());
        Fleet fleetUpdated = fleetRepository.save(fleet);
        return new FleetNewCoordinate(fleetUpdated.getCoordinateX(), fleetUpdated.getCoordinateY(), fleetUpdated.getProvision());
    }

    public FleetAttackResult attackToEnemyFleet(UUID fleetId, UUID enemyFleetId) {
        Fleet myFleet = fleetService.getExisting(fleetId);
        Fleet enemyFleet = fleetService.getExisting(enemyFleetId);

        if(myFleet.getAmmo() < 65) {
            throw new InsufficientAmmoException(fleetId);
        }

        int mySpentAmmo = randomService.simpleRandom(35, 65);
        int enemySpentAmmo =  randomService.simpleRandom(35, 65);

        myFleet.setAmmo();

        int distance = calculateDistance(myFleet.getCoordinateX(), myFleet.getCoordinateY(), enemyFleet.getCoordinateX(), enemyFleet.getCoordinateY());
        if (distance > 30) {
            throw new InsufficientDistanceToBattle(distance);
        }

        TeamDto myTeam = teamClientService.getByFleetId(fleetId);
        TeamDto enemyTeam = teamClientService.getByFleetId(enemyFleetId);

        FleetStatsDto myFleetStats = fleetService.getStats(fleetId);
        FleetStatsDto enemyFleetStats = fleetService.getStats(enemyFleetId);

        double myScore =
                (myFleetStats.militaryPower()   / 200.0) * 0.35 +
                        (myFleetStats.boardingPower()   / 200.0) * 0.25 +
                        (myFleetStats.combatStability() / 100.0) * 0.25 +
                        (myFleetStats.manoeuvrability() / 100.0) * 0.15;

        int myFleetRating = (int) Math.clamp(Math.round(myScore * 49) + 1, 1, 50);

        double enemyScore =
                (enemyFleetStats.militaryPower()   / 200.0) * 0.35 +
                        (enemyFleetStats.boardingPower()   / 200.0) * 0.25 +
                        (enemyFleetStats.combatStability() / 100.0) * 0.25 +
                        (enemyFleetStats.manoeuvrability() / 100.0) * 0.15;

        int enemyFleetRating = (int) Math.clamp(Math.round(enemyScore * 49) + 1, 1, 50);

        if (enemySpentAmmo > enemyFleet.getAmmo()){
            myFleetRating = (int)(myFleetRating * 0.9);
        }

        myFleetRating *= (int)(1 - 0.005 * myTeam.fatigue());
        enemyFleetRating *= (int)(1 - 0.005 * enemyTeam.fatigue());

        int center = 50 + (int) myFleetRating - (int) enemyFleetRating;
        int result = randomService.weightedAround(0, 100, center, 2.5D);

        int myTreasury = (int) ((int) myTeam.treasury() * 0.3 * enemyFleetStats.lootMultiplier());
        int enemyTreasury = (int) ((int) enemyTeam.treasury() * 0.3 * myFleetStats.lootMultiplier());

        String winnerName;
        if (result > 50) {
            winnerName = myFleet.getName();
            teamClientService.addTreasuryToTeam(myTeam.id(),
                    new TeamTreasuryCharacteristicClient(enemyTreasury));
            teamClientService.withdrawTreasuryToTeam(enemyTeam.id(),
                    new TeamTreasuryCharacteristicClient(enemyTreasury));

            teamClientService.addLoyaltyToTeam(myTeam.id(),
                    new TeamTreasuryCharacteristicClient(8));
            teamClientService.withdrawLoyaltyToTeam(enemyTeam.id(),
                    new TeamTreasuryCharacteristicClient(5));

            teamClientService.addMoraleToTeam(myTeam.id(),
                    new TeamTreasuryCharacteristicClient(6));
            teamClientService.withdrawMoraleToTeam(enemyTeam.id(),
                    new TeamTreasuryCharacteristicClient(4));


        } else if (result < 50) {
            winnerName = myFleet.getName();
            teamClientService.addTreasuryToTeam(enemyTeam.id(),
                    new TeamTreasuryCharacteristicClient(myTreasury));
            teamClientService.withdrawTreasuryToTeam(myTeam.id(),
                    new TeamTreasuryCharacteristicClient(myTreasury));

            teamClientService.addLoyaltyToTeam(enemyTeam.id(),
                    new TeamTreasuryCharacteristicClient(8));
            teamClientService.withdrawLoyaltyToTeam(myTeam.id(),
                    new TeamTreasuryCharacteristicClient(5));

            teamClientService.addMoraleToTeam(enemyTeam.id(),
                    new TeamTreasuryCharacteristicClient(6));
            teamClientService.withdrawMoraleToTeam(myTeam.id(),
                    new TeamTreasuryCharacteristicClient(4));


        } else {
            winnerName = "Draw";
        }

        int myFatigue = randomService.simpleRandom(15, 25);
        int enemyFatigue =  randomService.simpleRandom(15, 25);

        teamClientService.addFatigueToTeam(myTeam.id(), new TeamTreasuryCharacteristicClient(myFatigue));
        teamClientService.addFatigueToTeam(enemyTeam.id(), new TeamTreasuryCharacteristicClient(enemyFatigue));


        return new FleetAttackResult(fleetId, enemyFleetId, winnerName, result, myFatigue, mySpentAmmo, enemyTreasury);

    }
}
