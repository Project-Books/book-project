ALTER TABLE predefined_shelf ADD user_id BIGINT NOT NULL;
ALTER TABLE predefined_shelf ADD FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE;
ALTER TABLE predefined_shelf ADD UNIQUE unique_index(user_id, shelf_name);

ALTER TABLE custom_shelf ADD user_id BIGINT NOT NULL;
ALTER TABLE custom_shelf ADD FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE;
ALTER TABLE custom_shelf ADD UNIQUE unique_index(user_id, shelf_name);
