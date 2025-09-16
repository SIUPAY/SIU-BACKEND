package siu.siubackend.store.application.port.output

import siu.siubackend.store.domain.Store
import java.util.*

interface StoreRepository {
    fun save(store: Store): Store
    fun findById(identifier: UUID): Store?
    fun findByWalletAddress(walletAddress: String): Store?
    fun findAll(): List<Store>
    fun deleteById(identifier: UUID)
}
