package siu.siubackend.store.adapter.out.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface StoreJpaRepository : JpaRepository<StoreEntity, UUID> {
    fun findByWalletAddress(walletAddress: String): StoreEntity?

    @Query(
        value = """
        SELECT s.*, 
               ST_Distance(s.location, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)) as distance,
               COALESCE(order_count.total_orders, 0) as total_order_count
        FROM stores s
        LEFT JOIN (
            SELECT store_id, COUNT(*) as total_orders 
            FROM orders 
            GROUP BY store_id
        ) order_count ON s.identifier = order_count.store_id
        WHERE ST_DWithin(s.location, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326), :distance)
        AND (:query IS NULL OR s.name ILIKE CONCAT('%', :query, '%'))
        ORDER BY distance ASC
        """,
        nativeQuery = true
    )
    fun findStoresWithinDistance(
        @Param("latitude") latitude: Double,
        @Param("longitude") longitude: Double,
        @Param("distance") distance: Int,
        @Param("query") query: String?
    ): List<Array<Any>>
}
