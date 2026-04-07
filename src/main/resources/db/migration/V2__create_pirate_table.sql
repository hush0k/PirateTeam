CREATE TABLE pirate (
    id               UUID            PRIMARY KEY DEFAULT gen_random_uuid(),

    login            VARCHAR(100)    NOT NULL UNIQUE,
    first_name       VARCHAR(100)    NOT NULL,
    last_name        VARCHAR(100)    NOT NULL,
    date_of_birth    DATE            NOT NULL,
    hashed_password  VARCHAR(255)    NOT NULL,

    rank             rank_enum       NOT NULL DEFAULT 'CABIN_BOY',
    reputation       INT             NOT NULL DEFAULT 0,
    country          country_enum    NOT NULL DEFAULT 'SPAIN',

    created_at       DATE            NOT NULL DEFAULT CURRENT_DATE,
    updated_at       DATE            NOT NULL DEFAULT CURRENT_DATE
);