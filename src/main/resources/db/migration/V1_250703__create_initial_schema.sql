-- users テーブルの作成
CREATE TABLE users (
    id SERIAL PRIMARY KEY
    , username VARCHAR (50) NOT NULL UNIQUE
    , email VARCHAR (100) NOT NULL
    , password VARCHAR (100) NOT NULL
    , roles INTEGER NOT NULL
    , created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- products テーブルの作成
CREATE TABLE products(
    id SERIAL PRIMARY KEY
    , name VARCHAR (100) NOT NULL
    , price NUMERIC (10, 2) NOT NULL
    , description TEXT
    , created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
