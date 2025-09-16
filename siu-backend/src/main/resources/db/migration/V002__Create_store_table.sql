-- Enable PostGIS extension
CREATE EXTENSION IF NOT EXISTS postgis;

CREATE TABLE store (
    identifier UUID PRIMARY KEY,
    user_identifier UUID NOT NULL,
    name VARCHAR NOT NULL,
    address VARCHAR NOT NULL,
    phone VARCHAR,
    profile_img_url VARCHAR NOT NULL,
    wallet_address VARCHAR NOT NULL,
    location geography(Point, 4326) NOT NULL,
    total_order_count INTEGER NOT NULL DEFAULT 0,
    created_date TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_stores_user_identifier ON store(user_identifier);
CREATE INDEX idx_stores_wallet_address ON store(wallet_address);
CREATE INDEX idx_stores_location ON store USING GIST(location);
