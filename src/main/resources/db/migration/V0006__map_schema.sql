CREATE TABLE game_map (
  id UUID NOT NULL,
  width INTEGER NOT NULL,
  tiles bytea NOT NULL,
  PRIMARY KEY (id)
);
