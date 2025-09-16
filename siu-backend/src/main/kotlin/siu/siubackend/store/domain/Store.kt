package siu.siubackend.store.domain

import java.time.OffsetDateTime
import java.util.*

data class Store(
    val identifier: UUID,
    val name: String,
    val address: String,
    val phone: String?,
    val profileImgUrl: String,
    val walletAddress: String,
    val location: Location,
    val totalOrderCount: Int,
    val createdDate: OffsetDateTime
) {
    fun incrementOrderCount(): Store {
        return this.copy(totalOrderCount = totalOrderCount + 1)
    }

    fun updateOrderCount(newCount: Int): Store {
        return this.copy(totalOrderCount = newCount)
    }
}
