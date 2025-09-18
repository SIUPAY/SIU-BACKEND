package siu.siubackend.menu.adapter.out.persistence

import jakarta.persistence.*
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "menus")
class MenuEntity(
    @Id
    @Column(columnDefinition = "uuid")
    var id: UUID,

    @Column(name = "restaurant_id", columnDefinition = "uuid", nullable = false)
    var storeIdentifier: UUID,

    @Column(name = "category_id", columnDefinition = "uuid")
    var categoryIdentifier: UUID? = null,

    @Column(nullable = false, length = 200)
    var name: String,

    @Column(nullable = false)
    var price: Int,

    @Column(columnDefinition = "text")
    var description: String? = null,

    @Column(name = "image_url")
    var imageUrl: String? = null,

    @Column(name = "is_available", nullable = false)
    var isAvailable: Boolean,

    @Column(name = "created_at", nullable = false)
    var createdAt: OffsetDateTime
)