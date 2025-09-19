package siu.siubackend.order.application.service

import org.springframework.stereotype.Service
import siu.siubackend.order.application.port.input.GetOrderStatisticsUseCase
import siu.siubackend.order.application.port.output.OrderRepository
import siu.siubackend.order.domain.OrderStatistics
import java.time.OffsetDateTime
import java.util.*

@Service
class GetOrderStatisticsService(
    private val orderRepository: OrderRepository
) : GetOrderStatisticsUseCase {

    override fun getOrderStatistics(
        storeIdentifier: UUID,
        startDateTime: OffsetDateTime,
        endDateTime: OffsetDateTime
    ): OrderStatistics {
        val summary = orderRepository.getOrderStatisticsSummary(
            storeIdentifier = storeIdentifier,
            startDateTime = startDateTime,
            endDateTime = endDateTime
        )

        val dailyStatistics = orderRepository.getDailyOrderStatistics(
            storeIdentifier = storeIdentifier,
            startDateTime = startDateTime,
            endDateTime = endDateTime
        )

        return OrderStatistics(
            summary = summary,
            dailyStatistics = dailyStatistics
        )
    }
}
