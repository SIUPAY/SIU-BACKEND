-- 카테고리 테이블
CREATE TABLE IF NOT EXISTS categories (
    id UUID PRIMARY KEY,
    name VARCHAR(200) NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS uq_categories_name ON categories(name);


