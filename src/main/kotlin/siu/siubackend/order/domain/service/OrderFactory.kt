package siu.siubackend.order.domain.service

import siu.siubackend.order.domain.Order
import siu.siubackend.order.domain.OrderStatus
import siu.siubackend.order.domain.PaymentStatus
import java.time.OffsetDateTime
import java.util.*

object OrderFactory {

    fun createOrder(
        storeIdentifier: UUID,
        userIdentifier: UUID,
        totalFiatAmount: Double,
        orderNumber: Int
    ): Order {
        return Order(
            identifier = UUID.randomUUID(),
            storeIdentifier = storeIdentifier,
            userIdentifier = userIdentifier,
            status = OrderStatus.WAITING,
            paymentStatus = PaymentStatus.PENDING,
            totalFiatAmount = totalFiatAmount,
            orderNumber = orderNumber,
            createdDate = OffsetDateTime.now()
        )
    }
}
