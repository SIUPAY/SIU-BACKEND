package siu.siubackend.menu.adapter.`in`.dto

import java.time.OffsetDateTime
import java.util.*

data class MenuResponse(
    val identifier: UUID,
    val name: String,
    val price: Int,
    val image_url: String?,
    val created_date: OffsetDateTime
)