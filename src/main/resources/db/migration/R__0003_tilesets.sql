INSERT INTO tileset (id, name)
VALUES ('e75bb6e1-a6e9-45cf-bfb4-a9eea1e3b4be', 'The Temple') ON CONFLICT (id) DO
UPDATE SET name=EXCLUDED.name;

INSERT INTO tile (id, index, tileset, room_title, room_description, flags)
VALUES ('3e4d15e1-27b4-402f-a160-7c9758c9ca04', 0, 'e75bb6e1-a6e9-45cf-bfb4-a9eea1e3b4be', 'The Origin', 'A building of smooth polished stones has been constructed around a circular spot on the floor. A white glow emanates from the spot, radiating glowing particles up into the air.', 0) ON CONFLICT (id) DO
UPDATE SET tileset=EXCLUDED.tileset, room_title=EXCLUDED.room_title, flags=EXCLUDED.flags;

INSERT INTO tile (id, index, tileset, room_title, room_description, flags)
VALUES ('ce38de4f-b5d5-4b76-bbb3-98f46c03557c', 1, 'e75bb6e1-a6e9-45cf-bfb4-a9eea1e3b4be', 'Colonnade', 'The covered colonnade is built from smooth polished stone. The open sides look out into the evergreen forest beyond, providing only minimal protection from the elements.', 0) ON CONFLICT (id) DO
UPDATE SET tileset=EXCLUDED.tileset, room_title=EXCLUDED.room_title, flags=EXCLUDED.flags;

INSERT INTO tile (id, index, tileset, room_title, room_description, flags)
VALUES ('4b50f352-ef71-4fc0-a8d6-b237bc30bf5d', 2, 'e75bb6e1-a6e9-45cf-bfb4-a9eea1e3b4be', 'Vestibule', 'This small vestibule serves as a place for visitors to remove their wet or dirty clothing before proceeding further into the temple. ', 0) ON CONFLICT (id) DO
UPDATE SET tileset=EXCLUDED.tileset, room_title=EXCLUDED.room_title, flags=EXCLUDED.flags;

INSERT INTO tile (id, index, tileset, room_title, room_description, flags)
VALUES ('bca5bbea-1431-40ab-b7a7-89ea68381034', 3, 'e75bb6e1-a6e9-45cf-bfb4-a9eea1e3b4be', 'Courtyard', 'A small, but carefully maintained courtyard contains many examples of the local flora, arranged beautifully by some master gardener.', 0) ON CONFLICT (id) DO
UPDATE SET tileset=EXCLUDED.tileset, room_title=EXCLUDED.room_title, flags=EXCLUDED.flags;

INSERT INTO tile (id, index, tileset, room_title, room_description, flags)
VALUES ('23355f3d-1e43-4c84-88db-fc7d25e4d869', 4, 'e75bb6e1-a6e9-45cf-bfb4-a9eea1e3b4be', 'Armory', 'Weapon and armor racks line the walls, and a practice dummy stands forlornly in the corner.', 0) ON CONFLICT (id) DO
UPDATE SET tileset=EXCLUDED.tileset, room_title=EXCLUDED.room_title, flags=EXCLUDED.flags;

INSERT INTO tile (id, index, tileset, room_title, room_description, flags)
VALUES ('6ad8680b-1539-429a-b6c8-6d86e8465088', 5, 'e75bb6e1-a6e9-45cf-bfb4-a9eea1e3b4be', 'Lounge', 'Several couches and overstuffed chairs have been arranged here, offering comfort to weary travellers.', 0) ON CONFLICT (id) DO
UPDATE SET tileset=EXCLUDED.tileset, room_title=EXCLUDED.room_title, flags=EXCLUDED.flags;

INSERT INTO tileset (id, name)
VALUES ('6b9cdb5b-0560-4dde-b40b-89dd1b928844', 'Temperate Rain Forest') ON CONFLICT (id) DO
UPDATE SET name=EXCLUDED.name;

INSERT INTO tile (id, index, tileset, room_title, room_description, flags)
VALUES ('ff6d419e-35ff-441a-8d52-15ec048fdbf9', 0, '6b9cdb5b-0560-4dde-b40b-89dd1b928844', 'Impassable', 'This room is impassable, and not actually part of the map. What are you doing here?', 1) ON CONFLICT (id) DO
UPDATE SET tileset=EXCLUDED.tileset, room_title=EXCLUDED.room_title, flags=EXCLUDED.flags;

INSERT INTO tile (id, index, tileset, room_title, room_description, flags)
VALUES ('1d50bc5f-1eda-41ac-8cc5-97614c5c6f8f', 1, '6b9cdb5b-0560-4dde-b40b-89dd1b928844', 'A Clearing', 'The trees give way here to an open space, offering a view of the sky and less obstructed movement.', 2) ON CONFLICT (id) DO
UPDATE SET tileset=EXCLUDED.tileset, room_title=EXCLUDED.room_title, flags=EXCLUDED.flags;

INSERT INTO tile (id, index, tileset, room_title, room_description, flags)
VALUES ('b9dcfdce-fcb5-47d2-a3fe-ffa7338a3c75', 2, '6b9cdb5b-0560-4dde-b40b-89dd1b928844', 'Light Evergreen Forest', 'Large old growth evergreen trees make up the forest here, spaced widely apart. There is very little undergrowth. The forest floor is primarily just a bed of red and orange pine needles.', 2) ON CONFLICT (id) DO
UPDATE SET tileset=EXCLUDED.tileset, room_title=EXCLUDED.room_title, flags=EXCLUDED.flags;

INSERT INTO tile (id, index, tileset, room_title, room_description, flags)
VALUES ('950cadef-b7ce-4e37-92f9-a6be747ae03d', 3, '6b9cdb5b-0560-4dde-b40b-89dd1b928844', 'Evergreen Forest', 'Large evergreen trees thrive here, their thick boughs blocking any sunlight. The trunks grow fairly close to one another, but the darkness and acidity have choked out most smaller trees and shrubs. Patches of ivy are all the plant life that can survive beneath these behemoths.', 2) ON CONFLICT (id) DO
UPDATE SET tileset=EXCLUDED.tileset, room_title=EXCLUDED.room_title, flags=EXCLUDED.flags;

INSERT INTO tile (id, index, tileset, room_title, room_description, flags)
VALUES ('bb698d2f-2d69-448c-92f2-1abe8874e3b2', 4, '6b9cdb5b-0560-4dde-b40b-89dd1b928844', 'Dense Evergreen Forest', 'A tangle of young to medium sized evergreen trees makes up the forest here. Vines and shrubs grow between them, sucking up any rays of light that shine through the canopy and drawing nutrients from the larger trees themselves. Movement is made quite difficult in some places by the encroaching vines and branches.', 2) ON CONFLICT (id) DO
UPDATE SET tileset=EXCLUDED.tileset, room_title=EXCLUDED.room_title, flags=EXCLUDED.flags;

INSERT INTO tileset (id, name)
VALUES ('429a3d68-7658-47b0-bba7-8a1d52fb097e', 'Inside Someone''s Inventory') ON CONFLICT (id) DO
UPDATE SET name=EXCLUDED.name;

INSERT INTO tile (id, index, tileset, room_title, room_description, flags)
VALUES ('c825d1c5-1930-46f7-9b03-2eb7b891f049', 0, '429a3d68-7658-47b0-bba7-8a1d52fb097e', 'Carried', 'You are being carried by someone.', 0) ON CONFLICT (id) DO
UPDATE SET tileset=EXCLUDED.tileset, room_title=EXCLUDED.room_title, flags=EXCLUDED.flags;
