CREATE TABLE role (
    id      BIGSERIAL       NOT NULL PRIMARY KEY,
    role    VARCHAR(255)    NOT NULL UNIQUE
)
