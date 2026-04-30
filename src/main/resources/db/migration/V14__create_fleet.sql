CREATE TABLE fleet (
                       id          UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
                       owner_id    UUID        NOT NULL,
                       name        VARCHAR(100),
                       created_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);
