-- team テーブルの作成
CREATE TABLE team(
    id SERIAL PRIMARY KEY
    , name VARCHAR (255) NOT NULL
);

-- member テーブルの作成
CREATE TABLE member(
    id SERIAL PRIMARY KEY
    , team_id INT NOT NULL
    , name VARCHAR (255) NOT NULL
    , CONSTRAINT fk_team FOREIGN KEY (team_id) REFERENCES team(id)
        ON
    DELETE CASCADE
);
