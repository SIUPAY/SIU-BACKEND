package siu.siubackend.order.application.port.input

import java.util.*

interface RecordOrderSettlementUseCase {
    fun record(command: Command): Result

    data class Command(
        val orderIdentifier: UUID,
        val txId: String,
        val toWalletAddress: String,
        val fromWalletAddress: String,
        val totalBrokerageFee: Double, // SuiPaidEvent에서 오는 MIST 단위 값
        val totalCryptoAmount: Double, // SuiPaidEvent에서 오는 MIST 단위 값
        val updateOrderPaymentStatus: Boolean = true
    ) {
        companion object {
            fun fromSuiEvent(
                orderIdentifier: UUID,
                txId: String,
                toWalletAddress: String,
                fromWalletAddress: String,
                totalBrokerageFeeInMist: Double,
                totalCryptoAmountInMist: Double,
                updateOrderPaymentStatus: Boolean = true
            ): Command {
                return Command(
                    orderIdentifier = orderIdentifier,
                    txId = txId,
                    toWalletAddress = toWalletAddress,
                    fromWalletAddress = fromWalletAddress,
                    totalBrokerageFee = totalBrokerageFeeInMist,
                    totalCryptoAmount = totalCryptoAmountInMist,
                    updateOrderPaymentStatus = updateOrderPaymentStatus
                )
            }
        }
    }

    data class Result(
        val orderIdentifier: UUID,
        val txId: String
    )
}
