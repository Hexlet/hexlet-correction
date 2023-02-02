CREATE EXTENSION "pgcrypto";

ALTER TABLE workspace
    ADD COLUMN api_access_token UUID NOT NULL DEFAULT gen_random_uuid();
