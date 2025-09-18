-- 메뉴 테이블에 주문 가능 여부 컬럼 추가
ALTER TABLE menus
    ADD COLUMN IF NOT EXISTS is_available BOOLEAN;

-- 기본값 및 NOT NULL 처리 단계적 적용
UPDATE menus SET is_available = TRUE WHERE is_available IS NULL;
ALTER TABLE menus ALTER COLUMN is_available SET NOT NULL;


