-- slq query with schemas for tests

CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    username VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS transactions (
    id UUID PRIMARY KEY,
    user_id UUID,
    product_id UUID,
    type VARCHAR(255),
    amount INT
);

CREATE TABLE IF NOT EXISTS products (
    id UUID PRIMARY KEY,
    type VARCHAR(255)
);

CREATE TABLE if NOT EXISTS recommendations (
    id UUID,
    product_name TEXT,
    product_id UUID,
    product_text TEXT,
    PRIMARY KEY(id)
);

CREATE TABLE if NOT EXISTS rules (
    id UUID,
    query VARCHAR(255),
    negate BOOLEAN,
    recommendations_id UUID,
    PRIMARY KEY(id),
    FOREIGN KEY (recommendations_id) REFERENCES recommendations(id)
);

CREATE TABLE if NOT EXISTS stats (
    id UUID,
    count INTEGER,
    recommendations_id UUID,
    PRIMARY KEY(id),
    FOREIGN KEY (recommendations_id) REFERENCES recommendations(id)
);