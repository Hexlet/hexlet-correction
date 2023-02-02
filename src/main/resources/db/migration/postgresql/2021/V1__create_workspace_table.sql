CREATE SEQUENCE common_seq_id INCREMENT BY 50 START WITH 1;

CREATE TABLE workspace
(
    id            BIGINT PRIMARY KEY,
    created_by    VARCHAR(255)                NOT NULL,
    created_date  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    modified_by   VARCHAR(255)                NOT NULL,
    modified_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    name          VARCHAR(20)                 NOT NULL UNIQUE,
    url           VARCHAR(255)                NOT NULL UNIQUE,
    description   VARCHAR(1000)
);

