CREATE TABLE player_actor_template (
  id BINARY(16) NOT NULL,
  account VARCHAR(191) NOT NULL,
  given_name VARCHAR(191) NOT NULL UNIQUE,
  PRIMARY KEY (id),
  CONSTRAINT fk_pat_users FOREIGN KEY (account) REFERENCES users (username)
) ENGINE=InnoDB CHARACTER SET=utf8mb4, COLLATE=utf8mb4_unicode_ci;
