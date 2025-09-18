package siu.siubackend.order.domain

data class OrderWithDetails(
    val order: Order,
    val orderMenus: List<OrderMenuWithMenuName>
)

data class OrderMenuWithMenuName(
    val orderMenu: OrderMenu,
    val menuName: String
)
