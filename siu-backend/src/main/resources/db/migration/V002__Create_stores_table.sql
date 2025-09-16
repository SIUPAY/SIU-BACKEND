CREATE TABLE store (
    identifier UUID PRIMARY KEY,
    name VARCHAR NOT NULL,
    address VARCHAR NOT NULL,
    phone VARCHAR,
    profile_img_url VARCHAR NOT NULL,
    wallet_address VARCHAR NOT NULL,
    created_date TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_stores_wallet_address ON store(wallet_address);
