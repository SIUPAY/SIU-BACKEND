package siu.siubackend.order.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import siu.siubackend.common.exception.domain.EntityNotFoundException
import siu.siubackend.common.exception.domain.ErrorCode
import siu.siubackend.menu.application.port.output.MenuRepository
import siu.siubackend.order.application.port.input.GetOrderDetailUseCase
import siu.siubackend.order.application.port.output.*
import siu.siubackend.order.domain.OrderDetailView
import siu.siubackend.order.domain.OrderMenuDetail
import siu.siubackend.store.application.port.output.StoreRepository
import java.util.*

@Service
@Transactional(readOnly = true)
class GetOrderDetailService(
    private val orderRepository: OrderRepository,
    private val orderMenuRepository: OrderMenuRepository,
    private val orderSettlementRepository: OrderSettlementRepository,
    private val storeRepository: StoreRepository,
    private val menuRepository: MenuRepository
) : GetOrderDetailUseCase {

    override fun getOrderDetail(orderIdentifier: UUID): OrderDetailView {
        val order = orderRepository.findById(orderIdentifier)
            ?: throw EntityNotFoundException(ErrorCode.ORDER_NOT_FOUND.code, "주문을 찾을 수 없습니다: $orderIdentifier")

        val store = storeRepository.findById(order.storeIdentifier)
            ?: throw EntityNotFoundException(ErrorCode.STORE_NOT_FOUND.code, "매장을 찾을 수 없습니다: ${order.storeIdentifier}")

        val orderMenus = orderMenuRepository.findByOrderIdentifier(orderIdentifier)

        val menuIdentifiers = orderMenus.map { it.menuIdentifier }
        val menus = menuRepository.findByIdentifiers(menuIdentifiers)
        val menuMap = menus.associateBy { it.identifier }

        val orderMenuDetails = orderMenus.map { orderMenu ->
            val menu = menuMap[orderMenu.menuIdentifier]
                ?: throw EntityNotFoundException(ErrorCode.MENU_NOT_FOUND.code, "메뉴를 찾을 수 없습니다: ${orderMenu.menuIdentifier}")
            OrderMenuDetail(
                orderMenu = orderMenu,
                menuName = menu.name
            )
        }

        val orderSettlement = orderSettlementRepository.findFirstByOrderIdentifier(orderIdentifier)

        return OrderDetailView(
            order = order,
            storeName = store.name,
            orderMenus = orderMenuDetails,
            orderSettlement = orderSettlement
        )
    }
}
