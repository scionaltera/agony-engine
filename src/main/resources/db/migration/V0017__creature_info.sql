CREATE TABLE creature_info (
  id BINARY(16) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB CHARACTER SET=utf8mb4, COLLATE=utf8mb4_unicode_ci;

CREATE TABLE body_part (
  id BINARY(16) NOT NULL,
  creature_info_id BINARY(16),
  name VARCHAR(191) NOT NULL,
  capabilities BIGINT,
  PRIMARY KEY (id),
  CONSTRAINT body_part_creature_info_fk FOREIGN KEY (creature_info_id) REFERENCES creature_info (id)
) ENGINE=InnoDB CHARACTER SET=utf8mb4, COLLATE=utf8mb4_unicode_ci;

ALTER TABLE actor ADD COLUMN creature_info_id BINARY(16);
ALTER TABLE actor ADD CONSTRAINT actor_creature_info_fk FOREIGN KEY (creature_info_id) REFERENCES creature_info (id);
