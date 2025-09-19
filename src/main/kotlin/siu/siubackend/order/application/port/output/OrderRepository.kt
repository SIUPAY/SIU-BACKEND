package siu.siubackend.order.application.port.output

import siu.siubackend.menu.domain.Menu
import siu.siubackend.order.domain.Order
import siu.siubackend.order.domain.OrderMenu
import java.time.OffsetDateTime
import java.util.*

interface OrderRepository {
    fun save(order: Order): Order
    fun findById(identifier: UUID): Order?
    fun findByConditions(
        userIdentifier: UUID?,
        storeIdentifier: UUID?, 
        searchStartDate: OffsetDateTime?,
        searchEndDate: OffsetDateTime?
    ): List<Order>
}

interface OrderMenuRepository {
    fun saveAll(orderMenus: List<OrderMenu>): List<OrderMenu>
    fun findByOrderIdentifiers(orderIdentifiers: List<UUID>): List<OrderMenu>
    fun findByOrderIdentifier(orderIdentifier: UUID): List<OrderMenu>
}
