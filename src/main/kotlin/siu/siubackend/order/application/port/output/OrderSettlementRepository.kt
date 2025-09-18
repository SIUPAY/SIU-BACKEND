package siu.siubackend.order.application.port.output

import siu.siubackend.order.domain.OrderSettlement
import java.util.*

interface OrderSettlementRepository {
    fun save(orderSettlement: OrderSettlement): OrderSettlement
    fun findByTxId(txId: String): OrderSettlement?
    fun findByOrderIdentifier(orderIdentifier: UUID): List<OrderSettlement>
}


