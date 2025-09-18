package siu.siubackend.menu.adapter.out.persistence

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import siu.siubackend.menu.application.port.output.CategoryRepository
import siu.siubackend.menu.domain.Category
import java.time.OffsetDateTime
import java.util.*

@Repository
class CategoryRepositoryImpl(
    private val jpa: CategoryJpaRepository
) : CategoryRepository {

    override fun save(category: Category): Category =
        jpa.save(category.toEntity()).toDomain()

    override fun findById(id: UUID): Category? =
        jpa.findByIdOrNull(id)?.toDomain()

    override fun findAllByIds(ids: Collection<UUID>): List<Category> =
        jpa.findAllById(ids).map { it.toDomain() }
}

private fun Category.toEntity() = CategoryEntity(
    id = this.identifier,
    storeIdentifier = this.storeIdentifier,
    name = this.name,
    description = this.description,
    displayOrder = this.displayOrder,
    createdAt = this.createdAt
)

private fun CategoryEntity.toDomain() = Category(
    identifier = this.id,
    storeIdentifier = this.storeIdentifier,
    name = this.name,
    description = this.description,
    displayOrder = this.displayOrder,
    createdAt = this.createdAt
)


