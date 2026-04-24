ALTER TYPE rank_enum ADD VALUE 'LEGEND';
ALTER TYPE rank_enum ADD VALUE 'PIRATE_KING';

ALTER TABLE pirate
    ADD COLUMN home_island_id UUID;

CREATE TYPE island_location_enum AS ENUM (
    'CARIBBEAN_SEA',
    'MEDITERRANEAN_SEA',
    'BLACK_SEA',
    'ATLANTIC_OCEAN',
    'PACIFIC_OCEAN',
    'INDIAN_OCEAN',
    'NORTH_SEA',
    'BALTIC_SEA',
    'RED_SEA',
    'ARABIAN_SEA',
    'SOUTH_CHINA_SEA',
    'GULF_OF_MEXICO',
    'STRAIT_OF_GIBRALTAR',
    'ENGLISH_CHANNEL',
    'STRAIT_OF_MALACCA'
    );

CREATE TYPE island_level_enum AS ENUM (
    'WILD_SHORE',
    'BAY',
    'FISHING_VILLAGE',
    'SMUGGLER_DEN',
    'PIRATE_BAY',
    'TRADE_POST',
    'HARBOR',
    'FREE_PORT',
    'FORTRESS_ISLAND',
    'PROSPEROUS_CITY'
    );

CREATE TABLE island (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    nickname VARCHAR(100),
    area DOUBLE PRECISION NOT NULL,
    location island_location_enum NOT NULL DEFAULT 'CARIBBEAN_SEA',
    owner_id UUID,
    population INT NOT NULL DEFAULT 0,
    ship_traffic_per_day INT NOT NULL DEFAULT 0,
    gold_turnover BIGINT NOT NULL DEFAULT 0,
    tax_percentage DOUBLE PRECISION NOT NULL DEFAULT 0,
    level island_level_enum NOT NULL DEFAULT 'WILD_SHORE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE island_legendary_pirates (
    island_id UUID NOT NULL REFERENCES island(id) ON DELETE CASCADE,
    pirate_id UUID NOT NULL,
    PRIMARY KEY (island_id, pirate_id)
);
