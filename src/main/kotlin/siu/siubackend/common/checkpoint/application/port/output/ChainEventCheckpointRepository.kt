package siu.siubackend.common.checkpoint.application.port.output

import siu.siubackend.common.checkpoint.domain.ChainEventCheckpoint

interface ChainEventCheckpointRepository {
    fun findByChainAndContract(chainName: String, contractAddress: String): ChainEventCheckpoint?
    fun save(checkpoint: ChainEventCheckpoint): ChainEventCheckpoint
    fun saveOrUpdate(checkpoint: ChainEventCheckpoint): ChainEventCheckpoint
}
