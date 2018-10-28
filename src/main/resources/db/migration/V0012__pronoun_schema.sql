CREATE TABLE pronoun (
  subject VARCHAR(191) NOT NULL,
  object VARCHAR(191) NOT NULL,
  possessive VARCHAR(191) NOT NULL,
  possessive_pronoun VARCHAR(191) NOT NULL,
  reflexive VARCHAR(191) NOT NULL,
  PRIMARY KEY (subject)
) ENGINE=InnoDB CHARACTER SET=utf8mb4, COLLATE=utf8mb4_unicode_ci;

ALTER TABLE player_actor_template ADD COLUMN pronoun_subject VARCHAR(191);
UPDATE player_actor_template SET pronoun_subject='they' WHERE pronoun_subject IS NULL;
ALTER TABLE player_actor_template MODIFY pronoun_subject VARCHAR(191) NOT NULL;

ALTER TABLE actor ADD COLUMN pronoun_subject VARCHAR(191);
UPDATE actor SET pronoun_subject='they' WHERE pronoun_subject IS NULL;
ALTER TABLE actor MODIFY pronoun_subject VARCHAR(191) NOT NULL;
