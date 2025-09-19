package siu.siubackend.order.adapter.`in`.web

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import siu.siubackend.order.adapter.`in`.dto.OrderStatisticsResponse
import siu.siubackend.order.application.port.input.GetOrderStatisticsUseCase
import java.time.OffsetDateTime
import java.util.*

@RestController
@RequestMapping("/api/v1/stores")
class StoreOrderStatisticsController(
    private val getOrderStatisticsUseCase: GetOrderStatisticsUseCase
) {

    @GetMapping("/{store_identifier}/orders/statistics")
    fun getOrderStatistics(
        @PathVariable("store_identifier") storeIdentifier: UUID,
        @RequestParam("start_date_time") startDateTime: OffsetDateTime,
        @RequestParam("end_date_time") endDateTime: OffsetDateTime
    ): ResponseEntity<OrderStatisticsResponse> {
        val orderStatistics = getOrderStatisticsUseCase.getOrderStatistics(
            storeIdentifier = storeIdentifier,
            startDateTime = startDateTime,
            endDateTime = endDateTime
        )
        
        val response = OrderStatisticsResponse.from(orderStatistics)
        return ResponseEntity.ok(response)
    }
}
