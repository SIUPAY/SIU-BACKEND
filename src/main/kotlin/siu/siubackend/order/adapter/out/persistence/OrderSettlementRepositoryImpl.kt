package siu.siubackend.order.adapter.out.persistence

import org.springframework.stereotype.Repository
import siu.siubackend.order.application.port.output.OrderSettlementRepository
import siu.siubackend.order.domain.OrderSettlement
import siu.siubackend.order.adapter.out.persistence.OrderSettlementEntity.Companion.toEntity
import java.util.*

@Repository
class OrderSettlementRepositoryImpl(
    private val jpaRepository: OrderSettlementJpaRepository
) : OrderSettlementRepository {

    override fun save(orderSettlement: OrderSettlement): OrderSettlement {
        return jpaRepository.save(orderSettlement.toEntity()).toDomain()
    }

    override fun findByTxId(txId: String): OrderSettlement? {
        return jpaRepository.findByTxId(txId)?.toDomain()
    }

    override fun findByOrderIdentifier(orderIdentifier: UUID): List<OrderSettlement> {
        return jpaRepository.findByOrderIdentifier(orderIdentifier).map { it.toDomain() }
    }
    
    override fun findFirstByOrderIdentifier(orderIdentifier: UUID): OrderSettlement? {
        return jpaRepository.findFirstByOrderIdentifier(orderIdentifier)?.toDomain()
    }
}


