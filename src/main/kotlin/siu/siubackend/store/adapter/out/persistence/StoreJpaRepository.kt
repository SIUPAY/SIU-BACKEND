package siu.siubackend.store.adapter.out.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface StoreJpaRepository : JpaRepository<StoreEntity, UUID> {
    fun findByWalletAddress(walletAddress: String): StoreEntity?

    @Query(
        value = """
        SELECT 
            s.identifier,
            s.user_identifier,
            s.name,
            s.address,
            s.phone,
            s.profile_img_url,
            s.wallet_address,
            s.total_order_count,
            s.created_date,
            ST_Distance(s.location, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)) as distance
        FROM store s
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
