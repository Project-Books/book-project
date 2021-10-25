SET MODE MySQL;

INSERT IGNORE INTO user (id, password, email, active)
VALUES (1, '$2a$10$XUNDtM8r1v6pIjZFy8Ov7.qw4G9rj7DdqKcS36uVtQNWFosdnw/hu', 'user@user.user', true);

ALTER SEQUENCE uid_sequence restart with 2;
