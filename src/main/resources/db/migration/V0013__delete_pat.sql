CREATE EXTENSION IF NOT EXISTS "citext";

ALTER TABLE actor ADD COLUMN account CITEXT;

-- 1. Migrate 'account' field from PAT to Actor where an Actor exists.
UPDATE actor SET account=(SELECT account FROM player_actor_template WHERE actor.actor_template_id=player_actor_template.id);

-- 2. Create an Actor for PATs that don't have Actors (character was created but never logged in).
INSERT INTO actor (id, account, name, pronoun_subject)
  SELECT player_actor_template.id, player_actor_template.account, given_name, player_actor_template.pronoun_subject
  FROM player_actor_template LEFT JOIN actor ON player_actor_template.id=actor.actor_template_id
  WHERE actor.id IS NULL;

-- During the next deployment some instances will use the PAT table and some won't. If we drop columns and tables right
-- now then the prior version of the software will not be compatible with the database. Instead, we'll leave the field
-- and the table and deploy the new software out which simply won't use them. If something goes wrong we can still
-- roll back and have a usable database. Once everything is confirmed to be working, we can run the SQL statements
-- below to drop the unused elements from the database.

--ALTER TABLE actor DROP COLUMN actor_template_id;
--DROP TABLE player_actor_template;
