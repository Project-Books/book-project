CREATE TABLE book_publisher (
    book_id BIGINT NOT NULL,
    publisher_id  BIGINT NOT NULL,
    PRIMARY KEY (book_id, publisher_id),
    FOREIGN KEY (book_id) REFERENCES book (id),
    FOREIGN KEY (publisher_id) REFERENCES publisher (id)
)
