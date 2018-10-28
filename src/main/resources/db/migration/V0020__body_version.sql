ALTER TABLE creature_info ADD COLUMN body_version INTEGER;

UPDATE creature_info SET body_version = 0;

ALTER TABLE creature_info MODIFY body_version INTEGER NOT NULL;
