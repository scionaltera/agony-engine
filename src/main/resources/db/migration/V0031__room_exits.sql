ALTER TABLE room ADD COLUMN exits TINYINT DEFAULT 0;

-- 15 will turn on the exits in all four directions for the start room.
-- Without this it would have no exits at all and players would be stuck there.
UPDATE room SET exits=15 WHERE x=0 AND y=0 AND z=0;
