create table book_genre (
    book_id bigint not null,
    genre int not null,
    primary key (book_id, genre)
) ENGINE = InnoDB