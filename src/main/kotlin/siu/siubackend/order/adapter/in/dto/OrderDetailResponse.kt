package siu.siubackend.order.adapter.`in`.dto

import siu.siubackend.order.domain.*
import java.time.OffsetDateTime
import java.util.*

data class OrderDetailResponse(
    val order: OrderResponse,
    val storeName: String,
    val orderMenus: List<OrderMenuDetailResponse>,
    val orderSettlement: OrderSettlementResponse?
)

data class OrderResponse(
    val identifier: UUID,
    val storeIdentifier: UUID,
    val userIdentifier: UUID,
    val status: OrderStatus,
    val paymentStatus: PaymentStatus,
    val totalFiatAmount: Double,
    val orderNumber: Int,
    val createdDate: OffsetDateTime
)

data class OrderMenuDetailResponse(
    val identifier: UUID,
    val orderIdentifier: UUID,
    val menuIdentifier: UUID,
    val quantity: Int,
    val totalFiatAmount: Int,
    val menuName: String,
    val createdDate: OffsetDateTime
)

data class OrderSettlementResponse(
    val identifier: UUID,
    val orderIdentifier: UUID,
    val txId: String,
    val toWalletAddress: String,
    val fromWalletAddress: String,
    val totalBrokerageFee: Double,
    val totalCryptoAmount: Double,
    val createdDate: OffsetDateTime
)
