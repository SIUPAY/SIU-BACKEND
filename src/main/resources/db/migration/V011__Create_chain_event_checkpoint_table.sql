-- 체인 이벤트 소비 체크포인트 테이블
-- 이벤트 처리 진행 상황을 추적하여 중복 처리 방지 및 재시작 시 이어서 처리 가능
CREATE TABLE chain_event_checkpoint (
    id BIGSERIAL PRIMARY KEY,
    chain_name VARCHAR(50) NOT NULL,
    contract_address VARCHAR(255) NOT NULL,
    last_processed_block_number BIGINT NOT NULL DEFAULT 0,
    last_processed_timestamp BIGINT NOT NULL DEFAULT 0,
    last_updated TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    
    CONSTRAINT unique_chain_contract UNIQUE (chain_name, contract_address)
);

-- 인덱스 추가
CREATE INDEX idx_chain_event_checkpoint_chain_contract ON chain_event_checkpoint (chain_name, contract_address);

-- 초기 데이터 (Sui devnet)
INSERT INTO chain_event_checkpoint (chain_name, contract_address, last_processed_block_number, last_processed_timestamp) 
VALUES ('sui', '0x807d018a1f6314da2e59a3a2df4938dea6b33e5dff945d9aee5949e69645753c', 0, 0);
