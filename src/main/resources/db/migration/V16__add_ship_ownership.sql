CREATE TYPE ship_ownership_enum AS ENUM (
    'OWNED',
    'AVAILABLE_FOR_SALE',
    'ABANDONED'
    );

ALTER TABLE ship
    ADD COLUMN ownership ship_ownership_enum NOT NULL DEFAULT 'AVAILABLE_FOR_SALE';
