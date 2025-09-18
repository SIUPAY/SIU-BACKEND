package siu.siubackend.order.adapter.`in`.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.util.*

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CreateOrderRequest(
    val storeIdentifier: UUID,
    val userIdentifier: UUID,
    val orderMenus: List<OrderMenuRequest>
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class OrderMenuRequest(
    val menuIdentifier: UUID,
    val quantity: Int
)
