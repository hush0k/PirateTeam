CREATE TYPE defence_type_enum AS ENUM (
    'CROWD',
    'MILITIA',
    'GUARD_GROUP',
    'TRAINED_GUARDS',
    'PROFESSIONAL_GUARDS',
    'VETERANS',
    'ELITE_GUARD',
    'ROYAL_GUARD',
    'MERCENARIES',
    'SPECIAL_FORCES'
);

ALTER TABLE island
    ADD COLUMN defense_type defence_type_enum NOT NULL DEFAULT 'CROWD';
