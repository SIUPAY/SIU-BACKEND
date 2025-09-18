package siu.siubackend.menu.application.port.input

import java.util.*

interface CreateCategoryUseCase {
    fun handle(cmd: Command): UUID

    data class Command(
        val storeIdentifier: UUID,
        val name: String,
        val description: String?,
        val displayOrder: Int?
    )
}


