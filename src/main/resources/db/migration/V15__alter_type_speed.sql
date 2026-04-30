ALTER TABLE ship
    ALTER COLUMN max_speed TYPE INT USING max_speed::INT;

ALTER TABLE ship
    ADD COLUMN filled_cargo_space INT NOT NULL DEFAULT 0;

ALTER TABLE ship
    ADD CONSTRAINT chk_ship_filled_cargo_space
        CHECK (filled_cargo_space >= 0 AND filled_cargo_space <= cargo_capacity);
