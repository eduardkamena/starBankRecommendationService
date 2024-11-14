-- liquibase formatted sql

-- changeset ekamenskikh:1
CREATE TABLE if NOT EXISTS recommendations (
    id UUID NOT null,
    product_name TEXT NOT null,
    product_id UUID NOT null,
    product_text TEXT NOT null,
    PRIMARY KEY(id)
    )

-- changeset ekamenskikh:2
CREATE TABLE if NOT EXISTS rules (
    id UUID NOT null,
    query VARCHAR(255) NOT null,
    negate BOOLEAN NOT null,
    PRIMARY KEY(id)
    )

-- changeset ekamenskikh:3
CREATE TABLE stats (
    id UUID NOT null,
    count INTEGER NOT null,
    recommendations_id UUID NOT null ,
    PRIMARY KEY(id),
    FOREIGN KEY (recommendations_id) REFERENCES recommendations(id)
    )