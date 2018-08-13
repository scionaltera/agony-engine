CREATE TABLE pronoun (
  subject VARCHAR(255) NOT NULL,
  object VARCHAR(255) NOT NULL,
  possessive VARCHAR(255) NOT NULL,
  possessive_pronoun VARCHAR(255) NOT NULL,
  reflexive VARCHAR(255) NOT NULL,
  PRIMARY KEY (subject)
);

ALTER TABLE player_actor_template ADD COLUMN pronoun_subject VARCHAR(255);
UPDATE player_actor_template SET pronoun_subject='they' WHERE pronoun_subject IS NULL;
ALTER TABLE player_actor_template ALTER COLUMN pronoun_subject SET NOT NULL;

ALTER TABLE actor ADD COLUMN pronoun_subject VARCHAR(255);
UPDATE actor SET pronoun_subject='they' WHERE pronoun_subject IS NULL;
ALTER TABLE actor ALTER COLUMN pronoun_subject SET NOT NULL;
