package siu.siubackend.menu.adapter.out.persistence

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import siu.siubackend.menu.application.port.output.CategoryRepository
import siu.siubackend.menu.domain.Category
import java.util.*

@Repository
class CategoryRepositoryImpl(
    private val jpa: CategoryJpaRepository
) : CategoryRepository {

    override fun save(category: Category): Category =
        jpa.save(category.toEntity()).toDomain()

    override fun findById(id: UUID): Category? =
        jpa.findByIdOrNull(id)?.toDomain()
}

private fun Category.toEntity() = CategoryEntity(
    id = this.identifier,
    name = this.name
)

private fun CategoryEntity.toDomain() = Category(
    identifier = this.id,
    name = this.name
)


