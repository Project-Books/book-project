ALTER TABLE user_role
DROP FOREIGN KEY user_role_ibfk_1;

ALTER TABLE predefined_shelf
DROP FOREIGN KEY predefined_shelf_ibfk_1;

ALTER TABLE user_created_shelf
DROP FOREIGN KEY user_created_shelf_ibfk_1;

ALTER TABLE user
DROP PRIMARY KEY,
MODIFY id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT;

ALTER TABLE user_role
ADD CONSTRAINT user_role_user_fk FOREIGN KEY (user_id) REFERENCES user (id);

ALTER TABLE predefined_shelf
ADD CONSTRAINT user_id_fk FOREIGN KEY (user_id) REFERENCES user (id);

ALTER TABLE user_created_shelf
ADD CONSTRAINT user_created_shelf_user_id_fk FOREIGN KEY (user_id) REFERENCES user (id)
