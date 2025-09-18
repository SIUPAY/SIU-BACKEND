package siu.siubackend.order.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import siu.siubackend.common.exception.domain.EntityNotFoundException
import siu.siubackend.common.exception.domain.ErrorCode
import siu.siubackend.order.application.port.input.UpdateOrderStatusUseCase
import siu.siubackend.order.application.port.output.OrderRepository

@Service
class UpdateOrderStatusService(
    private val orderRepository: OrderRepository
) : UpdateOrderStatusUseCase {

    override fun updateOrderStatus(command: UpdateOrderStatusUseCase.Command) {
        val order = orderRepository.findById(command.orderIdentifier)
            ?: throw EntityNotFoundException(ErrorCode.ORDER_NOT_FOUND.code, ErrorCode.ORDER_NOT_FOUND.message)
            
        order.changeStatus(command.status)
        orderRepository.save(order)
    }
}
