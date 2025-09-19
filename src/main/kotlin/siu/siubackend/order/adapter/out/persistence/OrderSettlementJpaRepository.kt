package siu.siubackend.order.adapter.out.persistence

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface OrderSettlementJpaRepository : JpaRepository<OrderSettlementEntity, UUID> {
    fun findByTxId(txId: String): OrderSettlementEntity?
    fun findByOrderIdentifier(orderIdentifier: UUID): List<OrderSettlementEntity>
    fun findFirstByOrderIdentifier(orderIdentifier: UUID): OrderSettlementEntity?
}


