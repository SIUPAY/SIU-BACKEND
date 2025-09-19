package siu.siubackend.order.adapter.`in`.dto

import siu.siubackend.order.domain.DailyOrderStatistics
import siu.siubackend.order.domain.OrderStatistics
import siu.siubackend.order.domain.OrderStatisticsSummary
import java.time.LocalDate

data class OrderStatisticsResponse(
    val summary: OrderStatisticsSummaryResponse,
    val dailyStatistics: List<DailyOrderStatisticsResponse>
) {
    companion object {
        fun from(orderStatistics: OrderStatistics): OrderStatisticsResponse {
            return OrderStatisticsResponse(
                summary = OrderStatisticsSummaryResponse.from(orderStatistics.summary),
                dailyStatistics = orderStatistics.dailyStatistics.map { 
                    DailyOrderStatisticsResponse.from(it) 
                }
            )
        }
    }
}

data class OrderStatisticsSummaryResponse(
    val totalFiatAmount: Double,
    val totalCryptoAmount: Double,
    val totalOrderCount: Long
) {
    companion object {
        fun from(summary: OrderStatisticsSummary): OrderStatisticsSummaryResponse {
            return OrderStatisticsSummaryResponse(
                totalFiatAmount = summary.totalFiatAmount,
                totalCryptoAmount = summary.totalCryptoAmount,
                totalOrderCount = summary.totalOrderCount
            )
        }
    }
}

data class DailyOrderStatisticsResponse(
    val date: LocalDate,
    val totalOrderCount: Long,
    val totalFiatAmount: Double,
    val totalCryptoAmount: Double
) {
    companion object {
        fun from(dailyStats: DailyOrderStatistics): DailyOrderStatisticsResponse {
            return DailyOrderStatisticsResponse(
                date = dailyStats.date,
                totalOrderCount = dailyStats.totalOrderCount,
                totalFiatAmount = dailyStats.totalFiatAmount,
                totalCryptoAmount = dailyStats.totalCryptoAmount
            )
        }
    }
}
