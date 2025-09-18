package siu.siubackend.order.application.port.input

import siu.siubackend.order.adapter.`in`.dto.SearchOrderRequest
import siu.siubackend.order.domain.OrderWithDetails

interface SearchOrderUseCase {
    fun searchOrders(request: SearchOrderRequest): List<OrderWithDetails>
}
