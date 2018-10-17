INSERT INTO start_location (id, game_map_id, x, y)
VALUES ('7d64ae21-c513-4b53-acd2-28535a1bbf9a', '5231e20f-0658-4685-9396-6e69ebfb2c3b', 0, 0) ON CONFLICT (id) DO
UPDATE SET game_map_id=EXCLUDED.game_map_id, x=EXCLUDED.x, y=EXCLUDED.y;
