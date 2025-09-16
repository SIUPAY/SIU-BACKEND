package siu.siubackend.menu.adapter.out.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "categories")
class CategoryEntity(
    @Id
    @Column(columnDefinition = "uuid")
    var id: UUID,

    @Column(nullable = false, length = 200)
    var name: String
)


