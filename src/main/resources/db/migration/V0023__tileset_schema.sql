CREATE TABLE tileset (
  id BINARY(16) NOT NULL,
  name VARCHAR(191) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB CHARACTER SET=utf8mb4, COLLATE=utf8mb4_unicode_ci;

CREATE TABLE tile (
  id BINARY(16) NOT NULL,
  `index` INT NOT NULL,
  tileset BINARY(16) NOT NULL,
  room_title VARCHAR(191) NOT NULL,
  room_description TEXT,
  flags BIGINT NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  CONSTRAINT tile_tileset_fk FOREIGN KEY (tileset) REFERENCES tileset (id)
) ENGINE=InnoDB CHARACTER SET=utf8mb4, COLLATE=utf8mb4_unicode_ci;

ALTER TABLE game_map ADD COLUMN tileset_id BINARY(16);

UPDATE game_map SET tileset_id=unhex(replace('429a3d68-7658-47b0-bba7-8a1d52fb097e', '-', ''));
