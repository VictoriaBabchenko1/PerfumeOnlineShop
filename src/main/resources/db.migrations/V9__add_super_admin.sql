INSERT INTO users (username, email, password)
VALUES ('super_admin', 'super_admin@example.com', '$2a$10$CWBUkxKB9hmULrtMOgv.7edLk6UBa4cEPex95F/waSfoO7GCgwXpe');

INSERT INTO user_roles (user_id, role_id)
VALUES ((SELECT id FROM users WHERE username = 'super_admin'), 1);

INSERT INTO user_roles (user_id, role_id)
VALUES ((SELECT id FROM users WHERE username = 'super_admin'), 2);

INSERT INTO user_roles (user_id, role_id)
VALUES ((SELECT id FROM users WHERE username = 'super_admin'), 3);
