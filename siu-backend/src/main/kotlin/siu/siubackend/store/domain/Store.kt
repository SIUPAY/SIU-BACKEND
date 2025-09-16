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
    val createdDate: OffsetDateTime
)
