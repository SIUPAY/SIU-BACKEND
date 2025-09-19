package siu.siubackend.order.adapter.`in`.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import siu.siubackend.order.domain.OrderStatus

data class UpdateOrderStatusRequest @JsonCreator constructor(
    @JsonProperty("status") val status: OrderStatus
)
