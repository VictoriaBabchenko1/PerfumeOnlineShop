INSERT INTO users (username, email, password)
VALUES ('admin', 'admin@example.com', '$2a$10$iHnfOwR1pKC502dbihQywu0VZAakZO5.thXPcfB7ccerF4FG8TsGa');

INSERT INTO user_roles (user_id, role_id)
VALUES ((SELECT id FROM users WHERE username = 'admin'), 1);

INSERT INTO user_roles (user_id, role_id)
VALUES ((SELECT id FROM users WHERE username = 'admin'), 2);
