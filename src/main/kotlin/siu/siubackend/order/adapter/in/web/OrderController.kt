package siu.siubackend.order.adapter.`in`.web

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import siu.siubackend.order.adapter.`in`.dto.*
import siu.siubackend.order.application.port.input.CreateOrderUseCase
import siu.siubackend.order.application.port.input.SearchOrderUseCase
import siu.siubackend.order.application.port.input.UpdateOrderStatusUseCase
import java.time.OffsetDateTime
import java.util.*

@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val createOrderUseCase: CreateOrderUseCase,
    private val searchOrderUseCase: SearchOrderUseCase,
    private val updateOrderStatusUseCase: UpdateOrderStatusUseCase
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrder(@RequestBody request: CreateOrderRequest): CreateOrderResponse {
        val order = createOrderUseCase.createOrder(request)
        return CreateOrderResponse(orderIdentifier = order.identifier)
    }
    
    @GetMapping
    fun searchOrders(
        @RequestParam("user_identifier", required = false) userIdentifier: UUID?,
        @RequestParam("store_identifier", required = false) storeIdentifier: UUID?,
        @RequestParam("search_start_date", required = false) searchStartDate: OffsetDateTime?,
        @RequestParam("search_end_date", required = false) searchEndDate: OffsetDateTime?
    ): ResponseEntity<SearchOrderResponse> {
        val request = SearchOrderRequest(
            userIdentifier = userIdentifier,
            storeIdentifier = storeIdentifier,
            searchStartDate = searchStartDate,
            searchEndDate = searchEndDate
        )
        
        val ordersWithDetails = searchOrderUseCase.searchOrders(request)
        
        val orderDetailResponses = ordersWithDetails.map { orderWithDetails ->
            OrderDetailResponse(
                identifier = orderWithDetails.order.identifier,
                storeIdentifier = orderWithDetails.order.storeIdentifier,
                userIdentifier = orderWithDetails.order.userIdentifier,
                status = orderWithDetails.order.status,
                paymentStatus = orderWithDetails.order.paymentStatus,
                totalFiatAmount = orderWithDetails.order.totalFiatAmount,
                orderNumber = orderWithDetails.order.orderNumber,
                createdDate = orderWithDetails.order.createdDate,
                orderMenus = orderWithDetails.orderMenus.map { orderMenuWithName ->
                    OrderMenuDetailResponse(
                        identifier = orderMenuWithName.orderMenu.identifier,
                        orderIdentifier = orderMenuWithName.orderMenu.orderIdentifier,
                        menuIdentifier = orderMenuWithName.orderMenu.menuIdentifier,
                        quantity = orderMenuWithName.orderMenu.quantity,
                        totalFiatAmount = orderMenuWithName.orderMenu.totalFiatAmount,
                        createdDate = orderMenuWithName.orderMenu.createdDate,
                        menuName = orderMenuWithName.menuName
                    )
                }
            )
        }
        
        return ResponseEntity.ok(SearchOrderResponse(orders = orderDetailResponses))
    }
    
    @PutMapping("/{order_identifier}/status")
    fun updateOrderStatus(
        @PathVariable("order_identifier") orderIdentifier: UUID,
        @RequestBody request: UpdateOrderStatusRequest
    ): ResponseEntity<Void> {
        val command = UpdateOrderStatusUseCase.Command(
            orderIdentifier = orderIdentifier,
            status = request.status
        )
        updateOrderStatusUseCase.updateOrderStatus(command)
        return ResponseEntity.noContent().build()
    }
}
