ALTER TABLE book
    ADD CONSTRAINT FKhyaco698bbsgxf5orj1ewui2f FOREIGN KEY (predefined_shelf_id) REFERENCES predefined_shelf (id);
