CREATE TABLE account
(
    id               BIGINT                      NOT NULL PRIMARY KEY,
    created_by       VARCHAR(255)                NOT NULL,
    created_date     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    modified_by      VARCHAR(255)                NOT NULL,
    modified_date    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    external_open_id VARCHAR(20),
    auth_provider    VARCHAR(50)                 NOT NULL,
    email            VARCHAR(255) UNIQUE,
    first_name       VARCHAR(50),
    last_name        VARCHAR(50),
    password         VARCHAR(100),
    username         VARCHAR(20) UNIQUE,
    workspace_id     BIGINT
);

ALTER TABLE typo
    ADD COLUMN account_id BIGINT;
ALTER TABLE typo
    ADD CONSTRAINT fk_typo_on_account FOREIGN KEY (account_id) REFERENCES account (id);

ALTER TABLE account
    ADD CONSTRAINT fk_account_on_workspace FOREIGN KEY (workspace_id) REFERENCES workspace (id);
