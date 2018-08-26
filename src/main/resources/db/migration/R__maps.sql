INSERT INTO game_map (id, width, tiles)
VALUES ('5231e20f-0658-4685-9396-6e69ebfb2c3b', 3, decode('000102030405060708', 'hex')) ON CONFLICT (id) DO
UPDATE SET width=EXCLUDED.width, tiles=EXCLUDED.tiles;

INSERT INTO game_map (id, width, tiles)
VALUES ('a70bdd6f-b2a3-451e-aed2-90d1fe37f0dd', 5, decode('00000000000000000000000000000000000000000000000000', 'hex')) ON CONFLICT (id) DO
UPDATE SET width=EXCLUDED.width, tiles=EXCLUDED.tiles;

INSERT INTO exit (id, direction, location_map_id, location_x, location_y, destination_map_id, destination_x, destination_y)
VALUES ('f214b43f-188c-44a5-b2dc-2959cc2429aa', 'east', '5231e20f-0658-4685-9396-6e69ebfb2c3b', 2, 0, 'a70bdd6f-b2a3-451e-aed2-90d1fe37f0dd', 0, 4) ON CONFLICT (id) DO
UPDATE SET direction=EXCLUDED.direction, location_map_id=EXCLUDED.location_map_id, location_x=EXCLUDED.location_x, location_y=EXCLUDED.location_y,
destination_map_id=EXCLUDED.destination_map_id, destination_x=EXCLUDED.destination_x, destination_y=EXCLUDED.destination_y;

INSERT INTO exit (id, direction, location_map_id, location_x, location_y, destination_map_id, destination_x, destination_y)
VALUES ('808feb90-0d45-45e4-99f8-6d7deb1c45ba', 'west', 'a70bdd6f-b2a3-451e-aed2-90d1fe37f0dd', 0, 4, '5231e20f-0658-4685-9396-6e69ebfb2c3b', 2, 0) ON CONFLICT (id) DO
UPDATE SET direction=EXCLUDED.direction, location_map_id=EXCLUDED.location_map_id, location_x=EXCLUDED.location_x, location_y=EXCLUDED.location_y,
destination_map_id=EXCLUDED.destination_map_id, destination_x=EXCLUDED.destination_x, destination_y=EXCLUDED.destination_y;
