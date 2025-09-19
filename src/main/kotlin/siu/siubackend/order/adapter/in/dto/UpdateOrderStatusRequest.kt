package siu.siubackend.order.adapter.`in`.dto

import siu.siubackend.order.domain.OrderStatus

data class UpdateOrderStatusRequest(
    val status: OrderStatus
)
