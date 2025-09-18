package siu.siubackend.order.domain

import java.time.OffsetDateTime
import java.util.*

data class OrderMenu(
    val identifier: UUID,
    val orderIdentifier: UUID,
    val menuIdentifier: UUID,
    val quantity: Int,
    val totalFiatAmount: Int,
    val createdDate: OffsetDateTime
)
