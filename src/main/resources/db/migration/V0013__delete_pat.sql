CREATE EXTENSION IF NOT EXISTS "citext";

ALTER TABLE actor ADD COLUMN account CITEXT;

-- 1. migrate 'account' field from PAT to Actor where an Actor exists
UPDATE actor SET account=(SELECT account FROM player_actor_template WHERE actor.actor_template_id=player_actor_template.id);

-- 2. create an Actor for PATs that don't have Actors (character was created but never logged in)
INSERT INTO actor (account, name, pronoun_subject)
  SELECT player_actor_template.account, given_name, player_actor_template.pronoun_subject
  FROM player_actor_template LEFT JOIN actor ON player_actor_template.id=actor.actor_template_id
  WHERE actor.id IS NULL;

ALTER TABLE actor DROP COLUMN actor_template_id;
DROP TABLE player_actor_template;
