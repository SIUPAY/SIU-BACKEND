package siu.siubackend.order.adapter.out.persistence

import jakarta.persistence.*
import siu.siubackend.order.domain.OrderMenu
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "order_menu")
class OrderMenuEntity(

    @Id
    @Column(name = "identifier", nullable = false)
    val identifier: UUID,

    @Column(name = "order_identifier", nullable = false)
    val orderIdentifier: UUID,

    @Column(name = "menu_identifier", nullable = false)
    val menuIdentifier: UUID,

    @Column(name = "quantity", nullable = false)
    val quantity: Int,

    @Column(name = "total_fiat_amount", nullable = false)
    val totalFiatAmount: Int,

    @Column(name = "created_date", nullable = false)
    val createdDate: OffsetDateTime
) {

    fun toDomain(): OrderMenu {
        return OrderMenu(
            identifier = this.identifier,
            orderIdentifier = this.orderIdentifier,
            menuIdentifier = this.menuIdentifier,
            quantity = this.quantity,
            totalFiatAmount = this.totalFiatAmount,
            createdDate = this.createdDate
        )
    }

    companion object {
        fun OrderMenu.toEntity(): OrderMenuEntity {
            return OrderMenuEntity(
                identifier = this.identifier,
                orderIdentifier = this.orderIdentifier,
                menuIdentifier = this.menuIdentifier,
                quantity = this.quantity,
                totalFiatAmount = this.totalFiatAmount,
                createdDate = this.createdDate
            )
        }
    }
}
