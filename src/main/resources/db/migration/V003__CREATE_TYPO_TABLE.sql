CREATE TABLE typo
(
    id               INT8          NOT NULL,
    created_by       VARCHAR(255)  NOT NULL,
    created_date     TIMESTAMP     NOT NULL,
    modified_by      VARCHAR(255)  NOT NULL,
    modified_date    TIMESTAMP     NOT NULL,
    page_url         VARCHAR(1000) NOT NULL,
    reporter_comment VARCHAR(200),
    reporter_name    VARCHAR(50)   NOT NULL,
    text_after_typo  VARCHAR(100),
    text_before_typo VARCHAR(100),
    text_typo        VARCHAR(1000) NOT NULL,
    typo_status      TYPO_STATUS   NOT NULL,
    PRIMARY KEY (id)
);
