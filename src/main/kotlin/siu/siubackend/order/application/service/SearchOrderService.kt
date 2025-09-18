package siu.siubackend.order.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import siu.siubackend.menu.application.port.output.MenuRepository
import siu.siubackend.order.adapter.`in`.dto.SearchOrderRequest
import siu.siubackend.order.application.port.input.SearchOrderUseCase
import siu.siubackend.order.application.port.output.OrderMenuRepository
import siu.siubackend.order.application.port.output.OrderRepository
import siu.siubackend.order.domain.OrderMenuWithMenuName
import siu.siubackend.order.domain.OrderWithDetails

@Service
@Transactional(readOnly = true)
class SearchOrderService(
    private val orderRepository: OrderRepository,
    private val orderMenuRepository: OrderMenuRepository,
    private val menuRepository: MenuRepository
) : SearchOrderUseCase {

    override fun searchOrders(request: SearchOrderRequest): List<OrderWithDetails> {
        val orders = orderRepository.findByConditions(
            userIdentifier = request.userIdentifier,
            storeIdentifier = request.storeIdentifier,
            searchStartDate = request.searchStartDate,
            searchEndDate = request.searchEndDate
        )

        if (orders.isEmpty()) {
            return emptyList()
        }

        val orderIdentifiers = orders.map { it.identifier }
        val orderMenus = orderMenuRepository.findByOrderIdentifiers(orderIdentifiers)
        
        val menuIdentifiers = orderMenus.map { it.menuIdentifier }.distinct()
        val menus = menuIdentifiers.mapNotNull { menuId ->
            menuRepository.findById(menuId)?.let { menu ->
                menuId to menu.name
            }
        }.toMap()

        return orders.map { order ->
            val orderMenusForOrder = orderMenus.filter { it.orderIdentifier == order.identifier }
            val orderMenusWithMenuName = orderMenusForOrder.map { orderMenu ->
                OrderMenuWithMenuName(
                    orderMenu = orderMenu,
                    menuName = menus[orderMenu.menuIdentifier] ?: "Unknown Menu"
                )
            }
            
            OrderWithDetails(
                order = order,
                orderMenus = orderMenusWithMenuName
            )
        }
    }
}
