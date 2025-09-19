CREATE TABLE zklogin_salts (
    identifier UUID PRIMARY KEY,
    oauth_user_id VARCHAR NOT NULL UNIQUE,
    zklogin_salt VARCHAR NOT NULL
);

CREATE INDEX idx_zklogin_salts_oauth_user_id ON zklogin_salts(oauth_user_id);
