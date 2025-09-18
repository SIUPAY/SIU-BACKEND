package siu.siubackend.order.adapter.`in`.dto

import java.util.*

data class CreateOrderRequest(
    val storeIdentifier: UUID,
    val userIdentifier: UUID,
    val orderMenus: List<OrderMenuRequest>
)

data class OrderMenuRequest(
    val menuIdentifier: UUID,
    val quantity: Int
)
