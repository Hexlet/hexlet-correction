CREATE TABLE typo
(
    id               BIGINT                      NOT NULL PRIMARY KEY,
    created_by       VARCHAR(255)                NOT NULL,
    created_date     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    modified_by      VARCHAR(255)                NOT NULL,
    modified_date    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    page_url         VARCHAR(1000)               NOT NULL,
    reporter_name    VARCHAR(50)                 NOT NULL,
    reporter_comment VARCHAR(200),
    text_before_typo VARCHAR(100),
    text_typo        VARCHAR(1000)               NOT NULL,
    text_after_typo  VARCHAR(100),
    typo_status      VARCHAR(50)                 NOT NULL,
    workspace_id     BIGINT
);

ALTER TABLE typo
    ADD FOREIGN KEY (workspace_id) REFERENCES workspace (id);
