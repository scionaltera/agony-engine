CREATE TABLE actor (
  id UUID NOT NULL,
  name VARCHAR(256) NOT NULL,
  session_username VARCHAR(256),
  session_id VARCHAR(256),
  game_map_id UUID,
  x INTEGER,
  y INTEGER,
  PRIMARY KEY (id),
  CONSTRAINT ACTOR_GAME_MAP_FK FOREIGN KEY (game_map_id) REFERENCES game_map (id)
);
