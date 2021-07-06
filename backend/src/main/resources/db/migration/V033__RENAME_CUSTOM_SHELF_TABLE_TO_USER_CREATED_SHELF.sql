RENAME TABLE custom_shelf TO user_created_shelf;

ALTER TABLE book
    CHANGE custom_shelf_id user_created_shelf_id BIGINT;
