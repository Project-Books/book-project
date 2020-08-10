CREATE TABLE user
(
    id          BIGINT          NOT NULL,
    username    VARCHAR(255)    NOT NULL    UNIQUE,
    email       VARCHAR(255)    NOT NULL    UNIQUE,
    password    VARCHAR(255)    NOT NULL,
    active      BOOLEAN,
    PRIMARY KEY (id)
) ENGINE = InnoDB
