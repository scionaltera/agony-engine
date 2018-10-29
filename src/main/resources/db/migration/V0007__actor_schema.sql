CREATE TABLE actor (
  id BINARY(16) NOT NULL,
  name VARCHAR(191) NOT NULL,
  session_username VARCHAR(191),
  session_id VARCHAR(191),
  game_map_id BINARY(16),
  x INTEGER,
  y INTEGER,
  PRIMARY KEY (id),
  CONSTRAINT ACTOR_GAME_MAP_FK FOREIGN KEY (game_map_id) REFERENCES game_map (id)
) ENGINE=InnoDB CHARACTER SET=utf8mb4, COLLATE=utf8mb4_unicode_ci;
