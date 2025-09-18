package siu.siubackend.order.adapter.`in`.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import siu.siubackend.order.domain.OrderStatus

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class UpdateOrderStatusRequest(
    val status: OrderStatus
)
