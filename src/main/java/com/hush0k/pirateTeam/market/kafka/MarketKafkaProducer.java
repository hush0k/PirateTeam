package com.hush0k.pirateTeam.market.kafka;

import com.hush0k.pirateTeam.kafka.CargoLoadedEvent;
import com.hush0k.pirateTeam.kafka.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j

public class MarketKafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendCargoLoaded(CargoLoadedEvent event) {
        log.info("Sending CargoLoadedEvent: {}", event);
        kafkaTemplate.send(KafkaTopics.MARKET_CARGO_LOADED, event);
    }

}
