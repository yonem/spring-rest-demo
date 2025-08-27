DELETE FROM users;
DELETE FROM products;
DELETE FROM password_policy;

INSERT INTO users (id, username, email, password, roles) VALUES
-- パスワードは 'password' をbcryptでハッシュ化したもの
(1, 'admin', 'admin@example.com', '$2a$10$/hI4MfaHJLYdcc//0A/XB.hA1uIiB0roXwtYd./46KNHUqn0vlDfy', 1),
(2, 'user', 'user@example.com', '$2a$10$/hI4MfaHJLYdcc//0A/XB.hA1uIiB0roXwtYd./46KNHUqn0vlDfy', 2)
;

-- products テーブルにいくつかのサンプルデータを挿入
INSERT INTO products (name, price, description) VALUES
('Laptop Pro', 1200.00, 'Powerful laptop for professionals.'),
('Wireless Mouse', 25.50, 'Ergonomic wireless mouse.'),
('USB-C Hub', 49.99, 'Multi-port USB-C adapter.');

INSERT INTO password_policy (id, min, max, kinds, comb) VALUES
(1, 4, 8, 'lusd', 2);
