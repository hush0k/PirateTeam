ALTER TABLE ship
    ADD COLUMN owner_id UUID,
    ADD COLUMN fleet_id UUID,
    ADD COLUMN price INT;