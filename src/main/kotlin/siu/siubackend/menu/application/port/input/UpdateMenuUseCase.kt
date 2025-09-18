package siu.siubackend.menu.application.port.input

import java.util.*

interface UpdateMenuUseCase {
    fun handle(id: UUID, cmd: Command)
    data class Command(
        val name: String,
        val price: Int,
        val description: String?,
        val categoryIdentifier: UUID?,
        val isAvailable: Boolean?,
        val imageFileBytes: ByteArray?,   // null이면 이미지 변경 없음
        val imageOriginalName: String?
    )
}