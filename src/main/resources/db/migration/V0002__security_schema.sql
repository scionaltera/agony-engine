CREATE TABLE users (
  username VARCHAR(191) NOT NULL PRIMARY KEY,
  password VARCHAR(191) NOT NULL,
  enabled  BOOLEAN NOT NULL
) ENGINE=InnoDB CHARACTER SET=utf8mb4, COLLATE=utf8mb4_unicode_ci;

CREATE TABLE authorities (
  username VARCHAR(191) NOT NULL,
  authority VARCHAR(191) NOT NULL,
  CONSTRAINT fk_authorities_users FOREIGN KEY (username) REFERENCES users (username)
) ENGINE=InnoDB CHARACTER SET=utf8mb4, COLLATE=utf8mb4_unicode_ci;

CREATE UNIQUE INDEX ix_auth_username
  ON authorities (username, authority);
