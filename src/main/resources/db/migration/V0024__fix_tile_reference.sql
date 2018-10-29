ALTER TABLE tile DROP FOREIGN KEY tile_tileset_fk;
ALTER TABLE tile CHANGE tileset tileset_id BINARY(16);
ALTER TABLE tile ADD FOREIGN KEY tile_tileset_fk (tileset_id) REFERENCES tileset (id);
