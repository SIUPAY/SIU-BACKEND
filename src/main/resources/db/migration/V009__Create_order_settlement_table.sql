CREATE TABLE order_settlement (
    identifier UUID PRIMARY KEY,
    order_identifier UUID NOT NULL,
    tx_id VARCHAR(255) NOT NULL,
    to_wallet_address VARCHAR(255) NOT NULL,
    from_wallet_address VARCHAR(255) NOT NULL,
    total_brokerage_fee DOUBLE PRECISION NOT NULL,
    total_crypto_amount DOUBLE PRECISION NOT NULL,
    created_date TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE UNIQUE INDEX ux_order_settlement_tx_id ON order_settlement (tx_id);
CREATE INDEX ix_order_settlement_order_identifier ON order_settlement (order_identifier);


