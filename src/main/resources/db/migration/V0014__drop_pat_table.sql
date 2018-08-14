-- When we deploy this, all the deployed instances of the software will be ones that
-- don't use this column and table, so they are safe to delete now.

ALTER TABLE actor DROP COLUMN actor_template_id;
DROP TABLE player_actor_template;
