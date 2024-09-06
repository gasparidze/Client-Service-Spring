--liquibase formatted sql

--changeset agasparyan:1
CREATE SCHEMA IF NOT EXISTS client_service;

--changeset agasparyan:2
CREATE TABLE IF NOT EXISTS client_service.client
(
    id         SERIAL PRIMARY KEY,
    fio        VARCHAR(100)       NOT NULL,
    birth_date DATE               NOT NULL,
    login      VARCHAR(50) UNIQUE NOT NULL,
    password   VARCHAR(128)       NOT NULL,
    phone_numbers     VARCHAR[] NOT NULL,
    emails     VARCHAR[] NOT NULL
);

--changeset agasparyan:3
CREATE TABLE IF NOT EXISTS client_service.client_account
(
    id SERIAL PRIMARY KEY,
    balance   NUMERIC NOT NULL CHECK (balance > 0),
    client_id INT NOT NULL UNIQUE REFERENCES client_service.client (id) ON DELETE CASCADE
);