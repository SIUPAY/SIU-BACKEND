package siu.siubackend.common.checkpoint.domain

import java.time.OffsetDateTime

data class ChainEventCheckpoint(
    val id: Long,
    val chainName: String,
    val contractAddress: String,
    val lastProcessedBlockNumber: Long,
    val lastProcessedTimestamp: Long,
    val lastUpdated: OffsetDateTime
) {
    companion object {
        fun create(
            chainName: String,
            contractAddress: String,
            lastProcessedBlockNumber: Long = 0,
            lastProcessedTimestamp: Long = 0
        ): ChainEventCheckpoint {
            return ChainEventCheckpoint(
                id = 0, // JPA에서 자동 생성
                chainName = chainName,
                contractAddress = contractAddress,
                lastProcessedBlockNumber = lastProcessedBlockNumber,
                lastProcessedTimestamp = lastProcessedTimestamp,
                lastUpdated = OffsetDateTime.now()
            )
        }
    }
    
    fun updateProgress(blockNumber: Long, timestamp: Long): ChainEventCheckpoint {
        return this.copy(
            lastProcessedBlockNumber = blockNumber,
            lastProcessedTimestamp = timestamp,
            lastUpdated = OffsetDateTime.now()
        )
    }
}
