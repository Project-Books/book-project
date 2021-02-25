ALTER TABLE book
DROP FOREIGN KEY book_predefined_shelf_id_fk;

ALTER TABLE predefined_shelf
DROP PRIMARY KEY,
MODIFY id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT;

ALTER TABLE book
ADD CONSTRAINT book_predefined_shelf_id_fk FOREIGN KEY (predefined_shelf_id) REFERENCES predefined_shelf (id);