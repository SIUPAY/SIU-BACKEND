package siu.siubackend.order.application.port.input

import siu.siubackend.order.adapter.`in`.dto.CreateOrderRequest
import siu.siubackend.order.domain.Order

interface CreateOrderUseCase {
    fun createOrder(request: CreateOrderRequest): Order
}
