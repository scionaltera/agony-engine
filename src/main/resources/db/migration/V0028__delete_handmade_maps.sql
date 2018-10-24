DELETE FROM start_location;
DELETE FROM exit;
DELETE FROM game_map WHERE id IN ('5231e20f-0658-4685-9396-6e69ebfb2c3b', 'a70bdd6f-b2a3-451e-aed2-90d1fe37f0dd');
DELETE FROM tile WHERE tileset_id='e75bb6e1-a6e9-45cf-bfb4-a9eea1e3b4be';
DELETE FROM tileset WHERE id='e75bb6e1-a6e9-45cf-bfb4-a9eea1e3b4be';
