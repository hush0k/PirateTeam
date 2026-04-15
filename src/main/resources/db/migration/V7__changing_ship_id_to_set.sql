ALTER TABLE pirate
    DROP COLUMN IF EXISTS ship_id;

-- Создаём таблицу связи пират → корабли
CREATE TABLE pirate_ships (
                              pirate_id   UUID    NOT NULL REFERENCES pirate(id) ON DELETE CASCADE,
                              ship_id     UUID    NOT NULL,
                              PRIMARY KEY (pirate_id, ship_id)
);