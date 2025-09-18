package siu.siubackend.order.adapter.out.persistence

import jakarta.persistence.*
import siu.siubackend.order.domain.Order
import siu.siubackend.order.domain.OrderStatus
import siu.siubackend.order.domain.PaymentStatus
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "`order`")
class OrderEntity(

    @Id
    @Column(name = "identifier", nullable = false)
    val identifier: UUID,

    @Column(name = "store_identifier", nullable = false)
    val storeIdentifier: UUID,

    @Column(name = "user_identifier", nullable = false)
    val userIdentifier: UUID,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: OrderStatus,

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    var paymentStatus: PaymentStatus,

    @Column(name = "total_fiat_amount", nullable = false)
    val totalFiatAmount: Double,

    @Column(name = "order_number", nullable = false)
    val orderNumber: Int,

    @Column(name = "created_date", nullable = false)
    val createdDate: OffsetDateTime
) {

    fun toDomain(): Order {
        return Order(
            identifier = this.identifier,
            storeIdentifier = this.storeIdentifier,
            userIdentifier = this.userIdentifier,
            status = this.status,
            paymentStatus = this.paymentStatus,
            totalFiatAmount = this.totalFiatAmount,
            orderNumber = this.orderNumber,
            createdDate = this.createdDate
        )
    }

    companion object {
        fun Order.toEntity(): OrderEntity {
            return OrderEntity(
                identifier = this.identifier,
                storeIdentifier = this.storeIdentifier,
                userIdentifier = this.userIdentifier,
                status = this.status,
                paymentStatus = this.paymentStatus,
                totalFiatAmount = this.totalFiatAmount,
                orderNumber = this.orderNumber,
                createdDate = this.createdDate
            )
        }
    }
}
