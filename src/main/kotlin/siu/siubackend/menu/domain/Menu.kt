package siu.siubackend.menu.domain

import java.time.OffsetDateTime
import java.util.*

data class Menu(
    val identifier: UUID,
    val storeIdentifier: UUID,
    val categoryIdentifier: UUID?,
    val name: String,
    val price: Int,
    val description: String?,
    val imageUrl: String?,
    val isAvailable: Boolean,
    val createdDate: OffsetDateTime
) {
    fun update(
        name: String,
        price: Int,
        description: String?,
        categoryIdentifier: UUID?,
        imageUrl: String?,
        isAvailable: Boolean?
    ): Menu {
        require(name.isNotBlank()) { "메뉴명은 비어있을 수 없습니다." }
        require(price >= 0) { "가격은 0 이상이어야 합니다." }
        return this.copy(
            name = name,
            price = price,
            description = description,
            categoryIdentifier = categoryIdentifier,
            imageUrl = imageUrl,
            isAvailable = isAvailable ?: this.isAvailable
        )
    }
}