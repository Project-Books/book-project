ALTER TABLE book
DROP FOREIGN KEY FKklnrv3weler2ftkweewlky958,
ADD CONSTRAINT book_author_id_fk FOREIGN KEY (author_id) REFERENCES author (id);

ALTER TABLE book
DROP FOREIGN KEY FKhyaco698bbsgxf5orj1ewui2f,
ADD CONSTRAINT book_predefined_shelf_id_fk FOREIGN KEY (predefined_shelf_id) REFERENCES predefined_shelf (id);
