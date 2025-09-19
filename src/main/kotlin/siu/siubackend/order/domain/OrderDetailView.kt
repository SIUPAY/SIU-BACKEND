package siu.siubackend.order.domain

data class OrderDetailView(
    val order: Order,
    val storeName: String,
    val orderMenus: List<OrderMenuDetail>,
    val orderSettlement: OrderSettlement?
)

data class OrderMenuDetail(
    val orderMenu: OrderMenu,
    val menuName: String
)
