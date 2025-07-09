-- サンプルデータ
INSERT INTO team (id, name) VALUES (1, 'Team A');
INSERT INTO team (id, name) VALUES (2, 'Team B');

INSERT INTO member (team_id, name) VALUES (1, 'Alice');
INSERT INTO member (team_id, name) VALUES (1, 'Bob');
INSERT INTO member (team_id, name) VALUES (2, 'Charlie');
INSERT INTO member (team_id, name) VALUES (2, 'David');