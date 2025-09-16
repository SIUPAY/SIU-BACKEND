package siu.siubackend.store.application.port.output

import siu.siubackend.store.application.port.input.SearchStoreResult
import siu.siubackend.store.domain.Location
import siu.siubackend.store.domain.Store
import java.util.*

interface StoreRepository {
    fun save(store: Store, userIdentifier: UUID): Store
    fun findById(identifier: UUID): Store?
    fun findByWalletAddress(walletAddress: String): Store?
    fun findAll(): List<Store>
    fun deleteById(identifier: UUID)
    fun searchStoresWithinDistance(location: Location, distance: Int, query: String?): List<SearchStoreResult>
}
