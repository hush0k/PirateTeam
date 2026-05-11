package com.hush0k.pirateTeam.pirate.kafka;

import com.hush0k.pirateTeam.kafka.CapitanAssignedEvent;
import com.hush0k.pirateTeam.kafka.KafkaTopics;
import com.hush0k.pirateTeam.kafka.PirateStatChangedEvent;
import com.hush0k.pirateTeam.kafka.TeamMemberChangedEvent;
import com.hush0k.pirateTeam.pirate.dto.request.PirateExpChangeDto;
import com.hush0k.pirateTeam.pirate.dto.request.PirateReputationChange;
import com.hush0k.pirateTeam.pirate.service.PirateGamePlayService;
import com.hush0k.pirateTeam.pirate.service.PirateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PirateKafkaListener {

    private final PirateService pirateService;
    private final PirateGamePlayService pirateGamePlayService;

    @KafkaListener(topics = KafkaTopics.SHIP_CAPTAIN_ASSIGNED, groupId = "pirate-team-group")
    public void handleCaptainAssigned(CapitanAssignedEvent event) {
        log.info("Received CaptainAssignedEvent: {}", event);
        pirateService.assignShip(event.pirateId(), event.shipId());
    }

    @KafkaListener(topics = KafkaTopics.FLEET_PIRATE_STAT_CHANGE, groupId = "pirate-team-group")
    public void handlePirateStatChange(PirateStatChangedEvent event) {
        log.info("Received PirateStatChangeEvent: {}", event);
        switch (event.statType()) {
            case "EXP_ADD"    -> pirateGamePlayService.addExp(event.pirateId(), new PirateExpChangeDto(event.amount()));
            case "EXP_REMOVE" -> pirateGamePlayService.removeExp(event.pirateId(), new PirateExpChangeDto(event.amount()));
            case "REP_ADD"    -> pirateService.addReputation(event.pirateId(), new PirateReputationChange(event.amount()));
            case "REP_REMOVE" -> pirateService.removeReputation(event.pirateId(), new PirateReputationChange(event.amount()));
            default -> log.warn("Unknown statType: {}", event.statType());
        }
    }

    @KafkaListener(topics = KafkaTopics.TEAM_MEMBERS_CHANGE, groupId = "pirate-team-group")
    public void handleTeamMembersChange(TeamMemberChangedEvent event) {
        log.info("Received TeamMembersChangeEvent: {}", event);
        switch (event.action()) {
            case "ASSIGN" -> pirateService.assignManyToTeam(event.pirateIds(), event.teamId());
            case "REMOVE" -> pirateService.removeManyFromTeam(event.pirateIds(), event.teamId());
            default -> log.warn("Unknown action: {}", event.action());
        }
    }
}
