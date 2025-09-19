package siu.siubackend.order.adapter.`in`.dto

import siu.siubackend.order.domain.OrderStatus
import siu.siubackend.order.domain.PaymentStatus
import java.time.OffsetDateTime
import java.util.*

data class SearchOrderResponse(
    val orders: List<OrderSummaryResponse>
)

data class OrderSummaryResponse(
    val identifier: UUID,
    val storeIdentifier: UUID,
    val userIdentifier: UUID,
    val status: OrderStatus,
    val paymentStatus: PaymentStatus,
    val totalFiatAmount: Double,
    val orderNumber: Int,
    val createdDate: OffsetDateTime,
    val orderMenus: List<OrderMenuSummaryResponse>
)

data class OrderMenuSummaryResponse(
    val identifier: UUID,
    val orderIdentifier: UUID,
    val menuIdentifier: UUID,
    val menuName: String,
    val quantity: Int,
    val totalFiatAmount: Int,
    val createdDate: OffsetDateTime
)
