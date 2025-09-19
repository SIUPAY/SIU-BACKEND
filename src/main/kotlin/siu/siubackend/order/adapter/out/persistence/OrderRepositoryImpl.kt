package siu.siubackend.order.adapter.out.persistence

import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.Predicate
import org.springframework.stereotype.Repository
import siu.siubackend.order.application.port.output.OrderMenuRepository
import siu.siubackend.order.application.port.output.OrderRepository
import siu.siubackend.order.domain.Order
import siu.siubackend.order.domain.OrderMenu
import siu.siubackend.order.adapter.out.persistence.OrderEntity.Companion.toEntity
import siu.siubackend.order.adapter.out.persistence.OrderMenuEntity.Companion.toEntity
import java.time.OffsetDateTime
import java.util.*

@Repository
class OrderRepositoryImpl(
    private val orderJpaRepository: OrderJpaRepository,
    private val entityManager: EntityManager
) : OrderRepository {

    override fun save(order: Order): Order {
        return orderJpaRepository.save(order.toEntity()).toDomain()
    }

    override fun findById(identifier: UUID): Order? {
        return orderJpaRepository.findById(identifier)
            .map { it.toDomain() }
            .orElse(null)
    }
    
    override fun findByConditions(
        userIdentifier: UUID?,
        storeIdentifier: UUID?,
        searchStartDate: OffsetDateTime?,
        searchEndDate: OffsetDateTime?
    ): List<Order> {
        val criteriaBuilder = entityManager.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(OrderEntity::class.java)
        val root = criteriaQuery.from(OrderEntity::class.java)
        
        val predicates = mutableListOf<Predicate>()
        
        userIdentifier?.let {
            predicates.add(criteriaBuilder.equal(root.get<UUID>("userIdentifier"), it))
        }
        
        storeIdentifier?.let {
            predicates.add(criteriaBuilder.equal(root.get<UUID>("storeIdentifier"), it))
        }
        
        searchStartDate?.let {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), it))
        }
        
        searchEndDate?.let {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"), it))
        }
        
        criteriaQuery.where(*predicates.toTypedArray())
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get<OffsetDateTime>("createdDate")))
        
        return entityManager.createQuery(criteriaQuery)
            .resultList
            .map { it.toDomain() }
    }
}
