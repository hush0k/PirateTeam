CREATE TABLE team (
                      id          UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
                      name        VARCHAR(100) NOT NULL,
                      capitan_id  UUID,
                      treasury    INT         NOT NULL DEFAULT 0,
                      reputation  INT         NOT NULL DEFAULT 0,
                      cohesion    INT         NOT NULL DEFAULT 50,
                      created_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      updated_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE team_pirates (
                              team_id     UUID    NOT NULL REFERENCES team(id) ON DELETE CASCADE,
                              pirate_id   UUID    NOT NULL,
                              PRIMARY KEY (team_id, pirate_id)
);