package siu.siubackend.order.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import siu.siubackend.common.exception.domain.EntityNotFoundException
import siu.siubackend.common.exception.domain.ErrorCode
import siu.siubackend.order.application.port.input.RecordOrderSettlementUseCase
import siu.siubackend.order.application.port.output.OrderRepository
import siu.siubackend.order.application.port.output.OrderSettlementRepository
import siu.siubackend.order.domain.OrderSettlementFactory
import siu.siubackend.order.domain.PaymentStatus

@Service
class RecordOrderSettlementService(
    private val orderRepository: OrderRepository,
    private val orderSettlementRepository: OrderSettlementRepository
) : RecordOrderSettlementUseCase {

    @Transactional
    override fun record(command: RecordOrderSettlementUseCase.Command): RecordOrderSettlementUseCase.Result {
        // 멱등성: txId로 기존 정산이 있으면 그대로 성공 처리
        val existing = orderSettlementRepository.findByTxId(command.txId)
        if (existing != null) {
            return RecordOrderSettlementUseCase.Result(
                orderIdentifier = existing.orderIdentifier,
                txId = existing.txId
            )
        }

        // 주문 존재 확인
        val order = orderRepository.findById(command.orderIdentifier)
            ?: throw EntityNotFoundException(ErrorCode.ORDER_NOT_FOUND.code, ErrorCode.ORDER_NOT_FOUND.message)

        // SuiPaidEvent에서 오는 값들은 MIST 단위이므로 createFromMistAmounts 사용
        val settlement = OrderSettlementFactory.createFromMistAmounts(
            orderIdentifier = command.orderIdentifier,
            txId = command.txId,
            toWalletAddress = command.toWalletAddress,
            fromWalletAddress = command.fromWalletAddress,
            totalBrokerageFeeInMist = command.totalBrokerageFee,
            totalCryptoAmountInMist = command.totalCryptoAmount
        )
        orderSettlementRepository.save(settlement)

        // 옵션: 주문 결제상태 갱신
        if (command.updateOrderPaymentStatus) {
            order.paymentStatus = PaymentStatus.CONFIRMED
            orderRepository.save(order)
        }

        return RecordOrderSettlementUseCase.Result(
            orderIdentifier = settlement.orderIdentifier,
            txId = settlement.txId
        )
    }
}
