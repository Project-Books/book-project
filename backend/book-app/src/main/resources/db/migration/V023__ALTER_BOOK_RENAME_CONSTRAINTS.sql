ALTER TABLE book
DROP CONSTRAINT IF EXISTS FKhyaco698bbsgxf5orj1ewui2f,
ADD CONSTRAINT book_predefined_shelf_id_fk FOREIGN KEY (predefined_shelf_id) REFERENCES predefined_shelf (id);
