package siu.siubackend.store.application.port.input

import siu.siubackend.store.domain.Location

data class SearchStoreRequest(
    val location: Location,
    val distance: Int,
    val query: String?
)

data class SearchStoreResult(
    val store: siu.siubackend.store.domain.Store,
    val distance: Double
)

interface SearchStoreUseCase {
    fun searchStores(request: SearchStoreRequest): List<SearchStoreResult>
}
