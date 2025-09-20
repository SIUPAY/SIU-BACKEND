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
    val totalBrokerageFee: Double, // SUI 단위로 입력받음
    val totalCryptoAmount: Double, // SUI 단위로 입력받음
    val updateOrderPaymentStatus: Boolean = true,
    val unit: CurrencyUnit = CurrencyUnit.SUI // 기본값은 SUI 단위
) {
    enum class CurrencyUnit {
        SUI, MIST
    }
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class RecordOrderSettlementFromSuiEventRequest(
    val orderIdentifier: UUID,
    val txId: String,
    val toWalletAddress: String,
    val fromWalletAddress: String,
    val totalBrokerageFeeInMist: Double, // MIST 단위로 명시
    val totalCryptoAmountInMist: Double, // MIST 단위로 명시
    val updateOrderPaymentStatus: Boolean = true
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class RecordOrderSettlementResponse(
    val orderIdentifier: UUID,
    val txId: String
)
