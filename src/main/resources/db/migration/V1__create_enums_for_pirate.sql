-- Countries
CREATE TYPE country_enum AS ENUM (
    -- EUROPE
    'ENGLAND',
    'SPAIN',
    'FRANCE',
    'NETHERLANDS',
    'PORTUGAL',

    -- AMERICA
    'USA',
    'BAHAMAS',
    'HAITI',

    -- AFRICA
    'MOROCCO',
    'SOMALIA',

    -- NEAR EAST
    'TURKEY',
    'ARABIC'
    );

-- Ranks (levels from 1 to 12)
CREATE TYPE rank_enum AS ENUM (
    'CABIN_BOY',       -- 1
    'SAILOR',          -- 2
    'LOOKOUT',         -- 3
    'BOARDER',         -- 4
    'COOK',            -- 5
    'TREASURER',       -- 6
    'SHIP_SURGEON',    -- 7
    'GUNNER',          -- 8
    'BOATSWAIN',       -- 9
    'NAVIGATOR',       -- 10
    'QUARTERMASTER',   -- 11
    'CAPTAIN'          -- 12
    );