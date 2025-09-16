package siu.siubackend.menu.application.port.output

import siu.siubackend.menu.domain.Menu
import java.util.*

interface MenuRepository {
    fun save(menu: Menu): Menu
    fun findById(id: UUID): Menu?
    fun deleteById(id: UUID)
    fun findAll(): List<Menu>
    fun findAllByStore(storeIdentifier: UUID): List<Menu>
}