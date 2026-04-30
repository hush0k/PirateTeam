CREATE TYPE freedom_enum AS ENUM (
    'FREE',
    'PRISONER'
    );

ALTER TABLE pirate
    ADD COLUMN freedom freedom_enum NOT NULL DEFAULT 'FREE';

ALTER TABLE fleet
    ADD COLUMN ammo INT NOT NULL DEFAULT 0,
    ADD COLUMN provision INT NOT NULL DEFAULT 0;

