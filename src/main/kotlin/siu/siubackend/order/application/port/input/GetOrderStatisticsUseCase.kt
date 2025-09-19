package siu.siubackend.order.application.port.input

import siu.siubackend.order.domain.OrderStatistics
import java.time.OffsetDateTime
import java.util.*

interface GetOrderStatisticsUseCase {
    fun getOrderStatistics(
        storeIdentifier: UUID,
        startDateTime: OffsetDateTime,
        endDateTime: OffsetDateTime
    ): OrderStatistics
}
