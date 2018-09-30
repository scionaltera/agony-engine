-- Add version column
ALTER TABLE game_map ADD COLUMN version INTEGER;

-- Set every existing map to the immutable version
-- They're all inventories and two handmade zones, so they shouldn't get updated
-- when we wire that functionality in
UPDATE game_map SET version = -1;

-- Now that none of the versions are null we can add the NOT NULL constraint
-- to prevent any new maps without versions from getting in
ALTER TABLE game_map ALTER COLUMN version SET NOT NULL;

-- Fix any inventory maps that are already in the game with the wrong tile number
UPDATE game_map SET tiles = decode('00', 'hex') WHERE tileset_id = '429a3d68-7658-47b0-bba7-8a1d52fb097e';
