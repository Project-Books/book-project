-- If  there is a conflict, this test user already exists, so we can safely do nothing.
INSERT INTO bp_user (id, password, email, active)
VALUES (1, '$2a$10$XUNDtM8r1v6pIjZFy8Ov7.qw4G9rj7DdqKcS36uVtQNWFosdnw/hu', 'user@user.user', TRUE)
ON CONFLICT do nothing;

INSERT INTO role (id, role)
VALUES (1, 'USER')
ON CONFLICT do nothing;

INSERT INTO user_role(user_id, role_id)
VALUES (1, 1)
ON CONFLICT do nothing;