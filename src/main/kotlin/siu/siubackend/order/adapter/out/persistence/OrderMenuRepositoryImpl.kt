package siu.siubackend.order.adapter.out.persistence

import org.springframework.stereotype.Repository
import siu.siubackend.order.application.port.output.OrderMenuRepository
import siu.siubackend.order.domain.OrderMenu
import siu.siubackend.order.adapter.out.persistence.OrderMenuEntity.Companion.toEntity
import java.util.*

@Repository
class OrderMenuRepositoryImpl(
    private val orderMenuJpaRepository: OrderMenuJpaRepository
) : OrderMenuRepository {

    override fun saveAll(orderMenus: List<OrderMenu>): List<OrderMenu> {
        val entities = orderMenus.map { it.toEntity() }
        return orderMenuJpaRepository.saveAll(entities).map { it.toDomain() }
    }

    override fun findByOrderIdentifiers(orderIdentifiers: List<UUID>): List<OrderMenu> {
        return orderMenuJpaRepository.findByOrderIdentifierIn(orderIdentifiers)
            .map { it.toDomain() }
    }

    override fun findByOrderIdentifier(orderIdentifier: UUID): List<OrderMenu> {
        return orderMenuJpaRepository.findByOrderIdentifier(orderIdentifier)
            .map { it.toDomain() }
    }
}
