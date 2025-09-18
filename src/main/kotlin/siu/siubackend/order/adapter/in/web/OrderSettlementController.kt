package siu.siubackend.order.adapter.`in`.web

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import siu.siubackend.order.adapter.`in`.dto.RecordOrderSettlementRequest
import siu.siubackend.order.adapter.`in`.dto.RecordOrderSettlementResponse
import siu.siubackend.order.application.port.input.RecordOrderSettlementUseCase
import siu.siubackend.order.application.port.output.OrderSettlementRepository
import java.util.*

@RestController
@RequestMapping("/api/order-settlements")
class OrderSettlementController(
    private val recordOrderSettlementUseCase: RecordOrderSettlementUseCase,
    private val orderSettlementRepository: OrderSettlementRepository
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun record(@RequestBody request: RecordOrderSettlementRequest): RecordOrderSettlementResponse {
        val result = recordOrderSettlementUseCase.record(
            RecordOrderSettlementUseCase.Command(
                orderIdentifier = request.orderIdentifier,
                txId = request.txId,
                toWalletAddress = request.toWalletAddress,
                fromWalletAddress = request.fromWalletAddress,
                totalBrokerageFee = request.totalBrokerageFee,
                totalCryptoAmount = request.totalCryptoAmount,
                updateOrderPaymentStatus = request.updateOrderPaymentStatus
            )
        )
        return RecordOrderSettlementResponse(
            orderIdentifier = result.orderIdentifier,
            txId = result.txId
        )
    }

    @GetMapping
    fun get(
        @RequestParam("order_identifier", required = false) orderIdentifier: UUID?,
        @RequestParam("tx_id", required = false) txId: String?
    ): ResponseEntity<Any> {
        return when {
            txId != null -> {
                val settlement = orderSettlementRepository.findByTxId(txId)
                ResponseEntity.ok(settlement)
            }
            orderIdentifier != null -> {
                val settlements = orderSettlementRepository.findByOrderIdentifier(orderIdentifier)
                ResponseEntity.ok(settlements)
            }
            else -> ResponseEntity.badRequest().body(mapOf("error" to "order_identifier 또는 tx_id 중 하나는 필요합니다"))
        }
    }
}


