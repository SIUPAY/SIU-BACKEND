package siu.siubackend.menu.application.port.output

import siu.siubackend.menu.domain.Category
import java.util.*

interface CategoryRepository {
    fun save(category: Category): Category
    fun findById(id: UUID): Category?
}


