CREATE TABLE book_genre (
    book_id BIGINT NOT NULL,
    genre INT NOT NULL,
    PRIMARY KEY (book_id, genre)
)
