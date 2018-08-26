CREATE TABLE exit (
  id UUID NOT NULL,
  direction VARCHAR(255),
  location_map_id UUID,
  location_x INTEGER,
  location_y INTEGER,
  destination_map_id UUID,
  destination_x INTEGER,
  destination_y INTEGER,
  PRIMARY KEY (id),
  CONSTRAINT exit_location_map_id_fk FOREIGN KEY (location_map_id) REFERENCES game_map (id),
  CONSTRAINT exit_destination_map_id_fk FOREIGN KEY (destination_map_id) REFERENCES game_map (id)
);
