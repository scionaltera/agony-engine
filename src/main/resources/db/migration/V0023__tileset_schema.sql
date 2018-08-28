CREATE TABLE tileset (
  id UUID NOT NULL,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE tile (
  id UUID NOT NULL,
  index INT NOT NULL,
  tileset UUID NOT NULL,
  room_title VARCHAR(255) NOT NULL,
  flags BIGINT NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  CONSTRAINT tile_tileset_fk FOREIGN KEY (tileset) REFERENCES tileset (id)
);

ALTER TABLE game_map ADD COLUMN tileset_id UUID;

UPDATE game_map SET tileset_id='429a3d68-7658-47b0-bba7-8a1d52fb097e';
