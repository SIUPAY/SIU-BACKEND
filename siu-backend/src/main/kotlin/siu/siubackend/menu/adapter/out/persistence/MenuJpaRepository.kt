package siu.siubackend.menu.adapter.out.persistence

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MenuJpaRepository : JpaRepository<MenuEntity, UUID> {
    fun findAllByStoreIdentifierOrderByCreatedAtDesc(storeIdentifier: UUID): List<MenuEntity>
}