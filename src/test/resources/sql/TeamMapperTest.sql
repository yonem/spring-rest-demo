ALTER TABLE team ALTER COLUMN id RESTART WITH 10;

INSERT INTO team (id, name) VALUES (1, 'Team A');
INSERT INTO member (team_id, name) VALUES (1, 'Hoge');
