INSERT INTO body_part_group(id, name)
VALUES (uuid_bin('2d645926-b365-4563-8a5e-a6cbb7e1fb3b'), 'humanoid') ON DUPLICATE KEY
  UPDATE name=VALUES(name);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (uuid_bin('26bb4127-f3ab-4bc5-bb0c-b931a28900be'), uuid_bin('2d645926-b365-4563-8a5e-a6cbb7e1fb3b'), 'upper body', '', 'BODY_UPPER', NULL) ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (uuid_bin('fd94c62b-55a8-4977-b28c-11925afdad70'), uuid_bin('2d645926-b365-4563-8a5e-a6cbb7e1fb3b'), 'lower body', '', 'BODY_LOWER', 'WL:BODY_UPPER') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (uuid_bin('ba4f7580-95ff-47cd-b70e-6fb5f12f4733'), uuid_bin('2d645926-b365-4563-8a5e-a6cbb7e1fb3b'), 'neck', '', 'NECK', 'WL:BODY_UPPER') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (uuid_bin('e93a6acf-061c-4bca-ae80-01639600d724'), uuid_bin('2d645926-b365-4563-8a5e-a6cbb7e1fb3b'), 'head', '', 'HEAD', 'WL:NECK') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (uuid_bin('30eda10a-c7d5-41c9-bf42-6ac3d62fb150'), uuid_bin('2d645926-b365-4563-8a5e-a6cbb7e1fb3b'), 'right upper arm', '', 'ARM_UPPER', 'WL:BODY_UPPER') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (uuid_bin('6b5fe2fc-9556-4f9f-8640-91cfe191ab72'), uuid_bin('2d645926-b365-4563-8a5e-a6cbb7e1fb3b'), 'right lower arm', '', 'ARM_LOWER', 'NAME:right upper arm') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (uuid_bin('bfe8d1b8-8827-439f-8c29-a82d1211a2ac'), uuid_bin('2d645926-b365-4563-8a5e-a6cbb7e1fb3b'), 'right hand', 'HOLD', 'HAND', 'NAME:right lower arm') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (uuid_bin('2478dda2-0ace-4418-be9a-55826910eeea'), uuid_bin('2d645926-b365-4563-8a5e-a6cbb7e1fb3b'), 'left upper arm', '', 'ARM_UPPER', 'WL:BODY_UPPER') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (uuid_bin('c6b5c295-853b-401c-a130-74f9fc6b969d'), uuid_bin('2d645926-b365-4563-8a5e-a6cbb7e1fb3b'), 'left lower arm', '', 'ARM_LOWER', 'NAME:left upper arm') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (uuid_bin('6a142019-3c9e-4a67-8e38-ff0e05d4f269'), uuid_bin('2d645926-b365-4563-8a5e-a6cbb7e1fb3b'), 'left hand', 'HOLD', 'HAND', 'NAME:left lower arm') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (uuid_bin('f5b07ae0-f942-412e-b5f7-2dbdf5eca63a'), uuid_bin('2d645926-b365-4563-8a5e-a6cbb7e1fb3b'), 'right upper leg', '', 'LEG_UPPER', 'WL:BODY_LOWER') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (uuid_bin('c719148f-31e8-43b9-b87a-5cc135913b1d'), uuid_bin('2d645926-b365-4563-8a5e-a6cbb7e1fb3b'), 'right lower leg', '', 'LEG_LOWER', 'NAME:right upper leg') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (uuid_bin('b4b1db40-a8b7-4b59-9cb2-e45ba11c3283'), uuid_bin('2d645926-b365-4563-8a5e-a6cbb7e1fb3b'), 'right foot', 'WALK', 'FOOT', 'NAME:right lower leg') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (uuid_bin('6eeb49c2-f2d5-4664-90c3-4fdff7d3db10'), uuid_bin('2d645926-b365-4563-8a5e-a6cbb7e1fb3b'), 'left upper leg', '', 'LEG_UPPER', 'WL:BODY_LOWER') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (uuid_bin('a85d9095-46dd-4132-a29e-30e1338a84b7'), uuid_bin('2d645926-b365-4563-8a5e-a6cbb7e1fb3b'), 'left lower leg', '', 'LEG_LOWER', 'NAME:left upper leg') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (uuid_bin('5c345d74-cf99-4551-90b5-e6c2ab726cc2'), uuid_bin('2d645926-b365-4563-8a5e-a6cbb7e1fb3b'), 'left foot', 'WALK', 'FOOT', 'NAME:left lower leg') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_group(id, name)
VALUES (uuid_bin('29918f7d-dada-4b69-b383-80437f8d3392'), 'mouth') ON DUPLICATE KEY
  UPDATE name=VALUES(name);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (uuid_bin('ec2d8c37-eafd-445e-ad9d-9375af90bf88'), uuid_bin('29918f7d-dada-4b69-b383-80437f8d3392'), 'mouth', '', NULL, 'WL:HEAD') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (uuid_bin('c44bac8b-96be-41b8-91cc-ae4a85e481e3'), uuid_bin('29918f7d-dada-4b69-b383-80437f8d3392'), 'tongue', 'SPEAK', NULL, 'NAME:mouth') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (uuid_bin('38afd7a8-12f6-4bae-97bc-5309f78bdeb4'), uuid_bin('29918f7d-dada-4b69-b383-80437f8d3392'), 'lips', '', NULL, 'NAME:mouth') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_group(id, name)
VALUES (uuid_bin('9fb2a074-84ed-4dac-a933-0ee422b8e04e'), 'one eye') ON DUPLICATE KEY
  UPDATE name=VALUES(name);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (uuid_bin('d37f4223-3714-486c-9263-66125c2c0dc9'), uuid_bin('9fb2a074-84ed-4dac-a933-0ee422b8e04e'), 'eye', 'SEE', NULL, 'WL:HEAD') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_group(id, name)
VALUES (uuid_bin('e944f6dc-45fb-4890-a6b5-726215fafef1'), 'two eyes') ON DUPLICATE KEY
  UPDATE name=VALUES(name);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (uuid_bin('640680b0-787f-4a14-a90e-16ba3c43bff8'), uuid_bin('e944f6dc-45fb-4890-a6b5-726215fafef1'), 'right eye', 'SEE', NULL, 'WL:HEAD') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (uuid_bin('0d0777fb-640f-4f3c-b8e6-11628a48a7fe'), uuid_bin('e944f6dc-45fb-4890-a6b5-726215fafef1'), 'left eye', 'SEE', NULL, 'WL:HEAD') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_group(id, name)
VALUES (uuid_bin('7b8009c0-0618-48db-aaf2-bf2ac93eedf7'), 'three eyes') ON DUPLICATE KEY
  UPDATE name=VALUES(name);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (uuid_bin('b5f7810c-57a4-4550-b752-fabe9e1d604f'), uuid_bin('7b8009c0-0618-48db-aaf2-bf2ac93eedf7'), 'right eye', 'SEE', NULL, 'WL:HEAD') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (uuid_bin('ae0e1959-83c1-4dce-923a-535e0049cee2'), uuid_bin('7b8009c0-0618-48db-aaf2-bf2ac93eedf7'), 'center eye', 'SEE', NULL, 'WL:HEAD') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (uuid_bin('f3244826-4a03-4719-b117-f957608e464e'), uuid_bin('7b8009c0-0618-48db-aaf2-bf2ac93eedf7'), 'left eye', 'SEE', NULL, 'WL:HEAD') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);
