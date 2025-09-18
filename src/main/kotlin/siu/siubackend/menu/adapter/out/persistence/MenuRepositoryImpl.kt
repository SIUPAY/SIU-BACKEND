package siu.siubackend.menu.adapter.out.persistence

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import siu.siubackend.menu.application.port.output.MenuRepository
import siu.siubackend.menu.domain.Menu
import java.util.*

@Repository
class MenuRepositoryImpl(
    private val jpa: MenuJpaRepository
) : MenuRepository {

    override fun save(menu: Menu): Menu {
        val saved = jpa.save(menu.toEntity())
        return saved.toDomain()
    }

    override fun findById(id: UUID): Menu? =
        jpa.findByIdOrNull(id)?.toDomain()

    override fun deleteById(id: UUID) = jpa.deleteById(id)

    override fun findAll(): List<Menu> =
        jpa.findAll().map { it.toDomain() }

    override fun findAllByStore(storeIdentifier: UUID): List<Menu> =
        jpa.findAllByStoreIdentifierOrderByCreatedAtDesc(storeIdentifier)
            .map { it.toDomain() }

    override fun findByStoreWithFilters(
        storeIdentifier: UUID,
        categoryIdentifier: UUID?,
        available: Boolean?
    ): List<Menu> =
        jpa.findByStoreWithFilters(storeIdentifier, categoryIdentifier, available)
            .map { it.toDomain() }
}

// mapping
private fun Menu.toEntity() = MenuEntity(
    id = this.identifier,
    storeIdentifier = this.storeIdentifier,
    categoryIdentifier = this.categoryIdentifier,
    name = this.name,
    price = this.price,
    description = this.description,
    imageUrl = this.imageUrl,
    isAvailable = this.isAvailable,
    createdAt = this.createdDate
)

private fun MenuEntity.toDomain() = Menu(
    identifier = this.id,
    storeIdentifier = this.storeIdentifier,
    categoryIdentifier = this.categoryIdentifier,
    name = this.name,
    price = this.price,
    description = this.description,
    imageUrl = this.imageUrl,
    isAvailable = this.isAvailable,
    createdDate = this.createdAt
)
