CREATE TABLE item_info (
  id UUID NOT NULL,
  PRIMARY KEY (id)
);

-- blow away existing bodies so they get re-generated with all the new parts
UPDATE actor SET creature_info_id = NULL;
DELETE FROM body_part;
DELETE FROM creature_info;

ALTER TABLE actor ADD COLUMN item_info_id UUID;
ALTER TABLE actor ADD CONSTRAINT actor_item_info_fk FOREIGN KEY (item_info_id) REFERENCES item_info (id);

ALTER TABLE body_part ADD COLUMN armor_id UUID;
ALTER TABLE body_part ADD CONSTRAINT body_part_armor_fk FOREIGN KEY (armor_id) REFERENCES actor (id);
