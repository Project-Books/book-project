CREATE TABLE IF NOT EXISTS bp_user (
    id          BIGSERIAL       NOT NULL PRIMARY KEY,
    username    VARCHAR(255)    NOT NULL UNIQUE,
    email       VARCHAR(255)    NOT NULL UNIQUE,
    password    VARCHAR(255)    NOT NULL,
    active      BOOLEAN
)
