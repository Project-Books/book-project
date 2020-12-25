CREATE TABLE role
(
    id      BIGINT          NOT NULL,
    role    VARCHAR(255)    NOT NULL    UNIQUE,
    PRIMARY KEY (id)
) ENGINE = InnoDB
