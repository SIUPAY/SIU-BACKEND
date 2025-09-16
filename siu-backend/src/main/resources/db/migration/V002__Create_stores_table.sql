-- Enable PostGIS extension
CREATE EXTENSION IF NOT EXISTS postgis;

CREATE TABLE stores (
    identifier UUID PRIMARY KEY,
    user_identifier UUID NOT NULL,
    name VARCHAR NOT NULL,
    address VARCHAR NOT NULL,
    phone VARCHAR,
    profile_img_url VARCHAR NOT NULL,
    wallet_address VARCHAR NOT NULL,
    location geography(Point, 4326),
    created_date TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_stores_user_identifier ON stores(user_identifier);
CREATE INDEX idx_stores_wallet_address ON stores(wallet_address);
CREATE INDEX idx_stores_location ON stores USING GIST(location);
