CREATE TABLE reading_goal
(
    id        BIGINT  NOT NULL,
    goal_type INTEGER NOT NULL,
    target    INTEGER NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB
