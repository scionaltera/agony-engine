CREATE TABLE start_location (
  id BINARY(16) NOT NULL,
  game_map_id BINARY(16) NOT NULL,
  x INT NOT NULL,
  y INT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT start_location_game_map_fk FOREIGN KEY (game_map_id) REFERENCES game_map (id)
) ENGINE=InnoDB CHARACTER SET=utf8mb4, COLLATE=utf8mb4_unicode_ci;
