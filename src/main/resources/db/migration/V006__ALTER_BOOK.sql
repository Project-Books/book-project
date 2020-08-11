ALTER TABLE book
    ADD CONSTRAINT FKklnrv3weler2ftkweewlky958 FOREIGN KEY (author_id) REFERENCES author (id);
ALTER TABLE book
    ADD CONSTRAINT FKhyaco698bbsgxf5orj1ewui2f FOREIGN KEY (predefined_shelf_id) REFERENCES predefined_shelf (id);
