package siu.siubackend.order.adapter.`in`.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.OffsetDateTime
import java.util.*

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class SearchOrderRequest(
    val userIdentifier: UUID?,
    val storeIdentifier: UUID?,
    val searchStartDate: OffsetDateTime?,
    val searchEndDate: OffsetDateTime?
)
