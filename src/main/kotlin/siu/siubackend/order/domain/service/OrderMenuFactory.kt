package siu.siubackend.order.domain.service

import siu.siubackend.order.domain.OrderMenu
import java.time.OffsetDateTime
import java.util.*

object OrderMenuFactory {

    fun createOrderMenu(
        orderIdentifier: UUID,
        menuIdentifier: UUID,
        quantity: Int,
        unitPrice: Int
    ): OrderMenu {
        return OrderMenu(
            identifier = UUID.randomUUID(),
            orderIdentifier = orderIdentifier,
            menuIdentifier = menuIdentifier,
            quantity = quantity,
            totalFiatAmount = unitPrice * quantity,
            createdDate = OffsetDateTime.now()
        )
    }
}
