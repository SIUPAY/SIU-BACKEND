package siu.siubackend.order.domain

import java.time.LocalDate

data class OrderStatistics(
    val summary: OrderStatisticsSummary,
    val dailyStatistics: List<DailyOrderStatistics>
)

data class OrderStatisticsSummary(
    val totalFiatAmount: Double,
    val totalCryptoAmount: Double,
    val totalOrderCount: Long
)

data class DailyOrderStatistics(
    val date: LocalDate,
    val totalOrderCount: Long,
    val totalFiatAmount: Double,
    val totalCryptoAmount: Double
)
