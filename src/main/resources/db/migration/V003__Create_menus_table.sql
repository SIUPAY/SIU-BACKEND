-- 메뉴 테이블 생성 (MenuEntity 매핑과 1:1 대응)
CREATE TABLE IF NOT EXISTS menus (
    id UUID PRIMARY KEY,
    restaurant_id UUID NOT NULL,
    category_id UUID NULL,
    name VARCHAR(200) NOT NULL,
    price INTEGER NOT NULL,
    description TEXT NULL,
    image_url VARCHAR(255) NULL,
    created_at TIMESTAMPTZ NOT NULL
);

-- 조회 성능을 위한 인덱스
CREATE INDEX IF NOT EXISTS idx_menus_restaurant_id ON menus(restaurant_id);
CREATE INDEX IF NOT EXISTS idx_menus_category_id ON menus(category_id);

-- 외래키: menus.restaurant_id -> store.identifier
-- store 테이블이 존재하지 않으면 오류가 나므로, 먼저 V002를 적용하거나 수동으로 store 테이블을 만들어 주세요.
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints tc
        WHERE tc.constraint_name = 'fk_menus_store'
          AND tc.table_name = 'menus'
    ) THEN
        ALTER TABLE menus
            ADD CONSTRAINT fk_menus_store
            FOREIGN KEY (restaurant_id) REFERENCES store(identifier)
            ON UPDATE CASCADE
            ON DELETE CASCADE;
    END IF;
END $$;


