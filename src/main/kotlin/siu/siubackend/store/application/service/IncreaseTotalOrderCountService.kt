package siu.siubackend.store.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import siu.siubackend.store.application.port.input.IncreaseTotalOrderCountUseCase
import siu.siubackend.store.application.port.output.StoreRepository
import java.util.*

@Service
@Transactional
class IncreaseTotalOrderCountService(
    private val storeRepository: StoreRepository
) : IncreaseTotalOrderCountUseCase {
    
    override fun increaseTotalOrderCount(storeId: UUID) {
        val store = storeRepository.findById(storeId)
            ?: throw IllegalArgumentException("Store not found: $storeId")
        
        val updatedStore = store.incrementOrderCount()
        storeRepository.save(updatedStore)
    }
}
