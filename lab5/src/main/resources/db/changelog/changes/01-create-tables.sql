--liquibase formatted sql

--changeset author:2dear id:1
CREATE TABLE category (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE tag (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE pet (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category_id BIGINT,
    status VARCHAR(255),
    FOREIGN KEY (category_id) REFERENCES category(id)
);

CREATE TABLE pet_tag (
    pet_id BIGINT,
    tag_id BIGINT,
    PRIMARY KEY (pet_id, tag_id),
    FOREIGN KEY (pet_id) REFERENCES pet(id),
    FOREIGN KEY (tag_id) REFERENCES tag(id)
);