--Insert into user table

INSERT INTO user (id,email,password,active)
  SELECT 1,'user@user.user','$2a$10$XUNDtM8r1v6pIjZFy8Ov7.qw4G9rj7DdqKcS36uVtQNWFosdnw/hu',1 FROM DUAL
WHERE NOT EXISTS
	 (SELECT id FROM user WHERE id = 1);


	ALTER SEQUENCE uid_sequence restart with 2;
