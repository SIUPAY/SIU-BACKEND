package siu.siubackend.order.application.port.input

import siu.siubackend.order.domain.OrderStatus
import java.util.*

interface UpdateOrderStatusUseCase {
    fun updateOrderStatus(command: Command)
    
    data class Command(
        val orderIdentifier: UUID,
        val status: OrderStatus
    )
}
