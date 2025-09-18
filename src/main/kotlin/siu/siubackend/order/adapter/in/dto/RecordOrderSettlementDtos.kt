package siu.siubackend.order.adapter.`in`.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.util.*

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class RecordOrderSettlementRequest(
    val orderIdentifier: UUID,
    val txId: String,
    val toWalletAddress: String,
    val fromWalletAddress: String,
    val totalBrokerageFee: Double,
    val totalCryptoAmount: Double,
    val updateOrderPaymentStatus: Boolean = true
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class RecordOrderSettlementResponse(
    val orderIdentifier: UUID,
    val txId: String
)


