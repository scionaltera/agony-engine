CREATE TABLE start_location (
  id UUID NOT NULL,
  game_map_id UUID NOT NULL,
  x INT NOT NULL,
  y INT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT start_location_game_map_fk FOREIGN KEY (game_map_id) REFERENCES game_map (id)
);
