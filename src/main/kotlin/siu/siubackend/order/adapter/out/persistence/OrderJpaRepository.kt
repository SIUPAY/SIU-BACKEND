package siu.siubackend.order.adapter.out.persistence

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface OrderJpaRepository : JpaRepository<OrderEntity, UUID>
