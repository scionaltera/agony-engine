CREATE TABLE room (
  id BINARY(16) NOT NULL,
  x INT NOT NULL,
  y INT NOT NULL,
  z INT NOT NULL,
  PRIMARY KEY (id),
  INDEX idx_room_location (x, y, z)
);

ALTER TABLE actor ADD COLUMN room_id BINARY(16) AFTER name;
