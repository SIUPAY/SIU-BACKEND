package siu.siubackend.order.adapter.`in`.web

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import siu.siubackend.currency.domain.SuiCurrencyUtils
import siu.siubackend.order.adapter.`in`.dto.RecordOrderSettlementFromSuiEventRequest
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
        val command = when (request.unit) {
            RecordOrderSettlementRequest.CurrencyUnit.SUI -> {
                // SUI 단위로 입력받은 경우 MIST로 변환 후 처리
                RecordOrderSettlementUseCase.Command.fromSuiEvent(
                    orderIdentifier = request.orderIdentifier,
                    txId = request.txId,
                    toWalletAddress = request.toWalletAddress,
                    fromWalletAddress = request.fromWalletAddress,
                    totalBrokerageFeeInMist = SuiCurrencyUtils.suiToMist(request.totalBrokerageFee).toDouble(),
                    totalCryptoAmountInMist = SuiCurrencyUtils.suiToMist(request.totalCryptoAmount).toDouble(),
                    updateOrderPaymentStatus = request.updateOrderPaymentStatus
                )
            }
            RecordOrderSettlementRequest.CurrencyUnit.MIST -> {
                // MIST 단위로 입력받은 경우 그대로 처리
                RecordOrderSettlementUseCase.Command.fromSuiEvent(
                    orderIdentifier = request.orderIdentifier,
                    txId = request.txId,
                    toWalletAddress = request.toWalletAddress,
                    fromWalletAddress = request.fromWalletAddress,
                    totalBrokerageFeeInMist = request.totalBrokerageFee,
                    totalCryptoAmountInMist = request.totalCryptoAmount,
                    updateOrderPaymentStatus = request.updateOrderPaymentStatus
                )
            }
        }
        
        val result = recordOrderSettlementUseCase.record(command)
        return RecordOrderSettlementResponse(
            orderIdentifier = result.orderIdentifier,
            txId = result.txId
        )
    }

    @PostMapping("/from-sui-event")
    @ResponseStatus(HttpStatus.CREATED)
    fun recordFromSuiEvent(@RequestBody request: RecordOrderSettlementFromSuiEventRequest): RecordOrderSettlementResponse {
        val command = RecordOrderSettlementUseCase.Command.fromSuiEvent(
            orderIdentifier = request.orderIdentifier,
            txId = request.txId,
            toWalletAddress = request.toWalletAddress,
            fromWalletAddress = request.fromWalletAddress,
            totalBrokerageFeeInMist = request.totalBrokerageFeeInMist,
            totalCryptoAmountInMist = request.totalCryptoAmountInMist,
            updateOrderPaymentStatus = request.updateOrderPaymentStatus
        )
        
        val result = recordOrderSettlementUseCase.record(command)
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
