-- Delete all the body part templates and groups.
-- They will get recreated by the repeatable migration.
ALTER TABLE body_part_template DROP FOREIGN KEY body_part_template_body_part_fk;

TRUNCATE TABLE body_part_template;
TRUNCATE TABLE body_part_group;

ALTER TABLE body_part_template ADD CONSTRAINT body_part_template_body_part_fk FOREIGN KEY (body_part_group_id) REFERENCES body_part_group (id);
