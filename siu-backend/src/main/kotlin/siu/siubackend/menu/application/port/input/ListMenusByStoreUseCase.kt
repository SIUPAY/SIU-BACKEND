package siu.siubackend.menu.application.port.input

import siu.siubackend.menu.domain.Menu
import java.util.*

interface ListMenusByStoreUseCase {
    fun handle(storeIdentifier: UUID): List<Menu>
}