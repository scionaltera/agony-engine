CREATE TABLE item_info (
  id UUID NOT NULL,
  actor_id UUID NOT NULL,
  wear_locations BIGINT,
  PRIMARY KEY (id)
);

-- add columns and foreign keys for item_info and wear locations
ALTER TABLE actor ADD COLUMN item_info_id UUID;
ALTER TABLE actor ADD CONSTRAINT actor_item_info_fk FOREIGN KEY (item_info_id) REFERENCES item_info (id);

ALTER TABLE body_part ADD COLUMN armor_id UUID;
ALTER TABLE body_part ADD COLUMN wear_location VARCHAR(255);
ALTER TABLE body_part ADD CONSTRAINT body_part_armor_fk FOREIGN KEY (armor_id) REFERENCES actor (id);

-- create item_info rows for existing items and link them to the correct actors
INSERT INTO item_info (id, actor_id, wear_locations)
  SELECT uuid_generate_v4(), id, 0
  FROM actor
  WHERE connection_id IS NOT NULL;

UPDATE actor SET item_info_id=(SELECT id FROM item_info WHERE actor.id=item_info.actor_id);

ALTER TABLE item_info DROP COLUMN actor_id;
