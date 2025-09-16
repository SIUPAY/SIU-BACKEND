package siu.siubackend.store.adapter.out.persistence

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface StoreJpaRepository : JpaRepository<StoreEntity, UUID> {
    fun findByWalletAddress(walletAddress: String): StoreEntity?
}
