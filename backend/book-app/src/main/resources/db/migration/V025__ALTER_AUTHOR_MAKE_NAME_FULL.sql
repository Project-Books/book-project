ALTER TABLE author
DROP COLUMN last_name;

ALTER TABLE author
RENAME COLUMN first_name TO full_name;
