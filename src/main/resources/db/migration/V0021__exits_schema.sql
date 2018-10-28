CREATE TABLE `map_exit` (
  id BINARY(16) NOT NULL,
  direction VARCHAR(191),
  location_map_id BINARY(16),
  location_x INTEGER,
  location_y INTEGER,
  destination_map_id BINARY(16),
  destination_x INTEGER,
  destination_y INTEGER,
  PRIMARY KEY (id),
  CONSTRAINT exit_location_map_id_fk FOREIGN KEY (location_map_id) REFERENCES game_map (id),
  CONSTRAINT exit_destination_map_id_fk FOREIGN KEY (destination_map_id) REFERENCES game_map (id)
) ENGINE=InnoDB CHARACTER SET=utf8mb4, COLLATE=utf8mb4_unicode_ci;
