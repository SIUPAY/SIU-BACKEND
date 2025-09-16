package siu.siubackend.menu.application.port.input

import java.util.*

interface DeleteMenuUseCase {
    fun handle(id: UUID)
}