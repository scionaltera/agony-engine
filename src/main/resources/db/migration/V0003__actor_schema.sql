CREATE EXTENSION IF NOT EXISTS "citext";

CREATE TABLE player_actor_template (
  id UUID NOT NULL,
  account CITEXT NOT NULL,
  given_name VARCHAR(255) NOT NULL UNIQUE,
  PRIMARY KEY (id),
  CONSTRAINT fk_pat_users FOREIGN KEY (account) REFERENCES users (username)
);
