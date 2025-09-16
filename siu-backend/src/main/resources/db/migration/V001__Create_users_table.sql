-- User 테이블 생성
CREATE TABLE "user" (
                        identifier UUID PRIMARY KEY,
                        nickname VARCHAR(100) NOT NULL,
                        profile_img_url TEXT NOT NULL,
                        oauth_user_id VARCHAR(255),
                        zklogin_salt VARCHAR(255),
                        wallet_address VARCHAR(255),
                        store_identifier UUID,
                        created_date TIMESTAMPTZ NOT NULL
);

-- 인덱스 생성
CREATE INDEX idx_users_oauth_user_id ON "user"(oauth_user_id);
CREATE INDEX idx_users_wallet_address ON "user"(wallet_address);
CREATE INDEX idx_users_store_identifier ON "user"(store_identifier);
CREATE INDEX idx_users_created_date ON "user"(created_date);

ALTER TABLE "user" ADD CONSTRAINT uk_users_wallet_address
    UNIQUE (wallet_address);