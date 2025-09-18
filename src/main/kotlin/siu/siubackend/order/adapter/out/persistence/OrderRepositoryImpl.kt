package siu.siubackend.order.adapter.out.persistence

import org.springframework.stereotype.Repository
import siu.siubackend.order.application.port.output.OrderMenuRepository
import siu.siubackend.order.application.port.output.OrderRepository
import siu.siubackend.order.domain.Order
import siu.siubackend.order.domain.OrderMenu
import siu.siubackend.order.adapter.out.persistence.OrderEntity.Companion.toEntity
import siu.siubackend.order.adapter.out.persistence.OrderMenuEntity.Companion.toEntity
import java.util.*

@Repository
class OrderRepositoryImpl(
    private val orderJpaRepository: OrderJpaRepository
) : OrderRepository {

    override fun save(order: Order): Order {
        return orderJpaRepository.save(order.toEntity()).toDomain()
    }

    override fun findById(identifier: UUID): Order? {
        return orderJpaRepository.findById(identifier)
            .map { it.toDomain() }
            .orElse(null)
    }
}

@Repository
class OrderMenuRepositoryImpl(
    private val orderMenuJpaRepository: OrderMenuJpaRepository
) : OrderMenuRepository {

    override fun saveAll(orderMenus: List<OrderMenu>): List<OrderMenu> {
        val entities = orderMenus.map { it.toEntity() }
        return orderMenuJpaRepository.saveAll(entities).map { it.toDomain() }
    }
}
