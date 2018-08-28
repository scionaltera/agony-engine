CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

INSERT INTO tileset (id, name)
VALUES ('e75bb6e1-a6e9-45cf-bfb4-a9eea1e3b4be', 'The Temple') ON CONFLICT (id) DO
UPDATE SET name=EXCLUDED.name;

INSERT INTO tile (id, index, tileset, room_title, room_description, flags)
VALUES (uuid_generate_v4(), 0, 'e75bb6e1-a6e9-45cf-bfb4-a9eea1e3b4be', 'The Origin', 'A building of smooth polished stones has been constructed around a circular spot on the floor. A white glow emanates from the spot, radiating glowing particles up into the air.', 0) ON CONFLICT (id) DO
UPDATE SET tileset=EXCLUDED.tileset, room_title=EXCLUDED.room_title, flags=EXCLUDED.flags;

INSERT INTO tile (id, index, tileset, room_title, room_description, flags)
VALUES (uuid_generate_v4(), 1, 'e75bb6e1-a6e9-45cf-bfb4-a9eea1e3b4be', 'Colonnade', 'The covered colonnade is built from smooth polished stone. The open sides look out into the evergreen forest beyond, providing only minimal protection from the elements.', 0) ON CONFLICT (id) DO
UPDATE SET tileset=EXCLUDED.tileset, room_title=EXCLUDED.room_title, flags=EXCLUDED.flags;

INSERT INTO tile (id, index, tileset, room_title, room_description, flags)
VALUES (uuid_generate_v4(), 2, 'e75bb6e1-a6e9-45cf-bfb4-a9eea1e3b4be', 'Vestibule', 'This small vestibule serves as a place for visitors to remove their wet or dirty clothing before proceeding further into the temple. ', 0) ON CONFLICT (id) DO
UPDATE SET tileset=EXCLUDED.tileset, room_title=EXCLUDED.room_title, flags=EXCLUDED.flags;

INSERT INTO tile (id, index, tileset, room_title, room_description, flags)
VALUES (uuid_generate_v4(), 3, 'e75bb6e1-a6e9-45cf-bfb4-a9eea1e3b4be', 'Courtyard', 'A small, but carefully maintained courtyard contains many examples of the local flora, arranged beautifully by some master gardener.', 0) ON CONFLICT (id) DO
UPDATE SET tileset=EXCLUDED.tileset, room_title=EXCLUDED.room_title, flags=EXCLUDED.flags;

INSERT INTO tile (id, index, tileset, room_title, room_description, flags)
VALUES (uuid_generate_v4(), 4, 'e75bb6e1-a6e9-45cf-bfb4-a9eea1e3b4be', 'Armory', 'Weapon and armor racks line the walls, and a practice dummy stands forlornly in the corner.', 0) ON CONFLICT (id) DO
UPDATE SET tileset=EXCLUDED.tileset, room_title=EXCLUDED.room_title, flags=EXCLUDED.flags;

INSERT INTO tile (id, index, tileset, room_title, room_description, flags)
VALUES (uuid_generate_v4(), 5, 'e75bb6e1-a6e9-45cf-bfb4-a9eea1e3b4be', 'Lounge', 'Several couches and overstuffed chairs have been arranged here, offering comfort to weary travellers.', 0) ON CONFLICT (id) DO
UPDATE SET tileset=EXCLUDED.tileset, room_title=EXCLUDED.room_title, flags=EXCLUDED.flags;

INSERT INTO tileset (id, name)
VALUES ('6b9cdb5b-0560-4dde-b40b-89dd1b928844', 'Temperate Rain Forest') ON CONFLICT (id) DO
UPDATE SET name=EXCLUDED.name;

INSERT INTO tile (id, index, tileset, room_title, room_description, flags)
VALUES (uuid_generate_v4(), 0, '6b9cdb5b-0560-4dde-b40b-89dd1b928844', 'Temperate Rain Forest', 'Dark, somber evergreen trees make up this thick forest.', 0) ON CONFLICT (id) DO
UPDATE SET tileset=EXCLUDED.tileset, room_title=EXCLUDED.room_title, flags=EXCLUDED.flags;

INSERT INTO tileset (id, name)
VALUES ('429a3d68-7658-47b0-bba7-8a1d52fb097e', 'Inside Someone''s Inventory') ON CONFLICT (id) DO
UPDATE SET name=EXCLUDED.name;

INSERT INTO tile (id, index, tileset, room_title, room_description, flags)
VALUES (uuid_generate_v4(), 0, '429a3d68-7658-47b0-bba7-8a1d52fb097e', 'Carried', 'You are being carried by someone.', 0) ON CONFLICT (id) DO
UPDATE SET tileset=EXCLUDED.tileset, room_title=EXCLUDED.room_title, flags=EXCLUDED.flags;
