package com.hush0k.pirateTeam.ship.kafka;

import com.hush0k.pirateTeam.kafka.CapitanAssignedEvent;
import com.hush0k.pirateTeam.kafka.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ShipKafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendCaptainAssigned(CapitanAssignedEvent event) {
        log.info("Sending CaptainAssignedEvent: {}", event);
        kafkaTemplate.send(KafkaTopics.SHIP_CAPTAIN_ASSIGNED, event);
    }

}
