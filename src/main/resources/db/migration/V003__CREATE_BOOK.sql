CREATE TABLE book
(
    id                    BIGINT       NOT NULL,
    book_recommended_by   VARCHAR(255),
    custom_shelf_id       BIGINT,
    date_finished_reading DATE,
    date_started_reading  DATE,
    edition               VARCHAR(255),
    book_genre            INTEGER,
    number_of_pages       INTEGER,
    pages_read            INTEGER,
    predefined_shelf_id   BIGINT,
    rating                INTEGER,
    series_position       INTEGER,
    book_review           VARCHAR(1500),
    title                 VARCHAR(255) NOT NULL,
    author_id             BIGINT,
    PRIMARY KEY (id)
) ENGINE = InnoDB
