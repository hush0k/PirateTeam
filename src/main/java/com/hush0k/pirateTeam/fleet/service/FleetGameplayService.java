package com.hush0k.pirateTeam.fleet.service;

import com.hush0k.pirateTeam.common.random.RandomService;
import com.hush0k.pirateTeam.exception.fleet.InsufficientAmmoException;
import com.hush0k.pirateTeam.exception.fleet.InsufficientDistanceToBattle;
import com.hush0k.pirateTeam.exception.fleet.InsufficientProvisionException;
import com.hush0k.pirateTeam.fleet.client.dto.IslandClientDto;
import com.hush0k.pirateTeam.fleet.client.IslandClientService;
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
    private final PirateClientService pirateClientService;
    private final IslandClientService islandClientService;

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

        TeamDto myTeam = teamClientService.getByFleetId(fleetId);
        TeamDto enemyTeam = teamClientService.getByFleetId(enemyFleetId);

        FleetStatsDto myFleetStats = fleetService.getStats(fleetId);
        FleetStatsDto enemyFleetStats = fleetService.getStats(enemyFleetId);

        PirateClientDto myCaptain = pirateClientService.getPirate(myFleet.getOwnerId());
        PirateClientDto enemyCaptain = pirateClientService.getPirate(enemyFleet.getOwnerId());

        int mySpentAmmo = randomService.simpleRandom(35, 65);
        int enemySpentAmmo =  randomService.simpleRandom(35, 65);

        double myScore =
                (myFleetStats.militaryPower()   / 200.0) * 0.35 +
                        (myFleetStats.boardingPower()   / 200.0) * 0.25 +
                        (myFleetStats.combatStability() / 100.0) * 0.25 +
                        (myFleetStats.manoeuvrability() / 100.0) * 0.15;


        double enemyScore =
                (enemyFleetStats.militaryPower()   / 200.0) * 0.35 +
                        (enemyFleetStats.boardingPower()   / 200.0) * 0.25 +
                        (enemyFleetStats.combatStability() / 100.0) * 0.25 +
                        (enemyFleetStats.manoeuvrability() / 100.0) * 0.15;
        boolean enemyOutOfAmmo = enemySpentAmmo > enemyFleet.getAmmo();
        if (enemyOutOfAmmo){
            enemyScore = (int)(enemyScore * 0.9);
        }

        myScore =
                myScore * (1 + (myCaptain.intelligence() * 0.6
                        + myCaptain.strength() * 0.2
                        + myCaptain.bloodlust() * 0.2) / 100.0);
        enemyScore =
                enemyScore * (1 + (enemyCaptain.intelligence() * 0.6
                        + enemyCaptain.strength() * 0.2
                        + enemyCaptain.bloodlust() * 0.2) / 100.0);


        int enemyFleetRating = (int) Math.clamp(Math.round(enemyScore * 49) + 1, 1, 50);
        int myFleetRating = (int) Math.clamp(Math.round(myScore * 49) + 1, 1, 50);

        int center = 50 + myFleetRating - enemyFleetRating;
        int result = randomService.weightedAround(0, 100, center, 2.5D);

        int myTreasury = (int) ((int) myTeam.treasury() * 0.3 * enemyFleetStats.lootMultiplier());
        int enemyTreasury = (int) ((int) enemyTeam.treasury() * 0.3 * myFleetStats.lootMultiplier());

        myFleet.setAmmo(myFleet.getAmmo() - mySpentAmmo);
        if (enemyOutOfAmmo){
            enemyFleet.setAmmo(0);
        } else {
            enemyFleet.setAmmo(enemyFleet.getAmmo() - enemySpentAmmo);
        }

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
            addExpToReputablePirates(myTeam.pirateIds(), 7, 17);

            enemyTeam.pirateIds().forEach(pirateId -> {
                int amount = randomService.simpleRandom(3,10);
                pirateClientService.removeReputationToPirate(pirateId, new TeamTreasuryCharacteristicClient(amount));
            });

            enemyTeam.pirateIds().forEach(pirateId -> {
                int amount = randomService.simpleRandom(3,6);
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
                int amount = randomService.simpleRandom(3,6);
                pirateClientService.removeReputationToPirate(pirateId, new TeamTreasuryCharacteristicClient(amount));
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
        TeamDto team = teamClientService.getByFleetId(id);
        int result;
        int center = 0;

        if (fleet.isHasTreasuryMap()){
            center = (int) (randomService.simpleRandom(30, 70) * 1.6);
            center = randomService.clamp(center, 5, 97);
        } else {
            center = (int) (randomService.simpleRandom(30, 70));
            center = randomService.clamp(center, 5, 97);
        }

        result = randomService.weightedAround(0, 100, center, 2.0D);

        int treasury = calculateTreasury(result);

        teamClientService.addTreasuryToTeam(team.id(), new TeamTreasuryCharacteristicClient(result));

        team.pirateIds().forEach(pirateId -> {
            int amount = randomService.simpleRandom(3,6);
            pirateClientService.addReputationToPirate(pirateId, new TeamTreasuryCharacteristicClient(amount));
        });

        if (treasury > 0) {
            addExpToReputablePirates(team.pirateIds(), 5, 14);
        }

        return new FleetFindTreasure(team.id(), treasury);

    }

    public FleetCaptureIsland captureIsland(UUID fleetId, UUID islandId){
        Fleet fleet = fleetService.getExisting(fleetId);
        IslandClientDto island = islandClientService.getIsland(islandId);
        TeamDto team = teamClientService.getByFleetId(fleetId);
        FleetStatsDto stats = fleetService.getStats(fleetId);
        PirateClientDto pirate = pirateClientService.getPirate(fleet.getOwnerId());

        if (fleet.getAmmo() < island.level().getMaxAmmoToCapture()) {
            throw new InsufficientAmmoException(fleetId);
        }

        int distance = calculateDistance(fleet.getCoordinateX(), fleet.getCoordinateY(), island.coordinateX(), island.coordinateY());
        if (distance > 30) {
            throw new InsufficientDistanceToBattle(distance);
        }

        int ammo = randomService.simpleRandom((int)(island.level().getMaxAmmoToCapture() * 0.3), island.level().getMaxAmmoToCapture());

        double attackScore =
                (stats.militaryPower()   / 200.0) * 0.5 +
                        (stats.combatStability() / 100.0) * 0.45 +
                        (stats.manoeuvrability() / 100.0) * 0.05;


        attackScore =
                attackScore * (1 + (pirate.intelligence() * 0.6
                        + pirate.strength() * 0.2
                        + pirate.bloodlust() * 0.2) / 100.0);

        double defenceScore =
                (island.defenseType().getDefencePower() / 300.0) * 0.6 +
                        (island.defenseType().getCohesion() / 250.0) * 0.4;

        defenceScore =
                (defenceScore * (1 + 0.05 * island.legendaryPirateIds().size()) * (1 * island.level().getReputationMultiplier()) );

        int attackRating = (int) Math.clamp(Math.round(attackScore * 49) + 1, 1, 50);
        int defenceRating = (int) Math.clamp(Math.round(defenceScore * 49) + 1, 1, 50);

        int center = 50 + attackRating - defenceRating;

        int result = randomService.weightedAround(0, 100, center, 2.0D);

        fleet.setAmmo(fleet.getAmmo() - ammo);

        if (result >= 50) {
            islandClientService.assignOwner(islandId, fleetId);
            pirateClientService.addReputationToPirate(fleet.getOwnerId(),
                    new TeamTreasuryCharacteristicClient(15 * island.level().getMaxAmmoToCapture()));
            teamClientService.addMoraleToTeam(team.id(),
                    new TeamTreasuryCharacteristicClient(randomService.simpleRandom(5, 13)));
            teamClientService.addLoyaltyToTeam(team.id(),
                    new TeamTreasuryCharacteristicClient(randomService.simpleRandom(8, 15)));

            team.pirateIds().forEach(pirateId -> {
                pirateClientService.addReputationToPirate(pirateId,
                        new TeamTreasuryCharacteristicClient(randomService.simpleRandom(9, 13)));
            });
            addExpToReputablePirates(team.pirateIds(), 20, 28);
        } else {
            pirateClientService.removeReputationToPirate(fleet.getOwnerId(),
                    new TeamTreasuryCharacteristicClient(15 * island.level().getMaxAmmoToCapture()));
            teamClientService.withdrawMoraleToTeam(team.id(),
                    new TeamTreasuryCharacteristicClient(randomService.simpleRandom(5, 13)));
            teamClientService.withdrawLoyaltyToTeam(team.id(),
                    new TeamTreasuryCharacteristicClient(randomService.simpleRandom(8, 15)));

            team.pirateIds().forEach(pirateId -> {
                pirateClientService.removeReputationToPirate(pirateId,
                        new TeamTreasuryCharacteristicClient(randomService.simpleRandom(5, 8)));
            });
        }

        teamClientService.addFatigueToTeam(team.id(),
                new TeamTreasuryCharacteristicClient(randomService.simpleRandom(9, 19)));

        return new FleetCaptureIsland(fleetId, islandId, ammo);
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

    private void addExpToReputablePirates(Set<UUID> pirateIds, int minAmount, int maxAmount) {
        pirateIds.forEach(pirateId -> {
            PirateClientDto pirate = pirateClientService.getPirate(pirateId);
            if (pirate.reputation() > 0) {
                int amount = randomService.simpleRandom(minAmount, maxAmount);
                pirateClientService.addExpToPirate(pirateId, new TeamTreasuryCharacteristicClient(amount));
            }
        });
    }
}
