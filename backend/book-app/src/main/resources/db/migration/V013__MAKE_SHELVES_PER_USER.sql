ALTER TABLE predefined_shelf ADD user_id BIGINT NOT NULL;
ALTER TABLE predefined_shelf ADD FOREIGN KEY (user_id) REFERENCES bp_user(id) ON DELETE CASCADE;
-- ALTER TABLE predefined_shelf ADD UNIQUE index unique_index(user_id, shelf_name);

ALTER TABLE user_created_shelf ADD user_id BIGINT NOT NULL;
ALTER TABLE user_created_shelf ADD FOREIGN KEY (user_id) REFERENCES bp_user(id) ON DELETE CASCADE;
-- ALTER TABLE custom_shelf ADD UNIQUE unique_index(user_id, shelf_name);
