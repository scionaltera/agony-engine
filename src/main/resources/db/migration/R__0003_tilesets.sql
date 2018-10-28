INSERT INTO tileset (id, name, flags)
VALUES (unhex(replace('6b9cdb5b-0560-4dde-b40b-89dd1b928844', '-', '')), 'Temperate Rain Forest', 1) ON DUPLICATE KEY
  UPDATE name=VALUES(name), flags=VALUES(flags);

INSERT INTO tile (id, `index`, tileset_id, room_title, room_description, flags)
VALUES (unhex(replace('ff6d419e-35ff-441a-8d52-15ec048fdbf9', '-', '')), 0, unhex(replace('6b9cdb5b-0560-4dde-b40b-89dd1b928844', '-', '')), 'Impassable', 'This room is impassable, and not actually part of the map. What are you doing here?', 1) ON DUPLICATE KEY
  UPDATE tileset_id=VALUES(tileset_id), room_title=VALUES(room_title), room_description=VALUES(room_description), flags=VALUES(flags);

INSERT INTO tile (id, `index`, tileset_id, room_title, room_description, flags)
VALUES (unhex(replace('1d50bc5f-1eda-41ac-8cc5-97614c5c6f8f', '-', '')), 1, unhex(replace('6b9cdb5b-0560-4dde-b40b-89dd1b928844', '-', '')), 'A Clearing', 'The trees give way here to an open space, offering a view of the sky and less obstructed movement.', 2) ON DUPLICATE KEY
  UPDATE tileset_id=VALUES(tileset_id), room_title=VALUES(room_title), room_description=VALUES(room_description), flags=VALUES(flags);

INSERT INTO tile (id, `index`, tileset_id, room_title, room_description, flags)
VALUES (unhex(replace('b9dcfdce-fcb5-47d2-a3fe-ffa7338a3c75', '-', '')), 2, unhex(replace('6b9cdb5b-0560-4dde-b40b-89dd1b928844', '-', '')), 'Light Evergreen Forest', 'Large old growth evergreen trees make up the forest here, spaced widely apart. There is very little undergrowth. The forest floor is primarily just a bed of red and orange pine needles.', 2) ON DUPLICATE KEY
  UPDATE tileset_id=VALUES(tileset_id), room_title=VALUES(room_title), room_description=VALUES(room_description), flags=VALUES(flags);

INSERT INTO tile (id, `index`, tileset_id, room_title, room_description, flags)
VALUES (unhex(replace('950cadef-b7ce-4e37-92f9-a6be747ae03d', '-', '')), 3, unhex(replace('6b9cdb5b-0560-4dde-b40b-89dd1b928844', '-', '')), 'Evergreen Forest', 'Large evergreen trees thrive here, their thick boughs blocking any sunlight. The trunks grow fairly close to one another, but the darkness and acidity have choked out most smaller trees and shrubs. Patches of ivy are all the plant life that can survive beneath these behemoths.', 2) ON DUPLICATE KEY
  UPDATE tileset_id=VALUES(tileset_id), room_title=VALUES(room_title), room_description=VALUES(room_description), flags=VALUES(flags);

INSERT INTO tile (id, `index`, tileset_id, room_title, room_description, flags)
VALUES (unhex(replace('bb698d2f-2d69-448c-92f2-1abe8874e3b2', '-', '')), 4, unhex(replace('6b9cdb5b-0560-4dde-b40b-89dd1b928844', '-', '')), 'Dense Evergreen Forest', 'A tangle of young to medium sized evergreen trees makes up the forest here. Vines and shrubs grow between them, sucking up any rays of light that shine through the canopy and drawing nutrients from the larger trees themselves. Movement is made quite difficult in some places by the encroaching vines and branches.', 2) ON DUPLICATE KEY
  UPDATE tileset_id=VALUES(tileset_id), room_title=VALUES(room_title), room_description=VALUES(room_description), flags=VALUES(flags);

INSERT INTO tileset (id, name, flags)
VALUES (unhex(replace('429a3d68-7658-47b0-bba7-8a1d52fb097e', '-', '')), 'Inside Someone''s Inventory', 0) ON DUPLICATE KEY
  UPDATE name=VALUES(name), flags=VALUES(flags);

INSERT INTO tile (id, `index`, tileset_id, room_title, room_description, flags)
VALUES (unhex(replace('c825d1c5-1930-46f7-9b03-2eb7b891f049', '-', '')), 0, unhex(replace('429a3d68-7658-47b0-bba7-8a1d52fb097e', '-', '')), 'Carried', 'You are being carried by someone.', 0) ON DUPLICATE KEY
  UPDATE tileset_id=VALUES(tileset_id), room_title=VALUES(room_title), room_description=VALUES(room_description), flags=VALUES(flags);
