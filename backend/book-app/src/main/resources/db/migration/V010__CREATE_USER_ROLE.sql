CREATE TABLE user_role (
    user_id     BIGINT  NOT NULL,
    role_id     BIGINT  NOT NULL,
    PRIMARY KEY (role_id, user_id),
    FOREIGN KEY (user_id) REFERENCES bp_user (id),
    FOREIGN KEY (role_id) REFERENCES role (id)
)
