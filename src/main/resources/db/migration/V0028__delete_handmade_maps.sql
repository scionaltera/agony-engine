-- This is going to stick any items that were on the ground into "The Void", also known as "nowhere in the game".
-- It's easy to get them back, but you'll have to run some SQL to move them from the Void onto a map that exists
-- in your game's database. Because the UUIDs are all randomly generated, there's no way to do this automatically.
--
-- The SQL you would run is: UPDATE actor SET game_map_id='the id of your map', x=0, y=0 WHERE game_map_id IS NULL AND inventory_id IS NULL;
--
UPDATE actor SET game_map_id=NULL WHERE game_map_id IN ('5231e20f-0658-4685-9396-6e69ebfb2c3b', 'a70bdd6f-b2a3-451e-aed2-90d1fe37f0dd');

-- Now let's delete all the hand-built stuff...
DELETE FROM start_location;
DELETE FROM exit;
DELETE FROM game_map WHERE id IN ('5231e20f-0658-4685-9396-6e69ebfb2c3b', 'a70bdd6f-b2a3-451e-aed2-90d1fe37f0dd');
DELETE FROM tile WHERE tileset_id='e75bb6e1-a6e9-45cf-bfb4-a9eea1e3b4be';
DELETE FROM tileset WHERE id='e75bb6e1-a6e9-45cf-bfb4-a9eea1e3b4be';
