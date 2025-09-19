package siu.siubackend.order.application.port.input

import siu.siubackend.order.domain.OrderDetailView
import java.util.*

interface GetOrderDetailUseCase {
    fun getOrderDetail(orderIdentifier: UUID): OrderDetailView
}
