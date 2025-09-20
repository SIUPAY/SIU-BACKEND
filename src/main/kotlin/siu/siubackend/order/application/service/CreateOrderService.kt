package siu.siubackend.order.application.service

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import siu.siubackend.menu.application.port.output.MenuRepository
import siu.siubackend.order.adapter.`in`.dto.CreateOrderRequest
import siu.siubackend.order.application.port.input.CreateOrderUseCase
import siu.siubackend.order.application.port.output.OrderMenuRepository
import siu.siubackend.order.application.port.output.OrderRepository
import siu.siubackend.order.domain.service.OrderFactory
import siu.siubackend.order.domain.service.OrderMenuFactory
import siu.siubackend.order.domain.Order
import siu.siubackend.order.domain.event.OrderCreatedEvent

@Service
@Transactional
class CreateOrderService(
    private val orderRepository: OrderRepository,
    private val orderMenuRepository: OrderMenuRepository,
    private val menuRepository: MenuRepository,
    private val applicationEventPublisher: ApplicationEventPublisher
) : CreateOrderUseCase {

    override fun createOrder(request: CreateOrderRequest): Order {
        val menus = request.orderMenus.map { orderMenuRequest ->
            menuRepository.findById(orderMenuRequest.menuIdentifier)
                ?: throw IllegalArgumentException("메뉴를 찾을 수 없습니다: ${orderMenuRequest.menuIdentifier}")
        }

        val totalFiatAmount = request.orderMenus.zip(menus) { orderMenuRequest, menu ->
            menu.price * orderMenuRequest.quantity
        }.sum().toDouble()

        val orderNumber = generateOrderNumber()

        val order = OrderFactory.createOrder(
            storeIdentifier = request.storeIdentifier,
            userIdentifier = request.userIdentifier,
            totalFiatAmount = totalFiatAmount,
            orderNumber = orderNumber
        )

        val savedOrder = orderRepository.save(order)

        val orderMenus = request.orderMenus.zip(menus) { orderMenuRequest, menu ->
            OrderMenuFactory.createOrderMenu(
                orderIdentifier = savedOrder.identifier,
                menuIdentifier = menu.identifier,
                quantity = orderMenuRequest.quantity,
                unitPrice = menu.price
            )
        }

        orderMenuRepository.saveAll(orderMenus)
        
        applicationEventPublisher.publishEvent(
            OrderCreatedEvent(
                orderId = savedOrder.identifier,
                storeId = savedOrder.storeIdentifier
            )
        )
        
        return savedOrder
    }

    private fun generateOrderNumber(): Int {
        return (System.currentTimeMillis() % 1000000).toInt()
    }
}
