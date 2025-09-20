package siu.siubackend.order.domain.event

import java.util.*

data class OrderCreatedEvent(
    val orderId: UUID,
    val storeId: UUID
)
