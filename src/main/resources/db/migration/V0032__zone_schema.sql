CREATE TABLE zone (
  id BINARY(16) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB CHARACTER SET=utf8mb4, COLLATE=utf8mb4_unicode_ci;

# Delete any existing rooms that are on the world map.
DELETE FROM room WHERE x IS NOT NULL AND y IS NOT NULL AND z IS NOT NULL;

# Link rooms to zones.
ALTER TABLE room ADD COLUMN zone_id BINARY(16) AFTER id;
