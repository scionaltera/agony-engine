CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

INSERT INTO tileset (id, name)
VALUES ('e75bb6e1-a6e9-45cf-bfb4-a9eea1e3b4be', 'The Temple of Scion') ON CONFLICT (id) DO
UPDATE SET name=EXCLUDED.name;

INSERT INTO tile (id, index, tileset, room_title, flags)
VALUES (uuid_generate_v4(), 0, 'e75bb6e1-a6e9-45cf-bfb4-a9eea1e3b4be', 'The Origin', 0) ON CONFLICT (id) DO
UPDATE SET tileset=EXCLUDED.tileset, room_title=EXCLUDED.room_title, flags=EXCLUDED.flags;

INSERT INTO tile (id, index, tileset, room_title, flags)
VALUES (uuid_generate_v4(), 1, 'e75bb6e1-a6e9-45cf-bfb4-a9eea1e3b4be', 'Colonnade', 0) ON CONFLICT (id) DO
UPDATE SET tileset=EXCLUDED.tileset, room_title=EXCLUDED.room_title, flags=EXCLUDED.flags;

INSERT INTO tile (id, index, tileset, room_title, flags)
VALUES (uuid_generate_v4(), 2, 'e75bb6e1-a6e9-45cf-bfb4-a9eea1e3b4be', 'Vestibule', 0) ON CONFLICT (id) DO
UPDATE SET tileset=EXCLUDED.tileset, room_title=EXCLUDED.room_title, flags=EXCLUDED.flags;

INSERT INTO tile (id, index, tileset, room_title, flags)
VALUES (uuid_generate_v4(), 3, 'e75bb6e1-a6e9-45cf-bfb4-a9eea1e3b4be', 'Courtyard', 0) ON CONFLICT (id) DO
UPDATE SET tileset=EXCLUDED.tileset, room_title=EXCLUDED.room_title, flags=EXCLUDED.flags;

INSERT INTO tile (id, index, tileset, room_title, flags)
VALUES (uuid_generate_v4(), 4, 'e75bb6e1-a6e9-45cf-bfb4-a9eea1e3b4be', 'Armory', 0) ON CONFLICT (id) DO
UPDATE SET tileset=EXCLUDED.tileset, room_title=EXCLUDED.room_title, flags=EXCLUDED.flags;

INSERT INTO tile (id, index, tileset, room_title, flags)
VALUES (uuid_generate_v4(), 5, 'e75bb6e1-a6e9-45cf-bfb4-a9eea1e3b4be', 'Lounge', 0) ON CONFLICT (id) DO
UPDATE SET tileset=EXCLUDED.tileset, room_title=EXCLUDED.room_title, flags=EXCLUDED.flags;

INSERT INTO tileset (id, name)
VALUES ('6b9cdb5b-0560-4dde-b40b-89dd1b928844', 'Temperate Rain Forest') ON CONFLICT (id) DO
UPDATE SET name=EXCLUDED.name;

INSERT INTO tile (id, index, tileset, room_title, flags)
VALUES (uuid_generate_v4(), 0, '6b9cdb5b-0560-4dde-b40b-89dd1b928844', 'Temperate Rain Forest', 0) ON CONFLICT (id) DO
UPDATE SET tileset=EXCLUDED.tileset, room_title=EXCLUDED.room_title, flags=EXCLUDED.flags;

INSERT INTO tileset (id, name)
VALUES ('429a3d68-7658-47b0-bba7-8a1d52fb097e', 'Inside Someone''s Inventory') ON CONFLICT (id) DO
UPDATE SET name=EXCLUDED.name;

INSERT INTO tile (id, index, tileset, room_title, flags)
VALUES (uuid_generate_v4(), 0, '429a3d68-7658-47b0-bba7-8a1d52fb097e', 'Inside Someone''s Inventory', 0) ON CONFLICT (id) DO
UPDATE SET tileset=EXCLUDED.tileset, room_title=EXCLUDED.room_title, flags=EXCLUDED.flags;
