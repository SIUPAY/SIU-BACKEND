package siu.siubackend.store.adapter.`in`.event

import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import siu.siubackend.order.domain.event.OrderCreatedEvent
import siu.siubackend.store.application.port.input.IncreaseTotalOrderCountUseCase

@Component
class StoreInternalEventListener(
    private val increaseTotalOrderCountUseCase: IncreaseTotalOrderCountUseCase
) {
    
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun handleOrderCreatedEvent(event: OrderCreatedEvent) {
        increaseTotalOrderCountUseCase.increaseTotalOrderCount(event.storeId)
    }
}
