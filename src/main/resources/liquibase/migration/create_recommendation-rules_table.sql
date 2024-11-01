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
    arguments TEXT NOT null,
    negate BOOLEAN NOT null,
    PRIMARY KEY(id)
    )