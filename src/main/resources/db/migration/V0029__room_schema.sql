CREATE TABLE room (
  id BINARY(16) NOT NULL,
  x INT,
  y INT,
  z INT,
  PRIMARY KEY (id),
  INDEX idx_room_location (x, y, z)
);

ALTER TABLE actor ADD COLUMN room_id BINARY(16) AFTER name;
