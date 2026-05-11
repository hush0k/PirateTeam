package com.hush0k.pirateTeam.config;

import com.hush0k.pirateTeam.kafka.KafkaTopics;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic captainAssignedTopic() {
        return TopicBuilder.name(KafkaTopics.SHIP_CAPTAIN_ASSIGNED)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic pirateStatChangeTopic() {
        return TopicBuilder.name(KafkaTopics.FLEET_PIRATE_STAT_CHANGE)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic teamMembersChangeTopic() {
        return TopicBuilder.name(KafkaTopics.TEAM_MEMBERS_CHANGE)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic cargoLoadedTopic() {
        return TopicBuilder.name(KafkaTopics.MARKET_CARGO_LOADED)
                .partitions(1)
                .replicas(1)
                .build();
    }

}
