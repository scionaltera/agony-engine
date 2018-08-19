CREATE TABLE item_info (
  id UUID NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE actor ADD COLUMN item_info_id UUID;
ALTER TABLE actor ADD CONSTRAINT actor_item_info_fk FOREIGN KEY (item_info_id) REFERENCES item_info (id);

ALTER TABLE body_part ADD COLUMN armor_id UUID;
ALTER TABLE body_part ADD CONSTRAINT body_part_armor_fk FOREIGN KEY (armor_id) REFERENCES actor (id);
