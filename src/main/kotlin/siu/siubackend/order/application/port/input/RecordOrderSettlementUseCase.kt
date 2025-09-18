package siu.siubackend.order.application.port.input

import java.util.*

interface RecordOrderSettlementUseCase {
    fun record(command: Command): Result

    data class Command(
        val orderIdentifier: UUID,
        val txId: String,
        val toWalletAddress: String,
        val fromWalletAddress: String,
        val totalBrokerageFee: Double,
        val totalCryptoAmount: Double,
        val updateOrderPaymentStatus: Boolean = true
    )

    data class Result(
        val orderIdentifier: UUID,
        val txId: String
    )
}


