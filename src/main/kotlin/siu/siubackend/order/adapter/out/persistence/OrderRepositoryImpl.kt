package siu.siubackend.order.adapter.out.persistence

import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.Predicate
import org.springframework.stereotype.Repository
import siu.siubackend.order.application.port.output.OrderMenuRepository
import siu.siubackend.order.application.port.output.OrderRepository
import siu.siubackend.order.domain.DailyOrderStatistics
import siu.siubackend.order.domain.Order
import siu.siubackend.order.domain.OrderMenu
import siu.siubackend.order.domain.OrderStatisticsSummary
import siu.siubackend.order.adapter.out.persistence.OrderEntity.Companion.toEntity
import siu.siubackend.order.adapter.out.persistence.OrderMenuEntity.Companion.toEntity
import java.time.LocalDate
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
    
    override fun getOrderStatisticsSummary(
        storeIdentifier: UUID,
        startDateTime: OffsetDateTime,
        endDateTime: OffsetDateTime
    ): OrderStatisticsSummary {
        val summaryQuery = """
            SELECT 
                COUNT(o.identifier) as totalOrderCount,
                COALESCE(SUM(o.totalFiatAmount), 0) as totalFiatAmount,
                COALESCE(SUM(os.totalCryptoAmount), 0) as totalCryptoAmount
            FROM OrderEntity o
            LEFT JOIN OrderSettlementEntity os ON o.identifier = os.orderIdentifier
            WHERE o.storeIdentifier = :storeIdentifier
            AND o.createdDate >= :startDateTime
            AND o.createdDate <= :endDateTime
        """.trimIndent()
        
        val result = entityManager.createQuery(summaryQuery)
            .setParameter("storeIdentifier", storeIdentifier)
            .setParameter("startDateTime", startDateTime)
            .setParameter("endDateTime", endDateTime)
            .singleResult as Array<*>
            
        return OrderStatisticsSummary(
            totalOrderCount = (result[0] as Long),
            totalFiatAmount = (result[1] as Double),
            totalCryptoAmount = (result[2] as Double)
        )
    }
    
    override fun getDailyOrderStatistics(
        storeIdentifier: UUID,
        startDateTime: OffsetDateTime,
        endDateTime: OffsetDateTime
    ): List<DailyOrderStatistics> {
        val dailyQuery = """
            SELECT 
                DATE(o.createdDate) as orderDate,
                COUNT(o.identifier) as dailyOrderCount,
                COALESCE(SUM(o.totalFiatAmount), 0) as dailyFiatAmount,
                COALESCE(SUM(os.totalCryptoAmount), 0) as dailyCryptoAmount
            FROM OrderEntity o
            LEFT JOIN OrderSettlementEntity os ON o.identifier = os.orderIdentifier
            WHERE o.storeIdentifier = :storeIdentifier
            AND o.createdDate >= :startDateTime
            AND o.createdDate <= :endDateTime
            GROUP BY DATE(o.createdDate)
            ORDER BY DATE(o.createdDate)
        """.trimIndent()
        
        val results = entityManager.createQuery(dailyQuery)
            .setParameter("storeIdentifier", storeIdentifier)
            .setParameter("startDateTime", startDateTime)
            .setParameter("endDateTime", endDateTime)
            .resultList
            
        return results.map { result ->
            val row = result as Array<*>
            DailyOrderStatistics(
                date = (row[0] as java.sql.Date).toLocalDate(),
                totalOrderCount = (row[1] as Long),
                totalFiatAmount = (row[2] as Double),
                totalCryptoAmount = (row[3] as Double)
            )
        }
    }
}
