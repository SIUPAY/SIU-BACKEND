package siu.siubackend.order.adapter.`in`.web

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import siu.siubackend.order.adapter.`in`.dto.CreateOrderRequest
import siu.siubackend.order.adapter.`in`.dto.CreateOrderResponse
import siu.siubackend.order.application.port.input.CreateOrderUseCase

@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val createOrderUseCase: CreateOrderUseCase
) {

    @PostMapping
    fun createOrder(@RequestBody request: CreateOrderRequest): ResponseEntity<CreateOrderResponse> {
        val order = createOrderUseCase.createOrder(request)
        return ResponseEntity.ok(CreateOrderResponse(orderIdentifier = order.identifier))
    }
}
