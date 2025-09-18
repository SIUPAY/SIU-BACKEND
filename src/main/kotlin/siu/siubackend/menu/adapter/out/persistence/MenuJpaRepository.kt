package siu.siubackend.menu.adapter.out.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface MenuJpaRepository : JpaRepository<MenuEntity, UUID> {
    fun findAllByStoreIdentifierOrderByCreatedAtDesc(storeIdentifier: UUID): List<MenuEntity>

    @Query(
        """
        SELECT m FROM MenuEntity m
        WHERE m.storeIdentifier = :storeIdentifier
          AND (:categoryIdentifier IS NULL OR m.categoryIdentifier = :categoryIdentifier)
          AND (:available IS NULL OR m.isAvailable = :available)
        ORDER BY m.createdAt DESC
        """
    )
    fun findByStoreWithFilters(
        storeIdentifier: UUID,
        categoryIdentifier: UUID?,
        available: Boolean?
    ): List<MenuEntity>
}