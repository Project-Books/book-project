ALTER TABLE book_tag
DROP FOREIGN KEY book_tag_ibfk_1,
DROP FOREIGN KEY book_tag_id_fk;

ALTER TABLE book_publisher
DROP FOREIGN KEY book_publisher_ibfk_1,
DROP FOREIGN KEY book_publisher_id_fk;

ALTER TABLE book
DROP PRIMARY KEY,
MODIFY id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT;

ALTER TABLE book_tag
ADD CONSTRAINT book_tag_id_fk FOREIGN KEY (book_id) REFERENCES book (id);

ALTER TABLE book_publisher
ADD CONSTRAINT book_publisher_id_fk FOREIGN KEY (book_id) REFERENCES book (id);
