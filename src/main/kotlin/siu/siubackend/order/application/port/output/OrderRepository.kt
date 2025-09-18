package siu.siubackend.order.application.port.output

import siu.siubackend.order.domain.Order
import siu.siubackend.order.domain.OrderMenu
import java.util.*

interface OrderRepository {
    fun save(order: Order): Order
    fun findById(identifier: UUID): Order?
}

interface OrderMenuRepository {
    fun saveAll(orderMenus: List<OrderMenu>): List<OrderMenu>
}
