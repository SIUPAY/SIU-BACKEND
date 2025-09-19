package siu.siubackend.common.checkpoint.adapter.out.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChainEventCheckpointJpaRepository : JpaRepository<ChainEventCheckpointEntity, Long> {
    fun findByChainNameAndContractAddress(chainName: String, contractAddress: String): ChainEventCheckpointEntity?
}
