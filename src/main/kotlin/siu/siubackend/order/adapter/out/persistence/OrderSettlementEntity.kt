package siu.siubackend.order.adapter.out.persistence

import jakarta.persistence.*
import siu.siubackend.order.domain.OrderSettlement
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "order_settlement")
class OrderSettlementEntity(

    @Id
    @Column(name = "identifier", nullable = false)
    val identifier: UUID,

    @Column(name = "order_identifier", nullable = false)
    val orderIdentifier: UUID,

    @Column(name = "tx_id", nullable = false, unique = true)
    val txId: String,

    @Column(name = "to_wallet_address", nullable = false)
    val toWalletAddress: String,

    @Column(name = "from_wallet_address", nullable = false)
    val fromWalletAddress: String,

    @Column(name = "total_brokerage_fee", nullable = false)
    val totalBrokerageFee: Double, // SUI 단위로 저장 (MIST에서 변환됨)

    @Column(name = "total_crypto_amount", nullable = false)
    val totalCryptoAmount: Double, // SUI 단위로 저장 (MIST에서 변환됨)

    @Column(name = "created_date", nullable = false)
    val createdDate: OffsetDateTime
) {

    fun toDomain(): OrderSettlement {
        return OrderSettlement(
            identifier = this.identifier,
            orderIdentifier = this.orderIdentifier,
            txId = this.txId,
            toWalletAddress = this.toWalletAddress,
            fromWalletAddress = this.fromWalletAddress,
            totalBrokerageFee = this.totalBrokerageFee,
            totalCryptoAmount = this.totalCryptoAmount,
            createdDate = this.createdDate
        )
    }

    companion object {
        fun OrderSettlement.toEntity(): OrderSettlementEntity {
            return OrderSettlementEntity(
                identifier = this.identifier,
                orderIdentifier = this.orderIdentifier,
                txId = this.txId,
                toWalletAddress = this.toWalletAddress,
                fromWalletAddress = this.fromWalletAddress,
                totalBrokerageFee = this.totalBrokerageFee,
                totalCryptoAmount = this.totalCryptoAmount,
                createdDate = this.createdDate
            )
        }
    }
}
