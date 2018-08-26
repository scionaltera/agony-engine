CREATE TABLE body_part_group (
  id UUID NOT NULL,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE body_part_template (
  id UUID NOT NULL,
  body_part_group_id UUID,
  name VARCHAR(255) NOT NULL,
  capabilities VARCHAR(255) NOT NULL,
  wear_location VARCHAR(255),
  connection VARCHAR(255),
  PRIMARY KEY (id),
  CONSTRAINT body_part_template_body_part_fk FOREIGN KEY (body_part_group_id) REFERENCES body_part_group (id)
);

ALTER TABLE body_part ADD CONSTRAINT body_part_connection_fk FOREIGN KEY (connection_id) REFERENCES body_part (id);
