INSERT INTO body_part_group(id, name)
VALUES (unhex(replace('2d645926-b365-4563-8a5e-a6cbb7e1fb3b', '-', '')), 'humanoid') ON DUPLICATE KEY
  UPDATE name=VALUES(name);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (unhex(replace(uuid(), '-', '')), unhex(replace('2d645926-b365-4563-8a5e-a6cbb7e1fb3b', '-', '')), 'upper body', '', 'BODY_UPPER', NULL) ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (unhex(replace(uuid(), '-', '')), unhex(replace('2d645926-b365-4563-8a5e-a6cbb7e1fb3b', '-', '')), 'lower body', '', 'BODY_LOWER', 'WL:BODY_UPPER') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (unhex(replace(uuid(), '-', '')), unhex(replace('2d645926-b365-4563-8a5e-a6cbb7e1fb3b', '-', '')), 'neck', '', 'NECK', 'WL:BODY_UPPER') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (unhex(replace(uuid(), '-', '')), unhex(replace('2d645926-b365-4563-8a5e-a6cbb7e1fb3b', '-', '')), 'head', '', 'HEAD', 'WL:NECK') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (unhex(replace(uuid(), '-', '')), unhex(replace('2d645926-b365-4563-8a5e-a6cbb7e1fb3b', '-', '')), 'right upper arm', '', 'ARM_UPPER', 'WL:BODY_UPPER') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (unhex(replace(uuid(), '-', '')), unhex(replace('2d645926-b365-4563-8a5e-a6cbb7e1fb3b', '-', '')), 'right lower arm', '', 'ARM_LOWER', 'NAME:right upper arm') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (unhex(replace(uuid(), '-', '')), unhex(replace('2d645926-b365-4563-8a5e-a6cbb7e1fb3b', '-', '')), 'right hand', 'HOLD', 'HAND', 'NAME:right lower arm') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (unhex(replace(uuid(), '-', '')), unhex(replace('2d645926-b365-4563-8a5e-a6cbb7e1fb3b', '-', '')), 'left upper arm', '', 'ARM_UPPER', 'WL:BODY_UPPER') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (unhex(replace(uuid(), '-', '')), unhex(replace('2d645926-b365-4563-8a5e-a6cbb7e1fb3b', '-', '')), 'left lower arm', '', 'ARM_LOWER', 'NAME:left upper arm') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (unhex(replace(uuid(), '-', '')), unhex(replace('2d645926-b365-4563-8a5e-a6cbb7e1fb3b', '-', '')), 'left hand', 'HOLD', 'HAND', 'NAME:left lower arm') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (unhex(replace(uuid(), '-', '')), unhex(replace('2d645926-b365-4563-8a5e-a6cbb7e1fb3b', '-', '')), 'right upper leg', '', 'LEG_UPPER', 'WL:BODY_LOWER') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (unhex(replace(uuid(), '-', '')), unhex(replace('2d645926-b365-4563-8a5e-a6cbb7e1fb3b', '-', '')), 'right lower leg', '', 'LEG_LOWER', 'NAME:right upper leg') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (unhex(replace(uuid(), '-', '')), unhex(replace('2d645926-b365-4563-8a5e-a6cbb7e1fb3b', '-', '')), 'right foot', 'WALK', 'FOOT', 'NAME:right lower leg') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (unhex(replace(uuid(), '-', '')), unhex(replace('2d645926-b365-4563-8a5e-a6cbb7e1fb3b', '-', '')), 'left upper leg', '', 'LEG_UPPER', 'WL:BODY_LOWER') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (unhex(replace(uuid(), '-', '')), unhex(replace('2d645926-b365-4563-8a5e-a6cbb7e1fb3b', '-', '')), 'left lower leg', '', 'LEG_LOWER', 'NAME:left upper leg') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (unhex(replace(uuid(), '-', '')), unhex(replace('2d645926-b365-4563-8a5e-a6cbb7e1fb3b', '-', '')), 'left foot', 'WALK', 'FOOT', 'NAME:left lower leg') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_group(id, name)
VALUES (unhex(replace('2d645926-b365-4563-8a5e-a6cbb7e1fb3b', '-', '')), 'mouth') ON DUPLICATE KEY
  UPDATE name=VALUES(name);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (unhex(replace(uuid(), '-', '')), unhex(replace('2d645926-b365-4563-8a5e-a6cbb7e1fb3b', '-', '')), 'mouth', '', NULL, 'WL:HEAD') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (unhex(replace(uuid(), '-', '')), unhex(replace('2d645926-b365-4563-8a5e-a6cbb7e1fb3b', '-', '')), 'tongue', 'SPEAK', NULL, 'NAME:mouth') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (unhex(replace(uuid(), '-', '')), unhex(replace('2d645926-b365-4563-8a5e-a6cbb7e1fb3b', '-', '')), 'lips', '', NULL, 'NAME:mouth') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_group(id, name)
VALUES (unhex(replace('2d645926-b365-4563-8a5e-a6cbb7e1fb3b', '-', '')), 'one eye') ON DUPLICATE KEY
  UPDATE name=VALUES(name);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (unhex(replace(uuid(), '-', '')), unhex(replace('2d645926-b365-4563-8a5e-a6cbb7e1fb3b', '-', '')), 'eye', 'SEE', NULL, 'WL:HEAD') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_group(id, name)
VALUES (unhex(replace('2d645926-b365-4563-8a5e-a6cbb7e1fb3b', '-', '')), 'two eyes') ON DUPLICATE KEY
  UPDATE name=VALUES(name);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (unhex(replace(uuid(), '-', '')), unhex(replace('2d645926-b365-4563-8a5e-a6cbb7e1fb3b', '-', '')), 'right eye', 'SEE', NULL, 'WL:HEAD') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (unhex(replace(uuid(), '-', '')), unhex(replace('2d645926-b365-4563-8a5e-a6cbb7e1fb3b', '-', '')), 'left eye', 'SEE', NULL, 'WL:HEAD') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_group(id, name)
VALUES (unhex(replace('2d645926-b365-4563-8a5e-a6cbb7e1fb3b', '-', '')), 'three eyes') ON DUPLICATE KEY
  UPDATE name=VALUES(name);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (unhex(replace(uuid(), '-', '')), unhex(replace('2d645926-b365-4563-8a5e-a6cbb7e1fb3b', '-', '')), 'right eye', 'SEE', NULL, 'WL:HEAD') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (unhex(replace(uuid(), '-', '')), unhex(replace('2d645926-b365-4563-8a5e-a6cbb7e1fb3b', '-', '')), 'center eye', 'SEE', NULL, 'WL:HEAD') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);

INSERT INTO body_part_template(id, body_part_group_id, name, capabilities, wear_location, connection)
VALUES (unhex(replace(uuid(), '-', '')), unhex(replace('2d645926-b365-4563-8a5e-a6cbb7e1fb3b', '-', '')), 'left eye', 'SEE', NULL, 'WL:HEAD') ON DUPLICATE KEY
  UPDATE name=VALUES(name), capabilities=VALUES(capabilities), wear_location=VALUES(wear_location), connection=VALUES(connection);
