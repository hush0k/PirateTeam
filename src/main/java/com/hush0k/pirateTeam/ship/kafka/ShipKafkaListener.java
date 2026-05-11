package com.hush0k.pirateTeam.ship.kafka;

import com.hush0k.pirateTeam.kafka.CargoLoadedEvent;
import com.hush0k.pirateTeam.kafka.KafkaTopics;
import com.hush0k.pirateTeam.ship.service.ShipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ShipKafkaListener {

    private final ShipService shipService;

    @KafkaListener(topics = KafkaTopics.MARKET_CARGO_LOADED, groupId = "pirate-team-group")
    public void handleCargoLoaded(CargoLoadedEvent event) {
        log.info("Received CargoLoadedEvent: {}", event);
        shipService.loadCargo(event.fleetId(), event.amount());
    }
}
