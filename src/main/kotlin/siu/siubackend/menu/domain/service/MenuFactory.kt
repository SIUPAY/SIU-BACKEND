package siu.siubackend.menu.domain.service

import siu.siubackend.menu.domain.Menu
import java.time.OffsetDateTime
import java.util.*

object MenuFactory {
    fun create(
        storeIdentifier: UUID,
        categoryIdentifier: UUID?,
        name: String,
        price: Int,
        description: String?,
        imageUrl: String?,
        isAvailable: Boolean
    ): Menu {
        require(name.isNotBlank())
        require(price >= 0)
        return Menu(
            identifier = UUID.randomUUID(),
            storeIdentifier = storeIdentifier,
            categoryIdentifier = categoryIdentifier,
            name = name,
            price = price,
            description = description,
            imageUrl = imageUrl,
            isAvailable = isAvailable,
            createdDate = OffsetDateTime.now()
        )
    }
}