package siu.siubackend.menu.application.port.input

import siu.siubackend.menu.domain.Menu

interface ListMenusUseCase {
    fun handle(): List<Menu>
}