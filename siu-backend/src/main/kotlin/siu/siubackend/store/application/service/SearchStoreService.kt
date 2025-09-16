package siu.siubackend.store.application.service

import org.springframework.stereotype.Service
import siu.siubackend.store.application.port.input.SearchStoreRequest
import siu.siubackend.store.application.port.input.SearchStoreResult
import siu.siubackend.store.application.port.input.SearchStoreUseCase
import siu.siubackend.store.application.port.output.StoreRepository

@Service
class SearchStoreService(
    private val storeRepository: StoreRepository
) : SearchStoreUseCase {

    override fun searchStores(request: SearchStoreRequest): List<SearchStoreResult> {
        return storeRepository.searchStoresWithinDistance(
            location = request.location,
            distance = request.distance,
            query = request.query
        )
    }
}
