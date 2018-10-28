CREATE TABLE body_part_group (
  id BINARY(16) NOT NULL,
  name VARCHAR(191) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB CHARACTER SET=utf8mb4, COLLATE=utf8mb4_unicode_ci;

CREATE TABLE body_part_template (
  id BINARY(16) NOT NULL,
  body_part_group_id BINARY(16),
  name VARCHAR(191) NOT NULL,
  capabilities VARCHAR(191) NOT NULL,
  wear_location VARCHAR(191),
  connection VARCHAR(191),
  PRIMARY KEY (id),
  CONSTRAINT body_part_template_body_part_fk FOREIGN KEY (body_part_group_id) REFERENCES body_part_group (id)
) ENGINE=InnoDB CHARACTER SET=utf8mb4, COLLATE=utf8mb4_unicode_ci;

ALTER TABLE body_part ADD CONSTRAINT body_part_connection_fk FOREIGN KEY (connection_id) REFERENCES body_part (id);
