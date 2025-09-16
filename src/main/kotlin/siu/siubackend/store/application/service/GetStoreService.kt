package siu.siubackend.store.application.service

import org.springframework.stereotype.Service
import siu.siubackend.store.application.port.input.GetStoreUseCase
import siu.siubackend.store.application.port.output.StoreRepository
import siu.siubackend.store.domain.Store
import java.util.*

@Service
class GetStoreService(
    private val storeRepository: StoreRepository
) : GetStoreUseCase {

    override fun getStore(storeIdentifier: UUID): Store? {
        return storeRepository.findById(storeIdentifier)
    }
}
