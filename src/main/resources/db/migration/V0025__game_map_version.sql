ALTER TABLE game_map ADD COLUMN version INTEGER;

UPDATE game_map SET version = -1;
