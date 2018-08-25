ALTER TABLE creature_info ADD COLUMN body_version INTEGER;

UPDATE creature_info SET body_version = 0;

ALTER TABLE creature_info ALTER COLUMN body_version SET NOT NULL;
