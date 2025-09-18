CREATE TABLE "order" (
    identifier UUID PRIMARY KEY,
    store_identifier UUID NOT NULL,
    user_identifier UUID NOT NULL,
    status VARCHAR(50) NOT NULL,
    payment_status VARCHAR(50) NOT NULL,
    total_fiat_amount DOUBLE PRECISION NOT NULL,
    order_number INTEGER NOT NULL,
    created_date TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_order_store_identifier ON "order"(store_identifier);
CREATE INDEX idx_order_user_identifier ON "order"(user_identifier);
CREATE INDEX idx_order_status ON "order"(status);
CREATE INDEX idx_order_payment_status ON "order"(payment_status);
CREATE INDEX idx_order_created_date ON "order"(created_date);
