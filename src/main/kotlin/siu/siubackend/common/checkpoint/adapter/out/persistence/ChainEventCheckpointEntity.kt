package siu.siubackend.common.checkpoint.adapter.out.persistence

import jakarta.persistence.*
import siu.siubackend.common.checkpoint.domain.ChainEventCheckpoint
import java.time.OffsetDateTime

@Entity
@Table(name = "chain_event_checkpoint")
class ChainEventCheckpointEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "chain_name", nullable = false, length = 50)
    val chainName: String,

    @Column(name = "contract_address", nullable = false)
    val contractAddress: String,

    @Column(name = "last_processed_block_number", nullable = false)
    var lastProcessedBlockNumber: Long,

    @Column(name = "last_processed_timestamp", nullable = false)
    var lastProcessedTimestamp: Long,

    @Column(name = "last_updated", nullable = false)
    var lastUpdated: OffsetDateTime
) {
    fun toDomain(): ChainEventCheckpoint {
        return ChainEventCheckpoint(
            id = id,
            chainName = chainName,
            contractAddress = contractAddress,
            lastProcessedBlockNumber = lastProcessedBlockNumber,
            lastProcessedTimestamp = lastProcessedTimestamp,
            lastUpdated = lastUpdated
        )
    }

    fun updateFromDomain(domain: ChainEventCheckpoint) {
        this.lastProcessedBlockNumber = domain.lastProcessedBlockNumber
        this.lastProcessedTimestamp = domain.lastProcessedTimestamp
        this.lastUpdated = domain.lastUpdated
    }

    companion object {
        fun fromDomain(domain: ChainEventCheckpoint): ChainEventCheckpointEntity {
            return ChainEventCheckpointEntity(
                id = domain.id,
                chainName = domain.chainName,
                contractAddress = domain.contractAddress,
                lastProcessedBlockNumber = domain.lastProcessedBlockNumber,
                lastProcessedTimestamp = domain.lastProcessedTimestamp,
                lastUpdated = domain.lastUpdated
            )
        }
    }
}
