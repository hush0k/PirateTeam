CREATE TYPE ship_type_enum AS ENUM (
    'SLOOP', 'BRIGANTINE', 'SCHOONER', 'FRIGATE', 'GALLEON', 'BARQUE', 'MAN_OF_WAR'
    );

CREATE TABLE ship (
                      id UUID PRIMARY KEY,
                      capitan_id UUID,
                      name VARCHAR(255),
                      construction_date DATE,
                      ship_type VARCHAR(50),
                      max_crew INT,
                      max_speed FLOAT,
                      builder_country VARCHAR(50),
                      cargo_capacity INT,
                      created_at TIMESTAMP,
                      updated_at TIMESTAMP
);