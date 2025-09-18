package siu.siubackend.menu.domain

import java.time.OffsetDateTime
import java.util.*

data class Category(
    val identifier: UUID,
    val storeIdentifier: UUID,
    val name: String,
    val description: String?,
    val displayOrder: Int?,
    val createdAt: OffsetDateTime
)


