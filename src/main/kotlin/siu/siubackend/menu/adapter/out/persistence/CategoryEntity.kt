package siu.siubackend.menu.adapter.out.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "categories")
class CategoryEntity(
    @Id
    @Column(columnDefinition = "uuid")
    var id: UUID,

    @Column(name = "store_identifier", columnDefinition = "uuid", nullable = false)
    var storeIdentifier: UUID,

    @Column(nullable = false, length = 200)
    var name: String,

    @Column(columnDefinition = "text")
    var description: String? = null,

    @Column(name = "display_order")
    var displayOrder: Int? = null,

    @Column(name = "created_at", nullable = false)
    var createdAt: OffsetDateTime
)


