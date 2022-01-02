INSERT IGNORE INTO user (id, password, email, active)
VALUES (1, '$2a$10$XUNDtM8r1v6pIjZFy8Ov7.qw4G9rj7DdqKcS36uVtQNWFosdnw/hu', 'user@user.user', TRUE);

INSERT IGNORE INTO role (id, role)
VALUES (1, 'USER');

INSERT IGNORE INTO user_role(user_id, role_id)
VALUES (1, 1);