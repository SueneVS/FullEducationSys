CREATE TABLE IF NOT EXISTS papel
(
    id_papel SERIAL PRIMARY KEY,
    nome VARCHAR(255) UNIQUE
);