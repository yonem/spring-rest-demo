CREATE TABLE password_policy (
    id SERIAL PRIMARY KEY,
    min INT NOT NULL,
    max INT NOT NULL,
    kinds VARCHAR(4) NOT NULL,
    comb INT NOT NULL
);