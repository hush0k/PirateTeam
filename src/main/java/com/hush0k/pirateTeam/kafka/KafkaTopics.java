package com.hush0k.pirateTeam.kafka;

public final class KafkaTopics {

    public static final String SHIP_CAPTAIN_ASSIGNED = "ship.captain-assigned";
    public static final String FLEET_PIRATE_STAT_CHANGE = "fleet.pirate-stat-change";
    public static final String TEAM_MEMBERS_CHANGE = "team.members-change";
    public static final String MARKET_CARGO_LOADED = "market.cargo-loaded";

    private KafkaTopics() {
    }
}
