CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

INSERT INTO body_part_group(id, name)
VALUES ('2d645926-b365-4563-8a5e-a6cbb7e1fb3b', 'humanoid') ON CONFLICT (id) DO
UPDATE SET name=EXCLUDED.name;

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (uuid_generate_v4(), '2d645926-b365-4563-8a5e-a6cbb7e1fb3b', 'upper body', '', 'BODY_UPPER', NULL) ON CONFLICT (id) DO
UPDATE SET name=EXCLUDED.name, capabilities=EXCLUDED.capabilities, wear_location=EXCLUDED.wear_location, connection=EXCLUDED.connection;

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (uuid_generate_v4(), '2d645926-b365-4563-8a5e-a6cbb7e1fb3b', 'lower body', '', 'BODY_LOWER', 'WL:BODY_UPPER') ON CONFLICT (id) DO
UPDATE SET name=EXCLUDED.name, capabilities=EXCLUDED.capabilities, wear_location=EXCLUDED.wear_location, connection=EXCLUDED.connection;

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (uuid_generate_v4(), '2d645926-b365-4563-8a5e-a6cbb7e1fb3b', 'neck', '', 'NECK', 'WL:BODY_UPPER') ON CONFLICT (id) DO
UPDATE SET name=EXCLUDED.name, capabilities=EXCLUDED.capabilities, wear_location=EXCLUDED.wear_location, connection=EXCLUDED.connection;

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (uuid_generate_v4(), '2d645926-b365-4563-8a5e-a6cbb7e1fb3b', 'head', '', 'HEAD', 'WL:NECK') ON CONFLICT (id) DO
UPDATE SET name=EXCLUDED.name, capabilities=EXCLUDED.capabilities, wear_location=EXCLUDED.wear_location, connection=EXCLUDED.connection;

INSERT INTO body_part_group(id, name)
VALUES ('29918f7d-dada-4b69-b383-80437f8d3392', 'human_mouth') ON CONFLICT (id) DO
UPDATE SET name=EXCLUDED.name;

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (uuid_generate_v4(), '29918f7d-dada-4b69-b383-80437f8d3392', 'mouth', '', NULL, 'WL:HEAD') ON CONFLICT (id) DO
UPDATE SET name=EXCLUDED.name, capabilities=EXCLUDED.capabilities, wear_location=EXCLUDED.wear_location, connection=EXCLUDED.connection;

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (uuid_generate_v4(), '29918f7d-dada-4b69-b383-80437f8d3392', 'tongue', 'SPEAK', NULL, 'NAME:mouth') ON CONFLICT (id) DO
UPDATE SET name=EXCLUDED.name, capabilities=EXCLUDED.capabilities, wear_location=EXCLUDED.wear_location, connection=EXCLUDED.connection;

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (uuid_generate_v4(), '29918f7d-dada-4b69-b383-80437f8d3392', 'teeth', '', NULL, 'NAME:mouth') ON CONFLICT (id) DO
UPDATE SET name=EXCLUDED.name, capabilities=EXCLUDED.capabilities, wear_location=EXCLUDED.wear_location, connection=EXCLUDED.connection;

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (uuid_generate_v4(), '29918f7d-dada-4b69-b383-80437f8d3392', 'lips', '', NULL, 'NAME:mouth') ON CONFLICT (id) DO
UPDATE SET name=EXCLUDED.name, capabilities=EXCLUDED.capabilities, wear_location=EXCLUDED.wear_location, connection=EXCLUDED.connection;

INSERT INTO body_part_group(id, name)
VALUES ('29918f7d-dada-4b69-b383-80437f8d3392', 'human_mouth') ON CONFLICT (id) DO
UPDATE SET name=EXCLUDED.name;
