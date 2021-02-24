ALTER TABLE book_publisher
DROP FOREIGN KEY book_publisher_ibfk_2;

ALTER TABLE publisher
DROP PRIMARY KEY,
MODIFY id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT;

ALTER TABLE book_publisher
ADD CONSTRAINT book_publisher_id_fk FOREIGN KEY (publisher_id) REFERENCES publisher (id);