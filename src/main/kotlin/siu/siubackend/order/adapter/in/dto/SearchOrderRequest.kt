package siu.siubackend.order.adapter.`in`.dto

import java.time.OffsetDateTime
import java.util.*

data class SearchOrderRequest(
    val userIdentifier: UUID?,
    val storeIdentifier: UUID?,
    val searchStartDate: OffsetDateTime?,
    val searchEndDate: OffsetDateTime?
)
