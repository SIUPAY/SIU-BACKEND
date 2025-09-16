package siu.siubackend.store.adapter.out.persistence

import org.springframework.stereotype.Repository
import siu.siubackend.store.application.port.output.StoreRepository
import siu.siubackend.store.domain.Store
import java.util.*

@Repository
class StoreRepositoryImpl(
    private val storeJpaRepository: StoreJpaRepository
) : StoreRepository {

    override fun save(store: Store, userIdentifier: UUID): Store {
        val storeEntity = store.toEntity(userIdentifier)
        val savedEntity = storeJpaRepository.save(storeEntity)
        return savedEntity.toDomain()
    }

    override fun findById(identifier: UUID): Store? {
        return storeJpaRepository.findById(identifier)
            .map { it.toDomain() }
            .orElse(null)
    }

    override fun findByWalletAddress(walletAddress: String): Store? {
        return storeJpaRepository.findByWalletAddress(walletAddress)?.toDomain()
    }

    override fun findAll(): List<Store> {
        return storeJpaRepository.findAll().map { it.toDomain() }
    }

    override fun deleteById(identifier: UUID) {
        storeJpaRepository.deleteById(identifier)
    }
}
