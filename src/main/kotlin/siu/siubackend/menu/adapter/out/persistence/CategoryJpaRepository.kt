package siu.siubackend.menu.adapter.out.persistence

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CategoryJpaRepository : JpaRepository<CategoryEntity, UUID> {
    fun findAllByStoreIdentifierOrderByDisplayOrderAscCreatedAtAsc(storeIdentifier: UUID): List<CategoryEntity>
}


