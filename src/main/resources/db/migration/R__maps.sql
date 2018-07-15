INSERT INTO game_map (id, width, tiles)
VALUES ('5231e20f-0658-4685-9396-6e69ebfb2c3b', 3, decode('000102030405060708', 'hex')) ON CONFLICT (id) DO
UPDATE SET width=EXCLUDED.width, tiles=EXCLUDED.tiles;
