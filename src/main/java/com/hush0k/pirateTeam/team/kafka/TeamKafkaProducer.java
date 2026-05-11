package com.hush0k.pirateTeam.team.kafka;

import com.hush0k.pirateTeam.kafka.KafkaTopics;
import com.hush0k.pirateTeam.kafka.PirateStatChangedEvent;
import com.hush0k.pirateTeam.kafka.TeamMemberChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TeamKafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendTeamMembersChange(TeamMemberChangedEvent event) {
        log.info("Sending TeamMembersChangeEvent: {}", event);
        kafkaTemplate.send(KafkaTopics.TEAM_MEMBERS_CHANGE, event);
    }

    public void sendPirateStatChange(PirateStatChangedEvent event) {
        log.info("Sending PirateStatChangeEvent: {}", event);
        kafkaTemplate.send(KafkaTopics.FLEET_PIRATE_STAT_CHANGE, event);
    }
}
