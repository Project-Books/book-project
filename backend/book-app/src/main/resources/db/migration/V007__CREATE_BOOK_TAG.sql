CREATE TABLE IF NOT EXISTS book_tag (
    book_id BIGINT NOT NULL,
    tag_id  BIGINT NOT NULL,
    PRIMARY KEY (book_id, tag_id),
    FOREIGN KEY (book_id) REFERENCES book (id),
    FOREIGN KEY (tag_id) REFERENCES tag (id)
)
