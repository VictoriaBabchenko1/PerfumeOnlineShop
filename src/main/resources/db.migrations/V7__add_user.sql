INSERT INTO users (username, email, password)
VALUES ('user', 'user@example.com', '$2a$10$hlpC40ercfiPQJ7frK3r0OrC3qm28Ei6mtTphxx7dirQV7xmLZU4a');

INSERT INTO user_roles (user_id, role_id)
VALUES ((SELECT id FROM users WHERE username = 'user'), 1);
