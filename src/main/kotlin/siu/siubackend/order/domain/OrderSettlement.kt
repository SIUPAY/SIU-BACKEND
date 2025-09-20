package siu.siubackend.order.domain

import siu.siubackend.currency.domain.SuiCurrencyUtils
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

    fun createFromMistAmounts(
        orderIdentifier: UUID,
        txId: String,
        toWalletAddress: String,
        fromWalletAddress: String,
        totalBrokerageFeeInMist: Double,
        totalCryptoAmountInMist: Double
    ): OrderSettlement {
        return OrderSettlement(
            identifier = UUID.randomUUID(),
            orderIdentifier = orderIdentifier,
            txId = txId,
            toWalletAddress = toWalletAddress,
            fromWalletAddress = fromWalletAddress,
            totalBrokerageFee = SuiCurrencyUtils.mistToSui(totalBrokerageFeeInMist),
            totalCryptoAmount = SuiCurrencyUtils.mistToSui(totalCryptoAmountInMist),
            createdDate = OffsetDateTime.now()
        )
    }
}
