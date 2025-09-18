package siu.siubackend.order.domain

import java.time.OffsetDateTime
import java.util.*

data class Order(
    val identifier: UUID,
    val storeIdentifier: UUID,
    val userIdentifier: UUID,
    var status: OrderStatus,
    var paymentStatus: PaymentStatus,
    val totalFiatAmount: Double,
    val orderNumber: Int,
    val createdDate: OffsetDateTime
) {
    fun changeStatus(newStatus: OrderStatus) {
        this.status = newStatus
    }
}

enum class OrderStatus {
    WAITING,
    COOKING,
    COOKED,
    RECEIVED,
    CANCELED
}

enum class PaymentStatus {
    PENDING,
    CONFIRMED,
    FAILED
}
