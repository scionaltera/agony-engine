CREATE TABLE connection (
  id BINARY(16) NOT NULL,
  actor_id BINARY(16) NOT NULL,
  account VARCHAR(191) NOT NULL,
  session_username VARCHAR(191),
  session_id VARCHAR(191),
  remote_ip_address VARCHAR(16),
  disconnected_date TIMESTAMP NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB CHARACTER SET=utf8mb4, COLLATE=utf8mb4_unicode_ci;

ALTER TABLE actor ADD COLUMN connection_id BINARY(16);
ALTER TABLE actor ADD CONSTRAINT actor_connection_fk FOREIGN KEY (connection_id) REFERENCES connection (id);

INSERT INTO connection (id, actor_id, account, session_username, session_id, remote_ip_address, disconnected_date)
  SELECT uuid_bin(uuid()), id, account, session_username, session_id, remote_ip_address, disconnected_date
  FROM actor
  WHERE account IS NOT NULL;

UPDATE actor SET connection_id=(SELECT id FROM connection WHERE actor.id=connection.actor_id);

ALTER TABLE connection DROP COLUMN actor_id;
