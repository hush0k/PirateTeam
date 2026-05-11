-- Keep demo data easy to enter from the UI.
-- Plain password for every pirate after this migration: Kanysh27!

UPDATE pirate
SET hashed_password = '$2a$10$2fJ3N7mewl/Tn9hRD38M6ed.uNrXHVsFcoCefwLyblOe0YbhuEnz.',
    updated_at = CURRENT_TIMESTAMP;
