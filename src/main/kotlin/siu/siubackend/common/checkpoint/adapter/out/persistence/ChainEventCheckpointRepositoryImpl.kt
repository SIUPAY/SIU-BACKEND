package siu.siubackend.common.checkpoint.adapter.out.persistence

import org.springframework.stereotype.Repository
import siu.siubackend.common.checkpoint.application.port.output.ChainEventCheckpointRepository
import siu.siubackend.common.checkpoint.domain.ChainEventCheckpoint

@Repository
class ChainEventCheckpointRepositoryImpl(
    private val jpaRepository: ChainEventCheckpointJpaRepository
) : ChainEventCheckpointRepository {

    override fun findByChainAndContract(chainName: String, contractAddress: String): ChainEventCheckpoint? {
        return jpaRepository.findByChainNameAndContractAddress(chainName, contractAddress)?.toDomain()
    }

    override fun save(checkpoint: ChainEventCheckpoint): ChainEventCheckpoint {
        val entity = ChainEventCheckpointEntity.fromDomain(checkpoint)
        return jpaRepository.save(entity).toDomain()
    }

    override fun saveOrUpdate(checkpoint: ChainEventCheckpoint): ChainEventCheckpoint {
        val existing = jpaRepository.findByChainNameAndContractAddress(
            checkpoint.chainName, 
            checkpoint.contractAddress
        )
        
        return if (existing != null) {
            existing.updateFromDomain(checkpoint)
            jpaRepository.save(existing).toDomain()
        } else {
            save(checkpoint)
        }
    }
}
