CREATE TABLE order_menu (
    identifier UUID PRIMARY KEY,
    order_identifier UUID NOT NULL,
    menu_identifier UUID NOT NULL,
    quantity INTEGER NOT NULL,
    total_fiat_amount INTEGER NOT NULL,
    created_date TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_order_menu_order_identifier ON order_menu(order_identifier);
CREATE INDEX idx_order_menu_menu_identifier ON order_menu(menu_identifier);
CREATE INDEX idx_order_menu_created_date ON order_menu(created_date);
