CREATE TABLE creature_info (
  id UUID NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE body_part (
  id UUID NOT NULL,
  creature_info_id UUID,
  name VARCHAR(255) NOT NULL,
  capabilities BIGINT,
  PRIMARY KEY (id),
  CONSTRAINT body_part_creature_info_fk FOREIGN KEY (creature_info_id) REFERENCES creature_info (id)
);

ALTER TABLE actor ADD COLUMN creature_info_id UUID;
ALTER TABLE actor ADD CONSTRAINT actor_creature_info_fk FOREIGN KEY (creature_info_id) REFERENCES creature_info (id);
