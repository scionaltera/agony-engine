CREATE EXTENSION IF NOT EXISTS "citext";
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE connection (
  id UUID NOT NULL,
  account CITEXT NOT NULL,
  session_username VARCHAR(255),
  session_id VARCHAR(255),
  remote_ip_address VARCHAR(16),
  disconnected_date TIMESTAMP
);

ALTER TABLE actor ADD COLUMN connection_id UUID;
ALTER TABLE actor ADD CONSTRAINT actor_connection_fk FOREIGN KEY (connection_id) REFERENCES connection (id);

INSERT INTO connection (id, account, session_username, session_id, remote_ip_address, disconnected_date)
  SELECT uuid_generate_v4(), account, session_username, session_id, remote_ip_address, disconnected_date
  FROM actor
  WHERE account IS NOT NULL;
