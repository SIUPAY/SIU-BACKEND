-- 카테고리 테이블에 매장 연관 및 추가 필드 확장
ALTER TABLE categories
    ADD COLUMN IF NOT EXISTS store_identifier UUID,
    ADD COLUMN IF NOT EXISTS description TEXT,
    ADD COLUMN IF NOT EXISTS display_order INTEGER,
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMPTZ;

-- 기존 데이터 호환: created_at 기본값 및 NOT NULL 설정을 두 단계로 처리
UPDATE categories SET created_at = NOW() WHERE created_at IS NULL;
ALTER TABLE categories ALTER COLUMN created_at SET NOT NULL;

-- store_identifier는 NOT NULL + FK 제약 (기존 데이터가 있다면 임시로 허용 후 수동 보정 필요)
-- 우선 인덱스/외래키부터 추가하고, 마지막에 NOT NULL 처리
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints tc
        WHERE tc.constraint_name = 'fk_categories_store'
          AND tc.table_name = 'categories'
    ) THEN
        ALTER TABLE categories
            ADD CONSTRAINT fk_categories_store
            FOREIGN KEY (store_identifier) REFERENCES store(identifier)
            ON UPDATE CASCADE
            ON DELETE CASCADE;
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_categories_store_identifier ON categories(store_identifier);

-- 고유 제약: (store_identifier, name) 기준으로 유니크 처리
DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM pg_indexes WHERE indexname = 'uq_categories_name'
    ) THEN
        DROP INDEX uq_categories_name;
    END IF;
END $$;

CREATE UNIQUE INDEX IF NOT EXISTS uq_categories_store_name ON categories(store_identifier, name);

-- 마지막으로 NOT NULL 강제 (데이터 보정이 끝났다는 가정)
ALTER TABLE categories ALTER COLUMN store_identifier SET NOT NULL;


