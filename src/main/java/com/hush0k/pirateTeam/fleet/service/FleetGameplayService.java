package com.hush0k.pirateTeam.fleet.service;

import com.hush0k.pirateTeam.common.random.RandomService;
import com.hush0k.pirateTeam.exception.fleet.InsufficientAmmoException;
import com.hush0k.pirateTeam.exception.fleet.InsufficientDistanceToBattle;
import com.hush0k.pirateTeam.exception.fleet.InsufficientProvisionException;
import com.hush0k.pirateTeam.fleet.client.PirateClientService;
import com.hush0k.pirateTeam.fleet.client.TeamClientService;
import com.hush0k.pirateTeam.fleet.client.dto.PirateClientDto;
import com.hush0k.pirateTeam.fleet.client.dto.TeamDto;
import com.hush0k.pirateTeam.fleet.client.dto.TeamTreasuryCharacteristicClient;
import com.hush0k.pirateTeam.fleet.domain.Fleet;
import com.hush0k.pirateTeam.fleet.dto.request.FleetMoveDto;
import com.hush0k.pirateTeam.fleet.dto.response.*;
import com.hush0k.pirateTeam.fleet.repository.FleetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final PirateClientService pirateClientService;

    public int calculateDistance(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public FleetNewCoordinate moveToTarget(UUID fleetId, FleetMoveDto dto) {
        Fleet fleet = fleetService.getExisting(fleetId);
        int distance = calculateDistance(fleet.getCoordinateX(), fleet.getCoordinateY(), dto.coordinateX(), dto.coordinateY());

        TeamDto team = teamClientService.getByFleetId(fleetId);
        int teamSize = team.pirateIds().size();

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

        int distance = calculateDistance(myFleet.getCoordinateX(), myFleet.getCoordinateY(), enemyFleet.getCoordinateX(), enemyFleet.getCoordinateY());
        if (distance > 30) {
            throw new InsufficientDistanceToBattle(distance);
        }

        if(myFleet.getAmmo() < 65) {
            throw new InsufficientAmmoException(fleetId);
        }

        int mySpentAmmo = randomService.simpleRandom(35, 65);
        int enemySpentAmmo =  randomService.simpleRandom(35, 65);

        myFleet.setAmmo(myFleet.getAmmo() - mySpentAmmo);

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
            myFleetRating = (int)(enemyFleetRating * 0.9);
            enemyFleet.setAmmo(0);
        } else {
            enemyFleet.setAmmo(enemyFleet.getAmmo() - enemySpentAmmo);
        }

        PirateClientDto myCaptain = pirateClientService.getPirate(myFleet.getOwnerId());
        PirateClientDto enemyCaptain = pirateClientService.getPirate(enemyFleet.getOwnerId());

        myFleetRating =
                (int)(myFleetRating * (1 - 0.005 * myTeam.fatigue()) * (1 + 0.003) * (myCaptain.intelligence()*0.6 + myCaptain.strength()*0.2 + myCaptain.bloodlust()*0.2));
        enemyFleetRating =
                (int)(enemyFleetRating * (1 - 0.005 * enemyTeam.fatigue()) * (1 + 0.003) * (enemyCaptain.intelligence()*0.6 + enemyCaptain.strength()*0.2 + enemyCaptain.bloodlust()*0.2));


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

            myTeam.pirateIds().forEach(pirateId -> {
                int amount = randomService.simpleRandom(3,10);
                pirateClientService.addReputationToPirate(pirateId, new TeamTreasuryCharacteristicClient(amount));
            });

            enemyTeam.pirateIds().forEach(pirateId -> {
                int amount = randomService.simpleRandom(3,10);
                pirateClientService.removeReputationToPirate(pirateId, new TeamTreasuryCharacteristicClient(amount));
            });



        } else if (result < 50) {
            winnerName = enemyFleet.getName();
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

            enemyTeam.pirateIds().forEach(pirateId -> {
                int amount = randomService.simpleRandom(3,10);
                pirateClientService.addReputationToPirate(pirateId, new TeamTreasuryCharacteristicClient(amount));
            });

            myTeam.pirateIds().forEach(pirateId -> {
                int amount = randomService.simpleRandom(3,10);
                pirateClientService.removeReputationToPirate(pirateId, new TeamTreasuryCharacteristicClient(amount));
            });

        } else {
            winnerName = "Draw";
        }

        int myFatigue = randomService.simpleRandom(15, 25);
        int enemyFatigue =  randomService.simpleRandom(15, 25);

        teamClientService.addFatigueToTeam(myTeam.id(), new TeamTreasuryCharacteristicClient(myFatigue));
        teamClientService.addFatigueToTeam(enemyTeam.id(), new TeamTreasuryCharacteristicClient(enemyFatigue));

        String lootedTreasury = result > 50 ? "+" + enemyTreasury : (result < 50 ? "-" + myTreasury : "0");

        return new FleetAttackResult(fleetId, enemyFleetId, winnerName, result, myFatigue, mySpentAmmo, lootedTreasury);

    }

    public FleetFindTreasure findTreasury(UUID id){
        Fleet fleet = fleetService.getExisting(id);
        int result;
        int center = 0;

        if (fleet.isHasTreasuryMap()){
            center = (int) (randomService.simpleRandom(30, 70) * 1.6);
            center = randomService.clamp(center, 5, 97);
        }

        result = randomService.weightedAround(0, 100, center, 2.0D);

        int treasury = calculateTreasury(result);
        TeamDto team = teamClientService.getByFleetId(id);

        teamClientService.addTreasuryToTeam(team.id(), new TeamTreasuryCharacteristicClient(result));

        team.pirateIds().forEach(pirateId -> {
            int amount = randomService.simpleRandom(3,6);
            pirateClientService.addReputationToPirate(pirateId, new TeamTreasuryCharacteristicClient(amount));
        });

        return new FleetFindTreasure(team.id(), treasury);

    }

    public FleetCaptureIsland captureIsland(UUID fleetId, UUID islandId){

    }

    private int calculateTreasury(int result) {
        return result == 0   ? 0
                : result < 5    ? 300
                  : result < 10   ? 1000
                    : result < 20   ? 5000
                      : result < 50   ? 10000
                        : result < 70   ? 20000
                          : result < 80   ? 30000
                            : result < 90   ? 40000
                              : result < 95   ? 50000
                                : result < 99   ? 60000
                                  : 100000;
    }
}
