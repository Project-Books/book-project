ALTER TABLE author
    DROP COLUMN last_name;

ALTER TABLE author
    CHANGE first_name full_name VARCHAR(255) NOT NULL;
