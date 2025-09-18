package siu.siubackend.order.domain

import java.time.OffsetDateTime
import java.util.*

data class OrderSettlement(
    val identifier: UUID,
    val orderIdentifier: UUID,
    val txId: String,
    val toWalletAddress: String,
    val fromWalletAddress: String,
    val totalBrokerageFee: Double,
    val totalCryptoAmount: Double,
    val createdDate: OffsetDateTime
)

object OrderSettlementFactory {
    fun create(
        orderIdentifier: UUID,
        txId: String,
        toWalletAddress: String,
        fromWalletAddress: String,
        totalBrokerageFee: Double,
        totalCryptoAmount: Double
    ): OrderSettlement {
        return OrderSettlement(
            identifier = UUID.randomUUID(),
            orderIdentifier = orderIdentifier,
            txId = txId,
            toWalletAddress = toWalletAddress,
            fromWalletAddress = fromWalletAddress,
            totalBrokerageFee = totalBrokerageFee,
            totalCryptoAmount = totalCryptoAmount,
            createdDate = OffsetDateTime.now()
        )
    }
}


